package com.stars.tv.model;


import com.stars.tv.bean.contract.IQiYiMovieContract;
import com.stars.tv.server.RetrofitFactory;
import com.stars.tv.server.RetrofitService;
import com.stars.tv.utils.Constants;
import com.stars.tv.utils.RxUtils;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class IQiYiMovieModel implements IQiYiMovieContract.IQiYiMovieModel {
    @Override
    public Observable<ResponseBody> getIQiYiMovie(String url) {
        return RetrofitFactory.createApi(RetrofitService.class, Constants.BASE_IQIYI_LIST_URL)
                .getIQiYiMovieList(url).compose(RxUtils.rxSchedulerHelper());
    }}
