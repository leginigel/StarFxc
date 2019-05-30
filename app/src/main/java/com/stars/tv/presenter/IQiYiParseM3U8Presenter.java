package com.stars.tv.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stars.tv.bean.IQiYiM3U8Bean;
import com.stars.tv.server.RetrofitFactory;
import com.stars.tv.server.RetrofitService;
import com.stars.tv.server.RxManager;
import com.stars.tv.utils.CallBack;
import com.stars.tv.utils.Constants;
import com.stars.tv.utils.MD5;
import com.stars.tv.utils.RxUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class IQiYiParseM3U8Presenter {

  private Observable<ResponseBody> getIQiYiParseM3U8Url(String url) {
    return RetrofitFactory.createApi(RetrofitService.class, Constants.BASE_IQIYI_PARSE_M3U8_URL)
      .getIQiYiRealPlayUrl(url).compose(RxUtils.rxSchedulerHelper());
  }

  private Observable<ResponseBody> getIQiYiBaseUrl(String url) {
    return RetrofitFactory.createApi(RetrofitService.class, Constants.BASE_IQIYI_URL)
      .getIQiYiVideoBaseInfoWithUrl(url).compose(RxUtils.rxSchedulerHelper());
  }

  private Observable<ResponseBody> getIQiYiPostConsumerUrl(String tvId) {
    return RetrofitFactory.createApi(RetrofitService.class, Constants.BASE_IQIYI_POST_CONSUMER_URL)
      .getIQiYiVideoBaseInfo(tvId).compose(RxUtils.rxSchedulerHelper());
  }

  public void requestIQiYiRealPlayUrl(String playUrl, CallBack<List<IQiYiM3U8Bean>> listener) {

    RxManager.add(getIQiYiBaseUrl(playUrl).subscribe(responseBody -> {
      Document doc = Jsoup.parse(responseBody.string());
      if (null!=doc){
        Elements lis = doc.select("div[id=iqiyi-main]").select("div[is=i71-play]");
        String pageinfo = lis.attr(":page-info");
        JSONObject root = new JSONObject(pageinfo);
        String tvid = root.getString("tvId");
        String vid = root.getString("vid");
        String cacheUrl = getVMSUrl(tvid, vid);
        RxManager.add(getIQiYiParseM3U8Url(cacheUrl).subscribe(resBody -> {
          List<IQiYiM3U8Bean> list;
          list = getPlayList(resBody.string());
          if (listener != null) {
            listener.success(list);
          }
        }, Throwable ->listener.error(Throwable.toString())));
      }
    }, Throwable ->listener.error(Throwable.toString())));
  }

  public void requestIQiYiRealPlayUrlWithTvId(String tvId, CallBack<List<IQiYiM3U8Bean>> listener) {
    RxManager.add(getIQiYiPostConsumerUrl(tvId).subscribe(responseBody -> {
      JSONObject root = new JSONObject(responseBody.string());
      String code = root.getString("code");
      if(!code.equals("A00000")){
        //error
        listener.error("返回值错误");
      }
      JSONObject data = root.getJSONObject("data");
      String vid = data.getString("vid");
      String cacheUrl = getVMSUrl(tvId, vid);
      RxManager.add(getIQiYiParseM3U8Url(cacheUrl).subscribe(resBody -> {
        List<IQiYiM3U8Bean> list = getPlayList(resBody.string());
        if (listener != null) {
          listener.success(list);
        }
      }, Throwable ->listener.error(Throwable.toString())));
    }));
  }


  public void requestIQiYiRealPlayUrl(String tvId,String vid, CallBack<List<IQiYiM3U8Bean>> listener) {
    String cacheUrl = getVMSUrl(tvId, vid);
    RxManager.add(getIQiYiParseM3U8Url(cacheUrl).subscribe(resBody -> {
      List<IQiYiM3U8Bean> list = getPlayList(resBody.string());
      if (listener != null) {
        listener.success(list);
      }
    }, Throwable ->listener.error(Throwable.toString())));
  }

  private  String getVMSUrl(String tvid, String vid) {
    long t = (new Date()).getTime();
    String src = "76f90cbd92f94a2e925d83e8ccd22cb7";
    String key = "d5fb4bd9d50c4be6948c97edd7254b0e";
    String sc = MD5.getMD5CodeStr(t + key + vid);
    return "tmts/"+tvid + '/' + vid + "/?t=" + t + "&sc=" + sc + "&src=" + src;
  }

  private List<IQiYiM3U8Bean> getPlayList(String vms) {

    List<IQiYiM3U8Bean> list = new ArrayList<>();

    try {
      JSONObject root = new JSONObject(vms);
      String code = root.getString("code");
      if(!code.equals("A00000")){
        //error
      }
      JSONObject data = root.getJSONObject("data");
      String vidList = data.getString("vidl");
      Type listType = new TypeToken<List<IQiYiM3U8Bean>>() {}.getType();
      list = new Gson().fromJson(vidList,  listType);

    } catch (JSONException e) {
      e.printStackTrace();
    }
    return list;
  }
}
