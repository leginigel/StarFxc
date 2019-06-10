package com.stars.tv.view;

import android.content.Context;
import android.support.v17.leanback.widget.VerticalGridView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 *
 * @Author: Dicks.yang
 * @Date: 2019.04.16
 */

public class MyVerticalGridView extends VerticalGridView {

    public MyVerticalGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public MyVerticalGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyVerticalGridView(Context context) {
        super(context);
    }

    long mOldTime = 0;
    long mTimeStamp = 280;

    /**
     *  设置按键滚动的时间间隔.
     *  在小于time的间隔内消除掉.
     */
    public void setKeyScrollTime(long time) {
        this.mTimeStamp = time;
    }

    /**
     *  用于优化按键快速滚动卡顿的问题.
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getRepeatCount() >= 2 && event.getAction()==KeyEvent.ACTION_DOWN){
            long currentTime = System.currentTimeMillis();
            if (currentTime - mOldTime <= mTimeStamp) {
                return true;
            }
            mOldTime = currentTime;
        }
        return super.dispatchKeyEvent(event);
    }

    private static final int MORE_STATE_END = 0; // 加载结束
    private static final int MORE_STATE_LOADING = 1; // 加载中
    private static final int MORE_STATE_NO_DATA = -1; // 加载更多已没有数据.

    private int mMoreState = MORE_STATE_END;
    private OnLoadMoreListener mOnLoadMoreListener;

    public interface OnLoadMoreListener {
        void onLoadMore();
        void onLoadEnd();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener cb) {
        this.mOnLoadMoreListener = cb;
    }

    @Override
    public void onScrollStateChanged(int state) {
        if (state == SCROLL_STATE_IDLE) {
            // 加载更多回调
            if (getLastVisiblePosition() >= getAdapter().getItemCount() - 1) {
                if (null != mOnLoadMoreListener) {
                    if (mMoreState == MORE_STATE_END) {
                        mMoreState = MORE_STATE_LOADING;
                        mOnLoadMoreListener.onLoadMore();
                    }
                    if (mMoreState == MORE_STATE_NO_DATA) {
                        mOnLoadMoreListener.onLoadEnd();
                    }
                }
            }
        }
        super.onScrollStateChanged(state);
    }

    public void resetMoreRefresh() {

    }

    // 加载更多结束调用.
    public void endMoreRefreshComplete() {
        mMoreState = MORE_STATE_END;
    }

    // 没有更多加载.
    public void endRefreshingWithNoMoreData() {
        mMoreState = MORE_STATE_NO_DATA;
    }

    public int getLastVisiblePosition() {
        final int childCount = getChildCount();
        if (childCount == 0)
            return 0;
        else
            return getChildAdapterPosition(getChildAt(childCount - 1));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false; // 禁止滑动翻页
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;// 禁止滑动翻页
    }

}
