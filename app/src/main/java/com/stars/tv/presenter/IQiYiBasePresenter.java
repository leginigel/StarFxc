package com.stars.tv.presenter;

import com.stars.tv.server.RxManager;

public abstract class IQiYiBasePresenter<T> {
    public T mView;
    public RxManager mRxManager = new RxManager();

    public void attachVM(T v) {
        this.mView = v;
    }

    public void detachVM() {
        mRxManager.clear();
        mView = null;
    }

}
