package com.stars.tv.sample;

import android.support.v17.leanback.widget.Presenter;
import android.view.ViewGroup;
import android.widget.Button;

public class SeriesButtonItemPresenter extends Presenter {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        Button view = new Button(parent.getContext());
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        ((Button) viewHolder.view).setText((String) item);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }
}