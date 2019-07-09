package com.stars.tv.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 *
 * @Author: Dicks.yang
 * @Date: 2019.04.15
 */

public class TvViewPager extends ViewPager {

    public TvViewPager(@NonNull Context context) {
        super(context);
    }

    public TvViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // 禁止翻页,将权限交给leanback来执行.
        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            performClick();
        }
        return false; // 禁止滑动翻页
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;// 禁止滑动翻页
    }

}
