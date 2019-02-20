package com.hearatale.sightwords.ui.main;

import android.content.Intent;
import android.os.Bundle;

import com.hearatale.sightwords.R;
import com.hearatale.sightwords.data.Constants;
import com.hearatale.sightwords.data.model.typedef.MainDef;
import com.hearatale.sightwords.service.AudioPlayerHelper;
import com.hearatale.sightwords.ui.base.activity.ActivityMVP;
import com.hearatale.sightwords.ui.splash.SplashActivity;
import com.hearatale.sightwords.utils.Config;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends ActivityMVP<MainPresenter, IMain> implements IMain {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void makeView() {
        mView = this;
    }

    @Override
    protected void makePresenter() {
        mPresenter = new MainPresenter();
    }

    @OnClick(R.id.image_button_pre_k_sight_words)
    void onPerKSightWordsClick(){
        playAudio("pre-k sight words");
        pushIntent(MainDef.PRE_K_SIGHT_WORDS);
    }

    @OnClick(R.id.image_button_sight_words)
    void onSightWords() {
        playAudio("kindergarten sight words");
        pushIntent(MainDef.KINDERGARTEN_SIGHT_WORDS);
    }

    @OnClick(R.id.image_button_secret_stuff)
    void onSecretStuff() {
        playAudio("secret child stuff");
        pushIntent(MainDef.SECRET_STUFF);
    }


    private void playAudio(String fileName) {
        String path = Config.SOUND_PATH + fileName;
        AudioPlayerHelper.getInstance().playAudio(path);
    }

    public void pushIntent(@MainDef int mainDef) {
        Intent intent = new Intent(MainActivity.this, SplashActivity.class);
        Bundle args = new Bundle();
        args.putInt(Constants.Arguments.ARG_MAIN_DEF, mainDef);
        intent.putExtras(args);
        pushIntent(intent);
//        finish();  // uncomment when using
    }

}
