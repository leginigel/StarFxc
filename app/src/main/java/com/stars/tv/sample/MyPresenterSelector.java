package com.stars.tv.sample;

import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;

public class MyPresenterSelector extends PresenterSelector {
    private VideoListRowPresenter videoListRowPresenter = new VideoListRowPresenter();
    private ButtonRowPresenter buttonRowPresenter = new ButtonRowPresenter();

    public MyPresenterSelector() {
        videoListRowPresenter.setNumRows(1);
        videoListRowPresenter.setHeaderPresenter(new HeaderPresenter());

        buttonRowPresenter.setHeaderPresenter(new HeaderPresenter());
    }

    @Override
    public Presenter getPresenter(Object item) {
        ListRow listRow = (ListRow) item;
        if ((item instanceof ButtonListRow))
            return buttonRowPresenter;
        return videoListRowPresenter;
    }

    @Override
    public Presenter[] getPresenters() {
        return new Presenter[]{
                videoListRowPresenter,
                buttonRowPresenter
        };
    }
}
