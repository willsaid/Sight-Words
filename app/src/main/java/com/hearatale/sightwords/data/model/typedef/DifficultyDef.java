package com.hearatale.sightwords.data.model.typedef;

import android.support.annotation.IntDef;

@IntDef({
        DifficultyDef.EASY,
        DifficultyDef.STANDARD
})
public @interface DifficultyDef {
    int EASY = 0;
    int STANDARD = 1;
}
