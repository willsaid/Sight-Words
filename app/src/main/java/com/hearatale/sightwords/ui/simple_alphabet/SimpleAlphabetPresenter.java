package com.hearatale.sightwords.ui.simple_alphabet;

import android.support.annotation.NonNull;

import com.hearatale.sightwords.data.AppDataManager;
import com.hearatale.sightwords.data.DataManager;
import com.hearatale.sightwords.data.model.phonics.letters.LetterModel;
import com.hearatale.sightwords.data.model.phonics.letters.SimpleLetterModel;
import com.hearatale.sightwords.data.model.phonics.letters.TimedAudioInfoModel;
import com.hearatale.sightwords.data.model.typedef.DifficultyDef;
import com.hearatale.sightwords.ui.base.activity.PresenterMVP;
import com.hearatale.sightwords.utils.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimpleAlphabetPresenter extends PresenterMVP<ISimpleAlphabet> implements ISimpleAlphabetPresenter {

    DataManager mDataManager;

    public SimpleAlphabetPresenter() {
        mDataManager = AppDataManager.getInstance();
    }

    public List<SimpleLetterModel> getLetterByMode(@DifficultyDef int mode) {
        Map<String, List<LetterModel>> letters = mDataManager.getLetters();
        List<SimpleLetterModel> listItem = new ArrayList<>();
        for (String letterText : letters.keySet()) {
            List<LetterModel> letterModels = letters.get(letterText);


            //Image and progress
            if (mode == DifficultyDef.EASY) {
                //First letter
                LetterModel letterModel = letterModels.get(0);
                SimpleLetterModel e = getSimpleLetterModel(letterModel, mode);
                listItem.add(e);
            } else {
                for (LetterModel letter : letterModels) {
                    SimpleLetterModel e = getSimpleLetterModel(letter, mode);
                    listItem.add(e);
                }

            }

        }
        return listItem;
    }

    @NonNull
    private SimpleLetterModel getSimpleLetterModel(LetterModel letter, @DifficultyDef int mode) {
        //Sound
        TimedAudioInfoModel sourceLetterTiming = letter.getSourceLetterTiming();
        String pathImage;
        String text;
        if (mode == DifficultyDef.EASY) {
            pathImage = Config.IMAGES_SIMPLE_LETTER_PATH + "easy-letter-icon-" +
                    letter.getSourceLetter().toLowerCase() + ".jpg";
            text = letter.getSourceLetter();
        } else {

            pathImage = Config.IMAGES_WORD_BY_LETTER_PATH + letter.getPrimaryWords().get(0).getText() + ".jpg";
            text = letter.getDisplayString();

        }
        String id = getId(letter);
        int progress = getProgress(id);

        int starConsecutive = getStarConsecutive(id);

        return new SimpleLetterModel(
                id,
                text,
                progress,
                sourceLetterTiming,
                pathImage,
                letter.getColorString(),
                starConsecutive
        );
    }

    @NonNull
    private String getId(LetterModel letterModel) {
        return letterModel.getSourceLetter() + "-" + letterModel.getSoundId();
    }

    public int getProgress(String id) {
        return mDataManager.getCompletedPuzzlePieces(id).size();
    }

    public int getStarConsecutive(String id) {
        return mDataManager.getStarConsecutive(id);
    }
}
