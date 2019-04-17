package com.stars.tv.sample;

import com.stars.tv.bean.VideoBean;

import java.util.ArrayList;
import java.util.List;

public final class VideoSampleDataList {
    public static final String MOVIE_CATEGORY[] = {
            "电影 Zero",
            "游戏 One",
            "电视剧 Two",
            "综艺 Three",
            "少儿 Four",
    };

    public static List<VideoBean> setupMovies() {
        List<VideoBean> list = new ArrayList<>();
        String title[] = {
                "Phone",
                "Tablet",
                "Wear",
                "Watches",
                "Glasses",
                "PC"
        };


        list.add(buildMovieInfo( title[0], "Studio Zero", "0", "0"));
        list.add(buildMovieInfo( title[1], "Studio One", "0",  "0"));
        list.add(buildMovieInfo( title[2],  "Studio Two", "0", "0"));
        list.add(buildMovieInfo( title[3], "Studio Three", "0", "0"));
        list.add(buildMovieInfo( title[4], "Studio Four", "0", "0"));
        list.add(buildMovieInfo( title[5], "Studio Five", "0", "0"));

        return list;
    }

    private static VideoBean buildMovieInfo( String title, String description,  String videoUrl, String bgImageUrl) {
        VideoBean videoBean = new VideoBean();
        videoBean.setName(title);
        videoBean.setIntroduction(description);
        videoBean.setCover_url(bgImageUrl);
        videoBean.setPoster_url(videoUrl);
        return videoBean;
    }
}
