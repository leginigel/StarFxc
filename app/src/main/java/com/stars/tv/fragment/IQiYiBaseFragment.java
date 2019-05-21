package com.stars.tv.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stars.tv.model.IQiYiBaseView;
import com.stars.tv.presenter.IQiYiBasePresenter;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class IQiYiBaseFragment<T extends IQiYiBasePresenter>
        extends Fragment implements IQiYiBaseView {

    public T mPresenter;

    Unbinder unbinder;

    protected abstract T bindPresenter();

    protected void initData(){
    }


    protected void initView(){
    }

    @LayoutRes
    protected abstract int getContentId();

    protected void processLogic(){
        mPresenter = bindPresenter();
        mPresenter.attachVM(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int resId = getContentId();
        View view = inflater.inflate(resId, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        processLogic();
        initView();
        initData();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.detachVM();
        if(unbinder!=null) unbinder.unbind();
    }

}
