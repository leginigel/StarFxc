package com.stars.tv.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EmptyFragment extends BaseFragment {

    String mTvTitle;

    public static EmptyFragment getInstance(String titleMode) {
        return newInstance(titleMode);
    }

    public static EmptyFragment newInstance(String titleName) {
        EmptyFragment myFragment = new EmptyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("titleName", titleName);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTvTitle = getArguments() != null ? getArguments().getString("titleName") : null;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
    }

    @Override
    public boolean onKeyDown(KeyEvent event) {
        return false;
    }

}

