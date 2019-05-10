package com.stars.tv.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stars.tv.bean.IQiYiTopListBean;
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

public class IQiYiParseTopListPresenter {

    private Observable<ResponseBody> getIQiYiTopListUrl(String cid, String type,int size, int page) {
        return RetrofitFactory.createApi(RetrofitService.class, Constants.BASE_IQIYI_POST_CONSUMER_URL)
                .getIQiYiTopList(cid,type,size,page).compose(RxUtils.rxSchedulerHelper());
    }

    public void requestIQiYiTopList(String cid, String type,int size, int page, CallBack<List<IQiYiTopListBean>> listener) {

        RxManager.add(getIQiYiTopListUrl(cid,type,size,page).subscribe(responseBody -> {
            List<IQiYiTopListBean> hotList = new ArrayList<>();
            try {
                JSONObject root = new JSONObject(responseBody.string());
                String code = root.getString("code");
                if(!code.equals("A00000")){
                    //error
                    listener.error("返回值错误");
                }

                String dataList = root.getString("data");
                Type listType = new TypeToken<List<IQiYiTopListBean>>() {}.getType();
                hotList = new Gson().fromJson(dataList,  listType);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (listener != null) {
                listener.success(hotList);
            }
        }, Throwable ->listener.error(Throwable.toString())));
    }
}
