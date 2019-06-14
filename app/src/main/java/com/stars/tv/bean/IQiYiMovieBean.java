package com.stars.tv.bean;

import java.io.Serializable;
import java.util.List;


public class IQiYiMovieBean extends IQiYiBaseBean implements Serializable {

    private String name;
    private String docId;
    private String albumId;
    private String sourceId;
    private String tvId;
    private String vid;
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
    private Cast people;
    private List<Category> categories;
    private String albumImageUrl;
    private String period;
    private String shortTitle;
    private String subtitle;
    private List<VideoFocuse> videoFocuses;
    private String latestVideoUrl;
    private String firstVideoUrl;
    private String latestTvId;
    private String formatPeriod;

    @Override
    public String toString() {
        return "IQiYiMovie{" +
                "name='" + name + '\'' +
                ", docId='" + docId + '\'' +
                ", albumId='" + albumId + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", tvId='" + tvId + '\'' +
                ", vid='" + vid + '\'' +
                ", focus='" + focus + '\'' +
                ", playUrl='" + playUrl + '\'' +
                ", score='" + score + '\'' +
                ", duration='" + duration + '\'' +
//                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", latestOrder='" + latestOrder + '\'' +
                ", videoCount='" + videoCount + '\'' +
                ", videoInfoType='" + videoInfoType + '\'' +
                ", payMarkUrl='" + payMarkUrl + '\'' +
                ", secondInfo='" + secondInfo + '\'' +
                ", cast=" + cast +
                ", people=" + people +
                ", categories=" + categories +
                ", albumImageUrl='" + albumImageUrl + '\'' +
                ", period='" + period + '\'' +
                ", shortTitle='" + shortTitle + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", videoFocuses=" + videoFocuses +
                ", latestVideoUrl='" + latestVideoUrl + '\'' +
                ", firstVideoUrl='" + firstVideoUrl + '\'' +
                ", latestTvId='" + latestTvId + '\'' +
                ", formatPeriod='" + formatPeriod + '\'' +
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

    public Cast getPeople() {
        return people;
    }

    public void setPeople(Cast people) {
        this.people = people;
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

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getAlbumImageUrl() {
        return albumImageUrl;
    }

    public void setAlbumImageUrl(String albumImageUrl) {
        this.albumImageUrl = albumImageUrl;
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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<VideoFocuse> getVideoFocuses() {
        return videoFocuses;
    }

    public void setVideoFocuses(List<VideoFocuse> videoFocuses) {
        this.videoFocuses = videoFocuses;
    }

    public String getLatestVideoUrl() {
        return latestVideoUrl;
    }

    public void setLatestVideoUrl(String latestVideoUrl) {
        this.latestVideoUrl = latestVideoUrl;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getFirstVideoUrl() {
        return firstVideoUrl;
    }

    public void setFirstVideoUrl(String firstVideoUrl) {
        this.firstVideoUrl = firstVideoUrl;
    }

    public String getLatestTvId() {
        return latestTvId;
    }

    public void setLatestTvId(String latestTvId) {
        this.latestTvId = latestTvId;
    }

    public String getFormatPeriod() {
        return formatPeriod;
    }

    public void setFormatPeriod(String formatPeriod) {
        this.formatPeriod = formatPeriod;
    }

    @Override
    public String getId() {
        if(tvId!=null)
        {
            return tvId;
        }else if(albumId!=null)
        {
            return albumId;
        }else{
            return null;
        }
    }


    public class Role implements Serializable{

        private String image_url;
        private String name;
        private long id;


//        @Override
//        public String toString() {
//            return "{" +
//                    "image_url='" + image_url + '\'' +
//                    "，name='" + name + '\'' +
//                    ", id='" + id + '\'' +
//                    '}';
//        }

        @Override
        public String toString() {
            return "{" +
                    "\"image_url\":\"" + image_url + '\"' +
                    ",\"name\":\"" + name + '\"' +
                    ",\"id\":\"" + id + '\"' +
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

    public class Category implements Serializable {

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

    public class Cast implements Serializable{
        private List<Role> director;
        private List<Role> main_charactor;
        private List<Role> host;

        @Override
        public String toString() {
            return "{" +
                    "director=" + director +
                    ", main_charactor=" + main_charactor +
                    ", host=" + host +
                    '}';
        }

        public void setMain_charactor(List<Role> main_charactor) {
            this.main_charactor = main_charactor;
        }
        public List<Role> getMain_charactor() {
            return main_charactor;
        }

        public void setHost(List<Role> host) {
            this.host = host;
        }

        public List<Role> getHost() {
            return host;
        }

        public List<Role> getDirector() {
            return director;
        }

        public void setDirector(List<Role> director) {
            this.director = director;
        }
    }

    public class VideoFocuse implements Serializable{

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
