package com.hearatale.sightwords.ui.letter;

import com.hearatale.sightwords.data.AppDataManager;
import com.hearatale.sightwords.data.DataManager;
import com.hearatale.sightwords.data.model.phonics.letters.LetterModel;
import com.hearatale.sightwords.ui.base.activity.PresenterMVP;

import java.util.List;
import java.util.Map;

public class LetterPresenter extends PresenterMVP<ILetter> implements ILetterPresenter {

    DataManager mDataManager;

    LetterPresenter() {
        mDataManager = AppDataManager.getInstance();
    }

    Map<String, List<LetterModel>> getLetter() {
        return mDataManager.getLetters();
    }
}
