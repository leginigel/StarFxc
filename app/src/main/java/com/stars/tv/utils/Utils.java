package com.stars.tv.utils;


import com.stars.tv.bean.IQiYiListBean;

/**
 *
 * @Author: Dicks.yang
 * @Date: 2019.04.24
 */

public class Utils {

    public static String getIQiYiListUrl(IQiYiListBean listBean){
        return String.format("www/%s/%s-%s-%s-%s-%s-%s-%s-%s---%s-%s--%s-%s-%s-%s-%s-%s.html",
                listBean.getChannel(),
                listBean.getCategory(),
                listBean.getType(),
                listBean.getPublishCategory(),
                listBean.getBrandArea(),
                listBean.getNewType(),
                listBean.getStyle(),
                listBean.getThirdType(),
                listBean.getFourthType(),
                listBean.getCharge(),
                listBean.getTimes(),
                listBean.getSort(),
                listBean.getPage(),
                listBean.getVideoType(),
                listBean.getSearchScope(),
                listBean.getUserUpload(),
                listBean.getSerialState());
    }

}
