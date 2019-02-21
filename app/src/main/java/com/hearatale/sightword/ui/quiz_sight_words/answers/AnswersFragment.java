package com.hearatale.sightword.ui.quiz_sight_words.answers;


import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hearatale.sightword.R;
import com.hearatale.sightword.data.model.phonics.InstructionsContent;
import com.hearatale.sightword.data.model.phonics.SightWordModel;
import com.hearatale.sightword.data.model.typedef.SightWordsCategoryDef;
import com.hearatale.sightword.service.AudioPlayerHelper;
import com.hearatale.sightword.ui.base.fragment.FragmentMVP;
import com.hearatale.sightword.utils.DebugLog;
import com.hearatale.sightword.utils.Helper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnswersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnswersFragment extends FragmentMVP<AnswersPresenter, IAnswers> implements IAnswers {

    private static final String ARG_LIST_ANSWER = "ARG_LIST_ANSWER";
    private static final String ARG_CURRENT_WORD = "ARG_CURRENT_WORD";
    private static final String ARG_CATEGORY = "ARG_CATEGORY";

    @BindView(R.id.layout_answers)
    ConstraintLayout layoutAnswers;

    List<TextView> answerTextViews = new ArrayList<>();

    private SightWordModel mCurrentWord;
    private List<SightWordModel> mListAnswers;

    @SightWordsCategoryDef
    int mCategory = SightWordsCategoryDef.PRE_K; //Default category

    private String appFeature;

    int mGuessCount = 0;
    Handler handlerSchedule = new Handler();
    Handler handlerForInstruction = new Handler();
    Handler handlerForFlyCoin = new Handler();

    AnswersCallback answersCallback;

    ChooseCallBack chooseCallBack;
    private boolean mCurrentlyAnimating;


    public void setAnswersCallback(AnswersCallback answersCallback) {
        this.answersCallback = answersCallback;
    }

    public void setChooseCallBack(ChooseCallBack chooseCallBack) {
        this.chooseCallBack = chooseCallBack;
    }

    public AnswersFragment() {
        // Required empty public constructor
    }


    public static AnswersFragment newInstance(SightWordModel currentWord, List<SightWordModel> listAnswers,
                                              @SightWordsCategoryDef int category) {
        AnswersFragment fragment = new AnswersFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CURRENT_WORD, currentWord);
        args.putParcelableArrayList(ARG_LIST_ANSWER, (ArrayList<? extends Parcelable>) listAnswers);
        args.putInt(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentWord = getArguments().getParcelable(ARG_CURRENT_WORD);
            mListAnswers = getArguments().getParcelableArrayList(ARG_LIST_ANSWER);
            mCategory = getArguments().getInt(ARG_CATEGORY);
            if (mCategory == SightWordsCategoryDef.PRE_K) {
                appFeature = "PRE_K";
            } else {
                appFeature = "KINDERGARTEN";
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_answers, container, false);
        ButterKnife.bind(this, rootView);
        initViews();
        setTextForAnswerTextViews(mListAnswers);
        animateForNewWord();
        return rootView;
    }

    private void initViews() {
        for (int i = 0; i < layoutAnswers.getChildCount(); i++) {
            if (!(layoutAnswers.getChildAt(i) instanceof TextView)) return;

            TextView answerTextView = (TextView) layoutAnswers.getChildAt(i);
            answerTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCurrentlyAnimating) return;
                    chooseAnswerView((TextView) v);
                }
            });
            answerTextViews.add(answerTextView);
        }
    }

    private void animateForNewWord() {
        //Waiting animate slide completed
        handlerSchedule.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCurrentlyAnimating = false;
//                updateInstructions(InstructionsContent.listen);
                animateForCurrentWord();
            }
        }, 450);

    }

//    private void updateInstructions(InstructionsContent instructionsContent) {
//        if (answersCallback != null) {
//            answersCallback.updateInstruction(instructionsContent);
//        }
//    }

    public void animateForCurrentWord() {
//        updateInstructions(InstructionsContent.listen);

        //Schedule after 400ms play audio current word
        handlerSchedule.postDelayed(new Runnable() {
            @Override
            public void run() {
                playAudioCurrentWord();
            }
        }, 400);

        //Schedule after 1650ms update instruction to choose word
//        handlerForInstruction.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                updateInstructions(InstructionsContent.chooseWord);
////                mCurrentlyAnimating = false;
//            }
//        }, 1650);

    }

    private void setTextForAnswerTextViews(List<SightWordModel> wordsForQuizRound) {
        for (int i = 0; i < 4; i++) {
            TextView textViewAnswer = answerTextViews.get(i);
            SightWordModel sightWordModel = wordsForQuizRound.get(i);
            textViewAnswer.setText(sightWordModel.getText());
            //Make sure clear visible after it has been disabled
            textViewAnswer.setAlpha(1);
            textViewAnswer.setEnabled(true);
        }
    }


    private void chooseAnswerView(TextView view) {
        //handlerSchedule.removeCallbacksAndMessages(null);
        String answer = view.getText().toString();
        SightWordModel answerSightWord = null;
        for (SightWordModel sightWord : mPresenter.getAllWords(mCategory)) {
            if (sightWord.getText().equals(answer)) {
                answerSightWord = sightWord;
                break;
            }
        }
        if (answerSightWord == null) return;

        mGuessCount++;

        if (answerSightWord.getText().equals(mCurrentWord.getText())) {
            userSelectedCorrectWord(view);
        } else {
            userSelectedIncorrectWord(view);
        }

    }

    private void userSelectedCorrectWord(final TextView textView) {
        //Begin animating...
        mCurrentlyAnimating = true;
        //Play audio correct ringtone
        handlerForInstruction.removeCallbacksAndMessages(null);
        AudioPlayerHelper.getInstance().playCorrect(null);

        //Update instruction (animation) correct
//        updateInstructions(InstructionsContent.correct);

        //Animate hide incorrect answer (Alpha to 0) with 0.2s delay
        for (TextView answer : answerTextViews) {
            if (!(answer == textView)) {
                answer.animate().alpha(0f).setDuration(200).start();
            }
        }

        //Push correct answer to center parent
        pushToCenterParent(textView);

        //Play audio correct answer
//        handlerSchedule.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                playAudioCurrentWord();
//            }
//        }, 1000);
        if (chooseCallBack != null) {
            chooseCallBack.chooseCorrect(mGuessCount);
        }

        // Save coins
        switch (mGuessCount) {
            case 1:
                mPresenter.increaseTotalGoldCoins(appFeature);
                break;
            case 2:
                mPresenter.increaseTotalSilverCoins(appFeature);
                break;
        }


        handlerForFlyCoin.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (answersCallback != null) {
                    int positionXCorrectAnswers = Helper.getRelativeLeft(layoutAnswers) + layoutAnswers.getWidth() / 2;
                    int positionYCorrectAnswers = Helper.getRelativeTop(layoutAnswers) + layoutAnswers.getHeight() / 2;
                    switch (mGuessCount) {
                        case 1:
                            answersCallback.animateCoinFlyToPiggyBank(positionXCorrectAnswers, positionYCorrectAnswers, true);
                            break;
                        case 2:
                            answersCallback.animateCoinFlyToPiggyBank(positionXCorrectAnswers, positionYCorrectAnswers, false);
                            break;
                    }
                    DebugLog.i("FLY");
                }
            }
        }, 1750);


        //After 3s
        handlerSchedule.postDelayed(new Runnable() {
            @Override
            public void run() {
//                mCurrentlyAnimating = false;

                int goldCount = mPresenter.getTotalGoldCount(appFeature);
                int silverCount = mPresenter.getTotalSilverCount(appFeature);
                int totalGold = goldCount + silverCount / 2;

                if (totalGold > 0 && totalGold % 125 == 0) {
                    if (answersCallback != null) {
                        answersCallback.goToBank();
                    }
                } else {
                    if (answersCallback != null) {
                        answersCallback.requiredNewWord();
                    }
                }
            }
        }, 3000);
    }




    private void pushToCenterParent(TextView textView) {
        int positionXRelativeOfParent = textView.getLeft() + textView.getWidth() / 2;
        int positionYRelativeOfParent = textView.getTop() + textView.getHeight() / 2;

        textView.animate().translationX(layoutAnswers.getWidth() / 2 - positionXRelativeOfParent)
                .translationY(layoutAnswers.getHeight() / 2 - positionYRelativeOfParent);
    }

    private void playAudioCurrentWord() {
        mPresenter.playAudio(mCategory, mCurrentWord);
    }

    private void userSelectedIncorrectWord(TextView textView) {
        //mCurrentlyAnimating = true;

        //Shake view
        ObjectAnimator
                .ofFloat(textView, "translationX", 0.0f, 40.0f, -40.0f, 20.0f, -20.0f, 6.0f, -6.0f, 0.0f)
                .setDuration(700)
                .start();
        //Play audio incorrect
        AudioPlayerHelper.getInstance().playInCorrect();

        //Disable and reduced alpha to 0.4 (as disable)
        textView.animate().alpha(0.4f).setDuration(400).start();
        textView.setEnabled(false);

        if (chooseCallBack != null) {
            chooseCallBack.chooseIncorrect();
        }


        //After 0.5s Update instructions listen view tag
//        handlerSchedule.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                updateInstructions(InstructionsContent.listen);
//            }
//        }, 500);
//
//        //After 0.6s play current word
//        handlerSchedule.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                playAudioCurrentWord();
//            }
//        }, 800);
//
//        //After 0.7s enable click
//        handlerSchedule.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mCurrentlyAnimating = false;
//            }
//        }, 900);
//
//        //Update instruction
//        handlerSchedule.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                updateInstructions(InstructionsContent.chooseWord);
//            }
//        }, 2200);

    }


    @Override
    public void onStop() {
        super.onStop();
        handlerForFlyCoin.removeCallbacksAndMessages(null);
    }

    @Override
    public void onDestroy() {
        answersCallback = null;
        chooseCallBack = null;
        handlerSchedule.removeCallbacksAndMessages(null);
        layoutAnswers = null;
        for(TextView textView: answerTextViews){
            textView.setOnClickListener(null);
        }
        answerTextViews = null;
        mCurrentWord = null;
        mListAnswers = null;
        handlerForInstruction = null;
        handlerForFlyCoin = null;
        handlerSchedule = null;
        super.onDestroy();
        //Make sure clear memory
        Runtime.getRuntime().gc();

    }

    @Override
    protected void makeView() {
        mView = this;
    }

    @Override
    protected void makePresenter() {
        mPresenter = new AnswersPresenter();
    }

    public interface AnswersCallback {
        void updateInstruction(InstructionsContent instructionsContent);

        void animateCoinFlyToPiggyBank(int positionXCorrect, int positionYCorrect, boolean isCoinGold);

        void requiredNewWord();

        void goToBank();
    }

    public interface ChooseCallBack {
        void chooseCorrect(int guessCount);

        void chooseIncorrect();
    }
}
