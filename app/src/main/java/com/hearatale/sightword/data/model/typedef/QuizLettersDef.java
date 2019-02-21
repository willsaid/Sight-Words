package com.hearatale.sightword.data.model.typedef;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        QuizLettersDef.PUZZLE,
        QuizLettersDef.QUIZ,
})
@Retention(RetentionPolicy.SOURCE)
public @interface QuizLettersDef {
    int PUZZLE = 0;
    int QUIZ = 1;
}
