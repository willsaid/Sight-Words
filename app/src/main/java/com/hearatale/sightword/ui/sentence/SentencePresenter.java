package com.hearatale.sightword.ui.sentence;

import com.hearatale.sightword.data.AppDataManager;
import com.hearatale.sightword.data.DataManager;
import com.hearatale.sightword.ui.base.activity.PresenterMVP;

public class SentencePresenter extends PresenterMVP<ISentence> implements ISentencePresenter {

    private DataManager mDataManager;

    SentencePresenter() {
        mDataManager = AppDataManager.getInstance();
    }

}
