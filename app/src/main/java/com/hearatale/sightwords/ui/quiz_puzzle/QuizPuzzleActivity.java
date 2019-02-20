package com.hearatale.sightwords.ui.quiz_puzzle;

import com.hearatale.sightwords.Application;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hearatale.sightwords.Application;
import com.hearatale.sightwords.R;
import com.hearatale.sightwords.data.Constants;
import com.hearatale.sightwords.data.model.phonics.QuizPuzzleLetterModel;
import com.hearatale.sightwords.data.model.phonics.letters.LetterModel;
import com.hearatale.sightwords.data.model.phonics.letters.PuzzlePieceModel;
import com.hearatale.sightwords.data.model.phonics.letters.SimpleLetterModel;
import com.hearatale.sightwords.data.model.phonics.letters.TimedAudioInfoModel;
import com.hearatale.sightwords.data.model.phonics.letters.WordModel;
import com.hearatale.sightwords.data.model.typedef.AnimationDef;
import com.hearatale.sightwords.data.model.typedef.DifficultyDef;
import com.hearatale.sightwords.service.AudioPlayerHelper;
import com.hearatale.sightwords.ui.bank.BankActivity;
import com.hearatale.sightwords.ui.base.activity.ActivityMVP;
import com.hearatale.sightwords.ui.base.fragment.SafeFragmentTransaction;
import com.hearatale.sightwords.ui.custom_view.PHAnimationListener;
import com.hearatale.sightwords.ui.custom_view.PHListener;
import com.hearatale.sightwords.ui.custom_view.PHPuzzleListener;
import com.hearatale.sightwords.ui.quiz_puzzle.content.PuzzleLetterFragment;
import com.hearatale.sightwords.ui.quiz_puzzle.content.WordFragment;
import com.hearatale.sightwords.ui.simple_alphabet.SimpleAlphabetActivity;
import com.hearatale.sightwords.ui.simple_alphabet.SimpleAlphabetPresenter;
import com.hearatale.sightwords.utils.Config;
import com.hearatale.sightwords.utils.DebugLog;
import com.hearatale.sightwords.utils.Helper;
import com.hearatale.sightwords.utils.ImageHelper;
import com.hearatale.sightwords.utils.glide.GlideApp;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuizPuzzleActivity extends ActivityMVP<QuizPuzzlePresenter, IQuizPuzzle> implements IQuizPuzzle {

    public static volatile boolean PLAYING = false;

    public static int REQUEST_CODE = 69;

    private volatile int PIECE_W = 0;
    private volatile int PIECE_H = 0;

    @BindView(R.id.layout_activity)
    ConstraintLayout layoutActivity;

    // toolbar
    @BindView(R.id.toolbar_layout)
    ConstraintLayout layoutToolbar;

    @BindView(R.id.image_view_check)
    ImageView imageCheck;

    @BindView(R.id.image_view_piggy)
    ImageView imageViewPiggy;

    @BindView(R.id.image_view_home)
    ImageView imageHome;

    @BindView(R.id.image_view_menu)
    ImageView imageMenu;

    @BindView(R.id.image_view_question)
    ImageView imageQuestion;

    @BindView(R.id.image_view_puzzle)
    ImageView imagePuzzle;

    @BindView(R.id.image_view_forward)
    ImageView imageForward;

    @BindView(R.id.frame_layout_puzzle_letter)
    FrameLayout frameLayoutLetter;

    @BindView(R.id.frame_layout_word)
    FrameLayout frameLayoutWord;

    SafeFragmentTransaction safeFragmentTransaction;

    LetterModel mCurrentLetter = new LetterModel();

    @DifficultyDef
    int mLetterModeDef = DifficultyDef.EASY;

    boolean mPuzzleRandom = false;

    CopyOnWriteArrayList<WordModel> mSelectedWords = new CopyOnWriteArrayList<>();
    PuzzleLetterFragment mPuzzleLetterFragment;
    WordFragment mWordFragment;

    AtomicInteger mLifeCycleAtomic = new AtomicInteger(0);
    boolean isViewWordClicked = false;

    private Map<View, TimedAudioInfoModel> mapViewAudioInfo = new LinkedHashMap<>();

    Queue<QuizPuzzleLetterModel> mQueueQuizPuzzle = new LinkedBlockingQueue<>();

    AtomicInteger mTotalPuzzleCompleted = new AtomicInteger();

    private int[] mWordSize = new int[2];

    private String mDisplayLetter = "";
    private int mCurrentStarConsecutive = 0;

    SimpleAlphabetPresenter mAlphabetPresenter;
    List<SimpleLetterModel> mListItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_puzzle);
        ButterKnife.bind(this);

//        ImageView imageViewStars = helper.getView(R.id.image_view_question);

        // get argument from parent activity

        getArgs();

        initViews();

        initControls();
    }

    private void getArgs() {
        // mode EZ, STANDARD

        mAlphabetPresenter = new SimpleAlphabetPresenter();
        mListItem = mAlphabetPresenter.getLetterByMode(mLetterModeDef);

        if (getIntent() != null && getIntent().getExtras() != null) {
            mPuzzleRandom = getIntent().getExtras().getBoolean(Constants.Arguments.ARG_PUZZLE_RANDOM, false);
            mCurrentLetter = getIntent().getExtras().getParcelable(Constants.Arguments.ARG_LETTER);
            mLetterModeDef = getIntent().getExtras().getInt(Constants.Arguments.ARG_LETTER_MODE);
        }

//        initDebug();

        // get current letter
        if (mPuzzleRandom) {
            mCurrentLetter = mPresenter.randomLetter(mLetterModeDef);
        }

        mDisplayLetter = mCurrentLetter.getSourceLetter() + "-" + mCurrentLetter.getSoundId();

        TimedAudioInfoModel audioInfo = mCurrentLetter.getPronunciationTiming();
        if (TextUtils.isEmpty(audioInfo.getFileName())) {
            //Generator file name
            audioInfo.setFileName("words-" + mCurrentLetter.getSourceLetter().toUpperCase() + "-" + mCurrentLetter.getSoundId());
        }

        mSelectedWords = mPresenter.getSelectedWords(mCurrentLetter, mPuzzleRandom);

        if (mLetterModeDef == DifficultyDef.EASY && mPuzzleRandom == false) {
            mSelectedWords.add(new WordModel());
        }


    }

    private void initDebug() {
        mLetterModeDef = DifficultyDef.STANDARD;
        mCurrentLetter = mPresenter.randomLetter(mLetterModeDef);
    }

    private void initViews() {

        if (mLetterModeDef == DifficultyDef.EASY) {
            frameLayoutLetter.setVisibility(View.GONE);
            playLetterAudio();
        }


        initToolbar();

        mWordSize = getWordSize();

        safeFragmentTransaction = SafeFragmentTransaction.createInstance(getLifecycle(), getSupportFragmentManager());
        getLifecycle().addObserver(safeFragmentTransaction);

        // puzzle
//        if (mLetterModeDef == DifficultyDef.STANDARD) {
            safeFragmentTransaction.registerFragmentTransition(new SafeFragmentTransaction.TransitionHandler() {
                @Override
                public void onTransitionAvailable(FragmentManager managerBy) {
                    managerBy.beginTransaction()
                            .add(R.id.frame_layout_puzzle_letter, getPuzzleLetterFragment(mCurrentLetter, mPuzzleRandom, mPuzzleColor))
                            .commit();
                }
            });
//        }

        // word
        safeFragmentTransaction.registerFragmentTransition(new SafeFragmentTransaction.TransitionHandler() {
            @Override
            public void onTransitionAvailable(FragmentManager managerBy) {
                managerBy.beginTransaction()
                        .add(R.id.frame_layout_word, getWordFragment(mCurrentLetter, mSelectedWords, mPuzzleRandom, mWordSize))
                        .commit();
            }
        });

    }

    private int[] getWordSize() {
        int[] size = new int[2];
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //Width screen device
        int widthDevice = displayMetrics.widthPixels;

        //height screen device
        int heightDevice = displayMetrics.heightPixels;

        //4 Lobby space

        int padding16 = getResources().getDimensionPixelOffset(R.dimen.dp_16);

        int actionBarSize = getResources().getDimensionPixelOffset(R.dimen.dp_64);

        int puzzleLetterSize = Math.round(widthDevice / 3);

        // width
        size[0] = Math.round((widthDevice - actionBarSize - puzzleLetterSize - 2 * padding16) * 38 / 100);

        // height // 0.38 percent device, 5/6 view ( 1/6 = text ) padding top bot
        size[1] = Math.round((heightDevice - 2 * padding16) * 38 / 100 * 5 / 6);

        DebugLog.e("w: " + size[0] + " - h: " + size[1]);

        return size;
    }


    @IdRes
    int mPuzzleColor;

    private void initToolbar() {
        imageHome.setImageResource(R.mipmap.back);
        imageCheck.setVisibility(View.GONE);
        imageViewPiggy.setVisibility(View.VISIBLE);
        imageMenu.setVisibility(View.VISIBLE);
        imageQuestion.setVisibility(View.GONE);
        imagePuzzle.setImageResource(R.mipmap.repeat);
        imageForward.setVisibility(View.GONE);

        @IdRes int toolbarColor;
        @IdRes int puzzleLetterColor;

        if (mLetterModeDef == DifficultyDef.EASY) {
            toolbarColor = getResources().getColor(R.color.cyan_dark);
            puzzleLetterColor = getResources().getColor(R.color.cyan_light);
            mPuzzleColor = getResources().getColor(R.color.blue_ez);
        } else {
            toolbarColor = getResources().getColor(R.color.blue_dark_standard);
            puzzleLetterColor = getResources().getColor(R.color.blue_light_standard);
            mPuzzleColor = getResources().getColor(R.color.blue_light);
        }

        layoutToolbar.setBackgroundColor(toolbarColor);
//        frameLayoutLetter.setBackgroundColor(puzzleLetterColor);

    }

    private void initControls() {

    }

    @Override
    protected void makeView() {
        mView = this;
    }

    @Override
    protected void makePresenter() {
        mPresenter = new QuizPuzzlePresenter();
    }

    @Override
    public PuzzleLetterFragment getPuzzleLetterFragment(LetterModel letter, boolean puzzleRandom, @IdRes int puzzleColor) {
        mPuzzleLetterFragment = null;
        mPuzzleLetterFragment = PuzzleLetterFragment.newInstance(letter, puzzleRandom, puzzleColor);
        mPuzzleLetterFragment.setListener(new PuzzleLetterFragment.PuzzleLetterListener() {
            @Override
            public void onViewClick(View v, TimedAudioInfoModel audioInfo) {
                animationAndPlayAudio(v, AnimationDef.SHAKE, audioInfo, null);
            }

            @Override
            public void onSizePiece(int w, int h) {
                if (PIECE_W <= 0 && w > 0 && PIECE_H <= 0 && h > 0) {
                    PIECE_W = w;
                    PIECE_H = h;
                }

            }

            @Override
            public void onPuzzleClick() {
                isViewWordClicked = true;
            }
        });

        mPuzzleLetterFragment.setLifeCycleListener(new PuzzleLetterFragment.PuzzleLetterLifeCycleListener() {
            @Override
            public void onViewCreated() {
                if (mLifeCycleAtomic.incrementAndGet() >= 2) {
                    // repeat
                    repeatAudioInfo(true);
                }
            }

            @Override
            public void onViewDestroyView() {

            }
        });
        return mPuzzleLetterFragment;
    }

    @Override
    public WordFragment getWordFragment(LetterModel letter, List<WordModel> selectedWords, final boolean puzzleRandom, int[] size) {
        mWordFragment = null;
        mWordFragment = WordFragment.newInstance(letter, selectedWords, puzzleRandom, size, mLetterModeDef);
        mWordFragment.setSelectedWordListener(new WordFragment.SelectedWordListener() {
            @Override
            public void onCorrectAnswer(int totalPuzzleCompleted, final int coordinateX, final int coordinateY) {
                if (null != mPuzzleLetterFragment) {

                    String displayLetter = mCurrentLetter.getSourceLetter() + "-" + mCurrentLetter.getSoundId();
                    List<PuzzlePieceModel> puzzlePieces = mPresenter.getCompletedPuzzlePieces(displayLetter);

                    if (mLetterModeDef == DifficultyDef.EASY) {
                        mCurrentStarConsecutive = mPresenter.getAnswersWithoutMistake(mDisplayLetter + "ALPHABET_LETTERS_CurrentStarConsecutive");
                    } else {
                        mCurrentStarConsecutive = mPresenter.getAnswersWithoutMistake(mDisplayLetter + "PHONICS_CurrentStarConsecutive");
                    }

//                    if (totalPuzzleCompleted == Config.MAX_PUZZLE_COMPLETED) {
                    mCurrentStarConsecutive++;
//                    } else {
//                        mCurrentStarConsecutive = 0;
//                    }


                    if (mLetterModeDef == DifficultyDef.EASY) {
                        mPresenter.setAnswersWithoutMistake(mDisplayLetter + "ALPHABET_LETTERS_CurrentStarConsecutive", mCurrentStarConsecutive);
                    } else {
                        mPresenter.setAnswersWithoutMistake(mDisplayLetter + "PHONICS_CurrentStarConsecutive", mCurrentStarConsecutive);
                    }


                    if (mCurrentStarConsecutive >= Config.MIN_STAR_CONSECUTIVE) {
                        saveStarConsecutive(mCurrentStarConsecutive);
                    }

                    if (puzzlePieces.size() == (Config.PUZZLE_ROWS * Config.PUZZLE_COLUMNS) || totalPuzzleCompleted <= 0) {
                        setupView();
                        return;
                    }

                    if (mLetterModeDef == DifficultyDef.EASY) {
                        setupView();
                        return;
                    }

//                    replaceWordFragment();

//                    if (mPuzzleLetterFragment == null) {
//                        return;
//                    }


                    mTotalPuzzleCompleted.set(totalPuzzleCompleted);
                    if (mPuzzleLetterFragment == null) {
                        return;
                    }
                    mPuzzleLetterFragment.addPuzzle(totalPuzzleCompleted, new PuzzleLetterFragment.AddedPuzzleListener() {
                        @Override
                        public void onAddCompleted(final PuzzlePieceModel data, int xRootPiece, int yRootPiece) {

                            final ImageView imageViewPiece = new ImageView(Application.Context);
                            GlideApp.with(Application.Context)
                                    .load(data.getUrlArtwork())
                                    .disallowHardwareConfig()
                                    .into(new CustomViewTarget<ImageView, Drawable>(imageViewPiece) {
                                        @Override
                                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                            DebugLog.e("");
                                        }

                                        @Override
                                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                            DebugLog.e("");
                                            imageViewPiece.setImageDrawable(resource);
                                        }

                                        @Override
                                        protected void onResourceCleared(@Nullable Drawable placeholder) {
                                            DebugLog.e("");
                                        }
                                    });
                            ConstraintLayout.LayoutParams layoutParams =
                                    new ConstraintLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            layoutParams.width = data.getWidth();
                            layoutParams.height = data.getHeight();
                            layoutParams.leftMargin = coordinateX - data.getWidth() / 4;
                            layoutParams.topMargin = coordinateY - data.getHeight() / 4;
                            layoutParams.topToTop = layoutActivity.getId();
                            layoutParams.leftToLeft = layoutActivity.getId();
                            imageViewPiece.setLayoutParams(layoutParams);
                            layoutActivity.addView(imageViewPiece, layoutParams);

                            imageViewPiece.animate()
                                    .setDuration(300)
                                    .setInterpolator(new DecelerateInterpolator())
                                    .translationX(xRootPiece - coordinateX + data.getWidth() / 4)
                                    .translationY(yRootPiece - coordinateY + data.getHeight() / 4)
                                    .withEndAction(new Runnable() {
                                        @Override
                                        public void run() {
                                            layoutActivity.removeView(imageViewPiece);
                                            if (null == mPuzzleLetterFragment) {
                                                return;
                                            }
                                            mPuzzleLetterFragment.addPuzzleIntoLayout(data, imageViewPiece, new PHPuzzleListener() {
                                                @Override
                                                public void onPuzzleCompeted(boolean showIdiom) {

                                                    if (showIdiom) {
                                                        return;
                                                    }

                                                    if (mTotalPuzzleCompleted.decrementAndGet() <= 0) {
                                                        setupView();
                                                    }
                                                }
                                            });

                                        }
                                    });


                        }
                    });
                }


            }

            @Override
            public void onInCorrectAnswer() {
                mCurrentStarConsecutive = 0;

                if (mLetterModeDef == DifficultyDef.EASY) {
                    mPresenter.setAnswersWithoutMistake(mDisplayLetter + "ALPHABET_LETTERS_CurrentStarConsecutive", mCurrentStarConsecutive);
                } else {
                    mPresenter.setAnswersWithoutMistake(mDisplayLetter + "PHONICS_CurrentStarConsecutive", mCurrentStarConsecutive);
                }
            }

            @Override
            public void onViewClick(View v, TimedAudioInfoModel audioInfo, boolean isCorrectAnswer) {
                isViewWordClicked = true;
                AudioPlayerHelper.getInstance().stopPlayer();
                @AnimationDef int def = isCorrectAnswer ? AnimationDef.ZOOM : AnimationDef.SHAKE_ZOOM;
                animationAndPlayAudio(v, def, audioInfo);
            }

            @Override
            public void onLetterClick() {
                isViewWordClicked = true;
            }

            @Override
            public void onPlayAudio(String path) {
                if (mLetterModeDef == DifficultyDef.STANDARD) {
                    AudioPlayerHelper.getInstance().playAudio(path);
                } else {
                    playLetterAudio();
                }
            }

            @Override
            public void onPlayAudio(TimedAudioInfoModel audioInfo) {
                String path = generatorPrefixPath(audioInfo);
                AudioPlayerHelper.getInstance().playAudio(path, audioInfo);
            }

            @Override
            public void onZoomIn(View v) {
                scaleIn(v);
            }

            @Override
            public void onZoomOut(View v, TimedAudioInfoModel audioInfo) {
                int duration = convertSecondStringToMilliSeconds(audioInfo.getWordDuration());
                scaleOut(v, duration);
            }

            @Override
            public void onShake(View v) {
                shake(v, null);
            }

            @Override
            public void animateCoinFlyToPiggyBank(int positionXCorrect, int positionYCorrect, boolean isCoinGold) {
                animateFlyCoin(positionXCorrect, positionYCorrect, isCoinGold);
            }

            @Override
            public void goToBank() {
                bank();
            }
        });
        mWordFragment.setLifeCycleListener(new WordFragment.SelectedWordLifeCycleListener() {
            @Override
            public void onViewCreated() {

                if (mLifeCycleAtomic.incrementAndGet() >= 2) {
                    // repeat
                    repeatAudioInfo(true);
                }


            }

            @Override
            public void onViewDestroyView() {

            }
        });
        return mWordFragment;
    }

    private void playLetterAudio() {
        SimpleLetterModel letterSimple = null;
        for(SimpleLetterModel letterM: mListItem) {
            if (letterM.getLetter().equals(mCurrentLetter.getSourceLetter())) {
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


    private void replaceWordFragment() {

        mSelectedWords = mPresenter.getSelectedWords(mCurrentLetter, mPuzzleRandom);
        if (mLetterModeDef == DifficultyDef.EASY && mPuzzleRandom == false) {
            mSelectedWords.add(new WordModel());
        }
        safeFragmentTransaction.registerFragmentTransition(new SafeFragmentTransaction.TransitionHandler() {
            @Override
            public void onTransitionAvailable(final FragmentManager managerBy) {
                managerBy.beginTransaction()
                        .setCustomAnimations(R.anim.push_up_enter, R.anim.push_up_exit)
                        .replace(R.id.frame_layout_word, getWordFragment(mCurrentLetter, mSelectedWords, mPuzzleRandom, mWordSize))
                        .commit();
            }
        });

    }

    private void replacePuzzleLetterFragment() {
        safeFragmentTransaction.registerFragmentTransition(new SafeFragmentTransaction.TransitionHandler() {
            @Override
            public void onTransitionAvailable(final FragmentManager managerBy) {
                managerBy.beginTransaction()
                        .setCustomAnimations(R.anim.push_up_enter, R.anim.push_up_exit)
                        .replace(R.id.frame_layout_puzzle_letter, getPuzzleLetterFragment(mCurrentLetter, mPuzzleRandom, mPuzzleColor))
                        .commit();
            }
        });

    }

    private void animateFlyCoin(int positionXCorrect, int positionYCorrect, boolean isCoinGold) {
        int padding8dp = getResources().getDimensionPixelSize(R.dimen.dp_8);
        int coinSize = imageViewPiggy.getWidth() - (2 * padding8dp);

//        positionXCorrect = (Helper.getRelativeLeft(layoutActivity) + layoutActivity.getWidth()) / 2;
//        positionYCorrect = (Helper.getRelativeTop(layoutActivity) + layoutActivity.getHeight()) / 2;
//        positionXCorrect+=500;

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

    @NonNull
    private ImageView createImageCoin(int positionXImageCoin, int positionYImageCoin, boolean isCoinGold, int size) {
        final ImageView imageViewCoin = new ImageView(QuizPuzzleActivity.this);
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


    @OnClick(R.id.image_view_home)
    void back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        setResult(Constants.ResultCode.RELOAD_LETTER_CODE);
        super.onBackPressed();
    }

    @OnClick(R.id.image_view_puzzle)
    void repeat() {
        if (mLetterModeDef == DifficultyDef.EASY) {
            playLetterAudio();
            return;
        }
        if (PLAYING) {
            return;
        }
        repeatAudioInfo(false);
    }

    @OnClick(R.id.image_view_forward)
    void forward() {
        setupView(true);
    }

    @OnClick(R.id.image_view_piggy)
    void bank() {
        Intent intent = new Intent(QuizPuzzleActivity.this, BankActivity.class);

        // screenshot
        Bitmap bitmap = ImageHelper.getBitmapFromView(layoutActivity, Bitmap.Config.RGB_565);
        // compress
        Bitmap bitmapCompress = ImageHelper.compressBySampleSize(bitmap, 12);

        intent.putExtra(Constants.Arguments.ARG_BLUR_BITMAP, bitmapCompress);

        if (mLetterModeDef == DifficultyDef.EASY) {
            intent.putExtra("APP_FEATURE", "ALPHABET_LETTERS");
        } else {
            intent.putExtra("APP_FEATURE", "PHONICS");
        }

        pushIntent(intent);

    }

    @OnClick(R.id.image_view_menu)
    void backToAlphabetActivity() {
        AudioPlayerHelper.getInstance().stopPlayer();
        Intent intent = new Intent(QuizPuzzleActivity.this, SimpleAlphabetActivity.class);
        if (mLetterModeDef == DifficultyDef.EASY) {
            intent.putExtra(Constants.Arguments.ARG_LETTER_MODE, DifficultyDef.EASY);
        } else {
            intent.putExtra(Constants.Arguments.ARG_LETTER_MODE, DifficultyDef.STANDARD);
        }
        pushIntent(intent);
        finish();
    }


    private void animationAndPlayAudio(View view, @AnimationDef int animationDef, final TimedAudioInfoModel audioInfo) {
        animationAndPlayAudio(view, animationDef, audioInfo, null);
    }

    private void animationAndPlayAudio(final View view,
                                       @AnimationDef int animationDef,
                                       final TimedAudioInfoModel audioInfo,
                                       final PHListener.AnimationListener listener) {
        if (mLetterModeDef == DifficultyDef.STANDARD) {
            String path = generatorPrefixPath(audioInfo);
            AudioPlayerHelper.getInstance().playAudio(path, audioInfo);

            switch (animationDef) {

                case AnimationDef.SHAKE_ZOOM:
                    shake(view, listener);
                    zoomInOutAndFade(view, convertSecondStringToMilliSeconds(audioInfo.getWordDuration()), listener);
                    break;
                case AnimationDef.SHAKE:
                    shake(view, listener);
                    break;
                case AnimationDef.ZOOM:
                    zoomInOutAndFade(view, convertSecondStringToMilliSeconds(audioInfo.getWordDuration()), listener);
                    break;
            }
        }
    }


    @NonNull
    private String generatorPrefixPath(TimedAudioInfoModel audioInfo) {
        String path = Config.AUDIO_WORDS_PATH;


        //if file name not exist
        if (!TextUtils.isEmpty(audioInfo.getFileName()) && !audioInfo.getFileName().contains("/")) {
            // for alphabet sound
            //Generator file name
            return Config.AUDIO_WORDS_SETS_PATH;
        }

        if (audioInfo.getFileName().contains("/")) {
            return Config.AUDIO_ROOT_PATH;
        }


        return path;
    }


    private void scaleIn(View view) {
        Animation zoomInAnimation = new ScaleAnimation(1, 1.15f, 1, 1.15f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        zoomInAnimation.setInterpolator(new DecelerateInterpolator());
        zoomInAnimation.setDuration(400);
        zoomInAnimation.setFillAfter(true);
        if (view.getVisibility() == View.VISIBLE) {
            view.startAnimation(zoomInAnimation);
        }
    }

    private void scaleOut(final View v, final int timingAudioDuration) {

        final Animation zoomOutAnimation = new ScaleAnimation(1.15f, 1f, 1.15f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        zoomOutAnimation.setInterpolator(new DecelerateInterpolator());
        zoomOutAnimation.setFillAfter(true);
        zoomOutAnimation.setDuration(500);

        Animation animation = v.getAnimation();

        if (animation != null && animation.hasStarted() && !animation.hasEnded()) {
            DebugLog.e("SCALE IN OUT");
            animation.setAnimationListener(new PHAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    zoomOutAnimation.setStartOffset(timingAudioDuration);
                    if (v.getVisibility() == View.VISIBLE) {
                        v.startAnimation(zoomOutAnimation);
                    }
                }
            });
        } else {
            DebugLog.e("SCALE OUT");
            if (v.getVisibility() == View.VISIBLE) {
                v.startAnimation(zoomOutAnimation);
            }
        }


    }

    private void zoomInOutAndFade(final View view, int timingAudioDuration, @Nullable final PHListener.AnimationListener listener) {

        Animation zoomInAnimation = new ScaleAnimation(1, 1.15f, 1, 1.15f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        zoomInAnimation.setDuration(400);
        zoomInAnimation.setFillAfter(true);

        AlphaAnimation alphaAnimation = new AlphaAnimation(view.getAlpha(), 1f);
        view.setAlpha(1f);
        alphaAnimation.setDuration(400);
        alphaAnimation.setFillAfter(true);

        Animation zoomOutAnimation = new ScaleAnimation(1f, 20f / 23f, 1f, 20f / 23f,
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

        if (listener != null) {
            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    listener.onAnimationEnd();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        view.startAnimation(animationSet);


    }

    private void shake(View view, final PHListener.AnimationListener listener) {

        ObjectAnimator animator = ObjectAnimator
                .ofFloat(view, "translationX", 0.0f, 40.0f, -40.0f, 20.0f, -20.0f, 6.0f, -6.0f, 0.0f)
                .setDuration(700);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null) {
                    listener.onAnimationEnd();
                }
            }
        });

        animator.start();

    }

    private int convertSecondStringToMilliSeconds(String durationString) {
        if (TextUtils.isEmpty(durationString)) return 0;
        return (int) (Float.parseFloat(durationString) * 1000);
    }


    Handler mHandlerRepeatAudio = new Handler();

    private synchronized void repeatAudioInfo(boolean delayFirstTime) {
        isViewWordClicked = false;
        mQueueQuizPuzzle.clear();
        mLifeCycleAtomic.set(mPuzzleRandom ? 0 : 1);
        PLAYING = true;

        mapViewAudioInfo.clear();
        mapViewAudioInfo.put(mPuzzleLetterFragment.getTextTitle(), mCurrentLetter.getPronunciationTiming());
        mQueueQuizPuzzle.offer(new QuizPuzzleLetterModel(mPuzzleLetterFragment.getTextTitle(), mCurrentLetter.getPronunciationTiming()));

        for (Map.Entry<View, WordModel> entry : mWordFragment.getMapViewWord().entrySet()) {
            View view = entry.getKey();
            WordModel word = entry.getValue();
            mapViewAudioInfo.put(view, word.getTimedAudioInfo());
            mQueueQuizPuzzle.offer(new QuizPuzzleLetterModel(view, word.getTimedAudioInfo()));
        }

        int delayTime = delayFirstTime ? 1000 : 0;
        mHandlerRepeatAudio.removeCallbacksAndMessages(null);
        mHandlerRepeatAudio.postDelayed(new Runnable() {
            @Override
            public void run() {
                repeatAudioInfo(mQueueQuizPuzzle, AnimationDef.SHAKE);
            }
        }, delayTime);
    }


    private synchronized void repeatAudioInfo(final Queue<QuizPuzzleLetterModel> queue, @AnimationDef int animationDef) {
        if (!queue.isEmpty() && !isViewWordClicked) {
            QuizPuzzleLetterModel quiz = queue.poll();
            animationAndPlayAudio(quiz.getView(), animationDef, quiz.getAudioInfo(), new PHListener.AnimationListener() {
                @Override
                public void onAnimationEnd() {
                    repeatAudioInfo(queue, AnimationDef.ZOOM);
                }
            });
        } else {
            PLAYING = false;
        }
    }


    private void setupView() {
        setupView(false);
    }

    private void setupView(boolean foreSetup) {
        if (mRunnableSetupView != null) {
            mHandlerSetupView.removeCallbacks(mRunnableSetupView);
            new Handler(Looper.getMainLooper()).postDelayed(mRunnableSetupView, foreSetup ? 100 : 500);
        }
    }

    private Handler mHandlerSetupView = new Handler();

    private Runnable mRunnableSetupView = new Runnable() {
        @Override
        public void run() {
            resetRepeatAudio();
            if (mPuzzleRandom) {
                mCurrentLetter = mPresenter.randomLetter(mLetterModeDef);
                if (mLetterModeDef == DifficultyDef.STANDARD) {
                    replacePuzzleLetterFragment();
                }
            }
            replaceWordFragment();
            if (mLetterModeDef == DifficultyDef.EASY) {
                playLetterAudio();
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseRepeatAudio();
        releaseSetupView();
    }

    private void saveStarConsecutive(int starConsecutive) {

        if (mPuzzleRandom) {
            return;
        }
        if (mLetterModeDef == DifficultyDef.EASY) {
            mPresenter.saveStarConsecutive(mDisplayLetter + "ALPHABET_LETTERS_Stars", starConsecutive);
        } else {
            mPresenter.saveStarConsecutive(mDisplayLetter + "PHONICS_Stars", starConsecutive);
        }

    }

    private void releaseSetupView() {
        if (null != mHandlerSetupView) {
            mHandlerSetupView.removeCallbacks(mRunnableSetupView);
            mHandlerSetupView = null;
            mRunnableSetupView = null;
        }

        mWordFragment = null;
        mPuzzleLetterFragment = null;
    }

    private void releaseRepeatAudio() {
        if (null != mHandlerRepeatAudio) {
            mHandlerRepeatAudio.removeCallbacksAndMessages(null);
            mHandlerRepeatAudio = null;
        }
    }

    private void resetRepeatAudio() {
        if (null != mHandlerRepeatAudio) {
            mHandlerRepeatAudio.removeCallbacksAndMessages(null);
        }
    }
}
