package com.stars.tv.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stars.tv.bean.IQiYiMovieBean;
import com.stars.tv.bean.IQiYiMovieSimplifiedBean;
import com.stars.tv.bean.contract.IQiYiMovieContract;
import com.stars.tv.server.RetrofitFactory;
import com.stars.tv.server.RetrofitService;
import com.stars.tv.server.RxManager;
import com.stars.tv.utils.Constants;
import com.stars.tv.utils.RxUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class IQiYiMoviePresenter extends IQiYiMovieContract.IQiYiMoviePresenter {

    private Observable<ResponseBody> getIQiYiPostConsumerUrl(int channel, String orderList, String payStatus, String myYear,
                                                             int sortType, int pageNum, int dataType, String siteType,
                                                             int sourceType, String comicsStatus, int pageSize) {

        return RetrofitFactory.createApi(RetrofitService.class, Constants.BASE_IQIYI_POST_CONSUMER_URL)
                .getIQiYiMovieSimplifiedList(channel,orderList,payStatus,myYear,sortType,pageNum,
                        dataType,siteType,sourceType,comicsStatus,pageSize).compose(RxUtils.rxSchedulerHelper());
    }

    @Override
    public void requestIQiYiMovie(int channel, String orderList, String payStatus, String myYear,
                                  int sortType, int pageNum, int dataType, String siteType,
                                  int sourceType, String comicsStatus, int pageSize) {
        RxManager.add(getIQiYiPostConsumerUrl(channel, orderList, payStatus, myYear, sortType, pageNum,
                dataType, siteType, sourceType, comicsStatus, pageSize).subscribe(responseBody -> {
            IQiYiMovieSimplifiedBean movieListBean;
            try {
                JSONObject root = new JSONObject(responseBody.string());
                String code = root.getString("code");
                if (!code.equals("A00000")) {
                    //error
                    mView.showError("返回值错误");
                }
                String data = root.getString("data");
                Type type = new TypeToken<IQiYiMovieSimplifiedBean>() {
                }.getType();
                movieListBean = new Gson().fromJson(data, type);
                mView.returnIQiYiMovieList(movieListBean.getList(),movieListBean.getTotal());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
        }, Throwable -> mView.showError(Throwable.toString())));
    }
}
