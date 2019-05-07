package com.stars.tv.utils;


import com.stars.tv.bean.IQiYiListBean;

import java.io.IOException;
import java.io.InputStream;

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

    public static String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        } catch (IOException e) {
            return null;
        }
    }

}
