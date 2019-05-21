package com.stars.tv.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stars.tv.bean.IQiYiMovieSimplifiedBean;
import com.stars.tv.server.RetrofitFactory;
import com.stars.tv.server.RetrofitService;
import com.stars.tv.server.RxManager;
import com.stars.tv.utils.CallBack;
import com.stars.tv.utils.Constants;
import com.stars.tv.utils.RxUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class IQiYiMovieSimplifiedListPresenter {

    private Observable<ResponseBody> getIQiYiPostConsumerUrl(int channel, String orderList, String payStatus, String myYear,
                                                             int sortType, int pageNum, int dataType, String siteType,
                                                             int sourceType, String comicsStatus, int pageSize) {

        return RetrofitFactory.createApi(RetrofitService.class, Constants.BASE_IQIYI_POST_CONSUMER_URL)
                .getIQiYiMovieSimplifiedList(channel,orderList,payStatus,myYear,sortType,pageNum,
                        dataType,siteType,sourceType,comicsStatus,pageSize).compose(RxUtils.rxSchedulerHelper());
    }


    public void requestIQiYiMovieSimplifiedList(int channel, String orderList, String payStatus, String myYear,
                                                int sortType, int pageNum, int dataType, String siteType,
                                                int sourceType, String comicsStatus, int pageSize, CallBack<IQiYiMovieSimplifiedBean> listener) {

        RxManager.add(getIQiYiPostConsumerUrl(channel,orderList,payStatus,myYear,sortType,pageNum,
                dataType,siteType,sourceType,comicsStatus,pageSize).subscribe(responseBody -> {
            IQiYiMovieSimplifiedBean movieListBean = new IQiYiMovieSimplifiedBean();
            try {
                JSONObject root = new JSONObject(responseBody.string());
                String code = root.getString("code");
                if(!code.equals("A00000")){
                    //error
                    listener.error("返回值错误");
                }
                String data = root.getString("data");
                Type type = new TypeToken<IQiYiMovieSimplifiedBean>() {}.getType();
                movieListBean = new Gson().fromJson(data, type);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (listener != null) {
                listener.success(movieListBean);
            }
        }, Throwable ->listener.error(Throwable.toString())));
    }
}
