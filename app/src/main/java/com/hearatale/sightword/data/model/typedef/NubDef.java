package com.hearatale.sightword.data.model.typedef;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({
        NubDef.NONE,
        NubDef.OUTSIDE,
        NubDef.INSIDE,
})
@Retention(RetentionPolicy.SOURCE)
public @interface NubDef {
    String NONE = "none";
    String OUTSIDE = "outside";
    String INSIDE = "inside";
}
