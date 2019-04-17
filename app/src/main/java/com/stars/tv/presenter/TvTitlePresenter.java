package com.stars.tv.presenter;

import android.support.v17.leanback.widget.Presenter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stars.tv.R;
import com.stars.tv.bean.TvTitle;

public class TvTitlePresenter extends Presenter {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_title, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Presenter.ViewHolder viewHolder, Object o) {
        if (o instanceof TvTitle) {
            final ViewHolder vh = (ViewHolder) viewHolder;
            vh.tvTitle.setText(((TvTitle) o).getName());
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {

    }

    static class ViewHolder extends Presenter.ViewHolder {
        TextView tvTitle;

        public ViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tv_title);
        }

    }
}
