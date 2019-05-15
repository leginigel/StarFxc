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

public class IQiYiParseEpisodeListPresenter {

    private Observable<ResponseBody> getIQiYiPostConsumerUrl(String albumId, int size,int pageNum) {
        return RetrofitFactory.createApi(RetrofitService.class, Constants.BASE_IQIYI_POST_CONSUMER_URL)
                .getIQiYiEpisodeList(albumId,size,pageNum).compose(RxUtils.rxSchedulerHelper());
    }

    public void requestIQiYiEpisodeList(String albumId, int size, int pageNum, CallBack<List<IQiYiMovieBean>> listener) {

        RxManager.add(getIQiYiPostConsumerUrl(albumId,size,pageNum).subscribe(responseBody -> {
            List<IQiYiMovieBean> albumList = new ArrayList<>();
            try {
                JSONObject root = new JSONObject(responseBody.string());
                String code = root.getString("code");
                if(!code.equals("A00000")){
                    //error
                    listener.error("返回值错误");
                }
                JSONObject data = root.getJSONObject("data");
                String epsodelist = data.getString("epsodelist");
                Type listType = new TypeToken<List<IQiYiMovieBean>>() {}.getType();
                albumList = new Gson().fromJson(epsodelist,  listType);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (listener != null) {
                listener.success(albumList);
            }
        }, Throwable ->listener.error(Throwable.toString())));
    }
}
