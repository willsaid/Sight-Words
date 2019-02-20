package com.hearatale.sightwords.ui.quiz_sight_words;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hearatale.sightwords.R;
import com.hearatale.sightwords.data.Constants;
import com.hearatale.sightwords.data.model.phonics.InstructionsContent;
import com.hearatale.sightwords.data.model.phonics.SightWordModel;
import com.hearatale.sightwords.data.model.typedef.SightWordsCategoryDef;
import com.hearatale.sightwords.data.model.typedef.SightWordsModeDef;
import com.hearatale.sightwords.ui.bank.BankActivity;
import com.hearatale.sightwords.ui.base.activity.ActivityMVP;
import com.hearatale.sightwords.ui.base.fragment.SafeFragmentTransaction;
import com.hearatale.sightwords.ui.quiz_sight_words.answers.AnswersFragment;
import com.hearatale.sightwords.ui.sight_word.SightWordActivity;
import com.hearatale.sightwords.utils.Helper;
import com.hearatale.sightwords.utils.ImageHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuizSightWordsActivity extends ActivityMVP<QuizSightWordsPresenter, IQuizSightWords> implements IQuizSightWords {

    private static final int REQUEST_CODE = 1121;

    @BindView(R.id.toolbar_layout)
    ConstraintLayout layoutToolbar;

    @BindView(R.id.layout_activity)
    ConstraintLayout layoutActivity;

    @BindView(R.id.image_view_menu)
    ImageView imageMenu;

//    @BindView(R.id.layout_instruction)
//    LinearLayout layoutInstruction;

//    @BindView(R.id.image_view_instruction)
//    ImageView imageViewInstruction;
//
//    @BindView(R.id.text_view_instruction)
//    TextView textViewInstruction;
    @BindView(R.id.image_stars)
    ImageView imageStars;

    @BindView(R.id.image_view_piggy)
    ImageView imageViewPiggy;

    @BindView(R.id.image_view_forward)
    ImageView imageViewForward;

    @SightWordsModeDef
    int mMode = SightWordsModeDef.ALL_WORDS; //Default mode;

    @SightWordsCategoryDef
    int mCategory = SightWordsCategoryDef.PRE_K; //Default category

    List<SightWordModel> mRemainingWords = new ArrayList<>();

    private SightWordModel mCurrentWord;

    SafeFragmentTransaction mSafeFragmentTransaction;

    private boolean isTransferring = false;

    private int answerWithoutMistakeCount = 0;

    private String appFeature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_sight_words);
        ButterKnife.bind(this);
        getArgument();
        initViews();
//        updateLayoutInstructions(InstructionsContent.listen);
        setupForNewWord(false);
    }

    private void getArgument() {
        Intent intent = getIntent();
        if (intent == null) return;

        mMode = intent.getIntExtra(Constants.Arguments.SIGHT_WORD_MODE, 1);
        mCategory = intent.getIntExtra(Constants.Arguments.SIGHT_WORD_CATEGORY, 0);
        if (mCategory == SightWordsCategoryDef.PRE_K) {
            appFeature = "PRE_K";
        } else {
            appFeature = "KINDERGARTEN";
        }
        if (mMode == SightWordsModeDef.SINGLE_WORD) {
            mCurrentWord = intent.getParcelableExtra(Constants.Arguments.SPECIFIC_SIGHT_WORD);
        }
    }

    private void initViews() {
        //Enable transition changing
//        layoutInstruction.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        initToolbar();

        mSafeFragmentTransaction = SafeFragmentTransaction.createInstance(getLifecycle(), getSupportFragmentManager());
        getLifecycle().addObserver(mSafeFragmentTransaction);
    }

    private void setupForNewWord(final boolean animateTransition) {

        //Remaining words less then 4
        //Ensure remaining words  > 4
        if (mRemainingWords.size() < 4) {
            if (mMode == SightWordsModeDef.ALL_WORDS)
                mRemainingWords = mPresenter.allAvailableOptionAllWords(mCategory);
            else
                mRemainingWords = mPresenter.allAvailableOptionSingleWord(mCategory, mCurrentWord);
        }
        Collections.shuffle(mRemainingWords);

        //Set current word
        mCurrentWord = selectNextAnswerWord(mRemainingWords);
        if (mCurrentWord == null) return;


        setImageStars(mCurrentWord.getStarCount());

        //List incorrect words
        List<SightWordModel> listIncorrectWords = selectNextIncorrectWords(mRemainingWords);

        //Four answers (include single correct answer and three incorrect answers)
        List<SightWordModel> wordsForQuizRound = getWordsForQuizRound(listIncorrectWords);

        if (mPresenter.arrayHasHomophoneConflicts(wordsForQuizRound) && wordsForQuizRound.size() == 4) {
            //just try again -- this should be rare
            setupForNewWord(animateTransition);
            return;
        }

        final AnswersFragment fragment = AnswersFragment.newInstance(mCurrentWord, wordsForQuizRound, mCategory);
        fragment.setAnswersCallback(new AnswersFragment.AnswersCallback() {
            @Override
            public void updateInstruction(InstructionsContent instructionsContent) {
//                updateLayoutInstructions(instructionsContent);
            }

            @Override
            public void animateCoinFlyToPiggyBank(int positionXCorrect, int positionYCorrect, boolean isCoinGold) {
                animateFlyCoin(positionXCorrect, positionYCorrect, isCoinGold);
            }

            @Override
            public void requiredNewWord() {
                setupForNewWord(true);
            }

            @Override
            public void goToBank() {
                showBank();
            }

        });

        if (mMode == SightWordsModeDef.SINGLE_WORD) {
            fragment.setChooseCallBack(new AnswersFragment.ChooseCallBack() {
                @Override
                public void chooseCorrect(int guessCount) {
                    if (guessCount > 1) return;
                    answerWithoutMistakeCount = mPresenter.getAnswersWithoutMistake(mCurrentWord.getText());
                    answerWithoutMistakeCount++;
                    mPresenter.setAnswersWithoutMistake(mCurrentWord.getText(), answerWithoutMistakeCount);
                    mPresenter.saveStarCount(mCurrentWord, answerWithoutMistakeCount);

                    if (answerWithoutMistakeCount > mCurrentWord.getStarCount()) {
                        mCurrentWord.setStarCount(answerWithoutMistakeCount);
                        setImageStars(mCurrentWord.getStarCount());
                    }

                }

                @Override
                public void chooseIncorrect() {
                    answerWithoutMistakeCount = 0;
                    mPresenter.setAnswersWithoutMistake(mCurrentWord.getText(), answerWithoutMistakeCount);
                }
            });
        }

        mSafeFragmentTransaction.registerFragmentTransition(new SafeFragmentTransaction.TransitionHandler() {
            @Override
            public void onTransitionAvailable(FragmentManager managerBy) {

                isTransferring = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isTransferring = false;
                    }
                }, 600);

                FragmentTransaction transaction = managerBy.beginTransaction();
                if (animateTransition) {
                    transaction.setCustomAnimations(R.anim.push_enter, R.anim.push_exit);
                }
                transaction.replace(R.id.layout_content_answer, fragment).commit();
            }
        });


    }

    private void setImageStars(int starCount) {

        switch (starCount) {
            case 0:
                imageStars.setVisibility(View.INVISIBLE);
                break;
            case 1:
                imageStars.setVisibility(View.VISIBLE);
                imageStars.setImageResource(R.mipmap.star);
                break;
            case 2:
                imageStars.setImageResource(R.mipmap.star_2);
                break;
            case 3:
                imageStars.setImageResource(R.mipmap.star_3);
                break;
            case 4:
                imageStars.setImageResource(R.mipmap.star_4);
                break;
            default:
                imageStars.setImageResource(R.mipmap.star_5);
                break;
        }


    }

    private void animateFlyCoin(int positionXCorrect, int positionYCorrect, boolean isCoinGold) {
        int padding8dp = getResources().getDimensionPixelSize(R.dimen.dp_8);
        int coinSize = imageViewPiggy.getWidth() - (2 * padding8dp);

        int positionXImageCoin = positionXCorrect - coinSize;
        int positionYImageCoin = positionYCorrect - coinSize;
        int positionXPiggy = Helper.getRelativeLeft(imageViewPiggy) + padding8dp;
        int positionYPiggy = Helper.getRelativeTop(imageViewPiggy) + padding8dp;
        ViewCompat.setTranslationZ(imageViewPiggy, 1);
        final ImageView imageViewCoin = createImageCoin(positionXImageCoin, positionYImageCoin, isCoinGold, coinSize);
        layoutActivity.addView(imageViewCoin, layoutActivity.getChildCount() - 2);
        imageViewCoin.animate().alpha(1f).setDuration(125);
        imageViewCoin.animate()
                .setDuration(600)
                .setInterpolator(new DecelerateInterpolator())
                .scaleX(0.5f).scaleY(0.5f)
                .translationX(positionXPiggy - (positionXCorrect - coinSize / 2))
                .translationY(positionYPiggy - (positionYCorrect - coinSize / 2))
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        layoutActivity.removeView(imageViewCoin);
                    }
                });
        imageViewPiggy.animate().setDuration(300).setStartDelay(350).scaleX(1.3f).scaleY(1.3f).withEndAction(new Runnable() {
            @Override
            public void run() {
                imageViewPiggy.animate().setDuration(300).scaleX(1f).scaleY(1f);
            }
        });
    }

//    private void updateLayoutInstructions(InstructionsContent content) {
//        //do nothing if the content is already applied
//        if (textViewInstruction.getText().toString().equals(content.text)) {
//            return;
//        }
//
//        imageViewInstruction.setImageResource(content.imageDrawable);
//        textViewInstruction.setText(content.text);
//
//    }

    @NonNull
    private ImageView createImageCoin(int positionXImageCoin, int positionYImageCoin, boolean isCoinGold, int size) {
        final ImageView imageViewCoin = new ImageView(QuizSightWordsActivity.this);
        imageViewCoin.bringToFront();
        imageViewCoin.setImageResource(isCoinGold ? R.mipmap.gold_coin : R.mipmap.silver_coin);
        ConstraintLayout.LayoutParams layoutParams =
                new ConstraintLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.height = size * 2; //initial double size
        layoutParams.width = size * 2;
        layoutParams.leftMargin = positionXImageCoin;
        layoutParams.topMargin = positionYImageCoin;
        layoutParams.topToTop = layoutActivity.getId();
        layoutParams.leftToLeft = layoutActivity.getId();
        imageViewCoin.setLayoutParams(layoutParams);
        imageViewCoin.setAlpha(0f);
        return imageViewCoin;
    }

    private List<SightWordModel> getWordsForQuizRound(List<SightWordModel> listIncorrectWords) {
        List<SightWordModel> wordsForQuizRound = new ArrayList<>();
        wordsForQuizRound.add(mCurrentWord);
        wordsForQuizRound.addAll(listIncorrectWords);
        Collections.shuffle(wordsForQuizRound);
        return wordsForQuizRound;
    }

    private List<SightWordModel> selectNextIncorrectWords(List<SightWordModel> availableWords) {
        List<SightWordModel> incorrectWords = new ArrayList<>();
        incorrectWords.add(mPresenter.popLastList(availableWords));
        incorrectWords.add(mPresenter.popLastList(availableWords));
        incorrectWords.add(mPresenter.popLastList(availableWords));
        return incorrectWords;
    }

    private SightWordModel selectNextAnswerWord(List<SightWordModel> availableWords) {
        if (mMode == SightWordsModeDef.SINGLE_WORD) {
            return mCurrentWord;
        }
        return mPresenter.popLastList(availableWords);
    }


    private void initToolbar() {
        @IdRes int toolbarColor;
        @IdRes int activityColor;

        if (mCategory == SightWordsCategoryDef.PRE_K) {
            toolbarColor = getResources().getColor(R.color.cyan_darker);
            activityColor = getResources().getColor(R.color.cyan_lighter);
        } else {
            toolbarColor = getResources().getColor(R.color.green_dark);
            activityColor = getResources().getColor(R.color.green_light);
        }

        layoutToolbar.setBackgroundColor(toolbarColor);
        layoutActivity.setBackgroundColor(activityColor);
    }


    @Override
    protected void makeView() {
        mView = this;
    }

    @Override
    protected void makePresenter() {
        mPresenter = new QuizSightWordsPresenter();
    }

    @OnClick(R.id.image_view_back)
    void back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);

        super.onBackPressed();
    }

    @OnClick(R.id.image_view_replay)
    void replay() {
        AnswersFragment fragment = (AnswersFragment) getSupportFragmentManager().findFragmentById(R.id.layout_content_answer);
        fragment.animateForCurrentWord();
    }

    @OnClick(R.id.image_view_piggy)
    void showBank() {
        if (isTransferring) return;

        Intent intent = new Intent(this, BankActivity.class);
//        intent.putExtra(Constants.Arguments.ARG_PLAY_CELEBRATION, mPresenter.getTotalGoldCount() > 60);

        // screenshot
        Bitmap bitmap = ImageHelper.getBitmapFromView(layoutActivity, Bitmap.Config.RGB_565);
        // compress
        Bitmap bitmapCompress = ImageHelper.compressBySampleSize(bitmap, 12);

        intent.putExtra(Constants.Arguments.ARG_BLUR_BITMAP, bitmapCompress);

        intent.putExtra("APP_FEATURE", appFeature);

        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.ResultCode.SETUP_NEW_WORD_CODE == resultCode && requestCode == REQUEST_CODE
                && mPresenter.getTotalGoldCount(appFeature) > 60) {
            setupForNewWord(true);
        }
    }

    @OnClick(R.id.image_view_forward)
    public void forward() {
        setupForNewWord(true);
    }

    @OnClick(R.id.image_view_menu)
    void backToAlphabetActivity() {
        Intent intent = new Intent(QuizSightWordsActivity.this, SightWordActivity.class);
        if (mCategory == SightWordsCategoryDef.PRE_K) {
            intent.putExtra(Constants.Arguments.ARG_SIGHT_WORD_MODE, SightWordsCategoryDef.PRE_K);
        } else {
            intent.putExtra(Constants.Arguments.ARG_SIGHT_WORD_MODE, SightWordsCategoryDef.KINDERGARTEN);
        }
        pushIntent(intent);
        finish();
    }
}
