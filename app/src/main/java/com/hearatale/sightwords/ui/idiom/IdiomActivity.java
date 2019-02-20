package com.hearatale.sightwords.ui.idiom;

import com.hearatale.sightwords.Application;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hearatale.sightwords.Application;
import com.hearatale.sightwords.R;
import com.hearatale.sightwords.data.AppDataManager;
import com.hearatale.sightwords.data.Constants;
import com.hearatale.sightwords.data.model.phonics.letters.LetterModel;
import com.hearatale.sightwords.data.model.phonics.letters.PuzzlePieceModel;
import com.hearatale.sightwords.service.AudioPlayerHelper;
import com.hearatale.sightwords.ui.bank.BankActivity;
import com.hearatale.sightwords.ui.base.activity.BaseActivity;
import com.hearatale.sightwords.utils.Config;
import com.hearatale.sightwords.utils.DebugLog;
import com.hearatale.sightwords.utils.ImageHelper;
import com.hearatale.sightwords.utils.Utils;
import com.hearatale.sightwords.utils.glide.GlideApp;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.constraint.ConstraintSet.BOTTOM;
import static android.support.constraint.ConstraintSet.END;
import static android.support.constraint.ConstraintSet.START;
import static android.support.constraint.ConstraintSet.TOP;

public class IdiomActivity extends BaseActivity {

    @BindView(R.id.layout_activity)
    ConstraintLayout layoutActivity;

    @BindView(R.id.image_blur)
    ImageView imageBlur;

    // content
    @BindView(R.id.layout_content)
    ConstraintLayout layoutContent;

    @BindView(R.id.layout_content_text)
    LinearLayoutCompat layoutContentText;

    // toolbar
    @BindView(R.id.toolbar_layout)
    ConstraintLayout layoutToolbar;

    @BindView(R.id.image_view_home)
    ImageView imageHome;

    @BindView(R.id.image_view_puzzle)
    ImageView imagePuzzle;

    @BindView(R.id.image_view_question)
    ImageView imageQuestion;

    @BindView(R.id.image_puzzle_completed)
    ImageView imagePuzzleCompleted;

    @BindView(R.id.text_idiom)
    TextView textIdiom;

    LetterModel mLetter;

    boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idiom);
        ButterKnife.bind(this);

        supportPostponeEnterTransition();


        initViews();

        initControls();

        if (getIntent().getExtras() != null) {

            mLetter = getIntent().getExtras().getParcelable(Constants.Arguments.ARG_LETTER);

            Bitmap bitmapBlur = getIntent().getParcelableExtra(Constants.Arguments.ARG_BLUR_BITMAP);

            if (null != bitmapBlur) {
                setTheme(R.style.Theme_Transparent);
                ImageHelper.blurImageView(bitmapBlur, imageBlur);
                layoutToolbar.setBackgroundResource(0);
            }

            if (null != mLetter) {
                String mDisplayLetter = mLetter.getSourceLetter() + "-" + mLetter.getSoundId();
                String base64 = AppDataManager.getInstance().getPuzzleBase64(mDisplayLetter);


                if (!TextUtils.isEmpty(base64)) {
                    Bitmap bitmap = ImageHelper.convert(base64);
                    GlideApp.with(this)
                            .load(bitmap)
                            .dontAnimate()

                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    supportStartPostponedEnterTransition();
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    supportStartPostponedEnterTransition();
                                    return false;
                                }
                            })
                            .into(imagePuzzleCompleted);
                }


                List<PuzzlePieceModel> puzzlePieces = AppDataManager.getInstance().getCompletedPuzzlePieces(mDisplayLetter);

                if (puzzlePieces.size() != (Config.PUZZLE_ROWS * Config.PUZZLE_COLUMNS)) {
                    imageQuestion.setVisibility(View.GONE);

                    ConstraintSet set = new ConstraintSet();
                    set.clone(layoutContent);
                    set.connect(imagePuzzleCompleted.getId(), TOP, layoutContent.getId(), TOP, 0);
                    set.connect(imagePuzzleCompleted.getId(), START, layoutContent.getId(), START, 0);
                    set.connect(imagePuzzleCompleted.getId(), BOTTOM, layoutContent.getId(), BOTTOM, 0);
                    set.connect(imagePuzzleCompleted.getId(), END, layoutContent.getId(), END, 0);

                    set.applyTo(layoutContent);

                    return;
                }

                playAudio(500);

                String text = "";
                try {
                    String path = Config.AUDIO_RHYMES_TEXT + "puzzle-" + mDisplayLetter + "-rhymeText.txt";
                    InputStream inputStream = Application.Context.getAssets().open(path);
                    int size = inputStream.available();
                    byte[] buffer = new byte[size];
                    inputStream.read(buffer);
                    inputStream.close();
                    text = new String(buffer, "UTF-8").trim().replace(";", ",");
                    String formattedText = text.replaceAll("\n", "\n\n");
                    SpannableString spannableString = new SpannableString(formattedText);

                    Matcher matcherParagraphs = Pattern.compile("\n\n").matcher(formattedText);
                    while (matcherParagraphs.find()) {
                        spannableString.setSpan(new AbsoluteSizeSpan(10, true),
                                matcherParagraphs.start() + 1, matcherParagraphs.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    CharSequence result = Utils.setColorText(spannableString, "\\((.*?)\\)", Color.parseColor("#D5463C"));
                    textIdiom.setText(result);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


    }

    private void playAudio(int delayMilliSecond) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                updateButtonRepeat(true);
                String pathFile = Config.AUDIO_RHYMES + "puzzle-" + mLetter.getSourceLetter().toUpperCase() + "-" + mLetter.getSoundId() + "-rhyme";

                AudioPlayerHelper.getInstance().playIdiom(pathFile, new AudioPlayerHelper.CompletedListener() {
                    @Override
                    public void onCompleted() {
                        updateButtonRepeat(false);
                    }
                });
            }
        }, delayMilliSecond);
    }

    private void initControls() {
    }

    private void initViews() {
        initToolbar();
        int paddingTopBot = getPadding();
        layoutContentText.setPadding(0, paddingTopBot, 0, paddingTopBot);
    }

    private void initToolbar() {
        imageHome.setImageResource(R.mipmap.back);

        imagePuzzle.setImageResource(R.mipmap.bank);
        imageQuestion.setImageResource(R.mipmap.repeat);
    }


    @OnClick(R.id.image_view_home)
    void back() {
        onBackPressed();
    }

    // bank
    @OnClick(R.id.image_view_question)
    void repeat() {
        playAudio(0);
    }

    @OnClick(R.id.image_view_puzzle)
    void bank() {
        Intent intent = new Intent(IdiomActivity.this, BankActivity.class);

        // screenshot
        Bitmap bitmap = ImageHelper.getBitmapFromView(layoutActivity, Bitmap.Config.RGB_565);
        // compress
        Bitmap bitmapCompress = ImageHelper.compressBySampleSize(bitmap, 12);

        intent.putExtra(Constants.Arguments.ARG_BLUR_BITMAP, bitmapCompress);

        pushIntent(intent);

    }

    @Override
    protected void onPause() {
        DebugLog.e("");
        super.onPause();
        AudioPlayerHelper.getInstance().stopIdiom();
        updateButtonRepeat(false);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);

        super.onBackPressed();
    }

    private void updateButtonRepeat(boolean playing) {
        imageQuestion.setEnabled(!playing);
        imageQuestion.setAlpha(playing ? 0.5f : 1f);
    }

    private int getPadding() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        int heightDevice = displaymetrics.heightPixels;

        return (int) (heightDevice * 0.05);
    }
}
