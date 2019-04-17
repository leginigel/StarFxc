package com.stars.tv.model;

import com.stars.tv.bean.TvTitle;

import java.util.ArrayList;
import java.util.List;

public class TvTitleModel {
    public static List<TvTitle> getTitleList() {
        List<TvTitle> titleList = new ArrayList<>();
        titleList.add(new TvTitle("我的"));
        titleList.add(new TvTitle("频道"));
        titleList.add(new TvTitle("精选"));
        titleList.add(new TvTitle("电视剧"));
        titleList.add(new TvTitle("电影"));
        titleList.add(new TvTitle("综艺"));
        titleList.add(new TvTitle("少儿"));
        titleList.add(new TvTitle("动漫"));
        titleList.add(new TvTitle("4K专区"));
        titleList.add(new TvTitle("新闻"));
        titleList.add(new TvTitle("游戏"));
        titleList.add(new TvTitle("体育"));
        titleList.add(new TvTitle("生活"));
        titleList.add(new TvTitle("娱乐"));
        return titleList;
    }
}
