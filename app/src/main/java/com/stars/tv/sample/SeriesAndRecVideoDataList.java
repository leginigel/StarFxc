package com.stars.tv.sample;

import com.stars.tv.bean.IQiYiMovieBean;

import java.util.ArrayList;
import java.util.List;

public final class SeriesAndRecVideoDataList {

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
    public static final String TOP_CATEGORY[] = {
            "电影TOP10热度榜",
            "电视剧TOP10热度榜",
            "动漫TOP10热度榜"
    };

    public static final String REC_CATEGORY[] = {
            "精选电视剧",
            "电影推荐",
            "王牌综艺",
            "亲子动漫",
            "纪录片",
            "游戏",
            "资讯",
            "娱乐",
            "财经",
            "网络电影",
            "片花",
            "音乐",
            "军事",
            "教育",
            "体育",
            "儿童",
            "旅游",
            "时尚",
            "生活",
            "汽车",
            "搞笑",
            "原创",
            "母婴",
            "科技",
            "脱口秀",
            "健康"
    };

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
            "爱情喜剧电影",
            "巨制大电影"
    };

    public static final String VARIETY_CATEGORY[] = {
            "重磅推荐",
            "明星真人秀",
            "情感专区",
            "音乐畅想",
            "搞笑综艺",
            "脱口秀",
            "唯美食与爱不可辜负",
            "职场求职",
            "明星谈话",
            "明星带娃",
            "爱奇艺专区",
            "旅游冒险",
            "音乐选秀",
            "脑力比拼",
            "经典曲艺"
    };

    public static final String CARTOON_CATEGORY[] = {
            "C位动漫",
            "日本动漫",
            "国产动画",
            "欧美动画",
            "动画电影",
            "吐槽搞笑",
            "热血冒险",
            "恋爱青春",
            "运动竞技",
            "科幻未来",
            "布袋戏",
            "儿童剧场",
            "玩游戏打BOSS",
            "历史故事",
            "机甲"
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
