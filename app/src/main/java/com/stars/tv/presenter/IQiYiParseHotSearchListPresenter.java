package com.stars.tv.presenter;

import com.stars.tv.bean.IQiYiHotSearchItemBean;
import com.stars.tv.server.RetrofitFactory;
import com.stars.tv.server.RetrofitService;
import com.stars.tv.server.RxManager;
import com.stars.tv.utils.CallBack;
import com.stars.tv.utils.Constants;
import com.stars.tv.utils.RxUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class IQiYiParseHotSearchListPresenter {

    private Observable<ResponseBody> getIQiYiPage(String url) {
        return RetrofitFactory.createApi(RetrofitService.class, Constants.BASE_IQIYI_HOT_LIST_URL)
                .getIQiYiHotList(url).compose(RxUtils.rxSchedulerHelper());
    }

    public void requestIQiYiHotList(String url, CallBack<List<IQiYiHotSearchItemBean>> listener) {

        RxManager.add(getIQiYiPage(url).subscribe(responseBody -> {
            Document doc = Jsoup.parse(responseBody.string());
            List<IQiYiHotSearchItemBean> hotList = new ArrayList<>();
            Elements lis = doc.select("tbody[data-ranklist-elem = list]").select("tr");
            for(Element element:lis )
            {
                IQiYiHotSearchItemBean hotItem = new IQiYiHotSearchItemBean();
                String id = element.select("i.icon_top").text();
                String name = element.select("a.item_name").text();
                String searchUrl = element.select("a.item_name").attr("href");
                String yesterdayIndex = element.getElementsByAttribute("data-ranklist-yes").text();
                String yesterdayStatus = element.getElementsByAttribute("data-ranklist-yes").select("i").attr("title");
                String weekIndex = element.getElementsByAttribute("data-ranklist-week").text();
                String weekStatus = element.getElementsByAttribute("data-ranklist-week").select("i").attr("title");
                String monthIndex = element.getElementsByAttribute("data-ranklist-mon").text();
                hotItem.setId(id);
                hotItem.setName(name);
                hotItem.setSearchUrl(searchUrl);
                hotItem.setYesterdayIndex(yesterdayIndex);
                hotItem.setYesterdayStatus(yesterdayStatus);
                hotItem.setWeekIndex(weekIndex);
                hotItem.setWeekStatus(weekStatus);
                hotItem.setMonthIndex(monthIndex);
                hotList.add(hotItem);
            }

            if (listener != null) {
                listener.success(hotList);
            }
        }, Throwable ->listener.error(Throwable.toString())));
    }


    public void requestIQiYiSuggestHotList(CallBack<List<IQiYiHotSearchItemBean>> listener) {

        RxManager.add(getIQiYiPage("").subscribe(responseBody -> {
            Document doc = Jsoup.parse(responseBody.string());
            List<IQiYiHotSearchItemBean> hotList = new ArrayList<>();
            Elements lis = doc.select("div[data-widget-suggest= hotList]").select("a");
            for(Element element:lis )
            {
                IQiYiHotSearchItemBean hotItem = new IQiYiHotSearchItemBean();
                String name = element.text();
                String searchUrl = element.attr("href");

                hotItem.setName(name);
                hotItem.setSearchUrl(searchUrl);

                hotList.add(hotItem);
            }

            if (listener != null) {
                listener.success(hotList);
            }
        }, Throwable ->listener.error(Throwable.toString())));
    }
}
