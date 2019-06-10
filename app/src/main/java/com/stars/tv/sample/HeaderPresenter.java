package com.stars.tv.sample;

import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowHeaderPresenter;
import android.support.v17.leanback.widget.RowHeaderView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stars.tv.R;

/**
 *  关于横向item的头样式.
 */
public class HeaderPresenter extends RowHeaderPresenter {
    private boolean mNullItemVisibilityGone;


    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, null, false);
        root.setPadding(30,0,0,0);
//        RowHeaderPresenter.ViewHolder viewHolder = new RowHeaderPresenter.ViewHolder(root);
//        setSelectLevel(viewHolder,1.0F);
//        viewHolder.view.setAlpha(0.99F);

        return new ViewHolder(root);
   }

    public void setNullItemVisibilityGone(boolean nullItemVisibilityGone) {
        this.mNullItemVisibilityGone = nullItemVisibilityGone;
    }

    public boolean isNullItemVisibilityGone() {
        return this.mNullItemVisibilityGone;
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        super.onBindViewHolder(viewHolder, item);
//        viewHolder.view.setVisibility(View.GONE);
        RowHeaderView vg = (RowHeaderView) viewHolder.view;
        HeaderItem headerItem = item == null ? null : ((Row)item).getHeaderItem();
            if (headerItem == null ) {
        viewHolder.view.setVisibility(View.GONE);
            } else {
                viewHolder.view.setVisibility(View.VISIBLE);
                }
        RowHeaderView mTitleView = viewHolder.view.findViewById(R.id.row_header);
        mTitleView.setTextColor(vg.getContext().getResources().getColor(R.color.color_white));
        setSelectLevel((RowHeaderPresenter.ViewHolder)viewHolder, 1.0F );
        viewHolder.view.setAlpha(1.0F);
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        super.onUnbindViewHolder(viewHolder);

    }



}
