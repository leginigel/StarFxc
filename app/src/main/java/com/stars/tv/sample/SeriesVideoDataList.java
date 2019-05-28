package com.stars.tv.sample;

import com.stars.tv.bean.IQiYiMovieBean;

import java.util.ArrayList;
import java.util.List;

public final class SeriesVideoDataList {

    public static final String SERIES_CATEGORY[] = {
            "新鲜热剧",
            "重磅热播",
            "古装大剧",
            "家庭生活",
            "甜虐言情",
            "自制剧",
            "热门网剧",
            "青春偶像",
            "搞笑喜剧",
            "宫廷剧",
            "犯罪嫌疑剧",
            "穿越剧",
            "热血军旅",
            "年代传奇",
            "全球精选"
    };

    public static List<IQiYiMovieBean> seriesContent() {
        List<IQiYiMovieBean> list = new ArrayList<>();
        String title[] = {
                "item0",
                "item1",
                "item2",
                "item3",
                "item4",
                "item5",
                "item6",
                "item7",
                "item8",
                "item9",
                "item10",
                "item11"
        };

        list.add(buildMovieInfo( title[0]));
        list.add(buildMovieInfo( title[1]));
        list.add(buildMovieInfo( title[2]));
        list.add(buildMovieInfo( title[3]));
        list.add(buildMovieInfo( title[4]));
        list.add(buildMovieInfo( title[5]));
        list.add(buildMovieInfo( title[6]));
        list.add(buildMovieInfo( title[7]));
        list.add(buildMovieInfo( title[8]));
        list.add(buildMovieInfo( title[9]));
        list.add(buildMovieInfo( title[10]));
        list.add(buildMovieInfo( title[11]));

        return list;
    }

    private static IQiYiMovieBean buildMovieInfo(String title) {
        IQiYiMovieBean videoBean = new IQiYiMovieBean();
        videoBean.setName(title);
        return videoBean;
    }
}
