package com.stars.tv.bean;

import java.util.List;

public class IQiYiVideoBaseInfoBean extends IQiYiBaseBean{

    private String tvId;
    private String albumId;
    private int channelId;
    private String description;
    private String subtitle;
    private boolean rewardAllowed;
    private String vid;
    private String name;
    private String playUrl;
    private String issueTime;
    private String publishTime;
    private int featureAlbumId;
    private int contentType;
    private boolean displayCircle;
    private int payMark;
    private boolean displayUpDown;
    private String payMarkUrl;
    private String editorInfo;
    private String imageUrl;
    private String videoType;
    private String duration;
    private String ppsUrl;
    private boolean commentAllowed;
    private int videoCount;
    private int latestOrder;
    private boolean topChart;
    private boolean bossMixerAlbum;
    private String publicLevel;
    private int startTime;
    private int endTime;
    private List<Categories> categories;
    private String albumName;
    private String baikeUrl;
    private String albumImageUrl;
    private int userId;
    private String period;
    private boolean exclusive;
    private int order;
    private String playlistReason;
    private boolean effective;
    private boolean qiyiProduced;
    private String albumUrl;
    private int sourceId;
    private String focus;
    private String shortTitle;
    private boolean solo;
    private String albumFocus;
    private int fgtwVideo;
    private String qitanId;
    private boolean downloadAllowed;
    private String featureKeyword;
    private int is1080p;
    private int is720p;
    private People people;
    private boolean displayBarrage;
    private String previewImageUrl;
    private String publishDate;
    private String formatIssueTime;
    private int durationSec;
    private String pagePublishStatus;
    private String score;

    @Override
    public String toString() {
        return "IQiYiVideoBaseInfoBean{" +
                "tvId=" + tvId +
                ", albumId=" + albumId +
                ", channelId=" + channelId +
                ", description='" + description + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", rewardAllowed=" + rewardAllowed +
                ", vid='" + vid + '\'' +
                ", name='" + name + '\'' +
                ", playUrl='" + playUrl + '\'' +
                ", issueTime=" + issueTime +
                ", publishTime=" + publishTime +
                ", featureAlbumId=" + featureAlbumId +
                ", contentType=" + contentType +
                ", displayCircle=" + displayCircle +
                ", payMark=" + payMark +
                ", displayUpDown=" + displayUpDown +
                ", payMarkUrl='" + payMarkUrl + '\'' +
                ", editorInfo='" + editorInfo + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", videoType='" + videoType + '\'' +
                ", duration='" + duration + '\'' +
                ", ppsUrl='" + ppsUrl + '\'' +
                ", commentAllowed=" + commentAllowed +
                ", videoCount=" + videoCount +
                ", latestOrder=" + latestOrder +
                ", topChart=" + topChart +
                ", bossMixerAlbum=" + bossMixerAlbum +
                ", publicLevel='" + publicLevel + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", categories=" + categories +
                ", albumName='" + albumName + '\'' +
                ", baikeUrl='" + baikeUrl + '\'' +
                ", albumImageUrl='" + albumImageUrl + '\'' +
                ", userId=" + userId +
                ", period='" + period + '\'' +
                ", exclusive=" + exclusive +
                ", order=" + order +
                ", playlistReason='" + playlistReason + '\'' +
                ", effective=" + effective +
                ", qiyiProduced=" + qiyiProduced +
                ", albumUrl='" + albumUrl + '\'' +
                ", sourceId=" + sourceId +
                ", focus='" + focus + '\'' +
                ", shortTitle='" + shortTitle + '\'' +
                ", solo=" + solo +
                ", albumFocus='" + albumFocus + '\'' +
                ", fgtwVideo=" + fgtwVideo +
                ", qitanId=" + qitanId +
                ", downloadAllowed=" + downloadAllowed +
                ", featureKeyword='" + featureKeyword + '\'' +
                ", is1080p=" + is1080p +
                ", is720p=" + is720p +
                ", people=" + people +
                ", displayBarrage=" + displayBarrage +
                ", previewImageUrl='" + previewImageUrl + '\'' +
                ", publishDate='" + publishDate + '\'' +
                ", formatIssueTime='" + formatIssueTime + '\'' +
                ", durationSec=" + durationSec +
                ", pagePublishStatus='" + pagePublishStatus + '\'' +
                ", score=" + score +
                '}';
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


    public void setTvId(String tvId) {
         this.tvId = tvId;
     }
     public String getTvId() {
         return tvId;
     }

    public void setAlbumId(String albumId) {
         this.albumId = albumId;
     }
     public String getAlbumId() {
         return albumId;
     }

    public void setChannelId(int channelId) {
         this.channelId = channelId;
     }
     public int getChannelId() {
         return channelId;
     }

    public void setDescription(String description) {
         this.description = description;
     }
     public String getDescription() {
         return description;
     }

    public void setSubtitle(String subtitle) {
         this.subtitle = subtitle;
     }
     public String getSubtitle() {
         return subtitle;
     }

    public void setRewardAllowed(boolean rewardAllowed) {
         this.rewardAllowed = rewardAllowed;
     }
     public boolean getRewardAllowed() {
         return rewardAllowed;
     }

    public void setVid(String vid) {
         this.vid = vid;
     }
     public String getVid() {
         return vid;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setPlayUrl(String playUrl) {
         this.playUrl = playUrl;
     }
     public String getPlayUrl() {
         return playUrl;
     }

    public void setIssueTime(String issueTime) {
         this.issueTime = issueTime;
     }
     public String getIssueTime() {
         return issueTime;
     }

    public void setPublishTime(String publishTime) {
         this.publishTime = publishTime;
     }
     public String getPublishTime() {
         return publishTime;
     }

    public void setFeatureAlbumId(int featureAlbumId) {
         this.featureAlbumId = featureAlbumId;
     }
     public int getFeatureAlbumId() {
         return featureAlbumId;
     }

    public void setContentType(int contentType) {
         this.contentType = contentType;
     }
     public int getContentType() {
         return contentType;
     }

    public void setDisplayCircle(boolean displayCircle) {
         this.displayCircle = displayCircle;
     }
     public boolean getDisplayCircle() {
         return displayCircle;
     }

    public void setPayMark(int payMark) {
         this.payMark = payMark;
     }
     public int getPayMark() {
         return payMark;
     }

    public void setDisplayUpDown(boolean displayUpDown) {
         this.displayUpDown = displayUpDown;
     }
     public boolean getDisplayUpDown() {
         return displayUpDown;
     }

    public void setPayMarkUrl(String payMarkUrl) {
         this.payMarkUrl = payMarkUrl;
     }
     public String getPayMarkUrl() {
         return payMarkUrl;
     }

    public void setEditorInfo(String editorInfo) {
         this.editorInfo = editorInfo;
     }
     public String getEditorInfo() {
         return editorInfo;
     }

    public void setImageUrl(String imageUrl) {
         this.imageUrl = imageUrl;
     }
     public String getImageUrl() {
         return imageUrl;
     }

    public void setVideoType(String videoType) {
         this.videoType = videoType;
     }
     public String getVideoType() {
         return videoType;
     }

    public void setDuration(String duration) {
         this.duration = duration;
     }
     public String getDuration() {
         return duration;
     }

    public void setPpsUrl(String ppsUrl) {
         this.ppsUrl = ppsUrl;
     }
     public String getPpsUrl() {
         return ppsUrl;
     }

    public void setCommentAllowed(boolean commentAllowed) {
         this.commentAllowed = commentAllowed;
     }
     public boolean getCommentAllowed() {
         return commentAllowed;
     }

    public void setVideoCount(int videoCount) {
         this.videoCount = videoCount;
     }
     public int getVideoCount() {
         return videoCount;
     }

    public void setLatestOrder(int latestOrder) {
         this.latestOrder = latestOrder;
     }
     public int getLatestOrder() {
         return latestOrder;
     }

    public void setTopChart(boolean topChart) {
         this.topChart = topChart;
     }
     public boolean getTopChart() {
         return topChart;
     }

    public void setBossMixerAlbum(boolean bossMixerAlbum) {
         this.bossMixerAlbum = bossMixerAlbum;
     }
     public boolean getBossMixerAlbum() {
         return bossMixerAlbum;
     }

    public void setPublicLevel(String publicLevel) {
         this.publicLevel = publicLevel;
     }
     public String getPublicLevel() {
         return publicLevel;
     }

    public void setStartTime(int startTime) {
         this.startTime = startTime;
     }
     public int getStartTime() {
         return startTime;
     }

    public void setEndTime(int endTime) {
         this.endTime = endTime;
     }
     public int getEndTime() {
         return endTime;
     }

    public void setCategories(List<Categories> categories) {
         this.categories = categories;
     }
     public List<Categories> getCategories() {
         return categories;
     }

    public void setAlbumName(String albumName) {
         this.albumName = albumName;
     }
     public String getAlbumName() {
         return albumName;
     }

    public void setBaikeUrl(String baikeUrl) {
         this.baikeUrl = baikeUrl;
     }
     public String getBaikeUrl() {
         return baikeUrl;
     }

    public void setAlbumImageUrl(String albumImageUrl) {
         this.albumImageUrl = albumImageUrl;
     }
     public String getAlbumImageUrl() {
         return albumImageUrl;
     }

    public void setUserId(int userId) {
         this.userId = userId;
     }
     public int getUserId() {
         return userId;
     }

    public void setPeriod(String period) {
         this.period = period;
     }
     public String getPeriod() {
         return period;
     }

    public void setExclusive(boolean exclusive) {
         this.exclusive = exclusive;
     }
     public boolean getExclusive() {
         return exclusive;
     }

    public void setOrder(int order) {
         this.order = order;
     }
     public int getOrder() {
         return order;
     }

    public void setPlaylistReason(String playlistReason) {
         this.playlistReason = playlistReason;
     }
     public String getPlaylistReason() {
         return playlistReason;
     }

    public void setEffective(boolean effective) {
         this.effective = effective;
     }
     public boolean getEffective() {
         return effective;
     }

    public void setQiyiProduced(boolean qiyiProduced) {
         this.qiyiProduced = qiyiProduced;
     }
     public boolean getQiyiProduced() {
         return qiyiProduced;
     }

    public void setAlbumUrl(String albumUrl) {
         this.albumUrl = albumUrl;
     }
     public String getAlbumUrl() {
         return albumUrl;
     }

    public void setSourceId(int sourceId) {
         this.sourceId = sourceId;
     }
     public int getSourceId() {
         return sourceId;
     }

    public void setFocus(String focus) {
         this.focus = focus;
     }
     public String getFocus() {
         return focus;
     }

    public void setShortTitle(String shortTitle) {
         this.shortTitle = shortTitle;
     }
     public String getShortTitle() {
         return shortTitle;
     }

    public void setSolo(boolean solo) {
         this.solo = solo;
     }
     public boolean getSolo() {
         return solo;
     }

    public void setAlbumFocus(String albumFocus) {
         this.albumFocus = albumFocus;
     }
     public String getAlbumFocus() {
         return albumFocus;
     }

    public void setFgtwVideo(int fgtwVideo) {
         this.fgtwVideo = fgtwVideo;
     }
     public int getFgtwVideo() {
         return fgtwVideo;
     }

    public void setQitanId(String qitanId) {
         this.qitanId = qitanId;
     }
     public String getQitanId() {
         return qitanId;
     }

    public void setDownloadAllowed(boolean downloadAllowed) {
         this.downloadAllowed = downloadAllowed;
     }
     public boolean getDownloadAllowed() {
         return downloadAllowed;
     }

    public void setFeatureKeyword(String featureKeyword) {
         this.featureKeyword = featureKeyword;
     }
     public String getFeatureKeyword() {
         return featureKeyword;
     }

    public void setIs1080p(int is1080p) {
         this.is1080p = is1080p;
     }
     public int getIs1080p() {
         return is1080p;
     }

    public void setIs720p(int is720p) {
         this.is720p = is720p;
     }
     public int getIs720p() {
         return is720p;
     }

    public void setPeople(People people) {
         this.people = people;
     }
     public People getPeople() {
         return people;
     }

    public void setDisplayBarrage(boolean displayBarrage) {
         this.displayBarrage = displayBarrage;
     }
     public boolean getDisplayBarrage() {
         return displayBarrage;
     }

    public void setPreviewImageUrl(String previewImageUrl) {
         this.previewImageUrl = previewImageUrl;
     }
     public String getPreviewImageUrl() {
         return previewImageUrl;
     }

    public void setPublishDate(String publishDate) {
         this.publishDate = publishDate;
     }
     public String getPublishDate() {
         return publishDate;
     }

    public void setFormatIssueTime(String formatIssueTime) {
         this.formatIssueTime = formatIssueTime;
     }
     public String getFormatIssueTime() {
         return formatIssueTime;
     }

    public void setDurationSec(int durationSec) {
         this.durationSec = durationSec;
     }
     public int getDurationSec() {
         return durationSec;
     }

    public void setPagePublishStatus(String pagePublishStatus) {
         this.pagePublishStatus = pagePublishStatus;
     }
     public String getPagePublishStatus() {
         return pagePublishStatus;
     }

    public void setScore(String score) {
         this.score = score;
     }
     public String getScore() {
         return score;
     }

    public class Categories {

        private int id;
        private String name;
        private String url;
        private int subType;
        private String subName;
        private int level;

        @Override
        public String toString() {
            return "Categories{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", url='" + url + '\'' +
                    ", subType=" + subType +
                    ", subName='" + subName + '\'' +
                    ", level=" + level +
                    ", qipuId=" + qipuId +
                    ", parentId=" + parentId +
                    '}';
        }

        private int qipuId;
        private int parentId;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setUrl(String url) {
            this.url = url;
        }
        public String getUrl() {
            return url;
        }

        public void setSubType(int subType) {
            this.subType = subType;
        }
        public int getSubType() {
            return subType;
        }

        public void setSubName(String subName) {
            this.subName = subName;
        }
        public String getSubName() {
            return subName;
        }

        public void setLevel(int level) {
            this.level = level;
        }
        public int getLevel() {
            return level;
        }

        public void setQipuId(int qipuId) {
            this.qipuId = qipuId;
        }
        public int getQipuId() {
            return qipuId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }
        public int getParentId() {
            return parentId;
        }

    }

    public class Director {
        @Override
        public String toString() {
            return "{" +
                    "\"id\":\"" + id + '\"' +
                    ",\"name\":\"" + name + '\"' +
                    ",\"image_url\":\"" + image_url + '\"' +
                    '}';
        }

        private String id;
        private String name;
        private String image_url;
        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }
        public String getImage_url() {
            return image_url;
        }

    }

    public class Main_charactor {
        @Override
        public String toString() {
            return "{" +
                    "\"id\":\"" + id + '\"' +
                    ",\"name\":\"" + name + '\"' +
                    ",\"image_url\":\"" + image_url + '\"' +
                    ",\"character\":\"" + character + '\"' +
                    '}';
        }

        private long id;
        private String name;
        private String image_url;
        private List<String> character;
        public void setId(long id) {
            this.id = id;
        }
        public long getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }
        public String getImage_url() {
            return image_url;
        }

        public void setCharacter(List<String> character) {
            this.character = character;
        }
        public List<String> getCharacter() {
            return character;
        }

    }

    public class People {
        @Override
        public String toString() {
            return "People{" +
                    "director=" + director +
                    ", host=" + host +
                    ", guest=" + guest +
                    ", main_charactor=" + main_charactor +
                    '}';
        }

        private List<Director> director;

        public List<Director> getHost() {
            return host;
        }

        public void setHost(List<Director> host) {
            this.host = host;
        }

        public List<Director> getGuest() {
            return guest;
        }

        public void setGuest(List<Director> guest) {
            this.guest = guest;
        }

        private List<Director> host;
        private List<Director> guest;
        private List<Main_charactor> main_charactor;
        public void setDirector(List<Director> director) {
            this.director = director;
        }
        public List<Director> getDirector() {
            return director;
        }

        public void setMain_charactor(List<Main_charactor> main_charactor) {
            this.main_charactor = main_charactor;
        }
        public List<Main_charactor> getMain_charactor() {
            return main_charactor;
        }

    }

}