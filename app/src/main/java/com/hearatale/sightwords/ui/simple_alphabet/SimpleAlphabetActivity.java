package com.hearatale.sightwords.ui.simple_alphabet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hearatale.sightwords.R;
import com.hearatale.sightwords.data.Constants;
import com.hearatale.sightwords.data.model.event.ProgressPuzzleEvent;
import com.hearatale.sightwords.data.model.event.StarEvent;
import com.hearatale.sightwords.data.model.phonics.letters.SimpleLetterModel;
import com.hearatale.sightwords.data.model.phonics.letters.TimedAudioInfoModel;
import com.hearatale.sightwords.data.model.typedef.DifficultyDef;
import com.hearatale.sightwords.service.AudioPlayerHelper;
import com.hearatale.sightwords.ui.adapter.simple_alphabet.SimpleAlphabetAdapter;
import com.hearatale.sightwords.ui.bank.BankActivity;
import com.hearatale.sightwords.ui.base.activity.ActivityMVP;
import com.hearatale.sightwords.ui.custom_view.ItemOffsetDecoration;
import com.hearatale.sightwords.ui.letter.LetterActivity;
import com.hearatale.sightwords.ui.main.MainActivity;
import com.hearatale.sightwords.ui.quiz.QuizActivity;
import com.hearatale.sightwords.ui.quiz_puzzle.QuizPuzzleActivity;
import com.hearatale.sightwords.utils.Config;
import com.hearatale.sightwords.utils.Helper;
import com.hearatale.sightwords.utils.ImageHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SimpleAlphabetActivity extends ActivityMVP<SimpleAlphabetPresenter, ISimpleAlphabet> implements ISimpleAlphabet {


    @BindView(R.id.recycler_view_grid)
    RecyclerView recyclerView;

    @BindView(R.id.layout_activity)
    ConstraintLayout layoutActivity;

    @BindView(R.id.image_view_piggy)
    ImageView imageViewPiggy;

    @BindView(R.id.image_view_puzzle)
    ImageView imageViewPuzzle;

//    @BindView(R.id.progress_bar)
//    ProgressBar progressBar;

    SimpleAlphabetAdapter mAdapter;
    List<SimpleLetterModel> mListItem;

    boolean clickableItem = true;

    @DifficultyDef
    private int mMode = DifficultyDef.EASY; //Default mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_alphabet);
        ButterKnife.bind(this);
        getArgument();
        setupRecycleView();
        initControls();
    }

    private void getArgument() {
        if (getIntent() != null) {
            mMode = getIntent().getIntExtra(Constants.Arguments.ARG_LETTER_MODE, 0);
        }
    }

    @Override
    protected void makeView() {
        mView = this;
    }

    @Override
    protected void makePresenter() {
        mPresenter = new SimpleAlphabetPresenter();
    }

    private void setupRecycleView() {
        if (mMode == DifficultyDef.STANDARD) {
            recyclerView.setBackgroundColor(getResources().getColor(R.color.blue));
        }
        if (mMode == DifficultyDef.EASY) {
//            progressBar.setVisibility(View.GONE);
            imageViewPuzzle.setVisibility(View.GONE);
        }
        imageViewPiggy.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setHasFixedSize(true);
        int sizeImage = getSizeImage();


        mListItem = mPresenter.getLetterByMode(mMode);

        mAdapter = new SimpleAlphabetAdapter(mListItem, sizeImage, mMode);

        recyclerView.setAdapter(mAdapter);
    }

    private void initControls() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, final View view, int position) {
                if (!clickableItem) return;
                final SimpleLetterModel letter = mListItem.get(position);

                if (mMode == DifficultyDef.STANDARD) {
                    nextToLetter(letter);
                    return;
                }
                clickableItem = false;
                //Audio info is null then get sound name form assets
                String prefixPath = Config.AUDIO_WORDS_SETS_PATH;
                if (letter.getAudioInfo() == null || TextUtils.isEmpty(letter.getAudioInfo().getFileName())) {
                    initSourceLetterTiming(letter);
                    prefixPath = Config.AUDIO_SOUND_EXTRA_PATH;
                }
                scaleAnimationView(view, true, null);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scaleAnimationView(view, false, new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                nextToLetter(letter);
                                clickableItem = true;
                                AudioPlayerHelper.getInstance().stopPlayer();
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                }, (int) (Float.parseFloat(letter.getAudioInfo().getWordDuration()) * 1000));
                AudioPlayerHelper.getInstance().playAudio(prefixPath, letter.getAudioInfo());
            }
        });
    }

    private void initSourceLetterTiming(SimpleLetterModel letter) {
        //if the letter doesn't exist in the sound files, it should be in "assets > Audio > Sounds > Extra Letters"
        TimedAudioInfoModel audioInfo = new TimedAudioInfoModel();
        audioInfo.setFileName("extra-letter-" + letter.getLetter().toUpperCase());
        String pathExtraLetterAudioFile = Config.AUDIO_BY_LETTER_PATH + audioInfo.getFileName() + ".mp3";
        float duration = Helper.getDurationAudioFromAssets(this, pathExtraLetterAudioFile);
        audioInfo.setWordStart("0.0");
        audioInfo.setWordDuration(duration + "");
        letter.setAudioInfo(audioInfo);
    }

    private void nextToLetter(SimpleLetterModel letter) {
        Intent intent = new Intent(SimpleAlphabetActivity.this, LetterActivity.class);
        if (mMode == DifficultyDef.EASY) {
            intent.putExtra(Constants.Arguments.ARG_LETTER_TEXT, letter.getLetter());
        } else {
            String[] sourceLetterSoundId = letter.getId().split("-");
            intent.putExtra(Constants.Arguments.ARG_LETTER_TEXT, sourceLetterSoundId[0]);
            intent.putExtra(Constants.Arguments.ARG_SOUND_ID, sourceLetterSoundId[1]);
        }
        intent.putExtra(Constants.Arguments.ARG_LETTER_MODE, mMode);
        startActivity(intent);
    }

    private int getSizeImage() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //Width screen device
        int widthDevice = displayMetrics.widthPixels;

        //4 Lobby space
        int padding16 = Helper.dpToPx(16);

        int actionBarSize = Helper.dpToPx(64);

        //Width each cell
        int widthCell = (widthDevice - (4 * padding16) - actionBarSize) / 3;

        return (int) (widthCell * 0.5);
    }

    private void scaleAnimationView(final View v, boolean isZoomIn, @Nullable final Animation.AnimationListener listener) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), isZoomIn ? R.anim.zoom_in : R.anim.zoom_out);
        if (listener != null) animation.setAnimationListener(listener);
        v.startAnimation(animation);
    }

    @OnClick(R.id.image_view_check)
    void playIntructions() {
        if (mMode == DifficultyDef.EASY) {
            AudioPlayerHelper.getInstance().playAudio("Sounds/Win_as_many_as_five_gold_stars");
        } else {
            AudioPlayerHelper.getInstance().playAudio("Sounds/Complete a whole puzzle");
        }
    }


    @OnClick(R.id.image_view_puzzle)
    void pushQuizActivity() {
        Intent intent = new Intent(SimpleAlphabetActivity.this, QuizActivity.class);
        intent.putExtra(Constants.Arguments.ARG_LETTER_MODE, mMode);
        pushIntent(intent);
    }

    @OnClick(R.id.image_view_home)
    void backToHome() {
        AudioPlayerHelper.getInstance().stopPlayer();
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    // go to puzzle
    @OnClick(R.id.image_view_question)
    void onPuzzle() {
        Intent intent = new Intent(SimpleAlphabetActivity.this, QuizPuzzleActivity.class);
        Bundle args = new Bundle();
        args.putInt(Constants.Arguments.ARG_LETTER_MODE, mMode);
        args.putBoolean(Constants.Arguments.ARG_PUZZLE_RANDOM, true);
        intent.putExtras(args);
        pushIntent(intent);
    }

    @OnClick(R.id.image_view_piggy)
    void bank() {
        Intent intent = new Intent(SimpleAlphabetActivity.this, BankActivity.class);

        // screenshot
        Bitmap bitmap = ImageHelper.getBitmapFromView(layoutActivity, Bitmap.Config.RGB_565);
        // compress
        Bitmap bitmapCompress = ImageHelper.compressBySampleSize(bitmap, 12);

        intent.putExtra(Constants.Arguments.ARG_BLUR_BITMAP, bitmapCompress);

        if (mMode == DifficultyDef.EASY) {
            intent.putExtra("APP_FEATURE", "ALPHABET_LETTERS");
        } else {
            intent.putExtra("APP_FEATURE", "PHONICS");
        }

        pushIntent(intent);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventProgressChange(ProgressPuzzleEvent event) {
        for (int i = 0; i < mListItem.size(); i++) {
            SimpleLetterModel letterModel = mListItem.get(i);
            if (letterModel.getId().equals(event.getSourceLetterSoundId())) {
                int newProgress = mPresenter.getProgress(event.getSourceLetterSoundId());
                int starConsecutive = mPresenter.getStarConsecutive(event.getSourceLetterSoundId());
                letterModel.setStarConsecutive(starConsecutive);
                letterModel.setProgressCompleted(newProgress);
                mAdapter.notifyItemChanged(i);
                break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStarConsecutive(StarEvent event) {
        for (int i = 0; i < mListItem.size(); i++) {
            SimpleLetterModel letterModel = mListItem.get(i);
            if (letterModel.getId().equals(event.getText())) {
                int starConsecutive = mPresenter.getStarConsecutive(event.getText());
                letterModel.setStarConsecutive(starConsecutive);
                mAdapter.notifyItemChanged(i);
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
