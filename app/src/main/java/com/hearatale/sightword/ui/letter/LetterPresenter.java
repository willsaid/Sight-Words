package com.hearatale.sightword.ui.letter;

import com.hearatale.sightword.data.AppDataManager;
import com.hearatale.sightword.data.DataManager;
import com.hearatale.sightword.data.model.phonics.letters.LetterModel;
import com.hearatale.sightword.ui.base.activity.PresenterMVP;

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
