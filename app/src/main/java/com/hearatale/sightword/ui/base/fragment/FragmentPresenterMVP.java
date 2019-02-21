package com.hearatale.sightword.ui.base.fragment;

import android.support.v4.app.Fragment;

import com.hearatale.sightword.ui.base.activity.IPresenterMVP;
import com.hearatale.sightword.ui.base.activity.IViewMVP;

public class FragmentPresenterMVP<V extends IViewMVP> implements IPresenterMVP<V> {

    protected V mView;

    @Override
    public void attachView(V viewMVP) {
        mView = viewMVP;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    public void onReadyUI(Fragment fragment) {
    }

    public void onStart(Fragment fragment) {
    }

    public void onResume(Fragment fragment) {
    }

    public void onPause(Fragment fragment) {
    }

    public void onDestroyView(Fragment fragment) {
    }

    public void onDestroy(Fragment fragment) {
    }



}