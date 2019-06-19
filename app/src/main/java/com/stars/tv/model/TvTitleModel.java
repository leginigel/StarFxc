package com.stars.tv.model;

import com.stars.tv.bean.TvTitle;
import com.stars.tv.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class TvTitleModel {
    public static List<TvTitle> getTitleList() {
        List<TvTitle> titleList = new ArrayList<>();
        titleList.add(new TvTitle(Constants.MAIN_TITLE_JINGXUAN));
        titleList.add(new TvTitle(Constants.MAIN_TITLE_PINDAO));
        titleList.add(new TvTitle(Constants.MAIN_TITLE_DIANSHIJU));
        titleList.add(new TvTitle(Constants.MAIN_TITLE_DIANYING));
        titleList.add(new TvTitle(Constants.MAIN_TITLE_ZONGYI));
        titleList.add(new TvTitle(Constants.MAIN_TITLE_DONGYI));
        titleList.add(new TvTitle(Constants.MAIN_TITLE_SHAOER));
        titleList.add(new TvTitle(Constants.MAIN_TITLE_JIAOYU));
        titleList.add(new TvTitle(Constants.MAIN_TITLE_XINWEI));
        titleList.add(new TvTitle(Constants.MAIN_TITLE_YULE));
        return titleList;
    }
}
