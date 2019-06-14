package com.stars.tv.bean;

import java.io.Serializable;

public class IQiYiBannerInfoBean extends IQiYiBaseBean implements Serializable {

    private String name;
    private String playUrl;
    private String description;
    private String imageUrl;

    @Override
    public String toString() {
        return "BannerInfo{" +
                "name='" + name + '\'' +
                ", playUrl='" + playUrl + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String getId() {
        return null;
    }
}