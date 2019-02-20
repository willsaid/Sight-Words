package com.hearatale.sightwords.data.model.typedef;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        MainDef.BRAINY_PHONICS,
        MainDef.ALPHABET_LETTERS,
        MainDef.PRE_K_SIGHT_WORDS,
        MainDef.PHONICS,
        MainDef.KINDERGARTEN_SIGHT_WORDS,
        MainDef.SECRET_STUFF,
})
@Retention(RetentionPolicy.SOURCE)
public @interface MainDef {
    int BRAINY_PHONICS = 0;
    int ALPHABET_LETTERS = 1;
    int PRE_K_SIGHT_WORDS = 2;
    int PHONICS = 3;
    int KINDERGARTEN_SIGHT_WORDS = 4;
    int SECRET_STUFF = 5;
}
