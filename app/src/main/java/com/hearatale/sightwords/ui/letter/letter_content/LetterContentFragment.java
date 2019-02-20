package com.hearatale.sightwords.ui.letter.letter_content;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.hearatale.sightwords.R;
import com.hearatale.sightwords.data.AppDataManager;
import com.hearatale.sightwords.data.model.event.ProgressPuzzleEvent;
import com.hearatale.sightwords.data.model.phonics.letters.LetterModel;
import com.hearatale.sightwords.data.model.phonics.letters.SimpleLetterModel;
import com.hearatale.sightwords.data.model.phonics.letters.TimedAudioInfoModel;
import com.hearatale.sightwords.data.model.typedef.DifficultyDef;
import com.hearatale.sightwords.service.AudioPlayerHelper;
import com.hearatale.sightwords.ui.simple_alphabet.SimpleAlphabetPresenter;
import com.hearatale.sightwords.utils.Config;
import com.hearatale.sightwords.utils.Helper;
import com.hearatale.sightwords.utils.glide.GlideApp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class LetterContentFragment extends Fragment {

    private static String ARG_LETTER_MODE = "ARG_LETTER_MODE";

    @BindView(R.id.parent_layout)
    ConstraintLayout parentLayout;

    @BindView(R.id.text_view_letter)
    TextView textViewLetter;

    @BindView(R.id.text_view_first)
    TextView textViewFirst;

    @BindView(R.id.text_view_second)
    TextView textViewSecond;

    @BindView(R.id.text_view_third)
    TextView textViewThird;

    @BindView(R.id.image_first)
    ImageView imageViewFirst;

    @BindView(R.id.image_second)
    ImageView imageViewSecond;

    @BindView(R.id.image_third)
    ImageView imageViewThird;

    @BindView(R.id.check_mark)
    ImageView checkMark;

    @BindView(R.id.layout_first_word)
    LinearLayoutCompat layoutFirstWord;

    @BindView(R.id.layout_second_word)
    LinearLayoutCompat layoutSecondWord;

    @BindView(R.id.layout_third_word)
    LinearLayoutCompat layoutThirdWord;

    @BindView(R.id.frame_layout_letter)
    ConstraintLayout layoutLetter;

    LetterModel mLetterModel;
    private boolean mCurrentPlaying = false;

    UpdateViewListener updateViewListener;

    boolean isValidView;

    private static int mLetterModeDef = DifficultyDef.EASY;

    SimpleAlphabetPresenter mAlphabetPresenter;
    List<SimpleLetterModel> mListItem;

    public void setUpdateViewListener(UpdateViewListener updateViewListener) {
        this.updateViewListener = updateViewListener;
    }

    public LetterContentFragment() {
        // Required empty public constructor
    }

    public TextView getTextViewLetter() {
        return textViewLetter;
    }

    public void setTextViewLetter(TextView textViewLetter) {
        this.textViewLetter = textViewLetter;
    }

    public static LetterContentFragment newInstance(LetterModel letterModel, int modeDef) {
        mLetterModeDef = modeDef;
        LetterContentFragment fragment = new LetterContentFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_LETTER_MODE, letterModel);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLetterModel = getArguments().getParcelable(ARG_LETTER_MODE);
        }
        mAlphabetPresenter = new SimpleAlphabetPresenter();
        mListItem = mAlphabetPresenter.getLetterByMode(mLetterModeDef);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_letter_content, container, false);
        ButterKnife.bind(this, rootView);
        isValidView = true;
        initViews();
        visibleView();
        return rootView;
    }

    private void initViews() {

        if (mLetterModeDef == DifficultyDef.EASY) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(parentLayout);
            constraintSet.centerHorizontally(layoutLetter.getId(), parentLayout.getId());
            constraintSet.centerVertically(layoutLetter.getId(), parentLayout.getId());
            constraintSet.applyTo(parentLayout);
        }

        //Text view
        String letterText = mLetterModel.getDisplayString().toLowerCase();

        if (mLetterModeDef == DifficultyDef.EASY) {
            if (letterText.contains("qu")) {
                textViewLetter.setText("Q q");
            }else {
                textViewLetter.setText(letterText.toUpperCase() + " " + letterText);
            }
            String displayLetter = mLetterModel.getSourceLetter() + "-" + mLetterModel.getSoundId();
            int starCount = AppDataManager.getInstance().getCompletedPuzzlePieces(displayLetter + "ALPHABET_LETTERS_Stars").size();
            checkMark.setVisibility(starCount >= 5 ? View.VISIBLE : View.GONE);
        } else {
            textViewLetter.setText(letterText);


            Spanned firstWord = getWordDecorator(0);
            textViewFirst.setText(firstWord);
            Spanned secondWord = getWordDecorator(1);
            textViewSecond.setText(secondWord);
            Spanned thirdWord = getWordDecorator(2);
            textViewThird.setText(thirdWord);
            //ImageView
            String firstPath = Config.IMAGES_WORD_BY_LETTER_PATH +  mLetterModel.getPrimaryWords().get(0).getText() + ".jpg";
            GlideApp.with(this).load(Uri.parse(firstPath)).into(imageViewFirst);

            String secondPath = Config.IMAGES_WORD_BY_LETTER_PATH +  mLetterModel.getPrimaryWords().get(1).getText() + ".jpg";
            GlideApp.with(this).load(Uri.parse(secondPath)).into(imageViewSecond);

            String thirdPath = Config.IMAGES_WORD_BY_LETTER_PATH + mLetterModel.getPrimaryWords().get(2).getText() + ".jpg";
            GlideApp.with(this).load(Uri.parse(thirdPath)).into(imageViewThird);

            String displayLetter = mLetterModel.getSourceLetter() + "-" + mLetterModel.getSoundId();
            int size = AppDataManager.getInstance().getCompletedPuzzlePieces(displayLetter).size();
            checkMark.setVisibility(size == Config.PUZZLE_COLUMNS * Config.PUZZLE_ROWS ? View.VISIBLE : View.GONE);
        }
    }

    public void visibleView() {
        //Hidden all views contain words
        setupAlphaForLayoutWord(false);
        schedulePlaySoundAndAnimation();
    }

    private void setupAlphaForLayoutWord(boolean isVisible) {
        float alpha = isVisible ? 1 : 0;
        layoutFirstWord.setAlpha(alpha);
        layoutSecondWord.setAlpha(alpha);
        layoutThirdWord.setAlpha(alpha);
    }

    private Spanned getWordDecorator(int i) {
        String text = mLetterModel.getPrimaryWords().get(i).getText();
        return AppDataManager.getInstance()
                .decorateWord(text,
                        mLetterModel.getDisplayString().toLowerCase(),
                        getResources().getColor(R.color.colorAccent), mLetterModel.getSoundId());
    }

    private void schedulePlaySoundAndAnimation() {
        mCurrentPlaying = true;
        //Ensure animation replace fragment end (500ms) +  500ms delay
        int startTime = 1000;
        int timeBetween = 850;

        if (mLetterModeDef == DifficultyDef.STANDARD) {
            if (updateViewListener != null) updateViewListener.showButtonQuiz(false);
        } else {
            if (updateViewListener != null) updateViewListener.showButtonQuiz(true);
        }

        animationAndPlayAudio(layoutLetter, startTime, mLetterModel.getPronunciationTiming(), null);
        final int durationPronunciation = convertSecondStringToMilliSeconds(mLetterModel.getPronunciationTiming().getWordDuration());
        startTime += (durationPronunciation == 0 ? 500 : durationPronunciation) + timeBetween;

        if (mLetterModeDef == DifficultyDef.EASY) {
            mCurrentPlaying = false;
            return;
        }

        TimedAudioInfoModel timedAudioInfo = mLetterModel.getPrimaryWords().get(0).getTimedAudioInfo();
        final int durationFirstWord = convertSecondStringToMilliSeconds(timedAudioInfo.getWordDuration());
        animationAndPlayAudio(layoutFirstWord, startTime, timedAudioInfo, null);
        startTime += (durationFirstWord) + timeBetween;

        TimedAudioInfoModel timedAudioInfo1 = mLetterModel.getPrimaryWords().get(1).getTimedAudioInfo();
        final int durationSecondWord = convertSecondStringToMilliSeconds(timedAudioInfo1.getWordDuration());
        animationAndPlayAudio(layoutSecondWord, startTime, timedAudioInfo1, null);
        startTime += (durationSecondWord) + timeBetween;

        TimedAudioInfoModel timedAudioInfo2 = mLetterModel.getPrimaryWords().get(2).getTimedAudioInfo();
        animationAndPlayAudio(layoutThirdWord, startTime, timedAudioInfo2, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (updateViewListener != null) {
                    updateViewListener.showButtonQuiz(true);
                }
                mCurrentPlaying = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void animationAndPlayAudio(final View view, int startTime, final TimedAudioInfoModel audioInfo, final Animation.AnimationListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isValidView) return;
                if (mLetterModeDef == DifficultyDef.STANDARD) {
                    String path = generatorPrefixPath(audioInfo);
                    AudioPlayerHelper.getInstance().playAudio(path, audioInfo);
                    zoomInOutAndFade(view, convertSecondStringToMilliSeconds(audioInfo.getWordDuration()), listener);
                } else {
                    SimpleLetterModel letterSimple = null;
                    for(SimpleLetterModel letterM: mListItem) {
                        if (letterM.getLetter().equals(mLetterModel.getSourceLetter())) {
                            letterSimple = letterM;
                            break;
                        }
                    }
                    TimedAudioInfoModel audioInfo = letterSimple.getAudioInfo();

                    //Audio info is null then get sound name form assets
                    String prefixPath = Config.AUDIO_WORDS_SETS_PATH;
                    if (audioInfo.getFileName().contains("extra")) {
                        prefixPath = Config.AUDIO_SOUND_EXTRA_PATH;
                    }
                    if (audioInfo == null || TextUtils.isEmpty(audioInfo.getFileName())) {
                        initSourceLetterTiming(letterSimple);
                        prefixPath = Config.AUDIO_SOUND_EXTRA_PATH;
                    }
                    audioInfo = letterSimple.getAudioInfo();

                    AudioPlayerHelper.getInstance().playAudio(prefixPath, audioInfo);
                }
            }
        }, startTime);
    }

    private void initSourceLetterTiming(SimpleLetterModel letter) {
        //if the letter doesn't exist in the sound files, it should be in "assets > Audio > Sounds > Extra Letters"
        TimedAudioInfoModel audioInfo = new TimedAudioInfoModel();
        audioInfo.setFileName("extra-letter-" + letter.getLetter().toUpperCase());
        String pathExtraLetterAudioFile = Config.AUDIO_BY_LETTER_PATH + audioInfo.getFileName() + ".mp3";
        float duration = Helper.getDurationAudioFromAssets(this.getActivity(), pathExtraLetterAudioFile);
        audioInfo.setWordStart("0.0");
        audioInfo.setWordDuration(duration + "");
        letter.setAudioInfo(audioInfo);
    }

    @NonNull
    private String generatorPrefixPath(TimedAudioInfoModel audioInfo) {
        String path = Config.AUDIO_WORDS_SETS_PATH;

        //if file name not exist
        if (TextUtils.isEmpty(audioInfo.getFileName())) {
            //Generator file name
            audioInfo.setFileName("words-" + mLetterModel.getSourceLetter().toUpperCase() + "-" + mLetterModel.getSoundId());
        }

        if (audioInfo.getFileName().contains("/")) {
            path = Config.AUDIO_ROOT_PATH;
        }
        return path;
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
    public void onDestroy() {
        updateViewListener = null;
        isValidView = false;
        AudioPlayerHelper.getInstance().stopPlayer();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private int convertSecondStringToMilliSeconds(String durationString) {
        if (TextUtils.isEmpty(durationString)) return 0;
        return (int) (Float.parseFloat(durationString) * 1000);
    }

    public interface UpdateViewListener {
        void showButtonQuiz(boolean show);

    }

    public void onRepeat() {
        if (mCurrentPlaying) return;
//        schedulePlaySoundAndAnimation();
        animationAndPlayAudio(layoutLetter, 0, mLetterModel.getPronunciationTiming(), null);
    }

    @OnClick(R.id.frame_layout_letter)
    void playAudioAndAnimationLetter() {
        if (mCurrentPlaying) return;
        animationAndPlayAudio(layoutLetter, 0, mLetterModel.getPronunciationTiming(), null);
    }

    @OnClick(R.id.layout_first_word)
    void playAudioAndAnimationFirstWord() {
        if (mCurrentPlaying) return;
        TimedAudioInfoModel timedAudioInfo = mLetterModel.getPrimaryWords().get(0).getTimedAudioInfo();
        animationAndPlayAudio(layoutFirstWord, 0, timedAudioInfo, null);
    }

    @OnClick(R.id.layout_second_word)
    void playAudioAndAnimationSecondWord() {
        if (mCurrentPlaying) return;
        TimedAudioInfoModel timedAudioInfo = mLetterModel.getPrimaryWords().get(1).getTimedAudioInfo();
        animationAndPlayAudio(layoutSecondWord, 0, timedAudioInfo, null);
    }

    @OnClick(R.id.layout_third_word)
    void playAudioAndAnimationThirdWord() {
        if (mCurrentPlaying) return;
        TimedAudioInfoModel timedAudioInfo = mLetterModel.getPrimaryWords().get(2).getTimedAudioInfo();
        animationAndPlayAudio(layoutThirdWord, 0, timedAudioInfo, null);
    }

    @Override
    public void onStop() {
        AudioPlayerHelper.getInstance().stopPlayer();
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProgressPuzzle(ProgressPuzzleEvent event) {
        if (AppDataManager.getInstance().isPuzzleCompleted(event.getSourceLetterSoundId())) {
            checkMark.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }
}
