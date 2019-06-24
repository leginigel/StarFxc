package com.stars.tv.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.stars.tv.bean.YTM3U8Bean;
import com.stars.tv.server.RetrofitFactory;
import com.stars.tv.server.RetrofitService;
import com.stars.tv.utils.CallBack;
import com.stars.tv.utils.RxUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class StreamPresenter {
    private Observable<ResponseBody> getYTVideoInfo(String video_id) {
        return RetrofitFactory.createApi(RetrofitService.class, "https://www.youtube.com/")
                .getYTVideoInfo(video_id).compose(RxUtils.rxSchedulerHelper());
    }

    public void getYTVideoInfo(String video_id, CallBack<YTM3U8Bean> listener){
        getYTVideoInfo(video_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
//                            Log.d("onNext", s);
                            String json = parseYTJSON(responseBody.string());
                            YTM3U8Bean ytM3U8Bean = new Gson().fromJson(json, YTM3U8Bean.class);
                            listener.success(ytM3U8Bean);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private String parseYTJSON(String s){
        String decodeVideoInfo = null;
        try {
            decodeVideoInfo = URLDecoder.decode(s, "UTF-8");
//            Log.d("test1", decodeVideoInfo);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Pattern pattern = Pattern.compile("\\&player_response\\=(.*?)\\&");
        Matcher dataMatcher = pattern.matcher(decodeVideoInfo);
        String ls = null;
        while (dataMatcher.find()){
            ls = dataMatcher.group(1);
        }
//        Log.d("test2", ls);
        return ls;
    }
}
