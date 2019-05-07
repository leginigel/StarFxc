
package com.stars.tv.bean;

import java.util.List;

public class IQiYiVarietyBean {

    private String name;
    private String focus;
    private String tvId;
    private String vid;
    private String duration;
    private String imageUrl;
    private String playUrl;
    private String description;
    private String albumImageUrl;
    private String period;
    private String shortTitle;
    private String subtitle;
    private List<videoFocuse> videoFocuses;


    @Override
    public String toString() {
        return "IQiYiVarietyBean{" +
                "name='" + name + '\'' +
                ", focus='" + focus + '\'' +
                ", tvId='" + tvId + '\'' +
                ", vid='" + vid + '\'' +
                ", duration='" + duration + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", playUrl='" + playUrl + '\'' +
                ", description='" + description + '\''+
                ", albumImageUrl='" + albumImageUrl + '\''+
                ", period='" + period + '\''+
                ", shortTitle='" + shortTitle + '\''+
                ", subtitle='" + subtitle + '\''+
                ", videoFocuses='" + videoFocuses + '\''+
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

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getAlbumImageUrl() {
        return albumImageUrl;
    }

    public void setAlbumImageUrl(String albumImageUrl) {
        this.albumImageUrl = albumImageUrl;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<videoFocuse> getVideoFocuses() {
        return videoFocuses;
    }

    public void setVideoFocuses(List<videoFocuse> videoFocuses) {
        this.videoFocuses = videoFocuses;
    }

    public class videoFocuse{

        private String id;
        private String description;
        private String image_url;
        private String start_time_seconds;


        @Override
        public String toString() {
            return "{" +
                    "id='" + id + '\'' +
                    "，description='" + description + '\'' +
                    "，image_url='" + image_url + '\'' +
                    ", start_time_seconds='" + start_time_seconds + '\'' +
                    '}';
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getStart_time_seconds() {
            return start_time_seconds;
        }

        public void setStart_time_seconds(String start_time_seconds) {
            this.start_time_seconds = start_time_seconds;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}