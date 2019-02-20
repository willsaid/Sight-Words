package com.hearatale.sightwords.ui.base.activity;

public interface IPresenterMVP<V extends IViewMVP> {

    void attachView(V viewMVP);

    void detachView();
}
