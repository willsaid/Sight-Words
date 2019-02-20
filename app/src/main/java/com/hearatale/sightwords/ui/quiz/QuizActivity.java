package com.hearatale.sightwords.ui.quiz;

import com.hearatale.sightwords.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hearatale.sightwords.Application;
import com.hearatale.sightwords.R;
import com.hearatale.sightwords.data.Constants;
import com.hearatale.sightwords.data.model.event.ProgressPuzzleEvent;
import com.hearatale.sightwords.data.model.phonics.SectionQuiz;
import com.hearatale.sightwords.data.model.typedef.DifficultyDef;
import com.hearatale.sightwords.ui.adapter.quiz.QuizSectionAdapter;
import com.hearatale.sightwords.ui.base.activity.ActivityMVP;
import com.hearatale.sightwords.ui.custom_view.ItemOffsetDecoration;
import com.hearatale.sightwords.ui.idiom.IdiomActivity;
import com.hearatale.sightwords.ui.letter.LetterActivity;
import com.hearatale.sightwords.ui.main.MainActivity;
import com.hearatale.sightwords.utils.DebugLog;
import com.hearatale.sightwords.utils.ImageHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuizActivity extends ActivityMVP<QuizPresenter, IQuiz> implements IQuiz {

    @BindView(R.id.layout_activity)
    ConstraintLayout layoutActivity;

    // toolbar
    @BindView(R.id.toolbar_layout)
    ConstraintLayout layoutToolbar;

    @BindView(R.id.image_view_check)
    ImageView check;

    @BindView(R.id.image_view_menu)
    ImageView imageMenu;

    @BindView(R.id.image_view_puzzle)
    ImageView imagePuzzle;

    @BindView(R.id.image_view_question)
    ImageView imageQuestion;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    QuizSectionAdapter mAdapter;

    List<SectionQuiz> mData = new ArrayList<>();

    @DifficultyDef
    private int mMode = DifficultyDef.EASY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);

        getArgument();

        initViews();

        intControls();


    }

    private void getArgument() {
        Intent intent = getIntent();
        if (intent == null) return;
        mMode = intent.getIntExtra(Constants.Arguments.ARG_LETTER_MODE, DifficultyDef.EASY);
    }

    @Override
    protected void makeView() {
        mView = this;
    }

    @Override
    protected void makePresenter() {
        mPresenter = new QuizPresenter();
    }

    private void initViews() {
        initToolbar();

        initRV();
    }


    private void initToolbar() {
        if (mMode == DifficultyDef.EASY) {
            layoutToolbar.setBackgroundColor(getResources().getColor(R.color.cyan_dark));
        } else {
            layoutToolbar.setBackgroundColor(getResources().getColor(R.color.blue_dark_standard));
        }
        check.setVisibility(View.GONE);
        imageMenu.setVisibility(View.VISIBLE);
        imagePuzzle.setVisibility(View.GONE);
        imageQuestion.setVisibility(View.GONE);
    }

    private void initRV() {
        LinearLayoutManager layoutManager = new GridLayoutManager(Application.Context, 2);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //Height screen device
        int heightScreenWindow = displayMetrics.heightPixels;
        int paddingSize = (int) (heightScreenWindow * 0.025);
        mRecyclerView.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
        mRecyclerView.setClipToPadding(false);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, paddingSize, true);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setHasFixedSize(true);
        int heightItem = (heightScreenWindow - (paddingSize * 4)) / 2;

        mAdapter = new QuizSectionAdapter(mData, heightItem);

        mRecyclerView.setAdapter(mAdapter);

        // load data
        mPresenter.getAllLetter();

    }

    private void intControls() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SectionQuiz sectionQuiz = mData.get(position);
                if (sectionQuiz.isHeader) return;

                if (sectionQuiz.isCompleted()) {
                    DebugLog.e("puzzle");
                    // show idiom activity

                    Intent intent = new Intent(QuizActivity.this, IdiomActivity.class);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(QuizActivity.this,
                                    view,
                                    ViewCompat.getTransitionName(view));

                    view.setVisibility(View.INVISIBLE);

                    Bundle args = new Bundle();
                    args.putParcelable(Constants.Arguments.ARG_LETTER, sectionQuiz.t);

                    // screenshot
                    Bitmap bitmap = ImageHelper.getBitmapFromView(layoutActivity, Bitmap.Config.RGB_565);
                    // compress
                    Bitmap bitmapCompress = ImageHelper.compressBySampleSize(bitmap, 12);

                    args.putParcelable(Constants.Arguments.ARG_BLUR_BITMAP, bitmapCompress);

                    intent.putExtras(args);
                    startActivity(intent, options.toBundle());


                } else {
                    Intent intent = new Intent(QuizActivity.this, LetterActivity.class);
                    Bundle args = new Bundle();
                    args.putString(Constants.Arguments.ARG_SOUND_ID, sectionQuiz.t.getSoundId());
                    args.putString(Constants.Arguments.ARG_LETTER_TEXT, sectionQuiz.t.getSourceLetter().toLowerCase());
                    args.putInt(Constants.Arguments.ARG_LETTER_MODE, DifficultyDef.STANDARD);
                    intent.putExtras(args);
                    pushIntent(intent);
                }

            }
        });
    }

    @Override
    public void updateDataQuiz(List<SectionQuiz> data) {
        if (mData != data) {
            mAdapter.replaceData(data);
        }
    }

    @Override
    public int getDifficulty() {
        return mMode;
    }

    @OnClick(R.id.image_view_menu)
    void onMenu() {
        onBackPressed();
    }

    @OnClick(R.id.image_view_home)
    void onHome() {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProgressPuzzle(ProgressPuzzleEvent event) {
        if (mPresenter.isPuzzleCompleted(event.getSourceLetterSoundId())) {

            // delay time for capture screen
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //TODO not smooth
                    mPresenter.getAllLetter();
                }
            }, 1000);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
