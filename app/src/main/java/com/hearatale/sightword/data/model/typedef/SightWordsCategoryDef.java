package com.hearatale.sightword.data.model.typedef;


import android.support.annotation.IntDef;

@IntDef({
        SightWordsCategoryDef.PRE_K,
        SightWordsCategoryDef.KINDERGARTEN
})
public @interface SightWordsCategoryDef {
    int PRE_K = 0;
    int KINDERGARTEN = 1;
}
