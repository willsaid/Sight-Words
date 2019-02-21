package com.hearatale.sightword.ui.sentence;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hearatale.sightword.R;
import com.hearatale.sightword.data.Constants;
import com.hearatale.sightword.data.model.phonics.SightWordModel;
import com.hearatale.sightword.data.model.typedef.SightWordsCategoryDef;
import com.hearatale.sightword.data.model.typedef.SightWordsModeDef;
import com.hearatale.sightword.service.AudioPlayerHelper;
import com.hearatale.sightword.ui.base.activity.ActivityMVP;
import com.hearatale.sightword.ui.base.fragment.SafeFragmentTransaction;
import com.hearatale.sightword.ui.main.MainActivity;
import com.hearatale.sightword.ui.quiz_sight_words.QuizSightWordsActivity;
import com.hearatale.sightword.ui.sentence.content.SentenceContentFragment;
import com.hearatale.sightword.ui.sight_word.SightWordActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SentenceActivity extends ActivityMVP<SentencePresenter, ISentence> implements ISentence {

    public static final int REQUEST_CODE = 96;

    // toolbar
    @BindView(R.id.toolbar_layout)
    ConstraintLayout layoutToolbar;

    @BindView(R.id.image_view_check)
    ImageView imageCheck;

    @BindView(R.id.image_view_home)
    ImageView imageHome;

    @BindView(R.id.image_view_menu)
    ImageView imageMenu;

    @BindView(R.id.image_view_question)
    ImageView imageQuestion;

    @BindView(R.id.image_view_puzzle)
    ImageView imagePuzzle;

    // action

    @BindView(R.id.text_sight_word)
    TextView textSightWord;

    @BindView(R.id.button_next)
    ImageButton buttonNext;

    @BindView(R.id.button_prev)
    ImageButton buttonPrev;

    List<SightWordModel> mData = new ArrayList<>();
    int mCurrentPosition = 0;

    @SightWordsCategoryDef
    int mCategory;

    SafeFragmentTransaction mSafeFragmentTransaction;

    SentenceContentFragment mSentenceContentFragment;

    int[] mImageFocusSize = new int[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentence);
        ButterKnife.bind(this);

        getArguments();

        initViews();

    }

    private void getArguments() {
        if (null != getIntent()) {
            mData = getIntent().getParcelableArrayListExtra(Constants.Arguments.ARG_SIGHT_WORDS);
            mCurrentPosition = getIntent().getIntExtra(Constants.Arguments.ARG_SIGHT_WORD_POSITION, 0);
            mCategory = getIntent().getIntExtra(Constants.Arguments.ARG_SIGHT_WORD_MODE, SightWordsCategoryDef.PRE_K);
        }
    }

    private void initViews() {

        initToolbar();

        mImageFocusSize = getImageFocusSize();

        mSafeFragmentTransaction = SafeFragmentTransaction.createInstance(getLifecycle(), getSupportFragmentManager());
        getLifecycle().addObserver(mSafeFragmentTransaction);

        disablePrevNext();

        setTitleSighWord(mData.get(mCurrentPosition));

        mSafeFragmentTransaction.registerFragmentTransition(new SafeFragmentTransaction.TransitionHandler() {
            @Override
            public void onTransitionAvailable(FragmentManager managerBy) {
                managerBy.beginTransaction()
                        .add(R.id.main_content, getSentenceFragment(mData.get(mCurrentPosition), mImageFocusSize))
                        .commit();
            }
        });


    }

    private int[] getImageFocusSize() {

        int[] size = new int[2];
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //Width screen device
        int widthDevice = displayMetrics.widthPixels;

        //height screen device
        int heightDevice = displayMetrics.heightPixels;

        int actionBarSize = getResources().getDimensionPixelOffset(R.dimen.dp_64);

        int widthSentence = Math.round((widthDevice - actionBarSize));

        int heightSentence = Math.round(heightDevice * 65 / 100);// 0.65

        //width
        size[0] = Math.round(widthSentence * 4 / 10);

        // height
        size[1] = Math.round(heightSentence * 8 / 10);

        return size;

    }

    private void initToolbar() {
        @IdRes int toolbarColor;

        if (mCategory == SightWordsCategoryDef.PRE_K) {
            toolbarColor = getResources().getColor(R.color.cyan_lighter);
        } else {
            toolbarColor = getResources().getColor(R.color.green_light);
        }
        layoutToolbar.setBackgroundColor(toolbarColor);

        imageMenu.setVisibility(View.VISIBLE);
        imagePuzzle.setImageResource(R.mipmap.repeat);
        imageQuestion.setVisibility(View.INVISIBLE);

        imageCheck.setVisibility(View.INVISIBLE);

        animationButtonQuiz();
    }

    @Override
    protected void makeView() {
        mView = this;
    }

    @Override
    protected void makePresenter() {
        mPresenter = new SentencePresenter();
    }


    @OnClick(R.id.button_prev)
    void onPrev() {
        if (mCurrentPosition == 0) {
            return;
        }

        mCurrentPosition--;

        replaceFragment(false);

    }

    @OnClick(R.id.button_next)
    void onNext() {
        if (mCurrentPosition == mData.size() - 1) {
            return;
        }

        mCurrentPosition++;

        replaceFragment(true);
    }

    @OnClick(R.id.image_view_puzzle)
    void onRepeat() {
        mSentenceContentFragment.repeatCurrentWord(false);
    }

    @OnClick(R.id.image_view_menu)
    void onMenu() {
        AudioPlayerHelper.getInstance().stopPlayer();
        Intent intent = new Intent(SentenceActivity.this, SightWordActivity.class);
        if (mCategory == SightWordsCategoryDef.PRE_K) {
            intent.putExtra(Constants.Arguments.ARG_SIGHT_WORD_MODE, SightWordsCategoryDef.PRE_K);
        } else {
            intent.putExtra(Constants.Arguments.ARG_SIGHT_WORD_MODE, SightWordsCategoryDef.KINDERGARTEN);
        }
        pushIntent(intent);
        finish();
    }

    @OnClick(R.id.image_view_question)
    void onSightWordQuizView() {
        Intent intent = new Intent(this, QuizSightWordsActivity.class);
        intent.putExtra(Constants.Arguments.SIGHT_WORD_MODE, SightWordsModeDef.SINGLE_WORD);
        intent.putExtra(Constants.Arguments.SIGHT_WORD_CATEGORY, mCategory);
        intent.putExtra(Constants.Arguments.SPECIFIC_SIGHT_WORD, mData.get(mCurrentPosition));
        startActivityForResult(intent, REQUEST_CODE);
    }

    @OnClick(R.id.image_view_home)
    void onHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private SentenceContentFragment getSentenceFragment(SightWordModel sightWord, int[] imageFocusSize) {
        mSentenceContentFragment = SentenceContentFragment.newInstance(sightWord, imageFocusSize);
        mSentenceContentFragment.setListener(new SentenceContentFragment.FocusContentListener() {
            @Override
            public void onAnimateWord() {
                // animate letter in 0.1 second
                zoomInOutAndFade(textSightWord, 100, null);
            }

            @Override
            public void onStopView() {
                textSightWord.clearAnimation();
                textSightWord.animate().cancel();
            }
        });
        mSentenceContentFragment.setAudioFocusListener(new SentenceContentFragment.AudioFocusListener() {
            @Override
            public void onAudioPlaying() {
                enableButton(imagePuzzle, false);
                showQuizButton(false);
            }

            @Override
            public void onActionCompleted() {
                enableButton(imagePuzzle, true);
                showQuizButton(true);
            }
        });

        return mSentenceContentFragment;
    }

    private void replaceFragment(final boolean next) {

        final SightWordModel sightWord = mData.get(mCurrentPosition);

        // set title
        setTitleSighWord(sightWord);
        // replace

        mSafeFragmentTransaction.registerFragmentTransition(new SafeFragmentTransaction.TransitionHandler() {
            @Override
            public void onTransitionAvailable(FragmentManager managerBy) {
                disablePrevNext();
                managerBy.beginTransaction()
                        .setCustomAnimations(next ? R.anim.push_enter : R.anim.pop_enter,
                                next ? R.anim.push_exit : R.anim.pop_exit)
                        .replace(R.id.main_content, getSentenceFragment(sightWord, mImageFocusSize))
                        .commit();
            }
        });
    }

    private void setTitleSighWord(SightWordModel sightWord) {
        textSightWord.setText(sightWord.getText());
    }


    private void disablePrevNext() {
        // one
        if (mData.size() == 1) {
            enableButton(buttonPrev, false);
            enableButton(buttonNext, false);
            return;
        }

        // middle position
        if (mCurrentPosition != 0 && mCurrentPosition != mData.size() - 1) {
            enableButton(buttonPrev, true);
            enableButton(buttonNext, true);
            return;
        }

        // first position
        if (mCurrentPosition == 0) {
            enableButton(buttonPrev, false);
            enableButton(buttonNext, true);
            return;
        }

        // last position
        if (mCurrentPosition == mData.size() - 1) {
            enableButton(buttonPrev, true);
            enableButton(buttonNext, false);
        }

    }

    private void enableButton(View button, boolean show) {
        button.setEnabled(show);
        button.setFocusable(show);
        button.setAlpha(show ? 1 : 0.5f);
    }

    private void showQuizButton(boolean show) {
        imageQuestion.setAlpha(show ? 1f : 0f);
        imageQuestion.setEnabled(show);
        imageQuestion.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private void animationButtonQuiz() {
        Animation scaleAnimation = new ScaleAnimation(1f, 1.15f, 1f, 1.15f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        imageQuestion.startAnimation(scaleAnimation);
    }


    private void zoomInOutAndFade(final View view, int timingAudioDuration, @Nullable final Animation.AnimationListener listener) {

        Animation zoomInAnimation = new ScaleAnimation(1, 1.15f, 1, 1.15f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        zoomInAnimation.setDuration(400);
        zoomInAnimation.setFillAfter(true);

        AlphaAnimation alphaAnimation = new AlphaAnimation(view.getAlpha(), 1f);
        view.setAlpha(1f);
        alphaAnimation.setDuration(400);
        alphaAnimation.setFillAfter(true);

        // 1f/1,15f is identity size
        //Zoom out start from (view has zoom in to 1,15f) 1f to (identity size) 1f/1.15f
        Animation zoomOutAnimation = new ScaleAnimation(1f, 1f / 1.15f, 1f, 1f / 1.15f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        int startOffset = (timingAudioDuration == 0 ? 200 : timingAudioDuration) + 300;
        zoomOutAnimation.setDuration(500);
        zoomOutAnimation.setStartOffset(startOffset);
        zoomInAnimation.setFillAfter(true);

        final AnimationSet animationSet = new AnimationSet(false);
        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.addAnimation(zoomInAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(zoomOutAnimation);

        if (listener != null) animationSet.setAnimationListener(listener);

        view.startAnimation(animationSet);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            mSentenceContentFragment.repeatCurrentWord(false, true);
        }
    }
}