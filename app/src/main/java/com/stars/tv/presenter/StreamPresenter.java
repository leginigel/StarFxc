package com.stars.tv.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.stars.tv.bean.YTM3U8Bean;
import com.stars.tv.server.RetrofitFactory;
import com.stars.tv.server.RetrofitService;
import com.stars.tv.utils.CallBack;
import com.stars.tv.utils.Constants;
import com.stars.tv.utils.MD5;
import com.stars.tv.utils.RxUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class StreamPresenter {

    private int p;

    private Observable<ResponseBody> getYTVideoInfo(String video_id) {
        return RetrofitFactory.createApi(RetrofitService.class, "https://www.youtube.com/")
                .getYTVideoInfo(video_id).compose(RxUtils.rxSchedulerHelper());
    }

    private Observable<ResponseBody> getBilibiliRoom(String id) {
        return RetrofitFactory.createApi(RetrofitService.class, "https://api.live.bilibili.com/room/v1/Room/")
                .getBilibiliRoom(id).compose(RxUtils.rxSchedulerHelper());
    }

    private Observable<ResponseBody> getBilibiliRealPlayUrl(String cid) {
        return RetrofitFactory.createApi(RetrofitService.class, "https://api.live.bilibili.com/room/v1/Room/")
                .getBilibiliRealPlayUrl(cid).compose(RxUtils.rxSchedulerHelper());
    }

    private Observable<ResponseBody> getHuyaChannelIpadPage(String channel) {
        Constants.CastStream = "huya";
        return RetrofitFactory.createApi(RetrofitService.class, "http://m.huya.com/")
                .getHuyaChannelIpadPage(channel).compose(RxUtils.rxSchedulerHelper());
    }

    private Observable<ResponseBody> getDouyuRealPlayUrl(String suffix, String auth){
        Constants.CastStream = "douyu";
        return RetrofitFactory.createApi(RetrofitService.class, "https://capi.douyucdn.cn/api/v1/")
                .getDouyuRealPlayUrl(suffix, auth);
    }

    private Observable<ResponseBody> getTwitchCheckLive(String channel){
//        Constants.CastStream = "twitch";
        return RetrofitFactory.createApi(RetrofitService.class, "https://api.twitch.tv/kraken/streams/")
                .getTwitchCheckLive(channel);
    }

    private Observable<ResponseBody> getTwitchAccessToken(String channel){
//        Constants.CastStream = "twitch";
        return RetrofitFactory.createApi(RetrofitService.class, "https://api.twitch.tv/api/channels/")
            .getTwitchAccessToken(channel);
    }

    private Observable<Response<String>> getTwitchRealPlayUrl(String channel, String sig, String token){
        p = (int)(Math.random()*999999+1);
        return RetrofitFactory.createApi(RetrofitService.class, "https://usher.ttvnw.net/api/channel/hls/")
        .getTwitchRealPlayUrl(channel, p, sig, token);
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
                        listener.error(e.toString());
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

    public void getBilibiliRealPlayUrl(String id, CallBack<String> listener){
        getBilibiliRoom(id)
                .flatMap((Function<ResponseBody, ObservableSource<ResponseBody>>) responseBody -> {
                    JSONObject root = new JSONObject(responseBody.string());
                    String code = root.getString("code");
                    Log.d("bilibili", code);
//                            if(!code.equals("A00000")) {
//                                //error
//                                listener.error("返回值错误");
//                            }
                    JSONObject data = root.getJSONObject("data");
                    String room_id = data.getString("room_id");

                    Log.d("bilibili", room_id);
                    return getBilibiliRealPlayUrl(room_id);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            JSONObject root = new JSONObject(responseBody.string());
                            JSONObject data = root.getJSONObject("data");
                            JSONArray durl = data.getJSONArray("durl");
                            listener.success(((JSONObject)durl.get(0)).getString("url"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.error(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getHuyaRealPlayUrl(String channel, CallBack<String> listener){
        getHuyaChannelIpadPage(channel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String s = responseBody.string();
                            String url = parseHuyaM3U8(s);
                            url = "http:" + url;
//                            Log.d("Huya", url);
                            listener.success(url);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Constants.CastStream = "iqiyi";
                    }
                });
    }

    private String parseHuyaM3U8(String s){
        Pattern pattern = Pattern
                            .compile("\\s*<video\\s+id=\"html5player-video\"\\s+src=\"([^\"]+)\"");
        Matcher dataMatcher = pattern.matcher(s);
        String ls = null;
        while (dataMatcher.find()){
            ls = dataMatcher.group(1);
        }
        return ls;
    }

    private  String getVMSUrl(String suffix){
        String secret = "zNzMV1y4EMxOHS6I5WKm";
        String encode = null;
        encode = new String((suffix + secret).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);

        Log.d("encode before", suffix+secret);
        Log.d("encode", encode);
        String sign = MD5.getMD5CodeStr(suffix + secret);
        return sign;
    }

    // API Fail
    public void getDouyuRealPlayUrl(String channel, CallBack<String> listener){
        long t = (new Date()).getTime();
        int ts = (int) (t / 1000);
        String [] cdns = {"ws", "tct", "ws2", "dl"};
        String suffix = "room/"+ channel +"?aid=wp&cdn="+ cdns[0] +"&client_sys=wp&time=" + ts;
        Log.d("suffix", suffix);
        String sign = getVMSUrl(suffix);
        Log.d("sign", sign);
        getDouyuRealPlayUrl(suffix, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            Log.d("douyu", responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Constants.CastStream = "iqiyi";
                    }
                });
    }

    public void getTwitchRealPlayUrl(String channel, CallBack<String> listener){
        getTwitchCheckLive(channel)
                .flatMap(new Function<ResponseBody, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(ResponseBody responseBody) throws Exception {
                        String s = responseBody.string();
//                        Log.d("response123", s);
                        return getTwitchAccessToken(channel);
                    }
                })
                .flatMap(new Function<ResponseBody, ObservableSource<Response<String>>>() {
                    @Override
                    public ObservableSource<Response<String>> apply(ResponseBody responseBody) throws Exception {
                        String s = responseBody.string();

//                        Log.d("response", s);
                        JSONObject jsonResponse = new JSONObject(s);
                        String sig = jsonResponse.getString("sig");
                        String token = jsonResponse.getString("token");
//                        Log.d("Token", token);
//                        Log.d("sig", sig);
                        String encode = URLEncoder.encode(token, "UTF-8")
                                .replaceAll("\\+", "%20")
                                .replaceAll("\\%21", "!")
                                .replaceAll("\\%27", "'")
                                .replaceAll("\\%28", "(")
                                .replaceAll("\\%29", ")")
                                .replaceAll("\\%7E", "~");
                        String playUrl = "https://usher.ttvnw.net/api/channel/hls/" + channel + ".m3u8?" +
                                "type=any&allow_source=true&baking_bread=false&baking_brownies=false" +
                                "&baking_brownies_timeout=1050&fast_bread=true&allow_spectre=false" +
                                "&reassignments_supported=true" +
                                "&p=" + p +
                                "&sig=" + sig +
                                "&token=" + encode;
//                        Log.d("playUrl", playUrl);
//                        listener.success(playUrl);
                        return getTwitchRealPlayUrl(channel, sig, token);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<String>>() {
                    @Override
                    public void onNext(Response<String> response) {
                        String playUrl = response.raw().request().url().toString();
                        Log.d("response", response.raw().request().url().toString());
                        Log.d("response", response.code() +response.message());
                        if(response.code() == 200)
                            listener.success(playUrl);
//                        Log.d("response321", response.toString());
//                        Log.d("response321", response.body());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
