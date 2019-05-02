package com.stars.tv.sample;

import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.RowHeaderPresenter;
import android.view.View;

/**
 *  关于横向item的头样式.
 */
public class HeaderPresenter extends RowHeaderPresenter {

    HeaderPresenter() {
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        super.onBindViewHolder(viewHolder, item);
        viewHolder.view.setVisibility(View.GONE);
//        RowHeaderView headerView = (RowHeaderView) viewHolder.view;
//        headerView.setTextSize(25);
//        headerView.setTextColor(Color.WHITE);
//        headerView.setPadding(0, 0, 0, 20);
    }

}
