package com.hearatale.sightword.ui.sight_word;

import com.hearatale.sightword.data.AppDataManager;
import com.hearatale.sightword.data.DataManager;
import com.hearatale.sightword.data.model.phonics.SightWordModel;
import com.hearatale.sightword.data.model.typedef.SightWordsCategoryDef;
import com.hearatale.sightword.ui.base.activity.PresenterMVP;

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
