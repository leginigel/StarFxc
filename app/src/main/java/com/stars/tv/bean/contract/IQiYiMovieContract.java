package com.stars.tv.bean.contract;

import com.stars.tv.bean.IQiYiMovieBean;
import com.stars.tv.model.IQiYiBaseView;
import com.stars.tv.presenter.IQiYiBasePresenter;

import java.util.List;

public interface IQiYiMovieContract {

    interface IQiYiMovieView extends IQiYiBaseView {
        void returnIQiYiMovieList(List<IQiYiMovieBean> beans,int number);
    }

    abstract class IQiYiMoviePresenter extends IQiYiBasePresenter<IQiYiMovieView> {
        public abstract void requestIQiYiMovie(int channel, String orderList, String payStatus, String myYear,
                                                    int sortType, int pageNum, int dataType, String siteType,
                                                    int sourceType, String comicsStatus, int pageSize);
    }
}
