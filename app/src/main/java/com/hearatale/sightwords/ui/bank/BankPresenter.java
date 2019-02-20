package com.hearatale.sightwords.ui.bank;

import com.hearatale.sightwords.data.AppDataManager;
import com.hearatale.sightwords.data.DataManager;
import com.hearatale.sightwords.ui.base.activity.PresenterMVP;

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
