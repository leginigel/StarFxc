package com.stars.tv.sample;

import com.stars.tv.bean.IQiYiMovieBean;

import java.util.ArrayList;
import java.util.List;

public final class VideoSampleDataList {
    public static final String[] MOVIE_CATEGORY = {
            "电影 Zero",
            "游戏 One",
            "电视剧 Two",
            "综艺 Three",
            "少儿 Four",
    };

    public static List<IQiYiMovieBean> setupMovies() {
        List<IQiYiMovieBean> list = new ArrayList<>();
        String[] title = {
                "Phone",
                "Tablet",
                "Wear",
                "Watches",
                "Glasses",
                "PC"
        };


        list.add(buildMovieInfo( title[0]));
        list.add(buildMovieInfo( title[1]));
        list.add(buildMovieInfo( title[2]));
        list.add(buildMovieInfo( title[3]));
        list.add(buildMovieInfo( title[4]));
        list.add(buildMovieInfo( title[5]));

        return list;
    }

    private static IQiYiMovieBean buildMovieInfo(String title) {
        IQiYiMovieBean videoBean = new IQiYiMovieBean();
        videoBean.setName(title);
        return videoBean;
    }
}
