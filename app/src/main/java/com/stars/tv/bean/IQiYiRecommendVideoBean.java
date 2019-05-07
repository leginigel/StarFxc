package com.stars.tv.bean;

public class IQiYiRecommendVideoBean {

    private String name;
    private String tvId;
    private String focus;
    private String playUrl;
    private String score;
    private String duration;
    private String imageUrl;
    private String latestOrder;
    private String videoCount;
    private String videoInfoType;
    private String payMarkUrl;


    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", tvId='" + tvId + '\'' +
                ", focus='" + focus + '\'' +
                ", playUrl='" + playUrl + '\'' +
                ", score='" + score + '\'' +
                ", duration='" + duration + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", latestOrder='" + latestOrder + '\'' +
                ", videoCount='" + videoCount + '\'' +
                ", videoInfoType='" + videoInfoType + '\'' +
                ", payMarkUrl="+ payMarkUrl +
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

    public void setPayMarkUrl(String payMarkUrl) {
        this.payMarkUrl = payMarkUrl;
    }
    public String getPayMarkUrl() {
        return payMarkUrl;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setLatestOrder(String latestOrder) {
        this.latestOrder = latestOrder;
    }

    public String getLatestOrder() {
        return latestOrder;
    }

    public void setVideoCount(String videoCount) {
        this.videoCount = videoCount;
    }

    public String getVideoCount() {
        return videoCount;
    }

    public void setVideoInfoType(String videoInfoType) {
        this.videoInfoType = videoInfoType;
    }

    public String getVideoInfoType() {
        return videoInfoType;
    }

    public String getTvId() {
        return tvId;
    }

    public void setTvId(String tvId) {
        this.tvId = tvId;
    }

    public String getFocus() {
        return focus;
    }

    public void setFocus(String focus) {
        this.focus = focus;
    }

}
