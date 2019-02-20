package com.hearatale.sightwords.data.model.typedef;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        AnimationDef.SHAKE,
        AnimationDef.ZOOM,
        AnimationDef.SHAKE_ZOOM
})
@Retention(RetentionPolicy.SOURCE)
public @interface AnimationDef {
    int SHAKE = 0;
    int ZOOM = 1;
    int SHAKE_ZOOM = 2;
}
