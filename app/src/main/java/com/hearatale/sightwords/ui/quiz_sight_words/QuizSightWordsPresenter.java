package com.hearatale.sightwords.ui.quiz_sight_words;

import android.support.annotation.NonNull;

import com.hearatale.sightwords.data.AppDataManager;
import com.hearatale.sightwords.data.DataManager;
import com.hearatale.sightwords.data.model.event.StarEvent;
import com.hearatale.sightwords.data.model.phonics.SightWordModel;
import com.hearatale.sightwords.data.model.typedef.SightWordsCategoryDef;
import com.hearatale.sightwords.data.network.api.base.async.BackgroundTask;
import com.hearatale.sightwords.ui.base.activity.PresenterMVP;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class QuizSightWordsPresenter extends PresenterMVP<IQuizSightWords> implements IQuizSightWordsPresenter {
    DataManager mDataManager;

    QuizSightWordsPresenter() {
        mDataManager = AppDataManager.getInstance();
    }

    @NonNull
    public List<SightWordModel> getAllWords(@SightWordsCategoryDef int category) {
        return new ArrayList<>(mDataManager.getSightWords(category));
    }

    public List<SightWordModel> allAvailableOptionAllWords(@SightWordsCategoryDef int category) {

        return getAllWords(category);
    }

    public List<SightWordModel> allAvailableOptionSingleWord(@SightWordsCategoryDef int category, SightWordModel specificWord) {
        List<SightWordModel> allWords = getAllWords(category);

        if (specificWord == null) return allWords;

        int foundIndex = -1;
        for (int i = 0; i < allWords.size(); i++) {
            if (allWords.get(i).getText().equals(specificWord.getText())) {
                foundIndex = i;
                break;
            }
        }
        if (foundIndex != -1) {
            allWords.remove(foundIndex);
        }
        return allWords;
    }

    public <E> E popLastList(List<E> list) {
        int lastIndex = list.size() - 1;
        E found = list.get(lastIndex);
        list.remove(lastIndex);
        return found;
    }

    public boolean arrayHasHomophoneConflicts(List<SightWordModel> items) {
        return mDataManager.arrayHasHomophoneConflicts(items);
    }

    public int getTotalGoldCount(String appFeature) {
        return mDataManager.getTotalGoldCount(appFeature);
    }

    public void setAnswersWithoutMistake(String sightWord, int answersWithoutMistake) {
        mDataManager.setAnswersWithoutMistake(sightWord, answersWithoutMistake);
    }

    public int getAnswersWithoutMistake(String sightWord) {
        return mDataManager.getAnswersWithoutMistake(sightWord);
    }

    public void saveStarCount(final SightWordModel sightWordModel, final int count) {
        new BackgroundTask() {

            boolean notifyViewUpdate;

            @Override
            public void run() {
                int old = mDataManager.getSightWordStarCount(sightWordModel.getText());
                notifyViewUpdate = count > old;
                if(!notifyViewUpdate) return;
                mDataManager.setSightWordStarCount(sightWordModel.getText(), count);
            }

            @Override
            public void onFinish() {
                if(!notifyViewUpdate || !EventBus.getDefault().hasSubscriberForEvent(StarEvent.class)) return;
                EventBus.getDefault().post(new StarEvent(sightWordModel.getText(), count));

            }
        }.execute();
    }
}
