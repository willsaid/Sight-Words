package com.hearatale.sightwords.ui.base.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class PresenterMVP<V extends IViewMVP> implements IPresenterMVP<V> {
    protected V mView;

    @Override
    public void attachView(V viewMVP) {
        mView = viewMVP;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    public void onReadyUI(AppCompatActivity activity) {
    }

    public void onStart(AppCompatActivity activity) {
    }

    public void onResume(AppCompatActivity activity) {
    }

    public void onPause(AppCompatActivity activity) {
    }

    public void onStop(AppCompatActivity activity) {
    }

    public void onDestroy(AppCompatActivity activity) {
    }

    public void onActivityResult(AppCompatActivity activity, int requestCode, int resultCode, Intent data) {
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

    }

    public void onDetachedFromWindow(ActivityMVP activity) {

    }


}
