package com.hearatale.sightwords.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hearatale.sightwords.R;
import com.hearatale.sightwords.data.Constants;
import com.hearatale.sightwords.data.model.typedef.DifficultyDef;
import com.hearatale.sightwords.data.model.typedef.MainDef;
import com.hearatale.sightwords.data.model.typedef.SightWordsCategoryDef;
import com.hearatale.sightwords.service.AudioPlayerHelper;
import com.hearatale.sightwords.ui.base.activity.BaseActivity;
import com.hearatale.sightwords.ui.main.MainActivity;
import com.hearatale.sightwords.ui.secret_stuff.SecretStuffActivity;
import com.hearatale.sightwords.ui.sight_word.SightWordActivity;
import com.hearatale.sightwords.ui.simple_alphabet.SimpleAlphabetActivity;
import com.hearatale.sightwords.utils.Config;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    private static final long DELAY_TIME = 1500;

    @BindView(R.id.image_artwork)
    ImageView imageArtwork;

    @MainDef
    int mMainDef = MainDef.BRAINY_PHONICS;

    Handler handlerDelay = new Handler();
    Runnable runnableDelay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            Bundle args = getIntent().getExtras();
            mMainDef = args.getInt(Constants.Arguments.ARG_MAIN_DEF);

        }

        switch (mMainDef) {

            case MainDef.BRAINY_PHONICS:
                String path = Config.SOUND_PATH + "brainy sight words";
                AudioPlayerHelper.getInstance().playAudio(path);
                imageArtwork.setImageResource(R.mipmap.brainy_sight_words_logo);
                runnableDelay = new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        pushIntent(intent);
                        finish();
                    }
                };
                handlerDelay.postDelayed(runnableDelay, DELAY_TIME);


                break;

            case MainDef.ALPHABET_LETTERS:
                imageArtwork.setImageResource(R.mipmap.alphabet_logo_1);
                runnableDelay = new Runnable() {
                    @Override
                    public void run() {
                        // playAudio
                        Intent intent = new Intent(SplashActivity.this, SimpleAlphabetActivity.class);
                        intent.putExtra(Constants.Arguments.ARG_LETTER_MODE, DifficultyDef.EASY);
                        pushIntent(intent);
                        finish();
                    }
                };
                handlerDelay.postDelayed(runnableDelay, DELAY_TIME);

                break;

            case MainDef.PRE_K_SIGHT_WORDS:
                imageArtwork.setImageResource(R.mipmap.pre_k_sight_words_logo);

                runnableDelay = new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, SightWordActivity.class);
                        intent.putExtra(Constants.Arguments.ARG_SIGHT_WORD_MODE, SightWordsCategoryDef.PRE_K);
                        pushIntent(intent);
                        finish();
                    }
                };
                handlerDelay.postDelayed(runnableDelay, DELAY_TIME);
                break;

            case MainDef.PHONICS:
                imageArtwork.setImageResource(R.mipmap.phonics_logo);
                runnableDelay = new Runnable() {
                    @Override
                    public void run() {
                        // playAudio
                        Intent intent = new Intent(SplashActivity.this, SimpleAlphabetActivity.class);
                        intent.putExtra(Constants.Arguments.ARG_LETTER_MODE, DifficultyDef.STANDARD);
                        pushIntent(intent);
                        finish();
                    }
                };
                handlerDelay.postDelayed(runnableDelay, DELAY_TIME);
                break;


            case MainDef.KINDERGARTEN_SIGHT_WORDS:
                imageArtwork.setImageResource(R.mipmap.kindergarten_sight_words_logo);

                runnableDelay = new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, SightWordActivity.class);
                        intent.putExtra(Constants.Arguments.ARG_SIGHT_WORD_MODE, SightWordsCategoryDef.KINDERGARTEN);
                        pushIntent(intent);
                        finish();
                    }
                };
                handlerDelay.postDelayed(runnableDelay, DELAY_TIME);
                break;

            case MainDef.SECRET_STUFF:
                imageArtwork.setImageResource(R.mipmap.secret_stuff_logo);

                runnableDelay = new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, SecretStuffActivity.class);
                        pushIntent(intent);
                        finish();
                    }
                };
                handlerDelay.postDelayed(runnableDelay, DELAY_TIME);
                break;
        }

    }

    @Override
    protected void onDestroy() {
        AudioPlayerHelper.getInstance().stopPlayer();
        handlerDelay.removeCallbacks(runnableDelay);
        super.onDestroy();
    }
}
