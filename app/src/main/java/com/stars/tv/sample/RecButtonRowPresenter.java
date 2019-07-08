
package com.stars.tv.sample;

import android.content.Context;
import android.content.Intent;
import android.support.v17.leanback.widget.RowPresenter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.stars.tv.R;
import com.stars.tv.activity.TotalMediaListActivity;
import com.stars.tv.utils.ViewUtils;

import java.util.Objects;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.utils.AutoSizeUtils;

public class RecButtonRowPresenter extends RowPresenter {
    protected Context mContext;
    private static final String TAG = "RecButtonRowPresenter";
    private static final int GRID_VIEW_LEFT_PX = 80;
    private static final int GRID_VIEW_RIGHT_PX = 30;
    private static final int ITEM_NUM_ROW = 1;
    int LAYOUT_WIDTH = 0;
    int LAYOUT_HEIGHT = 0;

    public RecButtonRowPresenter() {

        // 屏蔽HEAD.
        setHeaderPresenter(null);
    }


    @Override
    protected ViewHolder createRowViewHolder(ViewGroup parent) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rec_button_row_layout, null, false);
        root.setPadding(30, 30, 30, 30);
        LAYOUT_WIDTH = (AutoSizeUtils.dp2px(Objects.requireNonNull(parent.getContext()), AutoSizeConfig.getInstance().getDesignWidthInDp()) - GRID_VIEW_LEFT_PX) / ITEM_NUM_ROW;
        LAYOUT_HEIGHT = ViewUtils.getPercentHeightSize(120);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(LAYOUT_WIDTH, LAYOUT_HEIGHT);
        root.setLayoutParams(lp);
        mContext = parent.getContext();
        return new ViewHolder(root);
    }

    @Override
    protected void onBindRowViewHolder(final ViewHolder holder, Object item) {
        super.onBindRowViewHolder(holder, item);
        ViewGroup vg = (ViewGroup) holder.view;
        Log.v(TAG, "select the Button:" + vg.getChildCount());
        for (int i = 0; i < vg.getChildCount(); i++) {
            final View childView = vg.getChildAt(i);
            childView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    ViewUtils.scaleAnimator(v, hasFocus, 1.1f, 150);
                }
            });
            childView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View btn = v;
                    String buttonName = ((Button) btn).getText().toString();
                    if (buttonName.contains("电视剧")) {
                        buttonName=buttonName+"Series全部";
                    }else if(buttonName.contains("电影")) {
                        buttonName=buttonName+"Film全部";
                    }else if(buttonName.contains("综艺")) {
                        buttonName=buttonName+"Variety全部";
                    }else if(buttonName.contains("动漫")) {
                        buttonName=buttonName+"Cartoon全部";
                    }
                    Intent intent = new Intent(mContext, TotalMediaListActivity.class);
                    intent.putExtra("buttonName", buttonName);
                    mContext.startActivity(intent);

                }
            });
        }

    }


    @Override
    protected void onUnbindRowViewHolder(ViewHolder holder) {
        super.onUnbindRowViewHolder(holder);
    }

    public Context getContext() {
        return mContext;
    }
}
