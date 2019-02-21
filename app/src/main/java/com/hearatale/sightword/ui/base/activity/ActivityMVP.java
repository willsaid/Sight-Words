package com.hearatale.sightword.ui.base.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;

public abstract class ActivityMVP<P extends PresenterMVP, V extends IViewMVP> extends BaseActivity {

    protected P mPresenter;
    protected V mView;

    public ActivityMVP() {
        makeView();
        makePresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.attachView(mView);
        mPresenter.onReadyUI(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onStart(this);
    }

    @Override
    protected void onStop() {
        mPresenter.onStop(this);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume(this);
    }

    @Override
    protected void onPause() {
        mPresenter.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        mPresenter.onDestroy(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDetachedFromWindow() {
        mPresenter.onDetachedFromWindow(this);
        super.onDetachedFromWindow();
    }

    protected abstract void makeView();

    protected abstract void makePresenter();

    protected void pushIntent(Class clazz){
        try {
            startActivity(new Intent(this, clazz));
        }catch (ActivityNotFoundException e){
            e.printStackTrace();
        }

    }
}
