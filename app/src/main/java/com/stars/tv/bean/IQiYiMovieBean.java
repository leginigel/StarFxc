package com.stars.tv.bean;

import java.io.Serializable;
import java.util.List;


public class IQiYiMovieBean implements Serializable {

    private String name;
    private String docId;
    private String albumId;
    private String tvId;
    private String focus;
    private String playUrl;
    private String score;
    private String duration;
    private String description;
    private String imageUrl;
    private String latestOrder;
    private String videoCount;
    private String videoInfoType;
    private String payMarkUrl;
    private String secondInfo;
    private Cast cast;
    private List<Category> categories;

    @Override
    public String toString() {
        return "IQiYiMovieBean{" +
                "name='" + name + '\'' +
                ", docId='" + docId + '\'' +
                ", albumId='" + albumId + '\'' +
                ", tvId='" + tvId + '\'' +
                ", focus='" + focus + '\'' +
                ", playUrl='" + playUrl + '\'' +
                ", score='" + score + '\'' +
                ", duration='" + duration + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", latestOrder='" + latestOrder + '\'' +
                ", videoCount='" + videoCount + '\'' +
                ", videoInfoType='" + videoInfoType + '\'' +
                ", payMarkUrl='" + payMarkUrl + '\'' +
                ", secondInfo='" + secondInfo + '\'' +
                ", cast=" + cast +
                ", categories=" + categories +
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

    public Cast getCast() {
        return cast;
    }

    public void setCast(Cast cast) {
        this.cast = cast;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public String getSecondInfo() {
        return secondInfo;
    }

    public void setSecondInfo(String secondInfo) {
        this.secondInfo = secondInfo;
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

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
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

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }


    public class Role{

        private String image_url;
        private String name;
        private long id;


        @Override
        public String toString() {
            return "{" +
                    "image_url='" + image_url + '\'' +
                    "，name='" + name + '\'' +
                    ", id='" + id + '\'' +
                    '}';
        }

        public Role(String imageUrl, String name, long id) {
            this.image_url = imageUrl;
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }

    public class Category {

        private String name;
        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "{ name='" + name + "'}";
        }

    }

    public class Cast {

        private List<Role> main_charactor;
        private List<Role> host;
        public void setMain_charactor(List<Role> main_charactor) {
            this.main_charactor = main_charactor;
        }
        public List<Role> getMain_charactor() {
            return main_charactor;
        }
        @Override
        public String toString() {
            if(main_charactor==null)
            {
                return "{ host='" + host + '}';
            }
            return "{ main_charactor='" + main_charactor + '}';
        }

        public void setHost(List<Role> host) {
            this.host = host;
        }

        public List<Role> getHost() {
            return host;
        }
    }

}
