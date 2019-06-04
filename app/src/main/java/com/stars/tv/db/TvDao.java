package com.stars.tv.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.stars.tv.bean.LiveTvBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TvDao {

    private SQLiteDatabase db;

    /**
     * 结构体
     * 实例化DBManager的步骤需要放在Activity的onCreate里，确保context已初始化
     *
     * @param context //上下文
     */
    public TvDao(Context context) {
        db = DBManager.getInstance(context).getWritableDatabase();

    }

    /**
     * 更新channel
     */
    public void update(LiveTvBean tvBean) {
        int channelNumber = tvBean.getChannelNumber();
        ContentValues values = new ContentValues();
        values.put("channelNumber", tvBean.getChannelNumber());
        values.put("channelName", tvBean.getChannelName());
        values.put("aliasName", tvBean.getAliasName());
        values.put("maoId", tvBean.getMaoId());
        values.put("logo", tvBean.getLogo());
        values.put("isCCTV", tvBean.getIsCCTV());
        values.put("isFav", tvBean.getIsFav());
        values.put("isHistory", tvBean.getIsHistory());
        values.put("url", tvBean.getUrl().toString());
        if (queryChannelNumberList().contains(channelNumber)) {
            db.update("television", values, "channelNumber=?", new String[]{String.valueOf(channelNumber)});
        } else {
            db.insert("television", null, values);
        }
    }

    /**
     * 根据channel id 删除
     *
     * @param channelNumber channelNumber
     */
    public void delete(int channelNumber) {
        db.delete("television", "channelNumber=?", new String[]{String.valueOf(channelNumber)});
    }


    /**
     * 查询数据库中所有channel Id list
     */
    private ArrayList<Integer> queryChannelNumberList() {
        ArrayList<Integer> channelNumberList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT channelNumber FROM television", null);
        while (c.moveToNext()) {
            channelNumberList.add(c.getInt(c.getColumnIndex("channelNumber")));
        }
        c.close();
        return channelNumberList;
    }

    /**
     * 查询all channel  list
     */
    public LiveTvBean queryChannelWithChannelNumber(int channelNumber) {
        LiveTvBean tvBean = new LiveTvBean();
        Cursor c = db.rawQuery("SELECT * FROM television WHERE channelNumber=?", new String[]{String.valueOf(channelNumber)});
        while (c.moveToNext()) {
            tvBean.setChannelNumber(c.getInt(c.getColumnIndex("channelNumber")));
            tvBean.setChannelName(c.getString(c.getColumnIndex("channelName")));
            tvBean.setAliasName(c.getString(c.getColumnIndex("aliasName")));
            tvBean.setMaoId(c.getString(c.getColumnIndex("maoId")));
            tvBean.setLogo(c.getString(c.getColumnIndex("logo")));
            tvBean.setIsCCTV(c.getInt(c.getColumnIndex("isCCTV"))==1);
            tvBean.setIsFav(c.getInt(c.getColumnIndex("isFav"))==1);
            tvBean.setIsHistory(c.getInt(c.getColumnIndex("isHistory"))==1);
            List<String> url = new ArrayList<>(Arrays.asList(c.getString(c.getColumnIndex("url")).split(",")));
            tvBean.setUrl(url);
        }
        c.close();
        return tvBean;
    }

    /**
     * 查询all channel  list
     */
    public ArrayList<LiveTvBean> queryChannelList() {
        ArrayList<LiveTvBean> cctvList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM television", null);
        while (c.moveToNext()) {
            LiveTvBean tvBean = new LiveTvBean();
            tvBean.setChannelNumber(c.getInt(c.getColumnIndex("channelNumber")));
            tvBean.setChannelName(c.getString(c.getColumnIndex("channelName")));
            tvBean.setAliasName(c.getString(c.getColumnIndex("aliasName")));
            tvBean.setMaoId(c.getString(c.getColumnIndex("maoId")));
            tvBean.setLogo(c.getString(c.getColumnIndex("logo")));
            tvBean.setIsCCTV(c.getInt(c.getColumnIndex("isCCTV"))==1);
            tvBean.setIsFav(c.getInt(c.getColumnIndex("isFav"))==1);
            tvBean.setIsHistory(c.getInt(c.getColumnIndex("isHistory"))==1);
            List<String> url = new ArrayList<>(Arrays.asList(c.getString(c.getColumnIndex("url")).split(",")));
            tvBean.setUrl(url);
            cctvList.add(tvBean);
        }
        c.close();
        return cctvList;
    }

    /**
     * 查询cctv list
     */
    public ArrayList<LiveTvBean> queryCCTVChannelList() {
        ArrayList<LiveTvBean> cctvList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM television WHERE isCCTV=1", null);
        while (c.moveToNext()) {
            LiveTvBean tvBean = new LiveTvBean();
            tvBean.setChannelNumber(c.getInt(c.getColumnIndex("channelNumber")));
            tvBean.setChannelName(c.getString(c.getColumnIndex("channelName")));
            tvBean.setAliasName(c.getString(c.getColumnIndex("aliasName")));
            tvBean.setMaoId(c.getString(c.getColumnIndex("maoId")));
            tvBean.setLogo(c.getString(c.getColumnIndex("logo")));
            tvBean.setIsCCTV(c.getInt(c.getColumnIndex("isCCTV"))==1);
            tvBean.setIsFav(c.getInt(c.getColumnIndex("isFav"))==1);
            tvBean.setIsHistory(c.getInt(c.getColumnIndex("isHistory"))==1);
            List<String> url = new ArrayList<>(Arrays.asList(c.getString(c.getColumnIndex("url")).split(",")));
            tvBean.setUrl(url);
            cctvList.add(tvBean);
        }
        c.close();
        return cctvList;
    }

    /**
     * 查询 satellite list
     */
    public ArrayList<LiveTvBean> querySatelliteChannelList() {
        ArrayList<LiveTvBean> cctvList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM television WHERE isCCTV=0", null);
        while (c.moveToNext()) {
            LiveTvBean tvBean = new LiveTvBean();
            tvBean.setChannelNumber(c.getInt(c.getColumnIndex("channelNumber")));
            tvBean.setChannelName(c.getString(c.getColumnIndex("channelName")));
            tvBean.setAliasName(c.getString(c.getColumnIndex("aliasName")));
            tvBean.setMaoId(c.getString(c.getColumnIndex("maoId")));
            tvBean.setLogo(c.getString(c.getColumnIndex("logo")));
            tvBean.setIsCCTV(c.getInt(c.getColumnIndex("isCCTV"))==1);
            tvBean.setIsFav(c.getInt(c.getColumnIndex("isFav"))==1);
            tvBean.setIsHistory(c.getInt(c.getColumnIndex("isHistory"))==1);
            List<String> url = new ArrayList<>(Arrays.asList(c.getString(c.getColumnIndex("url")).split(",")));
            tvBean.setUrl(url);
            cctvList.add(tvBean);
        }
        c.close();
        return cctvList;
    }

    /**
     * 查询 fav list
     */
    public ArrayList<LiveTvBean> queryFavChannelList() {
        ArrayList<LiveTvBean> cctvList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM television WHERE isFav=1", null);
        while (c.moveToNext()) {
            LiveTvBean tvBean = new LiveTvBean();
            tvBean.setChannelNumber(c.getInt(c.getColumnIndex("channelNumber")));
            tvBean.setChannelName(c.getString(c.getColumnIndex("channelName")));
            tvBean.setAliasName(c.getString(c.getColumnIndex("aliasName")));
            tvBean.setMaoId(c.getString(c.getColumnIndex("maoId")));
            tvBean.setLogo(c.getString(c.getColumnIndex("logo")));
            tvBean.setIsCCTV(c.getInt(c.getColumnIndex("isCCTV"))==1);
            tvBean.setIsFav(c.getInt(c.getColumnIndex("isFav"))==1);
            tvBean.setIsHistory(c.getInt(c.getColumnIndex("isHistory"))==1);
            List<String> url = new ArrayList<>(Arrays.asList(c.getString(c.getColumnIndex("url")).split(",")));
            tvBean.setUrl(url);
            cctvList.add(tvBean);
        }
        c.close();
        return cctvList;
    }

    /**
     * 查询 History list
     */
    public ArrayList<LiveTvBean> queryHistoryChannelList() {
        ArrayList<LiveTvBean> cctvList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM television WHERE isHistory=1", null);
        while (c.moveToNext()) {
            LiveTvBean tvBean = new LiveTvBean();
            tvBean.setChannelNumber(c.getInt(c.getColumnIndex("channelNumber")));
            tvBean.setChannelName(c.getString(c.getColumnIndex("channelName")));
            tvBean.setAliasName(c.getString(c.getColumnIndex("aliasName")));
            tvBean.setMaoId(c.getString(c.getColumnIndex("maoId")));
            tvBean.setLogo(c.getString(c.getColumnIndex("logo")));
            tvBean.setIsCCTV(c.getInt(c.getColumnIndex("isCCTV"))==1);
            tvBean.setIsFav(c.getInt(c.getColumnIndex("isFav"))==1);
            tvBean.setIsHistory(c.getInt(c.getColumnIndex("isHistory"))==1);
            List<String> url = new ArrayList<>(Arrays.asList(c.getString(c.getColumnIndex("url")).split(",")));
            tvBean.setUrl(url);
            cctvList.add(tvBean);
        }
        c.close();
        return cctvList;
    }

    /**
     * 更新channel fav
     */
    public void setFav(int channelNumber, boolean fav) {
        ContentValues values = new ContentValues();
        values.put("isFav", fav);
        db.update("television", values, "channelNumber=?", new String[]{String.valueOf(channelNumber)});
    }

    /**
     * 更新channel history
     */
    public void setHistory(int channelNumber, boolean history) {
        ContentValues values = new ContentValues();
        values.put("isHistory", history);
        db.update("television", values, "channelNumber=?", new String[]{String.valueOf(channelNumber)});
    }

    /**
     * 更新channel history
     */
    public void clearAllHistory() {
        ContentValues values = new ContentValues();
        values.put("isHistory", 0);
        db.update("television", values, null,null);
    }

}
