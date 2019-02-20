package com.hearatale.sightwords.ui.sentence;

import com.hearatale.sightwords.data.AppDataManager;
import com.hearatale.sightwords.data.DataManager;
import com.hearatale.sightwords.ui.base.activity.PresenterMVP;

public class SentencePresenter extends PresenterMVP<ISentence> implements ISentencePresenter {

    private DataManager mDataManager;

    SentencePresenter() {
        mDataManager = AppDataManager.getInstance();
    }

}
