package com.stars.tv.sample;

import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.FocusHighlightHelper;
import android.support.v17.leanback.widget.ItemBridgeAdapter;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnChildViewHolderSelectedListener;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public class HotVideoListRowPresenter extends ListRowPresenter {

    public HotVideoListRowPresenter()
    {
        setHeaderPresenter(null);
    }

    @Override
    protected void initializeRowViewHolder(RowPresenter.ViewHolder holder) {
        super.initializeRowViewHolder(holder);
        final ViewHolder rowViewHolder = (ViewHolder) holder;
        setShadowEnabled(false);
        ItemBridgeAdapter itemBridgeAdapter = rowViewHolder.getBridgeAdapter();
        // 焦点事件处理.
        FocusHighlightHelper.setupBrowseItemFocusHighlight(itemBridgeAdapter, FocusHighlight.ZOOM_FACTOR_MEDIUM,false);

        // 设置ROW Padding50.
        rowViewHolder.getGridView().setPadding(30,0,30,0);
        // 设置横向item的间隔30.
        rowViewHolder.getGridView().setHorizontalSpacing(30);
        rowViewHolder.getGridView().setBottom(10);
    }

    @Override
    protected RowPresenter.ViewHolder createRowViewHolder(ViewGroup parent) {
        ViewHolder holder = (ViewHolder) super.createRowViewHolder(parent);
        holder.getGridView().setOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {

            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
                super.onChildViewHolderSelected(parent, child, position, subposition);
                //TODO 设置选中行为
            }
        });
        // 在此添加Layout
        return holder;
    }
}
