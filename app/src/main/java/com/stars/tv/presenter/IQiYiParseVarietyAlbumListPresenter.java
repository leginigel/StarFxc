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

public class IQiYiParseVarietyAlbumListPresenter {

    private Observable<ResponseBody> getIQiYiPostConsumerUrl(String albumId,String timeList) {
        return RetrofitFactory.createApi(RetrofitService.class, Constants.BASE_IQIYI_POST_CONSUMER_URL)
                .getIQiYiVarietyAlbumList(albumId,6,timeList).compose(RxUtils.rxSchedulerHelper());
    }

    public void requestIQiYiVarietyAlbumList(String albumId,String timeList, CallBack<List<IQiYiMovieBean>> listener) {

        RxManager.add(getIQiYiPostConsumerUrl(albumId,timeList).subscribe(responseBody -> {
            List<IQiYiMovieBean> albumList = new ArrayList<>();
            try {
                JSONObject root = new JSONObject(responseBody.string());
                String code = root.getString("code");
                if(!code.equals("A00000")){
                    //error
                    listener.error("返回值错误");
                }
                JSONObject data = root.getJSONObject("data");
                String listData = data.getString(timeList);
                Type listType = new TypeToken<List<IQiYiMovieBean>>() {}.getType();
                albumList = new Gson().fromJson(listData,  listType);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (listener != null) {
                listener.success(albumList);
            }
        }, Throwable ->listener.error(Throwable.toString())));
    }

    private Observable<ResponseBody> getIQiYiHotPlayTimesPage(String tvId) {
        return RetrofitFactory.createApi(RetrofitService.class, Constants.BASE_IQIYI_POST_CONSUMER_URL)
                .getIQiYiHotPlayTimes(tvId).compose(RxUtils.rxSchedulerHelper());
    }

    /**
     * 获取video播放热度
     * @param tvId video tvid
     */
    public void requestIQiYiHotPlayTimes(String tvId, CallBack<String> listener) {

        RxManager.add(getIQiYiHotPlayTimesPage(tvId).subscribe(responseBody -> {
            String hot="";
            try {
                JSONObject root = new JSONObject(responseBody.string());
                String code = root.getString("code");
                if(!code.equals("A00000")){
                    //error
                }
                JSONObject data = root.getJSONArray("data").getJSONObject(0);
                hot = data.getString("hot");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (listener != null) {
                listener.success(hot);
            }
        }, Throwable ->listener.error(Throwable.toString())));
    }
}
