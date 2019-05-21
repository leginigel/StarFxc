package com.stars.tv.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stars.tv.bean.IQiYiHotQueryBean;
import com.stars.tv.bean.IQiYiSearchSimplifyDataBean;
import com.stars.tv.bean.IQiYiSearchResultBean;
import com.stars.tv.bean.IQiYiSearchSuggestBean;
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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class IQiYiParseSearchPresenter {

    private Observable<ResponseBody> getIQiYiSearchQueryUrl(String queryType) {
        return RetrofitFactory.createApi(RetrofitService.class, Constants.BASE_IQIYI_SEARCH_QUERY_URL)
                .getIQiYiSearchHotQueryWord(queryType).compose(RxUtils.rxSchedulerHelper());
    }

    private Observable<ResponseBody> getIQiYiSearchSuggestWordUrl(String keyWord,int resultNum) {
        return RetrofitFactory.createApi(RetrofitService.class, Constants.BASE_IQIYI_SEARCH_SUGGEST_WORD_URL)
                .getIQiYiSearchSuggestWord(keyWord,resultNum).compose(RxUtils.rxSchedulerHelper());
    }

    private Observable<ResponseBody> getIQiYiSearchBaseUrl(String keyWord, String channel,int duration, int pageNum, String publishTime, int sort, String pictureQuality) {
        return RetrofitFactory.createApi(RetrofitService.class, Constants.BASE_IQIYI_SEARCH_URL)
                .getIQiYiSearchResult(keyWord, channel, duration, pageNum, publishTime, sort, pictureQuality).compose(RxUtils.rxSchedulerHelper());
    }

    private Observable<ResponseBody> getIQiYiSearchSimplified(String keyWord, int pageNum, int pageSize) {
        return RetrofitFactory.createApi(RetrofitService.class, Constants.BASE_IQIYI_SEARCH_QUERY_URL)
                .getIQiYiSearchSimplified(keyWord,"html5",pageNum,pageSize).compose(RxUtils.rxSchedulerHelper());
    }

    public void requestIQiYiSearchHotQueryWord( CallBack<List<IQiYiHotQueryBean>> listener) {
        RxManager.add(getIQiYiSearchQueryUrl("hotQuery").subscribe(responseBody -> {
            List<IQiYiHotQueryBean> hotQueryList;
            try {
                JSONObject root = new JSONObject(responseBody.string());
                boolean isEmpty = root.getBoolean("is_empty");
                if (isEmpty) {
                    //error
                    listener.error("返回值错误");
                }

                String data = root.getString("data");
                Type listType = new TypeToken<List<IQiYiHotQueryBean>>() {}.getType();
                hotQueryList = new Gson().fromJson(data, listType);

                if (listener != null) {
                    listener.success(hotQueryList);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        },Throwable ->listener.error(Throwable.toString())));
    }

    public void requestIQiYiSearchSuggestWord(String keyWord,int resultNum, CallBack<List<IQiYiSearchSuggestBean>> listener) {
        RxManager.add(getIQiYiSearchSuggestWordUrl(keyWord,resultNum).subscribe(responseBody -> {
            List<IQiYiSearchSuggestBean> suggestList;
            try {
                JSONObject root = new JSONObject(responseBody.string());
                String code = root.getString("code");
                if (!code.equals("A00000")) {
                    //error
                    listener.error("返回值错误");
                }

                String data = root.getString("data");
                Type listType = new TypeToken<List<IQiYiSearchSuggestBean>>() {}.getType();
                suggestList = new Gson().fromJson(data, listType);

                if (listener != null) {
                    listener.success(suggestList);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        },Throwable ->listener.error(Throwable.toString())));
    }

    public void requestIQiYiSearchResult(String keyWord, String channel,int duration, int pageNum, String publishTime,
                                         int sort,String pictureQuality, CallBack<IQiYiSearchResultBean> listener) {
        RxManager.add(getIQiYiSearchBaseUrl(keyWord, channel, duration, pageNum, publishTime, sort, pictureQuality).subscribe(responseBody -> {
            Document doc = Jsoup.parse(responseBody.string());
            IQiYiSearchResultBean resultBean = new IQiYiSearchResultBean();
            if (null!=doc){
                Elements lis = doc.select("ul.mod_result_list").select("li.list_item");
                String resultNum = doc.select("div.search_content").text();
                resultBean.setResultNum(resultNum);
                List<IQiYiSearchResultBean.ResultItem> itemList = new ArrayList<>();
                for(Element element:lis)
                {
                    IQiYiSearchResultBean.ResultItem resultItem = resultBean.new ResultItem();
                    String tvname = element.attr("data-widget-searchlist-tvname");
                    String tvid = element.attr("data-widget-searchlist-tvid");
                    String albumid = element.attr("data-widget-searchlist-albumid");
                    String catageory = element.attr("data-widget-searchlist-catageory");
                    String pagesize = element.attr("data-widget-searchlist-pagesize");

                    String playUrl = "http:"+ element.select("a.figure").attr("href");
                    String imageUrl = "http:"+ element.select("a.figure").select("img").attr("src");
                    String times = element.select("em.fs12").text().trim();
                    String description = element.select("span.result_info_txt").text();

                    resultItem.setTvname(tvname);
                    resultItem.setTvid(tvid);
                    resultItem.setAlbumid(albumid);
                    resultItem.setCatageory(catageory);
                    resultItem.setPagesize(pagesize);

                    resultItem.setPlayUrl(playUrl);
                    resultItem.setImageUrl(imageUrl);
                    resultItem.setTimes(times);
                    resultItem.setDescription(description);

                    Elements subLis = element.select("li.album_item");
                    List<IQiYiSearchResultBean.SubItem> subItemList = new ArrayList<>();
                    for(Element item :subLis)
                    {
                        IQiYiSearchResultBean.SubItem subItem = resultBean.new SubItem();
                        String subTitle = item.select("a.album_link").attr("title");
                        String subPlayUrl = item.select("a.album_link").attr("href");

                        subItem.setSubTitle(subTitle);
                        subItem.setSubPlayUrl(subPlayUrl);

                        subItemList.add(subItem);
                    }
                    resultItem.setSubItem(subItemList);
                    if(!catageory.equals("文学")) {
                        itemList.add(resultItem);
                    }
                }
                resultBean.setItemList(itemList);

                if (listener != null) {
                    listener.success(resultBean);
                }
            }
        },Throwable ->listener.error(Throwable.toString())));
    }

    public void requestIQiYiSearchSimplified(String keyWord, int pageNum, int pageSize, CallBack<IQiYiSearchSimplifyDataBean> listener) {
        RxManager.add(getIQiYiSearchSimplified(keyWord, pageNum, pageSize).subscribe(responseBody -> {
            IQiYiSearchSimplifyDataBean searchBean;
            try {
                JSONObject root = new JSONObject(responseBody.string());
                String code = root.getString("code");
                if (!code.equals("A00000")) {
                    //error
                    listener.error("返回值错误");
                }
                String data = root.getString("data");
                searchBean = new Gson().fromJson(data, IQiYiSearchSimplifyDataBean.class);

                if (listener != null) {
                    listener.success(searchBean);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        },Throwable ->listener.error(Throwable.toString())));
    }

}
