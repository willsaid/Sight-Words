package com.hearatale.sightwords.data.model.typedef;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({
        DevelopDef.NONE,
        DevelopDef.WORKING,
})
@Retention(RetentionPolicy.SOURCE)
public @interface DevelopDef {
    String NONE = "none";
    String WORKING = "working";
}
