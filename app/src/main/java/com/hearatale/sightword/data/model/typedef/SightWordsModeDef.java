package com.hearatale.sightword.data.model.typedef;


import android.support.annotation.IntDef;

@IntDef({
        SightWordsModeDef.SINGLE_WORD,
        SightWordsModeDef.ALL_WORDS
})
public @interface SightWordsModeDef {
    int SINGLE_WORD = 0;
    int ALL_WORDS = 1;
}
