package com.stars.tv.presenter;

import com.google.gson.Gson;
import com.stars.tv.bean.IQiYiVideoBaseInfoBean;
import com.stars.tv.bean.IQiYiVideoBaseInfoWithUrlBean;
import com.stars.tv.server.RetrofitFactory;
import com.stars.tv.server.RetrofitService;
import com.stars.tv.server.RxManager;
import com.stars.tv.utils.CallBack;
import com.stars.tv.utils.Constants;
import com.stars.tv.utils.RxUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class IQiYiParseVideoBaseInfoPresenter {

    private Observable<ResponseBody> getIQiYiPostConsumerUrl(String tvId) {
        return RetrofitFactory.createApi(RetrofitService.class, Constants.BASE_IQIYI_POST_CONSUMER_URL)
                .getIQiYiVideoBaseInfo(tvId).compose(RxUtils.rxSchedulerHelper());
    }

    private Observable<ResponseBody> getIQiYiBaseUrl(String url) {
        return RetrofitFactory.createApi(RetrofitService.class, Constants.BASE_IQIYI_URL)
                .getIQiYiVideoBaseInfoWithUrl(url).compose(RxUtils.rxSchedulerHelper());
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

    public void requestIQiYiVideoBaseInfoWithUrl(String playUrl, CallBack<IQiYiVideoBaseInfoWithUrlBean> listener) {

        RxManager.add(getIQiYiBaseUrl(playUrl).subscribe(responseBody -> {
            Document doc = Jsoup.parse(responseBody.string());
            IQiYiVideoBaseInfoWithUrlBean bean;
            if (null!=doc){
                Elements lis = doc.select("div[id=iqiyi-main]").select("div[is=i71-play]");
                String pageinfo = lis.attr(":page-info");
                bean = new Gson().fromJson(pageinfo, IQiYiVideoBaseInfoWithUrlBean.class);
                if (listener != null) {
                    listener.success(bean);
                }
            }
        }, Throwable ->listener.error(Throwable.toString())));
    }

}
