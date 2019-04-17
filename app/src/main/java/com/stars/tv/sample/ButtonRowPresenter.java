
package com.stars.tv.sample;

import android.support.v17.leanback.widget.RowPresenter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stars.tv.R;
import com.stars.tv.utils.ViewUtils;

public class ButtonRowPresenter extends RowPresenter {

    public ButtonRowPresenter() {

        // 屏蔽HEAD.
        setHeaderPresenter(null);
    }


    @Override
    protected ViewHolder createRowViewHolder(ViewGroup parent) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_button_row_sample, null, false);
        root.setPadding(30,30,30,30);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindRowViewHolder(ViewHolder holder, Object item) {
        super.onBindRowViewHolder(holder, item);
        ViewGroup vg = (ViewGroup) holder.view;
        for (int i = 0; i < vg.getChildCount(); i++) {
            View childView = vg.getChildAt(i);
            childView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    ViewUtils.scaleAnimator(v,hasFocus,1.1f,150);
                }
            });
        }
    }


    @Override
    protected void onUnbindRowViewHolder(ViewHolder holder) {
        super.onUnbindRowViewHolder(holder);
    }

}
