package com.stars.tv.presenter;

import com.stars.tv.server.RxManager;

public abstract class IQiYiBasePresenter<M, T> {
    public M mModel;
    public T mView;
    public RxManager mRxManager = new RxManager();

    public void attachVM(T v, M m) {
        this.mView = v;
        this.mModel = m;
    }

    public void detachVM() {
        mRxManager.clear();
        mView = null;
        mModel = null;
    }

}
