package com.hearatale.sightwords.ui.quiz_sight_words.answers;

import android.support.annotation.NonNull;

import com.hearatale.sightwords.data.AppDataManager;
import com.hearatale.sightwords.data.DataManager;
import com.hearatale.sightwords.data.model.phonics.SightWordModel;
import com.hearatale.sightwords.data.model.typedef.SightWordsCategoryDef;
import com.hearatale.sightwords.service.AudioPlayerHelper;
import com.hearatale.sightwords.ui.base.fragment.FragmentPresenterMVP;
import com.hearatale.sightwords.utils.Config;

import java.util.ArrayList;
import java.util.List;

public class AnswersPresenter extends FragmentPresenterMVP<IAnswers> implements IAnswersPresenter {
    private static final int COINS_TRUCK = 125;

    DataManager mDataManager;

    public AnswersPresenter() {
        mDataManager = AppDataManager.getInstance();
    }

    @NonNull
    public List<SightWordModel> getAllWords(@SightWordsCategoryDef int category) {
        return new ArrayList<>(mDataManager.getSightWords(category));
    }

    @NonNull
    private String individualAudioFilePath(@SightWordsCategoryDef int category, SightWordModel sightWord) {
        String prefixFolder = "";
        if (category == SightWordsCategoryDef.PRE_K) {
            prefixFolder = "Pre-K Sight Words";
        } else {
            prefixFolder = "Kindergarten Sight Words";
        }
        return Config.AUDIO_ROOT_PATH + prefixFolder + "/Individual Words/" + sightWord.getText().toLowerCase();
    }

    public void playAudio(@SightWordsCategoryDef int category, SightWordModel sightWord) {
        String path = individualAudioFilePath(category, sightWord);
        AudioPlayerHelper.getInstance().playAudio(path);
    }

    public void increaseTotalGoldCoins(String appFeature) {
        int totalGoldCount = getTotalGoldCount(appFeature) + 1;
        int totalSilverCoins = getTotalSilverCount(appFeature);
        int totalCoins = totalGoldCount + (totalSilverCoins / 2);
        if (checkResetCoin(appFeature, totalSilverCoins, totalCoins)) return;
        mDataManager.setTotalGoldCount(appFeature, totalGoldCount);
    }

    public void increaseTotalSilverCoins(String appFeature) {
        int totalSilverCoins = mDataManager.getTotalSilverCount(appFeature) + 1;
        int totalCoins = (totalSilverCoins / 2) + getTotalGoldCount(appFeature);
        if (checkResetCoin(appFeature, totalSilverCoins, totalCoins)) return;
        mDataManager.setTotalSilverCount(appFeature, totalSilverCoins);
    }

    private boolean checkResetCoin(String appFeature, int totalSilverCount, int totalCoins) {
        int remainingSliver = totalSilverCount % 2;
        int trucks = totalCoins / COINS_TRUCK;
        if (trucks >= 10 && (totalCoins % COINS_TRUCK > 0 || remainingSliver > 0)) {
            //Reset coin
            resetCoins(appFeature);
            return true;
        }
        return false;
    }


    public int getTotalGoldCount(String appFeature) {
        return mDataManager.getTotalGoldCount(appFeature);
    }

    public int getTotalSilverCount(String appFeature) {
        return mDataManager.getTotalSilverCount(appFeature);
    }

    public void resetCoins(String appFeature) {
        mDataManager.setTotalGoldCount(appFeature, 0);
        mDataManager.setTotalSilverCount(appFeature, 0);
    }
}
