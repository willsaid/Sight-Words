package com.hearatale.sightwords;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.hearatale.sightwords.utils.Config;

import io.fabric.sdk.android.Fabric;

public class Application extends android.app.Application {

    public static Context Context;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Config.PH_PRODUCTION) {
            Fabric.with(this, new Crashlytics());
        }

        Context = this;

    }
}

