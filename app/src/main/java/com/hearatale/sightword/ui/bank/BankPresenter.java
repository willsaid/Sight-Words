package com.hearatale.sightword.ui.bank;

import com.hearatale.sightword.data.AppDataManager;
import com.hearatale.sightword.data.DataManager;
import com.hearatale.sightword.ui.base.activity.PresenterMVP;

public class BankPresenter extends PresenterMVP<IBank> implements IBankPresenter {
    DataManager mDataManager;

    BankPresenter(){
        mDataManager = AppDataManager.getInstance();
    }

    public int getTotalGoldCount(String appFeature){
        return mDataManager.getTotalGoldCount(appFeature);
    }

    public int getTotalSilverCount(String appFeature){
        return mDataManager.getTotalSilverCount(appFeature);
    }

}
