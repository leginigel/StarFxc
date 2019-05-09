package com.stars.tv.presenter;

import com.stars.tv.bean.IQiYiBannerInfoBean;
import com.stars.tv.server.RetrofitFactory;
import com.stars.tv.server.RetrofitService;
import com.stars.tv.server.RxManager;
import com.stars.tv.utils.CallBack;
import com.stars.tv.utils.Constants;
import com.stars.tv.utils.RxUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class IQiYiParseBannerInfoPresenter {

    private Observable<ResponseBody> getIQiYiBaseUrl(String channel) {
        return RetrofitFactory.createApi(RetrofitService.class, Constants.BASE_IQIYI_URL)
                .getIQiYiBannerInfo(channel).compose(RxUtils.rxSchedulerHelper());
    }

    public void requestIQiYiBannerInfo(String channel, CallBack<List<IQiYiBannerInfoBean>> listener) {
        RxManager.add(getIQiYiBaseUrl(channel).subscribe(responseBody -> {

            List<IQiYiBannerInfoBean> infoList = new ArrayList<>();
            try {
                Document doc = Jsoup.parse(responseBody.string());
                if (null!=doc) {
                    Elements imgList = doc.select("ul.img-list").select("li");

                    switch (channel) {
                        case "dianying":
                            {
                                for (int i = 0; i < imgList.size(); i++) {
                                    IQiYiBannerInfoBean bean = new IQiYiBannerInfoBean();
                                    String style = imgList.get(i).attr("style");
                                    String imageUrl = "http://" + style.substring(style.indexOf("pic"), style.indexOf("jpg") + 3);
                                    String name = imgList.get(i).select("a.img-link").attr("data-indexfocus-currenttitleelem");
                                    String playUrl = "http:" + imgList.get(i).select("a.img-link").attr("href");
                                    String description = imgList.get(i).select("a.img-link").attr("data-indexfocus-description");

                                    bean.setImageUrl(imageUrl);
                                    bean.setName(name);
                                    bean.setPlayUrl(playUrl);
                                    bean.setDescription(description);
                                    infoList.add(bean);
                                }
                            }
                            break;
                        case "dianshiju":
                            {
                                Elements nameList = doc.select("a.focus_title_inner");
                                for (int i = 0; i < imgList.size(); i++) {
                                    IQiYiBannerInfoBean bean = new IQiYiBannerInfoBean();
                                    String style = imgList.get(i).attr("style");
                                    String sty = imgList.get(i).attr(":style");
                                    String imageUrl;
                                    if (!style.contains("pic")) {
                                        imageUrl = "http://" + sty.substring(sty.indexOf("pic"), sty.indexOf("jpg") + 3);
                                    } else {
                                        imageUrl = "http://" + style.substring(style.indexOf("pic"), style.indexOf("jpg") + 3);
                                    }

                                    String name = nameList.get(i).select("div.caption").text();
                                    String playUrl = "http:" + nameList.get(i).attr("href");
                                    String description = nameList.get(i).select("p.desc").text();

                                    bean.setImageUrl(imageUrl);
                                    bean.setName(name);
                                    bean.setPlayUrl(playUrl);
                                    bean.setDescription(description);
                                    infoList.add(bean);
                                }
                            }
                            break;
                        case "weidianying":
                        {
                            Elements nameList = doc.select("a.focus_title_inner");
                            for (int i = 0; i < imgList.size(); i++) {
                                IQiYiBannerInfoBean bean = new IQiYiBannerInfoBean();
                                String style = imgList.get(i).attr("style");
                                String imageUrl = "http://" + style.substring(style.indexOf("pic"), style.indexOf("jpg") + 3);

                                String name = nameList.get(i).select("div.caption").text();
                                String playUrl = "http:" + nameList.get(i).attr("href");
                                String description = nameList.get(i).select("p.desc").text();

                                bean.setImageUrl(imageUrl);
                                bean.setName(name);
                                bean.setPlayUrl(playUrl);
                                bean.setDescription(description);
                                infoList.add(bean);
                            }
                        }
                        break;
                        case "dongman":
                            {
                                for (int i = 0; i < imgList.size(); i++) {
                                    IQiYiBannerInfoBean bean = new IQiYiBannerInfoBean();
                                    String style = imgList.get(i).attr("style");
                                    String imageUrl = "http://" + style.substring(style.indexOf("pic"), style.indexOf("jpg") + 3);
                                    String name = imgList.get(i).select("a.img-link").attr("title");
                                    String playUrl = "http:" + imgList.get(i).select("a.img-link").attr("href");

                                    bean.setImageUrl(imageUrl);
                                    bean.setName(name);
                                    bean.setPlayUrl(playUrl);
                                    infoList.add(bean);
                                }
                            }
                            break;
                        case "zongyi":
                            {
                                Elements nameList = doc.select("a.focus-title-con");
                                for (int i = 0; i < imgList.size(); i++) {
                                    IQiYiBannerInfoBean bean = new IQiYiBannerInfoBean();
                                    String img = imgList.get(i).attr("data-jpg-img");
                                    String imageUrl;
                                    if (img.equals("")) {
                                        String style = imgList.get(i).attr(":style");
                                        imageUrl = "http://" + style.substring(style.indexOf("pic"), style.indexOf("jpg") + 3);
                                    } else {
                                        imageUrl = "http:" + img;
                                    }
                                    String playUrl = nameList.get(i).attr("href");
                                    String name = nameList.get(i).select("h2.focus-title").text();
                                    String description = nameList.get(i).select("h2.focus-title-more").text();

                                    bean.setImageUrl(imageUrl);
                                    bean.setName(name);
                                    bean.setPlayUrl(playUrl);
                                    bean.setDescription(description);
                                    infoList.add(bean);
                                }
                            }
                            break;
                    }
                }
                if (listener != null) {
                    listener.success(infoList);
                }
            } catch (IOException | StringIndexOutOfBoundsException e) {
                listener.error(e.toString());
            }
        },Throwable ->listener.error(Throwable.toString())));
    }

}
