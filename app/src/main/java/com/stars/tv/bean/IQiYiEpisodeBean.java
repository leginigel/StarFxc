
package com.stars.tv.bean;

public class IQiYiEpisodeBean {

    private String name;
    private String focus;
    private String tvId;
    private String vid;
    private String duration;
    private String imageUrl;
    private String playUrl;
    private String description;


    @Override
    public String toString() {
        return "IQiYiEpisodeBean{" +
                "name='" + name + '\'' +
                ", focus='" + focus + '\'' +
                ", tvId='" + tvId + '\'' +
                ", vid='" + vid + '\'' +
                ", duration='" + duration + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", playUrl='" + playUrl + '\'' +
                ", description='" + description + '\''+
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFocus(String focus) {
        this.focus = focus;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getFocus() {
        return focus;
    }

    public String getDuration() {
        return duration;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getTvId() {
        return tvId;
    }

    public void setTvId(String tvId) {
        this.tvId = tvId;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }
}