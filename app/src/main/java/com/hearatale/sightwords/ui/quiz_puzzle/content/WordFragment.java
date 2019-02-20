package com.hearatale.sightwords.ui.quiz_puzzle.content;

import com.hearatale.sightwords.Application;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.ChangeBounds;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.target.CustomViewTarget;
import com.hearatale.sightwords.R;
import com.hearatale.sightwords.data.AppDataManager;
import com.hearatale.sightwords.data.model.phonics.letters.LetterModel;
import com.hearatale.sightwords.data.model.phonics.letters.TimedAudioInfoModel;
import com.hearatale.sightwords.data.model.phonics.letters.WordModel;
import com.hearatale.sightwords.data.model.typedef.DifficultyDef;
import com.hearatale.sightwords.service.AudioPlayerHelper;
import com.hearatale.sightwords.ui.custom_view.PHTransitionListener;
import com.hearatale.sightwords.ui.quiz_sight_words.answers.AnswersPresenter;
import com.hearatale.sightwords.utils.Config;
import com.hearatale.sightwords.utils.DebugLog;
import com.hearatale.sightwords.utils.Helper;
import com.hearatale.sightwords.utils.glide.GlideApp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.constraint.ConstraintSet.BOTTOM;
import static android.support.constraint.ConstraintSet.END;
import static android.support.constraint.ConstraintSet.START;
import static android.support.constraint.ConstraintSet.TOP;


public class WordFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    private static final String ARG_LETTER = "ARG_LETTER";
    private static final String ARG_PUZZLE_RANDOM = "ARG_PUZZLE_RANDOM";
    private static final String ARG_SELECTED_WORDS = "ARG_SELECTED_WORDS";
    private static final String ARG_WORD_SIZE = "ARG_WORD_SIZE";

    // parent view
    @BindView(R.id.layout_letter)
    ConstraintLayout layoutLetter;

    // top left
    @BindView(R.id.layout_standard_top_left)
    View layoutStandardTopLeft;

    // top right
    @BindView(R.id.layout_standard_top_right)
    View layoutStandardTopRight;

    // ez
    @BindView(R.id.layout_top_ez)
    View layoutTopEz;

    // bot left
    @BindView(R.id.layout_letter_bot_left)
    View layoutLetterBotLeft;

    // bot right
    @BindView(R.id.layout_letter_bot_right)
    View layoutLetterBotRight;

    LetterModel mLetter = new LetterModel();
    List<WordModel> mSelectedWords = new ArrayList<>();
    boolean mPuzzleRandom = false;

    Map<View, WordModel> mapViewWord = new LinkedHashMap<>();

    SelectedWordListener mListener;
    int mPuzzleCompleted = 2;

    SelectedWordLifeCycleListener mLifeCycleListener;

    private int[] mWordSize = new int[2];

    int mLetterModeDef = DifficultyDef.EASY;

    int mGuessCount = 0;

    AnswersPresenter mPresenter;

    private String appFeature;

    Handler handlerSchedule = new Handler();
    Handler handlerForFlyCoin = new Handler();


    public WordFragment() {
        // Required empty public constructor
    }

    public static WordFragment newInstance(LetterModel letter, List<WordModel> selectedWords, boolean puzzleRandom, int[] wordSize, int mLetterModeDef) {
        WordFragment fragment = new WordFragment();
        Bundle args = new Bundle();
        ArrayList<WordModel> words = new ArrayList<>(selectedWords);
        args.putParcelableArrayList(ARG_SELECTED_WORDS, words);
        args.putParcelable(ARG_LETTER, letter);
        args.putBoolean(ARG_PUZZLE_RANDOM, puzzleRandom);
        args.putIntArray(ARG_WORD_SIZE, wordSize);
        args.putInt("ARG_MODE", mLetterModeDef);
        fragment.setArguments(args);
        return fragment;
    }

    public void setSelectedWordListener(SelectedWordListener listener) {
        this.mListener = listener;
    }

    public void setLifeCycleListener(SelectedWordLifeCycleListener lifeCycleListener) {
        this.mLifeCycleListener = lifeCycleListener;
    }

    public Map<View, WordModel> getMapViewWord() {
        return mapViewWord;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new AnswersPresenter();
        if (getArguments() != null) {
            mLetter = getArguments().getParcelable(ARG_LETTER);
            mPuzzleRandom = getArguments().getBoolean(ARG_PUZZLE_RANDOM, false);
            mSelectedWords = getArguments().getParcelableArrayList(ARG_SELECTED_WORDS);
            mWordSize = getArguments().getIntArray(ARG_WORD_SIZE);
            mLetterModeDef = getArguments().getInt("ARG_MODE");
            if (mLetterModeDef == DifficultyDef.EASY) {
                getLetterAnswerChoices();
            }
            if (mLetterModeDef == DifficultyDef.EASY) {
                appFeature = "ALPHABET_LETTERS";
            } else {
                appFeature = "PHONICS";
            }
        }
    }

    public void getLetterAnswerChoices() {
        List<String> answerChoices = new ArrayList<>();
        answerChoices.add(mLetter.getSourceLetter());
        //random incorrect letter choices
        Random random = new Random();
        String randomLetter;
        int index = 0;
        while (index < 3) {
            randomLetter = (char)('A' + random.nextInt(25)) + "";
            if (!answerChoices.contains(randomLetter)) {
                answerChoices.add(randomLetter);
                index++;
            }
        }

        for (int i = 0; i < mSelectedWords.size(); i++) {
            mSelectedWords.get(i).setAnswer(false);
            mSelectedWords.get(i).setText(answerChoices.get(i));
        }

        mSelectedWords.get(0).setAnswer(true);
        Collections.shuffle(mSelectedWords);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_word, container, false);
        ButterKnife.bind(this, rootView);

        initViews();

        initControls();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (null != mLifeCycleListener) {
            mLifeCycleListener.onViewCreated();
        }
    }

    @Override
    public void onDestroyView() {
        if (null != mLifeCycleListener) {
            mLifeCycleListener.onViewDestroyView();
            mLifeCycleListener = null;
        }

        mLetter = null;

        if (mSelectedWords != null) {
            mSelectedWords.clear();
            mSelectedWords = null;
        }

        if (mapViewWord != null) {
            mapViewWord.clear();
            mapViewWord = null;
        }

//        if (mListener != null) {
//            mListener = null;
//        }
        super.onDestroyView();

    }


    private void initViews() {

        if (mPuzzleRandom || mLetterModeDef == DifficultyDef.EASY) {
            // show 4 piece
            layoutTopEz.setVisibility(View.GONE);
            layoutStandardTopLeft.setVisibility(View.VISIBLE);
            layoutStandardTopRight.setVisibility(View.VISIBLE);

            mapViewWord.put(layoutStandardTopLeft, new WordModel());
            mapViewWord.put(layoutStandardTopRight, new WordModel());
        } else {
            // show 3 piece
            layoutTopEz.setVisibility(View.VISIBLE);
            layoutStandardTopLeft.setVisibility(View.GONE);
            layoutStandardTopRight.setVisibility(View.GONE);

            mapViewWord.put(layoutTopEz, new WordModel());
        }

        mapViewWord.put(layoutLetterBotLeft, new WordModel());
        mapViewWord.put(layoutLetterBotRight, new WordModel());

        updatePuzzleLetter();

    }

    private void initControls() {

    }

    @Override
    public void onClick(final View v) {

        v.findViewById(R.id.text_letter).setVisibility(View.VISIBLE);

        // answer
        final WordModel selectedWord = mapViewWord.get(v);

        // play audio
        if (null != mListener) {
            mListener.onViewClick(v, selectedWord.getTimedAudioInfo(), selectedWord.isAnswer());


            if (selectedWord.isAnswer()) {
                // alpha all view and animate answer view to center
                if (v instanceof LinearLayoutCompat) {
                    TransitionManager.beginDelayedTransition((LinearLayoutCompat) v,
                            new ChangeBounds()
                                    .setDuration(500)
                                    .addListener(new PHTransitionListener() {
                                        @Override
                                        public void onTransitionEnd(@NonNull Transition transition) {

                                            AudioPlayerHelper.getInstance().playCorrect(new AudioPlayerHelper.CompletedListener() {
                                                @Override
                                                public void onCompleted() {
                                                    // get X,Y of correct Answer
                                                    int xCorrect = Helper.getRelativeLeft(v) + (int) v.getX() / 2;
                                                    int yCorrect = Helper.getRelativeTop(v) + (int) v.getY() / 2;

                                                    if (mListener != null) {
                                                        mListener.onCorrectAnswer(mPuzzleCompleted, xCorrect, yCorrect);
                                                    }

                                                    resetState();
                                                }
                                            });
                                        }
                                    }));
                }

                for (View view : mapViewWord.keySet()) {
                    view.setOnClickListener(null);

                    if (v.getId() == view.getId()) {
                        continue;
                    }
                    view.clearAnimation();
                    view.animate().cancel();
                    view.setVisibility(View.GONE);
                }

                ConstraintSet set = new ConstraintSet();
                set.clone(layoutLetter);
                set.connect(v.getId(), TOP, layoutLetter.getId(), TOP, 0);
                set.connect(v.getId(), START, layoutLetter.getId(), START, 0);
                set.connect(v.getId(), BOTTOM, layoutLetter.getId(), BOTTOM, 0);
                set.connect(v.getId(), END, layoutLetter.getId(), END, 0);
                set.applyTo(layoutLetter);


            } else {
                mPuzzleCompleted--;
                for (View view : mapViewWord.keySet()) {
                    if (v.getId() == view.getId()) {
                        continue;
                    }
                    view.clearAnimation();
                    view.animate().cancel();
                }
            }
        }

    }


    View mCurrentView;

    @Override
    public boolean onTouch(final View v, MotionEvent event) {
//        DebugLog.e("EVENT: " + event.getAction());

        final WordModel selectedWord = mapViewWord.get(v);

        if (mListener != null) {

//            for (View view : mapViewWord.keySet()) {
//                if (v.getId() == view.getId()) {
//                    continue;
//                }
//                view.clearAnimation();
//                view.animate().cancel();
//            }
            mListener.onLetterClick();
            switch (event.getAction()) {

                case MotionEvent.ACTION_CANCEL:
                    if (mCurrentView != null) {
                        mListener.onZoomOut(mCurrentView, mapViewWord.get(mCurrentView).getTimedAudioInfo());
                        mCurrentView = null;
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    if (mLetterModeDef == DifficultyDef.EASY) {
                        mListener.onPlayAudio("Audio/Sounds/sound-" + mLetter.getSourceLetter());
                    } else {
                        mListener.onPlayAudio(selectedWord.getTimedAudioInfo());
                    }                    if (null == mCurrentView) {
                        mCurrentView = v;
                        mListener.onZoomIn(mCurrentView);
                    }

//                    else if (!mCurrentView.equals(v)) {
//                        mListener.onZoomOut(mCurrentView, mapViewWord.get(mCurrentView).getTimedAudioInfo());
//                        mCurrentView = v;
//                        mListener.onZoomIn(mCurrentView);
//                    }

                    break;

                case MotionEvent.ACTION_MOVE:
                    if (null == mCurrentView) {
                        mCurrentView = v;
                        mListener.onZoomIn(mCurrentView);
                        if (mLetterModeDef == DifficultyDef.EASY) {
                            mListener.onPlayAudio("Audio/Sounds/sound-" + mLetter.getSourceLetter());
                        } else {
                            mListener.onPlayAudio(selectedWord.getTimedAudioInfo());
                        }
                    } else if (!mCurrentView.equals(v)) {
                        // touch on other view zoom out
                        // remove old
                        mListener.onZoomOut(mCurrentView, mapViewWord.get(mCurrentView).getTimedAudioInfo());

                        // play new
                        mCurrentView = v;
                        if (mLetterModeDef == DifficultyDef.EASY) {
                            mListener.onPlayAudio("Audio/Sounds/sound-" + mLetter.getSourceLetter());
                        } else {
                            mListener.onPlayAudio(selectedWord.getTimedAudioInfo());
                        }                        mListener.onZoomIn(mCurrentView);
                    }

                    break;

                case MotionEvent.ACTION_UP:
                    //TODO show text from bot to top
                    TextView textView = v.findViewById(R.id.text_letter);
                    if (textView.getVisibility() != View.VISIBLE) {
                        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, textView.getHeight() / 2, 0);
                        translateAnimation.setDuration(400);
                        translateAnimation.setFillAfter(true);

                        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
                        textView.setAlpha(1f);
                        alphaAnimation.setDuration(400);
                        alphaAnimation.setFillAfter(true);


                        final AnimationSet animationSet = new AnimationSet(false);
                        animationSet.setInterpolator(new DecelerateInterpolator());
                        animationSet.addAnimation(translateAnimation);
                        animationSet.addAnimation(alphaAnimation);

                        textView.startAnimation(animationSet);
                        textView.setVisibility(View.VISIBLE);
                    }

//                    v.findViewById(R.id.text_letter).setVisibility(View.VISIBLE);


                    if (selectedWord.isAnswer()) {
                        // alpha all view and animate answer view to center
                        if (v instanceof LinearLayoutCompat) {
                            TransitionManager.beginDelayedTransition((LinearLayoutCompat) v,
                                    new ChangeBounds()
                                            .setDuration(500)
                                            .addListener(new PHTransitionListener() {
                                                @Override
                                                public void onTransitionEnd(@NonNull Transition transition) {

                                                    AudioPlayerHelper.getInstance().playCorrect(new AudioPlayerHelper.CompletedListener() {
                                                        @Override
                                                        public void onCompleted() {
                                                            // get X,Y of correct Answer
                                                            int xCorrect = Helper.getRelativeLeft(v) + (int) v.getX() / 2;
                                                            int yCorrect = Helper.getRelativeTop(v) + (int) v.getY() / 2;

                                                            if (mListener != null) {
                                                                mListener.onCorrectAnswer(mPuzzleCompleted, xCorrect, yCorrect);
                                                                mGuessCount++;
                                                                animateFlyCoin();
                                                            }

                                                            resetState();
                                                        }
                                                    });
                                                }
                                            }));
                        }

                        for (View view : mapViewWord.keySet()) {
                            view.setOnClickListener(null);
                            view.setOnTouchListener(null);

                            if (v.getId() == view.getId()) {
                                continue;
                            }
                            view.clearAnimation();
                            view.animate().cancel();
                            view.setVisibility(View.GONE);
                        }

                        ConstraintSet set = new ConstraintSet();
                        set.clone(layoutLetter);
                        set.connect(v.getId(), TOP, layoutLetter.getId(), TOP, 0);
                        set.connect(v.getId(), START, layoutLetter.getId(), START, 0);
                        set.connect(v.getId(), BOTTOM, layoutLetter.getId(), BOTTOM, 0);
                        set.connect(v.getId(), END, layoutLetter.getId(), END, 0);
                        set.applyTo(layoutLetter);


                    } else {

                        mListener.onInCorrectAnswer();
                        mGuessCount++;
                        // shake
                        mListener.onShake(v);

                        mListener.onZoomOut(v, mapViewWord.get(v).getTimedAudioInfo());

                        mCurrentView = null;

                        mPuzzleCompleted--;
                    }
                    break;

            }
        }

        return true;
    }

    public void animateFlyCoin() {


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

                if (mListener != null) {
                    int positionXCorrectAnswers = Helper.getRelativeLeft(layoutLetter) + layoutLetter.getWidth() / 2;
                    int positionYCorrectAnswers = Helper.getRelativeTop(layoutLetter) + layoutLetter.getHeight() / 2;

                    switch (mGuessCount) {
                        case 1:
                            mListener.animateCoinFlyToPiggyBank(positionXCorrectAnswers, positionYCorrectAnswers, true);
                            break;
                        case 2:
                            mListener.animateCoinFlyToPiggyBank(positionXCorrectAnswers, positionYCorrectAnswers, false);
                            break;
                    }
                    DebugLog.i("FLY");
                }
            }
        }, 0);


        //After 3s
        handlerSchedule.postDelayed(new Runnable() {
            @Override
            public void run() {
//                mCurrentlyAnimating = false;

                int goldCount = mPresenter.getTotalGoldCount(appFeature);
                int silverCount = mPresenter.getTotalSilverCount(appFeature);
                int totalGold = goldCount + silverCount / 2;

                if (totalGold > 0 && totalGold % 125 == 0) {
                    if (mListener != null) {
                        mListener.goToBank();
                    }
                }
//                else {
//                    if (mListener != null) {
//                        mListener.requiredNewWord();
//                    }
//                }
            }
        }, 1000);

    }

    private void resetState() {
        mPuzzleCompleted = 2;
    }


    public void updatePuzzleLetter() {

        View[] views = mapViewWord.keySet().toArray(new View[mapViewWord.keySet().size()]);
        for (int i = 0; i < views.length && Helper.isListValid(mSelectedWords); i++) {
            View view = views[i];
//            view.setOnClickListener(this);
            view.setOnTouchListener(this);

            final WordModel word = mSelectedWords.get(i);
            final ImageView imageView = view.findViewById(R.id.image_letter);
            TextView textView = view.findViewById(R.id.text_letter);
            textView.setVisibility(View.INVISIBLE);

            mapViewWord.put(view, word);
            String urlArtwork = Config.IMAGES_WORD_BY_LETTER_PATH + word.getText() + ".jpg";

            if (mLetterModeDef == DifficultyDef.STANDARD) {
                GlideApp
                        .with(Application.Context)
                        .load(urlArtwork)
                        .override(mWordSize[0], mWordSize[1])
                        .disallowHardwareConfig()
                        .into(new CustomViewTarget<ImageView, Drawable>(imageView) {
                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                DebugLog.e("");
                            }

                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                                DebugLog.e("");
                                imageView.setImageDrawable(resource);
                            }

                            @Override
                            protected void onResourceCleared(@Nullable Drawable placeholder) {
                                DebugLog.e("");
                            }
                        });

                Spanned wordText = AppDataManager
                        .getInstance()
                        .decorateWord(mSelectedWords.get(i).getText(),
                                mLetter.getDisplayString().toLowerCase(),
                                getResources().getColor(R.color.colorAccent), mLetter.getSoundId());
                textView.setText(wordText);
            } else {
                imageView.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                final TextView textView1 = view.findViewById(R.id.text_letter1);
                textView1.setVisibility(View.VISIBLE);

                textView1.setText(word.getText() + " " + word.getText().toLowerCase());
            }

            TimedAudioInfoModel audioInfo = word.getTimedAudioInfo();
            if (TextUtils.isEmpty(audioInfo.getFileName())) {
                audioInfo.setFileName("Words/" + word.getText());
                String pathExtraLetterAudioFile = Config.AUDIO_ROOT_PATH + audioInfo.getFileName() + ".mp3";
                float duration = Helper.getDurationAudioFromAssets(Application.Context, pathExtraLetterAudioFile);
                audioInfo.setWordStart("0.0");
                audioInfo.setWordDuration(duration + "");
            }

        }


    }


    public interface SelectedWordListener {
        void onCorrectAnswer(int totalPuzzleCompleted, int coordinateX, int coordinateY);

        void onInCorrectAnswer();

        void onViewClick(View v, TimedAudioInfoModel audioInfo, boolean isCorrectAnswer);

        void onLetterClick();

        void onPlayAudio(String path);

        void onPlayAudio(TimedAudioInfoModel audioInfo);

        void onZoomIn(View v);

        void onZoomOut(View v, TimedAudioInfoModel audioInfo);

        void onShake(View v);

        void animateCoinFlyToPiggyBank(int positionXCorrect, int positionYCorrect, boolean isCoinGold);

        void goToBank();
    }

    public interface SelectedWordLifeCycleListener {
        void onViewCreated();

        void onViewDestroyView();
    }
}
