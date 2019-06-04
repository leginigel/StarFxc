package com.stars.tv.presenter;

import android.support.v17.leanback.widget.Presenter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stars.tv.R;

public class LiveTvTitlePresenter extends Presenter {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_live_tv_category_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Presenter.ViewHolder viewHolder, Object o) {
        if (o instanceof String) {
            final ViewHolder vh = (ViewHolder) viewHolder;
            vh.title.setText((String)o);
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {

    }

    static class ViewHolder extends Presenter.ViewHolder {
        TextView title;
        ImageView wave;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.item_live_tv_category_title_text);
            wave = view.findViewById(R.id.item_live_tv_category_wave);
        }

    }
}
