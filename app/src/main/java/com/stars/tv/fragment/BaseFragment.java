package com.stars.tv.fragment;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;

public abstract class BaseFragment extends Fragment{

    public abstract boolean onKeyDown(KeyEvent event);
}
