package com.hearatale.sightword.ui.sight_word;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hearatale.sightword.R;
import com.hearatale.sightword.data.Constants;
import com.hearatale.sightword.data.model.event.StarEvent;
import com.hearatale.sightword.data.model.phonics.SightWordModel;
import com.hearatale.sightword.data.model.typedef.SightWordsCategoryDef;
import com.hearatale.sightword.data.model.typedef.SightWordsModeDef;
import com.hearatale.sightword.ui.adapter.sight_word.SightWordAdapter;
import com.hearatale.sightword.ui.bank.BankActivity;
import com.hearatale.sightword.ui.base.activity.ActivityMVP;
import com.hearatale.sightword.ui.custom_view.ItemOffsetDecoration;
import com.hearatale.sightword.ui.custom_view.PHListener;
import com.hearatale.sightword.ui.main.MainActivity;
import com.hearatale.sightword.ui.quiz_sight_words.QuizSightWordsActivity;
import com.hearatale.sightword.ui.sentence.SentenceActivity;
import com.hearatale.sightword.utils.Helper;
import com.hearatale.sightword.utils.ImageHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SightWordActivity extends ActivityMVP<SightWordPresenter, ISightWord> implements ISightWord {


    @BindView(R.id.layout_activity)
    ConstraintLayout layoutActivity;

    // toolbar
    @BindView(R.id.toolbar_layout)
    ConstraintLayout layoutToolbar;

    @BindView(R.id.image_view_check)
    ImageView imageCheck;

    @BindView(R.id.image_view_home)
    ImageView imageHome;

    @BindView(R.id.image_view_question)
    ImageView imageQuestion;

    @BindView(R.id.image_view_puzzle)
    ImageView imagePuzzle;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


    SightWordAdapter mAdapter;
    List<SightWordModel> mData = new ArrayList<>();

    @SightWordsCategoryDef
    int mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sight_word);
        ButterKnife.bind(this);
        getArguments();
        initViews();
        initControls();
    }

    @Override
    protected void makeView() {
        mView = this;
    }

    @Override
    protected void makePresenter() {
        mPresenter = new SightWordPresenter();
    }


    private void getArguments() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            mCategory = getIntent().getIntExtra(Constants.Arguments.ARG_SIGHT_WORD_MODE, SightWordsCategoryDef.PRE_K);
        }
    }

    private void initViews() {
        initRV();

        initToolbar();
    }

    private void initRV() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setHasFixedSize(true);

        int widthCell = getWidthCell();

        mData = mPresenter.getSightWords(mCategory);

        mAdapter = new SightWordAdapter(mData, widthCell);

        mRecyclerView.setAdapter(mAdapter);
    }


    private void initToolbar() {

        @IdRes int toolbarColor;
        @IdRes int recyclerViewColor;

        if (mCategory == SightWordsCategoryDef.PRE_K) {
            toolbarColor = getResources().getColor(R.color.cyan_lighter);
            recyclerViewColor = getResources().getColor(R.color.cyan_darker);
        } else {
            toolbarColor = getResources().getColor(R.color.green_light);
            recyclerViewColor = getResources().getColor(R.color.green_dark);
        }

        layoutToolbar.setBackgroundColor(toolbarColor);
        mRecyclerView.setBackgroundColor(recyclerViewColor);

        imageQuestion.setImageResource(R.mipmap.bank);
        imagePuzzle.setImageResource(R.mipmap.question);

        imageCheck.setVisibility(View.INVISIBLE);
    }

    private void initControls() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
                scaleAnimationView(view, new PHListener.AnimationListener() {
                    @Override
                    public void onAnimationEnd() {

                        Intent intent = new Intent(SightWordActivity.this, SentenceActivity.class);
                        Bundle args = new Bundle();
                        ArrayList<SightWordModel> arrSightWords = new ArrayList<>(mData);
                        args.putParcelableArrayList(Constants.Arguments.ARG_SIGHT_WORDS, arrSightWords);
                        args.putInt(Constants.Arguments.ARG_SIGHT_WORD_POSITION, position);
                        args.putInt(Constants.Arguments.ARG_SIGHT_WORD_MODE, mCategory);
                        intent.putExtras(args);
                        pushIntent(intent);

                    }
                });
            }
        });
    }

    private void scaleAnimationView(final View v, @Nullable final PHListener.AnimationListener listener) {
        v.animate().scaleX(1.05f).scaleY(1.05f).setDuration(300).withEndAction(new Runnable() {
            @Override
            public void run() {
                v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(300);
                if (null != listener) {
                    listener.onAnimationEnd();
                }
            }
        });
    }

    private int getWidthCell() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //Width screen device
        int widthDevice = displayMetrics.widthPixels;

        //4 Lobby space
        int padding16 = Helper.dpToPx(16);

        int actionBarSize = Helper.dpToPx(64);

        //Width each cell
        return (widthDevice - (4 * padding16) - actionBarSize) / 3;

    }


    @OnClick(R.id.image_view_puzzle)
    void onQuizSightWord() {
        Intent intent = new Intent(this, QuizSightWordsActivity.class);
        intent.putExtra(Constants.Arguments.SIGHT_WORD_MODE, SightWordsModeDef.ALL_WORDS);
        intent.putExtra(Constants.Arguments.SIGHT_WORD_CATEGORY, mCategory);
        pushIntent(intent);
    }

    @OnClick(R.id.image_view_home)
    void onBackHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @OnClick(R.id.image_view_question)
    void bank() {
        Intent intent = new Intent(SightWordActivity.this, BankActivity.class);
//        intent.putExtra(Constants.Arguments.ARG_PLAY_CELEBRATION, AppDataManager.getInstance().getTotalGoldCount() > 60);

        // screenshot
        Bitmap bitmap = ImageHelper.getBitmapFromView(layoutActivity, Bitmap.Config.RGB_565);
        // compress
        Bitmap bitmapCompress = ImageHelper.compressBySampleSize(bitmap, 12);

        intent.putExtra(Constants.Arguments.ARG_BLUR_BITMAP, bitmapCompress);

        if (mCategory == SightWordsCategoryDef.PRE_K) {
            intent.putExtra("APP_FEATURE", "PRE_K");
        } else {
            intent.putExtra("APP_FEATURE", "KINDERGARTEN");
        }

        pushIntent(intent);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventProgressChange(StarEvent event) {
        for (SightWordModel sightWord: mData) {
            if(sightWord.getText().equals(event.getText())){
                sightWord.setStarCount(event.getCount());
                mAdapter.notifyItemChanged(mData.indexOf(sightWord));
                break;
            }
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
