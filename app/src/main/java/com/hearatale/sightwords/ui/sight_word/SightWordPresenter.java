package com.hearatale.sightwords.ui.sight_word;

import com.hearatale.sightwords.data.AppDataManager;
import com.hearatale.sightwords.data.DataManager;
import com.hearatale.sightwords.data.model.phonics.SightWordModel;
import com.hearatale.sightwords.data.model.typedef.SightWordsCategoryDef;
import com.hearatale.sightwords.ui.base.activity.PresenterMVP;

import java.util.List;

public class SightWordPresenter extends PresenterMVP<ISightWord> implements ISightWordPresenter {

    private DataManager mDataManager;

    SightWordPresenter() {
        mDataManager = AppDataManager.getInstance();
    }

    @Override
    public List<SightWordModel> getSightWords(@SightWordsCategoryDef int category) {
        return mDataManager.getSightWords(category);
    }
}
