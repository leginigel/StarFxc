package com.stars.tv.sample;

import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;

public class MyPresenterSelector extends PresenterSelector {
    private VideoListRowPresenter videoListRowPresenter = new VideoListRowPresenter();
    private ButtonRowPresenter buttonRowPresenter = new ButtonRowPresenter();
    private PortraitVideoListRowPresenter portraitvideoListRowPresenter = new PortraitVideoListRowPresenter();
    private PortraitVideoListRow1Presenter portraitvideoListRow1Presenter = new PortraitVideoListRow1Presenter();
    private SeriesButtonRowPresenter seriesButtonRowPresenter = new SeriesButtonRowPresenter();
    private HotVideoListRowPresenter hotVideoListRowPresenter = new HotVideoListRowPresenter();
    private RecButtonRowPresenter recbuttonRowPresenter = new RecButtonRowPresenter();
    private LandscapeVideoListRowPresenter landscapevideoListRowPresenter = new LandscapeVideoListRowPresenter();
    private FilmVideoListRowPresenter filmVideoListRowPresenter = new FilmVideoListRowPresenter();
    private FilmButtonRowPresenter filmButtonRowPresenter = new FilmButtonRowPresenter();
    private FilmHotListRowPresenter filmHotListRowPresenter = new FilmHotListRowPresenter();

    public MyPresenterSelector() {

        videoListRowPresenter.setNumRows(1);
        videoListRowPresenter.setHeaderPresenter(new HeaderPresenter());
        filmVideoListRowPresenter.setNumRows(2);
        filmVideoListRowPresenter.setHeaderPresenter(new HeaderPresenter());
        filmVideoListRowPresenter.setHeaderPresenter(new HeaderPresenter());
        buttonRowPresenter.setHeaderPresenter(new HeaderPresenter());

        hotVideoListRowPresenter.setNumRows(1);
        seriesButtonRowPresenter.setHeaderPresenter(new HeaderPresenter());
        filmHotListRowPresenter.setNumRows(1);
        filmButtonRowPresenter.setHeaderPresenter(new HeaderPresenter());
        portraitvideoListRowPresenter.setNumRows(2);
        portraitvideoListRowPresenter.setHeaderPresenter(new HeaderPresenter());
        portraitvideoListRow1Presenter.setHeaderPresenter(new HeaderPresenter());
        landscapevideoListRowPresenter.setHeaderPresenter(new HeaderPresenter());
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
        if((item instanceof PortraitVideoListRow))
            return portraitvideoListRowPresenter;
        if((item instanceof PortraitVideoListRow1))
            return portraitvideoListRow1Presenter;
        if((item instanceof RecButtonListRow))
            return recbuttonRowPresenter;
        if((item instanceof LandscapeVideoListRow))
            return landscapevideoListRowPresenter;
        if((item instanceof FilmVideoListRow))
            return filmVideoListRowPresenter;
        if((item instanceof FilmHotListRow))
            return filmHotListRowPresenter;
        if((item instanceof FilmButtonListRow))
            return filmButtonRowPresenter;
        return videoListRowPresenter;
    }

    @Override
    public Presenter[] getPresenters() {
        return new Presenter[]{
                videoListRowPresenter,
                buttonRowPresenter,
                seriesButtonRowPresenter,
                hotVideoListRowPresenter,
                portraitvideoListRowPresenter,
                portraitvideoListRow1Presenter,
                landscapevideoListRowPresenter,
                recbuttonRowPresenter,
                filmButtonRowPresenter,
                filmHotListRowPresenter,
                filmVideoListRowPresenter
        };
    }
}
