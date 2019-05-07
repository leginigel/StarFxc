package com.stars.tv.presenter;

import com.google.gson.Gson;
import com.stars.tv.bean.IQiYiVideoBaseInfoBean;
import com.stars.tv.server.RetrofitFactory;
import com.stars.tv.server.RetrofitService;
import com.stars.tv.server.RxManager;
import com.stars.tv.utils.CallBack;
import com.stars.tv.utils.Constants;
import com.stars.tv.utils.RxUtils;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class IQiYiParseVideoBaseInfoPresenter {

    private Observable<ResponseBody> getIQiYiPostConsumerUrl(String tvId) {
        return RetrofitFactory.createApi(RetrofitService.class, Constants.BASE_IQIYI_POST_CONSUMER_URL)
                .getIQiYiVideoBaseInfo(tvId).compose(RxUtils.rxSchedulerHelper());
    }



    public void requestIQiYiVideoBaseInfo(String tvId, CallBack<IQiYiVideoBaseInfoBean> listener) {
        RxManager.add(getIQiYiPostConsumerUrl(tvId).subscribe(responseBody -> {
            IQiYiVideoBaseInfoBean bean;
            try {
                JSONObject root = new JSONObject(responseBody.string());
                String code = root.getString("code");
                if (!code.equals("A00000")) {
                    //error
                    listener.error("返回值错误");
                }

                String data = root.getString("data");
                bean = new Gson().fromJson(data, IQiYiVideoBaseInfoBean.class);

                if (listener != null) {
                    listener.success(bean);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        },Throwable ->listener.error(Throwable.toString())));
    }

}
