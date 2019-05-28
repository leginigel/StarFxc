package com.stars.tv.sample;

import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;

public class MyPresenterSelector extends PresenterSelector {
    private VideoListRowPresenter videoListRowPresenter = new VideoListRowPresenter();
    private SeriesVideoListRowPresenter seriesvideoListRowPresenter = new SeriesVideoListRowPresenter();
    private ButtonRowPresenter buttonRowPresenter = new ButtonRowPresenter();
    private SeriesButtonRowPresenter seriesButtonRowPresenter = new SeriesButtonRowPresenter();
    private HotVideoListRowPresenter hotVideoListRowPresenter = new HotVideoListRowPresenter();

    public MyPresenterSelector() {

        videoListRowPresenter.setNumRows(1);
        videoListRowPresenter.setHeaderPresenter(new HeaderPresenter());
        seriesvideoListRowPresenter.setNumRows(2);
        seriesvideoListRowPresenter.setHeaderPresenter(new HeaderPresenter());
        hotVideoListRowPresenter.setNumRows(1);
//        hotVideoListRowPresenter.setHeaderPresenter(new HeaderPresenter());
        buttonRowPresenter.setHeaderPresenter(new HeaderPresenter());

        seriesButtonRowPresenter.setHeaderPresenter(new HeaderPresenter());
    }

    @Override
    public Presenter getPresenter(Object item) {
        ListRow listRow = (ListRow) item;
//        HeaderItem headerItem = listRow.getHeaderItem();
//        if (headerItem == null) {
//            videoListRowPresenter.setNumRows(1);
//        } else {
//            videoListRowPresenter.setNumRows(2);
//        }
        if ((item instanceof ButtonListRow))
            return buttonRowPresenter;
        if((item instanceof SeriesButtonListRow))
            return seriesButtonRowPresenter;
        if((item instanceof HotVideoListRow))
            return hotVideoListRowPresenter;
        if((item instanceof SeriesVideoListRow))
            return seriesvideoListRowPresenter;
        return videoListRowPresenter;
    }

    @Override
    public Presenter[] getPresenters() {
        return new Presenter[]{
                videoListRowPresenter,
                buttonRowPresenter,
                seriesButtonRowPresenter,
                hotVideoListRowPresenter,
                seriesvideoListRowPresenter
        };
    }
}
