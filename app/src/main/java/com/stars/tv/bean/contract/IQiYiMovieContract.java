package com.stars.tv.bean.contract;

import com.stars.tv.bean.IQiYiMovieBean;
import com.stars.tv.model.IQiYiBaseModel;
import com.stars.tv.model.IQiYiBaseView;
import com.stars.tv.presenter.IQiYiBasePresenter;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public interface IQiYiMovieContract {
    interface IQiYiMovieModel extends IQiYiBaseModel {
        Observable<ResponseBody> getIQiYiMovie(String url);
    }

    interface IQiYiMovieView extends IQiYiBaseView {
        void returnIQiYiMovieList(ArrayList<IQiYiMovieBean> beans);
    }

    abstract class IQiYiMoviePresenter extends IQiYiBasePresenter<IQiYiMovieModel, IQiYiMovieView> {
        public abstract void requestIQiYiMovie(String url);
    }
}
