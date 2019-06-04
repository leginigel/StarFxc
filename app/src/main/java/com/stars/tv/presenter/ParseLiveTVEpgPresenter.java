package com.stars.tv.presenter;

import android.util.Log;

import com.stars.tv.bean.LiveTvBean;
import com.stars.tv.bean.LiveTvEpgBean;
import com.stars.tv.server.RetrofitFactory;
import com.stars.tv.server.RetrofitService;
import com.stars.tv.server.RxManager;
import com.stars.tv.utils.CallBack;
import com.stars.tv.utils.Constants;
import com.stars.tv.utils.RxUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class ParseLiveTVEpgPresenter {

    private Observable<ResponseBody> getTvMaoEpgUrl(String url) {
        return RetrofitFactory.createApi(RetrofitService.class, Constants.LIVE_TV_TVMAO_EPG_URL)
                .getTvMaoEpg(url).compose(RxUtils.rxSchedulerHelper());
    }

    public void requestTvMaoEpgData(LiveTvBean tvBean, CallBack<List<LiveTvEpgBean>> listener) {
        String channelName = tvBean.getAliasName();
        String url;
        if (tvBean.getIsCCTV()) {
            url = "/program/playing/cctv/";
        } else {
            url = "/program/playing/satellite/";
        }
        RxManager.add(getTvMaoEpgUrl(url).subscribe(responseBody -> {

            Document doc = Jsoup.parse(responseBody.string());
            List<LiveTvEpgBean> epgList = new ArrayList<>();
            Elements nodes = doc.select("tr:contains(" + channelName + ")");
            for (int i = 1; i < 4; i++) {
                LiveTvEpgBean epgBean = new LiveTvEpgBean();
                String startTime = nodes.select("td.td" + i).select("span").text();
                String programName = nodes.select("td.td" + i).text().split(" ")[0];
                epgBean.setProgramName(programName);
                epgBean.setStartTime(startTime);
                epgList.add(epgBean);
            }
            if (listener != null) {
                listener.success(epgList);
            }
        }, Throwable -> listener.error(Throwable.toString())));
    }
}
