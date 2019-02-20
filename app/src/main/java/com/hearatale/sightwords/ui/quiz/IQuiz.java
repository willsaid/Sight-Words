package com.hearatale.sightwords.ui.quiz;

import com.hearatale.sightwords.data.model.phonics.SectionQuiz;
import com.hearatale.sightwords.ui.base.activity.IViewMVP;

import java.util.List;

public interface IQuiz extends IViewMVP {

    void updateDataQuiz(List<SectionQuiz> data);

    int getDifficulty();
}
