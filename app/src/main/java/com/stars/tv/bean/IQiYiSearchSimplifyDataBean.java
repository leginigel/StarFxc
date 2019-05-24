
package com.stars.tv.bean;

import java.util.List;

public class IQiYiSearchSimplifyDataBean {

    private int result_num;
    private int page_num;
    private int page_size;
    private int max_result_number;
    private String real_query;
    private List<DocinfosBean> docinfos;

    @Override
    public String toString() {
        return "IQiYiSearchData{" +
                "result_num=" + result_num +
                ", page_num=" + page_num +
                ", page_size=" + page_size +
                ", max_result_number=" + max_result_number +
                ", real_query='" + real_query + '\'' +
                ", docinfos=" + docinfos +
                '}';
    }

    public int getResult_num() {
        return result_num;
    }

    public void setResult_num(int result_num) {
        this.result_num = result_num;
    }

    public int getPage_num() {
        return page_num;
    }

    public void setPage_num(int page_num) {
        this.page_num = page_num;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public int getMax_result_number() {
        return max_result_number;
    }

    public void setMax_result_number(int max_result_number) {
        this.max_result_number = max_result_number;
    }

    public String getReal_query() {
        return real_query;
    }

    public void setReal_query(String real_query) {
        this.real_query = real_query;
    }

    public List<DocinfosBean> getDocinfos() {
        return docinfos;
    }

    public void setDocinfos(List<DocinfosBean> docinfos) {
        this.docinfos = docinfos;
    }

    public class DocinfosBean {

        private String doc_id;
        private double score;
        private int pos;
        private int sort;
        private AlbumDocInfoBean albumDocInfo;

        @Override
        public String toString() {
            return "{" +
                    "doc_id='" + doc_id + '\'' +
                    ", score=" + score +
                    ", pos=" + pos +
                    ", sort=" + sort +
                    ", albumDocInfo=" + albumDocInfo +
                    '}';
        }

        public String getDoc_id() {
            return doc_id;
        }

        public void setDoc_id(String doc_id) {
            this.doc_id = doc_id;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public AlbumDocInfoBean getAlbumDocInfo() {
            return albumDocInfo;
        }

        public void setAlbumDocInfo(AlbumDocInfoBean albumDocInfo) {
            this.albumDocInfo = albumDocInfo;
        }
    }

    public class AlbumDocInfoBean {

        private String albumId;
        private String albumTitle;
        private String albumAlias;
        private String albumEnglishTitle;
        private String channel;
        private String albumLink;
        private String albumVImage;
        private String albumHImage;
        private String region;
        private String season;
        private String releaseDate;
        private int itemTotalNumber;
        private String siteName;
        private String siteId;
        private String albumImg;
        private int contentType;
        private int isPurchase;
        private int playCount;
        private double score;
        private boolean series;
        private String threeCategory;
        private String tvFocus;
        private int videoDocType;
        private String stragyTime;
        private String qipu_id;
        private int latest_update_time;
        private int album_type;
        private int newest_item_number;
        private String bitrate;
        private String pay_mark_url;
        private int episode_type;
        private VideoLibMetaBean video_lib_meta;
        private List<VideoinfosBean> videoinfos;
        private List<StarinfosBean> starinfos;
        private List<VideoinfosBean> prevues;
        private List<VideoinfosBean> recommendation;
        private List<CircleSummariesBean> circle_summaries;

        @Override
        public String toString() {
            return "AlbumDocInfoBean{" +
                    "albumId='" + albumId + '\'' +
                    ", albumTitle='" + albumTitle + '\'' +
                    ", albumAlias='" + albumAlias + '\'' +
                    ", albumEnglishTitle='" + albumEnglishTitle + '\'' +
                    ", channel='" + channel + '\'' +
                    ", albumLink='" + albumLink + '\'' +
                    ", albumVImage='" + albumVImage + '\'' +
                    ", albumHImage='" + albumHImage + '\'' +
                    ", region='" + region + '\'' +
                    ", season='" + season + '\'' +
                    ", releaseDate='" + releaseDate + '\'' +
                    ", itemTotalNumber=" + itemTotalNumber +
                    ", siteName='" + siteName + '\'' +
                    ", siteId='" + siteId + '\'' +
                    ", albumImg='" + albumImg + '\'' +
                    ", contentType=" + contentType +
                    ", isPurchase=" + isPurchase +
                    ", playCount=" + playCount +
                    ", score=" + score +
                    ", series=" + series +
                    ", threeCategory='" + threeCategory + '\'' +
                    ", tvFocus='" + tvFocus + '\'' +
                    ", videoDocType=" + videoDocType +
                    ", stragyTime='" + stragyTime + '\'' +
                    ", qipu_id='" + qipu_id + '\'' +
                    ", latest_update_time=" + latest_update_time +
                    ", album_type=" + album_type +
                    ", newest_item_number=" + newest_item_number +
                    ", bitrate='" + bitrate + '\'' +
                    ", pay_mark_url='" + pay_mark_url + '\'' +
                    ", episode_type=" + episode_type +
                    ", video_lib_meta=" + video_lib_meta +
                    ", videoinfos=" + videoinfos +
                    ", starinfos=" + starinfos +
                    ", prevues=" + prevues +
                    ", recommendation=" + recommendation +
                    ", circle_summaries=" + circle_summaries +
                    '}';
        }

        public String getAlbumId() {
            return albumId;
        }

        public void setAlbumId(String albumId) {
            this.albumId = albumId;
        }

        public String getAlbumTitle() {
            return albumTitle;
        }

        public void setAlbumTitle(String albumTitle) {
            this.albumTitle = albumTitle;
        }

        public String getAlbumAlias() {
            return albumAlias;
        }

        public void setAlbumAlias(String albumAlias) {
            this.albumAlias = albumAlias;
        }

        public String getAlbumEnglishTitle() {
            return albumEnglishTitle;
        }

        public void setAlbumEnglishTitle(String albumEnglishTitle) {
            this.albumEnglishTitle = albumEnglishTitle;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getAlbumLink() {
            return albumLink;
        }

        public void setAlbumLink(String albumLink) {
            this.albumLink = albumLink;
        }

        public String getAlbumVImage() {
            return albumVImage;
        }

        public void setAlbumVImage(String albumVImage) {
            this.albumVImage = albumVImage;
        }

        public String getAlbumHImage() {
            return albumHImage;
        }

        public void setAlbumHImage(String albumHImage) {
            this.albumHImage = albumHImage;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getSeason() {
            return season;
        }

        public void setSeason(String season) {
            this.season = season;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        public int getItemTotalNumber() {
            return itemTotalNumber;
        }

        public void setItemTotalNumber(int itemTotalNumber) {
            this.itemTotalNumber = itemTotalNumber;
        }

        public String getSiteName() {
            return siteName;
        }

        public void setSiteName(String siteName) {
            this.siteName = siteName;
        }

        public String getSiteId() {
            return siteId;
        }

        public void setSiteId(String siteId) {
            this.siteId = siteId;
        }

        public String getAlbumImg() {
            return albumImg;
        }

        public void setAlbumImg(String albumImg) {
            this.albumImg = albumImg;
        }

        public int getContentType() {
            return contentType;
        }

        public void setContentType(int contentType) {
            this.contentType = contentType;
        }

        public int getIsPurchase() {
            return isPurchase;
        }

        public void setIsPurchase(int isPurchase) {
            this.isPurchase = isPurchase;
        }

        public int getPlayCount() {
            return playCount;
        }

        public void setPlayCount(int playCount) {
            this.playCount = playCount;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public boolean isSeries() {
            return series;
        }

        public void setSeries(boolean series) {
            this.series = series;
        }

        public String getThreeCategory() {
            return threeCategory;
        }

        public void setThreeCategory(String threeCategory) {
            this.threeCategory = threeCategory;
        }

        public String getTvFocus() {
            return tvFocus;
        }

        public void setTvFocus(String tvFocus) {
            this.tvFocus = tvFocus;
        }

        public int getVideoDocType() {
            return videoDocType;
        }

        public void setVideoDocType(int videoDocType) {
            this.videoDocType = videoDocType;
        }

        public String getStragyTime() {
            return stragyTime;
        }

        public void setStragyTime(String stragyTime) {
            this.stragyTime = stragyTime;
        }

        public String getQipu_id() {
            return qipu_id;
        }

        public void setQipu_id(String qipu_id) {
            this.qipu_id = qipu_id;
        }

        public int getLatest_update_time() {
            return latest_update_time;
        }

        public void setLatest_update_time(int latest_update_time) {
            this.latest_update_time = latest_update_time;
        }

        public int getAlbum_type() {
            return album_type;
        }

        public void setAlbum_type(int album_type) {
            this.album_type = album_type;
        }

        public int getNewest_item_number() {
            return newest_item_number;
        }

        public void setNewest_item_number(int newest_item_number) {
            this.newest_item_number = newest_item_number;
        }

        public String getBitrate() {
            return bitrate;
        }

        public void setBitrate(String bitrate) {
            this.bitrate = bitrate;
        }

        public String getPay_mark_url() {
            return pay_mark_url;
        }

        public void setPay_mark_url(String pay_mark_url) {
            this.pay_mark_url = pay_mark_url;
        }

        public int getEpisode_type() {
            return episode_type;
        }

        public void setEpisode_type(int episode_type) {
            this.episode_type = episode_type;
        }

        public VideoLibMetaBean getVideo_lib_meta() {
            return video_lib_meta;
        }

        public void setVideo_lib_meta(VideoLibMetaBean video_lib_meta) {
            this.video_lib_meta = video_lib_meta;
        }

        public List<VideoinfosBean> getVideoinfos() {
            return videoinfos;
        }

        public void setVideoinfos(List<VideoinfosBean> videoinfos) {
            this.videoinfos = videoinfos;
        }

        public List<StarinfosBean> getStarinfos() {
            return starinfos;
        }

        public void setStarinfos(List<StarinfosBean> starinfos) {
            this.starinfos = starinfos;
        }

        public List<VideoinfosBean> getPrevues() {
            return prevues;
        }

        public void setPrevues(List<VideoinfosBean> prevues) {
            this.prevues = prevues;
        }

        public List<VideoinfosBean> getRecommendation() {
            return recommendation;
        }

        public void setRecommendation(List<VideoinfosBean> recommendation) {
            this.recommendation = recommendation;
        }

        public List<CircleSummariesBean> getCircle_summaries() {
            return circle_summaries;
        }

        public void setCircle_summaries(List<CircleSummariesBean> circle_summaries) {
            this.circle_summaries = circle_summaries;
        }

    }

    public class VideoLibMetaBean {
        private String entity_id;
        private String title;
        private List<Role> director;
        private List<Role> actor;

        @Override
        public String toString() {
            return "{" +
                    "entity_id=" + entity_id +
                    ", title='" + title + '\'' +
                    ", director=" + director +
                    ", actor=" + actor +
                    '}';
        }

        public String getEntity_id() {
            return entity_id;
        }

        public void setEntity_id(String entity_id) {
            this.entity_id = entity_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<Role> getDirector() {
            return director;
        }

        public void setDirector(List<Role> director) {
            this.director = director;
        }

        public List<Role> getActor() {
            return actor;
        }

        public void setActor(List<Role> actor) {
            this.actor = actor;
        }
    }

    public class VideoinfosBean {

        private String itemTitle;
        private int itemNumber;
        private String itemHImage;
        private String itemLink;
        private int playedNumber;
        private String initialIssueTime;
        private String tvId;
        private String vid;
        private String subTitle;
        private String vFocus;
        private String albumId;
        private int year;
        private String qipu_id;
        private String screen_size;
        private boolean is_vip;
        private LatestVideoBean latest_video;

        @Override
        public String toString() {
            return "{" +
                    "itemTitle='" + itemTitle + '\'' +
                    ", itemNumber=" + itemNumber +
                    ", itemHImage='" + itemHImage + '\'' +
                    ", itemLink='" + itemLink + '\'' +
                    ", playedNumber=" + playedNumber +
                    ", initialIssueTime='" + initialIssueTime + '\'' +
                    ", tvId='" + tvId + '\'' +
                    ", vid='" + vid + '\'' +
                    ", subTitle='" + subTitle + '\'' +
                    ", vFocus='" + vFocus + '\'' +
                    ", albumId='" + albumId + '\'' +
                    ", year=" + year +
                    ", qipu_id='" + qipu_id + '\'' +
                    ", screen_size='" + screen_size + '\'' +
                    ", is_vip=" + is_vip +
                    ", latest_video=" + latest_video +
                    '}';
        }

        public String getItemTitle() {
            return itemTitle;
        }

        public void setItemTitle(String itemTitle) {
            this.itemTitle = itemTitle;
        }

        public int getItemNumber() {
            return itemNumber;
        }

        public void setItemNumber(int itemNumber) {
            this.itemNumber = itemNumber;
        }

        public String getItemHImage() {
            return itemHImage;
        }

        public void setItemHImage(String itemHImage) {
            this.itemHImage = itemHImage;
        }

        public String getItemLink() {
            return itemLink;
        }

        public void setItemLink(String itemLink) {
            this.itemLink = itemLink;
        }

        public int getPlayedNumber() {
            return playedNumber;
        }

        public void setPlayedNumber(int playedNumber) {
            this.playedNumber = playedNumber;
        }

        public String getInitialIssueTime() {
            return initialIssueTime;
        }

        public void setInitialIssueTime(String initialIssueTime) {
            this.initialIssueTime = initialIssueTime;
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

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getAlbumId() {
            return albumId;
        }

        public void setAlbumId(String albumId) {
            this.albumId = albumId;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public String getQipu_id() {
            return qipu_id;
        }

        public void setQipu_id(String qipu_id) {
            this.qipu_id = qipu_id;
        }

        public String getScreen_size() {
            return screen_size;
        }

        public void setScreen_size(String screen_size) {
            this.screen_size = screen_size;
        }

        public String getvFocus() {
            return vFocus;
        }

        public void setvFocus(String vFocus) {
            this.vFocus = vFocus;
        }

        public boolean isIs_vip() {
            return is_vip;
        }

        public void setIs_vip(boolean is_vip) {
            this.is_vip = is_vip;
        }

        public LatestVideoBean getLatest_video() {
            return latest_video;
        }

        public void setLatest_video(LatestVideoBean latest_video) {
            this.latest_video = latest_video;
        }
    }

    public class StarinfosBean {

        private String starNation;
        private String starName;
        private String starPic;
        private String starDesc;
        private int starBirth;
        private String qipu_id;
        private String constellation;
        private String star_english_name;
        private String star_region;
        private int height;
        private String occupation;

        @Override
        public String toString() {
            return "{" +
                    "starNation='" + starNation + '\'' +
                    ", starName='" + starName + '\'' +
                    ", starPic='" + starPic + '\'' +
                    ", starDesc='" + starDesc + '\'' +
                    ", starBirth=" + starBirth +
                    ", qipu_id=" + qipu_id +
                    ", constellation='" + constellation + '\'' +
                    ", star_english_name='" + star_english_name + '\'' +
                    ", star_region='" + star_region + '\'' +
                    ", height=" + height +
                    ", occupation='" + occupation + '\'' +
                    '}';
        }

        public String getStarNation() {
            return starNation;
        }

        public void setStarNation(String starNation) {
            this.starNation = starNation;
        }

        public String getStarName() {
            return starName;
        }

        public void setStarName(String starName) {
            this.starName = starName;
        }

        public String getStarPic() {
            return starPic;
        }

        public void setStarPic(String starPic) {
            this.starPic = starPic;
        }

        public String getStarDesc() {
            return starDesc;
        }

        public void setStarDesc(String starDesc) {
            this.starDesc = starDesc;
        }

        public int getStarBirth() {
            return starBirth;
        }

        public void setStarBirth(int starBirth) {
            this.starBirth = starBirth;
        }

        public String getQipu_id() {
            return qipu_id;
        }

        public void setQipu_id(String qipu_id) {
            this.qipu_id = qipu_id;
        }

        public String getConstellation() {
            return constellation;
        }

        public void setConstellation(String constellation) {
            this.constellation = constellation;
        }

        public String getStar_english_name() {
            return star_english_name;
        }

        public void setStar_english_name(String star_english_name) {
            this.star_english_name = star_english_name;
        }

        public String getStar_region() {
            return star_region;
        }

        public void setStar_region(String star_region) {
            this.star_region = star_region;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getOccupation() {
            return occupation;
        }

        public void setOccupation(String occupation) {
            this.occupation = occupation;
        }
    }

    public class CircleSummariesBean {

        private int id;
        private String title;
        private String image_url;
        private String description;
        private int circle_user_count;

        @Override
        public String toString() {
            return "{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", image_url='" + image_url + '\'' +
                    ", description='" + description + '\'' +
                    ", circle_user_count=" + circle_user_count +
                    '}';
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public int getCircle_user_count() {
            return circle_user_count;
        }

        public void setCircle_user_count(int circle_user_count) {
            this.circle_user_count = circle_user_count;
        }
    }

    public class Role {

        private int id;
        private String name;
        private String image_url;

        @Override
        public String toString() {
            return "{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", image_url='" + image_url + '\'' +
                    '}';
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
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
    }

    public class LatestVideoBean {

        private String title;
        private String qipu_id;
        private String display_name;
        private int channel_id;
        private String tvid;
        private String thumbnail_url;
        private String page_url;
        private int duration;
        private String publish_time;
        private String father_album_id;
        private String vid;
        private boolean is_vip;

        @Override
        public String toString() {
            return "{" +
                    "title='" + title + '\'' +
                    ", qipu_id=" + qipu_id +
                    ", display_name='" + display_name + '\'' +
                    ", channel_id=" + channel_id +
                    ", tvid=" + tvid +
                    ", thumbnail_url='" + thumbnail_url + '\'' +
                    ", page_url='" + page_url + '\'' +
                    ", duration=" + duration +
                    ", publish_time='" + publish_time + '\'' +
                    ", father_album_id=" + father_album_id +
                    ", vid='" + vid + '\'' +
                    ", is_vip=" + is_vip +
                    '}';
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getQipu_id() {
            return qipu_id;
        }

        public void setQipu_id(String qipu_id) {
            this.qipu_id = qipu_id;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        public int getChannel_id() {
            return channel_id;
        }

        public void setChannel_id(int channel_id) {
            this.channel_id = channel_id;
        }

        public String getTvid() {
            return tvid;
        }

        public void setTvid(String tvid) {
            this.tvid = tvid;
        }

        public String getThumbnail_url() {
            return thumbnail_url;
        }

        public void setThumbnail_url(String thumbnail_url) {
            this.thumbnail_url = thumbnail_url;
        }

        public String getPage_url() {
            return page_url;
        }

        public void setPage_url(String page_url) {
            this.page_url = page_url;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getPublish_time() {
            return publish_time;
        }

        public void setPublish_time(String publish_time) {
            this.publish_time = publish_time;
        }

        public String getFather_album_id() {
            return father_album_id;
        }

        public void setFather_album_id(String father_album_id) {
            this.father_album_id = father_album_id;
        }

        public String getVid() {
            return vid;
        }

        public void setVid(String vid) {
            this.vid = vid;
        }

        public boolean isIs_vip() {
            return is_vip;
        }

        public void setIs_vip(boolean is_vip) {
            this.is_vip = is_vip;
        }
    }
}