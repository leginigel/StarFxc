package com.stars.tv.bean;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @Author: Dicks.yang
 * @Date: 2019.05.24
 */
public class LiveTvBean implements Serializable {

    int channelNumber;
    String channelName;
    String aliasName;
    String maoId;
    String logo;
    boolean isCCTV;
    boolean isFav;
    boolean isHistory;
    List<String> url;

    @Override
    public String toString() {
        return "LiveTv{" +
                "channelNumber=" + channelNumber +
                ", channelName='" + channelName + '\'' +
                ", aliasName='" + aliasName + '\'' +
                ", maoId='" + maoId + '\'' +
                ", logo='" + logo + '\'' +
                ", isCCTV=" + isCCTV +
                ", isFav=" + isFav +
                ", isHistory=" + isHistory +
                ", url=" + url +
                '}';
    }

    public int getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(int channelNumber) {
        this.channelNumber = channelNumber;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getMaoId() {
        return maoId;
    }

    public void setMaoId(String maoId) {
        this.maoId = maoId;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean getIsCCTV() {
        return isCCTV;
    }

    public void setIsCCTV(boolean isCCTV) {
        this.isCCTV = isCCTV;
    }

    public boolean getIsFav() {
        return isFav;
    }

    public void setIsFav(boolean isFav) {
        this.isFav = isFav;
    }

    public boolean getIsHistory() {
        return isHistory;
    }

    public void setIsHistory(boolean isHistory) {
        this.isHistory = isHistory;
    }

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }
}
