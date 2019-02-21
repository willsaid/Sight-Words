package com.hearatale.sightword.ui.quiz;

import com.hearatale.sightword.data.model.phonics.SectionQuiz;
import com.hearatale.sightword.ui.base.activity.IViewMVP;

import java.util.List;

public interface IQuiz extends IViewMVP {

    void updateDataQuiz(List<SectionQuiz> data);

    int getDifficulty();
}
