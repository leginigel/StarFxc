package com.stars.tv.utils;


import com.stars.tv.bean.IQiYiListBean;

/**
 *
 * @Author: Dicks.yang
 * @Date: 2019.05.07
 */

public class Utils {

    public static String getIQiYiListUrl(IQiYiListBean listBean){

        int channel = listBean.getPageNum() == 0 ? 1 : listBean.getChannel();
        String[] order = listBean.getOrderList();
        String payStatus = listBean.getPayStatus();
        String myYear = listBean.getMyYear();
        int sortType = listBean.getSortType() == 0 ? 24 :listBean.getSortType();
        int pageNum = listBean.getPageNum() == 0 ? 1 :listBean.getPageNum();
        int dataType = listBean.getDataType() == 0 ? 1 :listBean.getDataType();
        String siteType = listBean.getSiteType().equals("")?"iqiyi":listBean.getSiteType();
        int sourceType = listBean.getSourceType() == 0 ? 1 :listBean.getSourceType();
        String comicsStatus = listBean.getComicsStatus();

        return String.format("www/%s/%s-%s-%s-%s-%s-%s-%s-%s---%s-%s--%s-%s-%s-%s-%s-%s.html",
                channel,
                order[0],
                order[1],
                order[2],
                order[3],
                order[4],
                order[5],
                order[6],
                order[7],
                payStatus,
                myYear,
                sortType,
                pageNum,
                dataType,
                siteType,
                sourceType,
                comicsStatus);
    }

}
