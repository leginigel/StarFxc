package com.stars.tv.sample;

import com.stars.tv.bean.IQiYiMovieBean;

import java.util.ArrayList;
import java.util.List;

public final class FilmVideoDataList {

    public static final String FILM_CATEGORY[] = {
            "院线热映",
            "独播剧场",
            "网络大电影",
            "华语经典电影",
            "华语奇幻电影",
            "美国惊悚电影",
            "美国动作电影",
            "经典喜剧电影",
            "暴力惊悚电影",
            "烧脑犯罪电影",
            "关于青春的喜剧电影",
            "关于警匪的动作电影",
            "美国冒险电影",
            "爱情喜剧电影"
    };

    public static List<IQiYiMovieBean> filmContent() {
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
