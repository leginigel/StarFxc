package com.stars.tv.presenter;

import com.google.gson.Gson;
import com.stars.tv.bean.IQiYiBasicStarInfoBean;
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

public class IQiYiParseBasicStarInfoPresenter {

    private Observable<ResponseBody> getIQiYiPostConsumerUrl(String starId, String channleIds, int size) {
        return RetrofitFactory.createApi(RetrofitService.class, Constants.BASE_IQIYI_POST_CONSUMER_URL)
                .getIQiYiBasicStarsInfo(starId,channleIds,size).compose(RxUtils.rxSchedulerHelper());
    }

    public void requestIQiYiStarsInfo(String starId, String channleIds, int size, CallBack<IQiYiBasicStarInfoBean> listener) {

        RxManager.add(getIQiYiPostConsumerUrl(starId,channleIds,size).subscribe(responseBody -> {
            IQiYiBasicStarInfoBean basicStarInfo = new IQiYiBasicStarInfoBean();
            try {
                JSONObject root = new JSONObject(responseBody.string());
                String code = root.getString("code");
                if (!code.equals("A00000")) {
                    //error
                    listener.error("返回值错误");
                }
                String data = root.getJSONArray("data").getString(0);
                basicStarInfo = new Gson().fromJson(data, IQiYiBasicStarInfoBean.class);

                if (listener != null) {
                    listener.success(basicStarInfo);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable ->listener.error(Throwable.toString())));
    }
}
