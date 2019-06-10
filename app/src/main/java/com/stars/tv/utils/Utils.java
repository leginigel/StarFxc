package com.stars.tv.utils;


import android.content.Context;
import android.content.SharedPreferences;

import com.stars.tv.bean.IQiYiListBean;

import static android.content.Context.MODE_PRIVATE;

/**
 *
 * @Author: Dicks.yang
 * @Date: 2019.05.07
 */

public class Utils {

    public static Object getSharedValue(Context context, String key, Object defVal)
    {
        SharedPreferences shared = context.getSharedPreferences("share", MODE_PRIVATE);
        if(defVal instanceof String)
        {
            return shared.getString(key,(String)defVal);
        }else if(defVal instanceof Boolean)
        {
            return shared.getBoolean(key, (boolean)defVal);
        }else if(defVal instanceof Integer)
        {
            return shared.getInt(key, (int)defVal);
        }
        return defVal;
    }


    public static void setSharedValue(Context context, String key, Object value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences("share", MODE_PRIVATE).edit();

        if(value instanceof String)
        {
            editor.putString(key,(String)value);
        }else if(value instanceof Boolean)
        {
            editor.putBoolean(key, (boolean)value);
        }else if(value instanceof Integer)
        {
            editor.putInt(key, (int)value);
        }
        editor.apply();
    }

    public static String getIQiYiListUrl(IQiYiListBean listBean){

        int channel = listBean.getPageNum() == 0 ? 1 : listBean.getChannel();
        String orderList = listBean.getOrderList();
        String[] order = {"","","","","","","",""};
        String[] orderTemp = orderList.split(",");
        System.arraycopy(orderTemp, 0, order, 0, orderTemp.length);
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
