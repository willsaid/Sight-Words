package com.hearatale.sightword.ui.sentence.content;

import com.hearatale.sightword.Application;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.ChangeBounds;
import android.support.transition.Fade;
import android.support.transition.Slide;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v4.app.Fragment;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hearatale.sightword.R;
import com.hearatale.sightword.data.model.phonics.SentenceModel;
import com.hearatale.sightword.data.model.phonics.SightWordModel;
import com.hearatale.sightword.service.AudioPlayerHelper;
import com.hearatale.sightword.ui.custom_view.PHListener;
import com.hearatale.sightword.ui.custom_view.PHTransitionListener;
import com.hearatale.sightword.utils.DebugLog;
import com.hearatale.sightword.utils.Helper;
import com.hearatale.sightword.utils.Utils;
import com.hearatale.sightword.utils.glide.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SentenceContentFragment extends Fragment {

    private static final int TIME_ANIMATION = 500;//ms

    private static final String ARG_SIGHT_WORD = "ARG_SIGHT_WORD";
    private static final String ARG_IMAGE_FOCUS_SIZE = "ARG_IMAGE_FOCUS_SIZE";

    @BindView(R.id.layout_sentence)
    ConstraintLayout layoutSentence;

    // first
    @BindView(R.id.layout_first)
    ConstraintLayout layoutFirst;

    @BindView(R.id.image_first)
    ImageView imageFirst;

    @BindView(R.id.text_first)
    TextView textFirst;


    // second
    @BindView(R.id.layout_second)
    ConstraintLayout layoutSecond;

    @BindView(R.id.image_second)
    ImageView imageSecond;

    @BindView(R.id.text_second)
    TextView textSecond;

    SightWordModel mSightWord = new SightWordModel();

    FocusContentListener mListener;

    AudioFocusListener mAudioFocusListener;

    int[] mImageFocusSize = new int[2];

    public SentenceContentFragment() {
        // Required empty public constructor
    }

    public static SentenceContentFragment newInstance(SightWordModel sightWord, int[] imageFocusSize) {
        SentenceContentFragment fragment = new SentenceContentFragment();
        Bundle args = new Bundle();
        args.putIntArray(ARG_IMAGE_FOCUS_SIZE, imageFocusSize);
        args.putParcelable(ARG_SIGHT_WORD, sightWord);
        fragment.setArguments(args);
        return fragment;
    }

    public void setListener(FocusContentListener listener) {
        this.mListener = listener;
    }

    public void setAudioFocusListener(AudioFocusListener audioFocusListener) {
        this.mAudioFocusListener = audioFocusListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSightWord = getArguments().getParcelable(ARG_SIGHT_WORD);

            if (mSightWord == null) {
                mSightWord = new SightWordModel();
            }

            mImageFocusSize = getArguments().getIntArray(ARG_IMAGE_FOCUS_SIZE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sentence_content, container, false);
        ButterKnife.bind(this, rootView);

        initViews();

        return rootView;
    }

    Handler mHandler = new Handler();

    private void initViews() {

        updateFocusView(true);
        updateFocusView(false);

        repeatCurrentWord(true);

    }

    public void repeatCurrentWord(final boolean animateFirstSentence) {
        repeatCurrentWord(animateFirstSentence, false);
    }

    public void repeatCurrentWord(final boolean animateFirstSentence, boolean immediately) {
        if (mAudioFocusListener != null) {
            mAudioFocusListener.onAudioPlaying();
        }
        mHandler.removeCallbacksAndMessages(null);
        // disable image click
        enableButton(imageFirst, false);
        enableButton(imageSecond, false);

        int startTime = 0; // ms


        if (animateFirstSentence) {

            startTime += 600;
        } else {

            int timeAnimation = immediately ? 0 : TIME_ANIMATION;

            // gone second
            goneImageSentence(timeAnimation, false);
            // show first sentence
            animateFocusSentence(timeAnimation, true, false);

            startTime += TIME_ANIMATION;
        }

        // play first sentence
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playAudio(mSightWord.getFirstSentence().getAudioFileName(), null);
            }
        }, startTime);

        startTime += Helper.getIntDurationAudioFromAssets(Application.Context, mSightWord.getFirstSentence().getAudioFileName() + ".mp3") + 1000;

        // gone first view
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goneFirstSentence();
            }
        }, startTime);

        startTime += 400;

        // show second sentence
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animateFocusSentence(false, true);
            }
        }, startTime);

        startTime += 500;

        // play second sentence
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playAudio(mSightWord.getSecondSentence().getAudioFileName(), null);
            }
        }, startTime);

        startTime += Helper.getIntDurationAudioFromAssets(Application.Context, mSightWord.getSecondSentence().getAudioFileName() + ".mp3") + 1000;

        // show 2 image of sentence
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animateImageSentence(true, null);

                animateImageSentence(false, new PHTransitionListener() {
                    @Override
                    public void onTransitionEnd(@NonNull Transition transition) {
                        enableButton(imageFirst, true);
                        enableButton(imageSecond, true);

                        if (mAudioFocusListener != null) {
                            mAudioFocusListener.onActionCompleted();
                        }
                    }
                });
            }
        }, startTime);


    }

    private void enableButton(View button, boolean show) {
        button.setEnabled(show);
        button.setFocusable(show);
    }

    private void playAudio(int delayTime, String fileName, PHListener.AudioListener listener) {
        AudioPlayerHelper.getInstance().playAudio(fileName, listener);
        if (mListener != null) {
            mListener.onAnimateWord();
        }
    }

    private void playAudio(String fileName, PHListener.AudioListener listener) {
        playAudio(0, fileName, listener);
    }

    @OnClick(R.id.image_second)
    void showSecondSentence() {
        showFocusSentence(false, false);
    }


    @OnClick(R.id.image_first)
    void showFirstSentence() {
        showFocusSentence(true, false);
    }


    private void showFocusSentence(final boolean showFirst, final boolean animateSlide) {
        enableButton(imageFirst, false);
        enableButton(imageSecond, false);
        if (mAudioFocusListener != null) {
            mAudioFocusListener.onAudioPlaying();
        }
        int startTime = 0;

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (showFirst) {
                    animateFocusSentence(true, animateSlide);
                    goneImageSentence(false);

                } else {
                    animateFocusSentence(false, animateSlide);
                    goneImageSentence(true);
                }
            }
        }, startTime);

        startTime += 500;

        final String fileName = showFirst
                ? mSightWord.getFirstSentence().getAudioFileName()
                : mSightWord.getSecondSentence().getAudioFileName();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playAudio(fileName, null);
            }
        }, startTime);

        startTime += Helper.getIntDurationAudioFromAssets(Application.Context, fileName + ".mp3") + 1000;

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animateImageSentence(true, null);
                animateImageSentence(false, new PHTransitionListener() {
                    @Override
                    public void onTransitionEnd(@NonNull Transition transition) {
                        enableButton(imageFirst, true);
                        enableButton(imageSecond, true);

                        if (mAudioFocusListener != null) {
                            mAudioFocusListener.onActionCompleted();
                        }
                    }
                });

            }
        }, startTime);
    }

    private void initControls() {

    }

    private Spanned getWordDecorator(String text, String highlight) {
        return Utils.getMatchesSpannedFromString(text, highlight,
                getResources().getColor(R.color.colorAccent));
    }

    private void updateFocusView(boolean first) {

        SentenceModel sentence;
        ImageView iv;
        TextView tv;
        if (first) {
            sentence = mSightWord.getFirstSentence();
            iv = imageFirst;
            tv = textFirst;
        } else {
            sentence = mSightWord.getSecondSentence();
            iv = imageSecond;
            tv = textSecond;
        }

        GlideApp.with(Application.Context)
                .load(sentence.getImageFileName())
                .override(mImageFocusSize[0], mImageFocusSize[1])
                .into(iv);


        tv.setText(getWordDecorator(sentence.getText(), sentence.getHighlightWord()));
    }


    private void goneFirstSentence() {
        TransitionSet transitionSet = new TransitionSet()
                .setDuration(500)
                .addTransition(new Fade())
                .addTransition(new Slide(Gravity.START));

        TransitionManager.beginDelayedTransition(layoutFirst, transitionSet);

        layoutFirst.setVisibility(View.GONE);
    }

    private void goneSecondSentence() {
        TransitionManager.beginDelayedTransition(layoutSecond, new Slide(Gravity.START));
        layoutSecond.setVisibility(View.INVISIBLE);
    }

    private void animateFocusSentence(boolean first, boolean animateSlide) {
        animateFocusSentence(TIME_ANIMATION, first, animateSlide);
    }

    private void animateFocusSentence(int timeDuration, boolean first, boolean animateSlide) {

        ConstraintLayout viewGroup = first ? layoutFirst : layoutSecond;
        ImageView imageView = first ? imageFirst : imageSecond;
        TextView textView = first ? textFirst : textSecond;

        scaleAnimationView(imageView, true, null);

        TransitionSet transitionSet = new TransitionSet()
                .setDuration(timeDuration)
                .addTransition(new Fade())
                .addTransition(new ChangeBounds());

        if (animateSlide) {
            transitionSet.addTransition(new Slide(Gravity.END));
            textView.setVisibility(View.VISIBLE);
        }
        TransitionManager.beginDelayedTransition(viewGroup, transitionSet);

        if (viewGroup.getVisibility() != View.VISIBLE) {
            viewGroup.setVisibility(View.VISIBLE);
        }

        if (!animateSlide) {
            textView.setVisibility(View.VISIBLE);
        }

        ConstraintSet set = new ConstraintSet();
        set.clone(viewGroup);
        set.setHorizontalBias(imageView.getId(), 0.05f);
        set.applyTo(viewGroup);
    }

    private void animateImageSentence(boolean first, PHTransitionListener listener) {

        ConstraintLayout viewGroup = first ? layoutFirst : layoutSecond;
        ImageView imageView = first ? imageFirst : imageSecond;
        TextView textView = first ? textFirst : textSecond;
        scaleAnimationView(imageView, false, null);
        float horBias = first ? 0.15f : 0.8f;


        TransitionSet transitionSet = new TransitionSet()
                .addTransition(new Fade())
                .addTransition(new ChangeBounds());

        if (listener != null) {
            transitionSet.addListener(listener);
        }
        TransitionManager.beginDelayedTransition(viewGroup, transitionSet);

        if (viewGroup.getVisibility() != View.VISIBLE) {
            viewGroup.setVisibility(View.VISIBLE);
        }
        textView.setVisibility(View.GONE);

        ConstraintSet set = new ConstraintSet();
        set.clone(viewGroup);
        set.setHorizontalBias(imageView.getId(), horBias);
        set.applyTo(viewGroup);
    }

    private void goneImageSentence(boolean first) {
        goneImageSentence(TIME_ANIMATION, first);
    }

    private void goneImageSentence(int timeDuration, boolean first) {
        ConstraintLayout viewGroup = first ? layoutFirst : layoutSecond;
        ImageView imageView = first ? imageFirst : imageSecond;
        TextView textView = first ? textFirst : textSecond;

        TransitionSet transitionSet = new TransitionSet()
                .setDuration(timeDuration)
                .addTransition(new Fade())
                .addTransition(new ChangeBounds());

        TransitionManager.beginDelayedTransition(viewGroup, transitionSet);
        viewGroup.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        DebugLog.e("");
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        AudioPlayerHelper.getInstance().stopPlayer();

        if (null != mListener) {
            mListener.onStopView();
        }
        super.onDestroyView();

    }

    private void scaleAnimationView(final View v, boolean isZoomIn, @Nullable final PHListener.AnimationListener listener) {
        v.animate().scaleX(isZoomIn ? 1.15f : 1.0f).scaleY(isZoomIn ? 1.15f : 1.0f).setDuration(200).withEndAction(new Runnable() {
            @Override
            public void run() {
                if (null != listener) {
                    listener.onAnimationEnd();
                }
            }
        });
    }

    public interface FocusContentListener {
        void onAnimateWord();

        void onStopView();
    }

    public interface AudioFocusListener {
        void onAudioPlaying();

        void onActionCompleted();
    }
}
