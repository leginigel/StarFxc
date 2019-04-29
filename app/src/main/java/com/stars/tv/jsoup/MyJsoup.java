package com.stars.tv.jsoup;

import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stars.tv.bean.IQiYiListBean;
import com.stars.tv.bean.IQiYiMovieBean;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MyJsoup {

    private String url = "http://list.iqiyi.com";

    private requestEndListener mRequestEndListener;
    private List<IQiYiMovieBean> simpleList=new ArrayList<>();


    /*
    * 启动下载线程
    * */
    public void start(){

        new LoadThread().start();
    }
    public void setUrl(IQiYiListBean listBean){
        url = "http://list.iqiyi.com/www/"+
                listBean.getChannel()+"/" +
                listBean.getCategory() + "-" +
                listBean.getType() + "-" +
                listBean.getPublishCategory() + "-" +
                listBean.getBrandArea() + "-" +
                listBean.getNewType() + "-" +
                listBean.getStyle() + "-" +
                listBean.getThirdType() + "-" +
                listBean.getFourthType() + "---" +
                listBean.getCharge() + "-" +
                listBean.getTimes() + "--" +
                listBean.getSort() + "-" +
                listBean.getPage() + "-" +
                listBean.getVideoType() + "-" +
                listBean.getSearchScope() + "-" +
                listBean.getUserUpload() + "-" +
                listBean.getSerialState() + ".html";
    }

  //新线程
    private class LoadThread extends Thread{

        @Override
        public void run() {
            super.run();
            //发起请求
            Log.v("ttt","url=:"+ url);
            Document doc = getRequestAddHeader(url);
            boolean isOK = false;
            if (null!=doc){

                Elements block = doc.select("div[id=block-D]");
                String result = block.attr(":first-search-list");
                try {
                    JSONObject root = new JSONObject(result);
                    String movieList = root.getString("list");
                    Type listType = new TypeToken<List<IQiYiMovieBean>>() {}.getType();
                    simpleList = new Gson().fromJson(movieList,  listType);
                    isOK = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            Message message=new Message();
            message.obj=simpleList;
            message.what=1;
            mRequestEndListener.onRequestEnd(message.obj,isOK);
        }
    }

    /*
    * jsoup添加header
    * */
    private Document getRequestAddHeader(String url) {

        Document doc = null;
        try {

            doc = Jsoup.connect(url)
                    .header("Accept", "*/*")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                    .header("Referer", "https://www.baidu.com/")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
                    .timeout(60000)
                    .maxBodySize(0)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    /*
    * 请求完成后的监听
    * */
    public interface requestEndListener{
        void onRequestEnd(Object movieList, boolean isSuccess);
    }

    public void setRequestListener(requestEndListener listener){
        this.mRequestEndListener=listener;
    }


}
