package com.stars.tv.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stars.tv.bean.IQiYiMovieBean;
import com.stars.tv.server.RetrofitFactory;
import com.stars.tv.server.RetrofitService;
import com.stars.tv.server.RxManager;
import com.stars.tv.utils.CallBack;
import com.stars.tv.utils.Constants;
import com.stars.tv.utils.RxUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class IQiYiParseStarRecommendPresenter {

    private Observable<ResponseBody> getIQiYiPostConsumerUrl(String starId, String size,String tvId, boolean withCookie) {
        return RetrofitFactory.createApi(RetrofitService.class, Constants.BASE_IQIYI_POST_CONSUMER_URL)
                .getIQiYiStarRecommend(starId, size, tvId, withCookie).compose(RxUtils.rxSchedulerHelper());
    }



    public void requestIQiYiStarRecommendList(String starId, String size,String tvId, boolean withCookie, CallBack<List<IQiYiMovieBean>> listener) {
        RxManager.add(getIQiYiPostConsumerUrl(starId, size, tvId, withCookie).subscribe(responseBody -> {
            ArrayList<IQiYiMovieBean> list;
            try {
                JSONObject root = new JSONObject(responseBody.string());
                String code = root.getString("code");
                if (!code.equals("A00000")) {
                    //error
                    listener.error("返回值错误");
                }

                String dataList = root.getJSONObject("data").getString("list");
                Type listType = new TypeToken<List<IQiYiMovieBean>>() {}.getType();

                list = new Gson().fromJson(dataList, listType);
                if (listener != null) {
                    listener.success(list);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        },Throwable ->listener.error(Throwable.toString())));
    }

}
