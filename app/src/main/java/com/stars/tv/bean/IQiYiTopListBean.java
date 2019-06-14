package com.stars.tv.bean;

import java.io.Serializable;
import java.util.List;

public class IQiYiTopListBean extends IQiYiBaseBean implements Serializable {
    private String album_id;
    private int album_channel;
    private String album_name;
    private String album_play_url;
    private String album_pic_url;
    private List<Album_main_actor> album_main_actor;
    private String episode_play_url;
    private String prompt_description;
    private String short_title;
    private int latest_order;
    private int video_count;
    private String period;
    private int charge_pay_mark;
    private String vid;
    private String sns_score;
    private boolean is_solo;
    private boolean is_juji;
    private boolean is_source;
    private boolean is_qiyi_produced;
    private boolean is_exclusive;
    private boolean is_1080p;
    private boolean is_member_only;
    private int album_rank_trend;
    private int hot_idx;

    @Override
    public String toString() {
        return "IQiYiTopListBean{" +
                "album_id='" + album_id + '\'' +
                ", album_channel=" + album_channel +
                ", album_name='" + album_name + '\'' +
                ", album_play_url='" + album_play_url + '\'' +
                ", album_pic_url='" + album_pic_url + '\'' +
                ", album_main_actor=" + album_main_actor +
                ", episode_play_url='" + episode_play_url + '\'' +
                ", prompt_description='" + prompt_description + '\'' +
                ", short_title='" + short_title + '\'' +
                ", latest_order=" + latest_order +
                ", video_count=" + video_count +
                ", period='" + period + '\'' +
                ", charge_pay_mark=" + charge_pay_mark +
                ", vid='" + vid + '\'' +
                ", sns_score='" + sns_score + '\'' +
                ", is_solo=" + is_solo +
                ", is_juji=" + is_juji +
                ", is_source=" + is_source +
                ", is_qiyi_produced=" + is_qiyi_produced +
                ", is_exclusive=" + is_exclusive +
                ", is_1080p=" + is_1080p +
                ", is_member_only=" + is_member_only +
                ", album_rank_trend=" + album_rank_trend +
                ", hot_idx=" + hot_idx +
                '}';
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getUrl() {
        return episode_play_url;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_channel(int album_channel) {
        this.album_channel = album_channel;
    }

    public int getAlbum_channel() {
        return album_channel;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_play_url(String album_play_url) {
        this.album_play_url = album_play_url;
    }

    public String getAlbum_play_url() {
        return album_play_url;
    }

    public void setAlbum_pic_url(String album_pic_url) {
        this.album_pic_url = album_pic_url;
    }

    public String getAlbum_pic_url() {
        return album_pic_url;
    }

    public void setAlbum_main_actor(List<Album_main_actor> album_main_actor) {
        this.album_main_actor = album_main_actor;
    }

    public List<Album_main_actor> getAlbum_main_actor() {
        return album_main_actor;
    }

    public void setEpisode_play_url(String episode_play_url) {
        this.episode_play_url = episode_play_url;
    }

    public String getEpisode_play_url() {
        return episode_play_url;
    }

    public void setPrompt_description(String prompt_description) {
        this.prompt_description = prompt_description;
    }

    public String getPrompt_description() {
        return prompt_description;
    }

    public void setShort_title(String short_title) {
        this.short_title = short_title;
    }

    public String getShort_title() {
        return short_title;
    }

    public void setLatest_order(int latest_order) {
        this.latest_order = latest_order;
    }

    public int getLatest_order() {
        return latest_order;
    }

    public void setVideo_count(int video_count) {
        this.video_count = video_count;
    }

    public int getVideo_count() {
        return video_count;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPeriod() {
        return period;
    }

    public void setCharge_pay_mark(int charge_pay_mark) {
        this.charge_pay_mark = charge_pay_mark;
    }

    public int getCharge_pay_mark() {
        return charge_pay_mark;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getVid() {
        return vid;
    }

    public void setSns_score(String sns_score) {
        this.sns_score = sns_score;
    }

    public String getSns_score() {
        return sns_score;
    }

    public void setIs_solo(boolean is_solo) {
        this.is_solo = is_solo;
    }

    public boolean getIs_solo() {
        return is_solo;
    }

    public void setIs_juji(boolean is_juji) {
        this.is_juji = is_juji;
    }

    public boolean getIs_juji() {
        return is_juji;
    }

    public void setIs_source(boolean is_source) {
        this.is_source = is_source;
    }

    public boolean getIs_source() {
        return is_source;
    }

    public void setIs_qiyi_produced(boolean is_qiyi_produced) {
        this.is_qiyi_produced = is_qiyi_produced;
    }

    public boolean getIs_qiyi_produced() {
        return is_qiyi_produced;
    }

    public void setIs_exclusive(boolean is_exclusive) {
        this.is_exclusive = is_exclusive;
    }

    public boolean getIs_exclusive() {
        return is_exclusive;
    }

    public void setIs_1080p(boolean is_1080p) {
        this.is_1080p = is_1080p;
    }

    public boolean getIs_1080p() {
        return is_1080p;
    }

    public void setIs_member_only(boolean is_member_only) {
        this.is_member_only = is_member_only;
    }

    public boolean getIs_member_only() {
        return is_member_only;
    }

    public void setAlbum_rank_trend(int album_rank_trend) {
        this.album_rank_trend = album_rank_trend;
    }

    public int getAlbum_rank_trend() {
        return album_rank_trend;
    }

    public void setHot_idx(int hot_idx) {
        this.hot_idx = hot_idx;
    }

    public int getHot_idx() {
        return hot_idx;
    }

    public class Album_main_actor implements Serializable{

        private long tag_id;

        @Override
        public String toString() {
            return "Album_main_actor{" +
                    "tag_id=" + tag_id +
                    ", tag_name='" + tag_name + '\'' +
                    '}';
        }

        private String tag_name;

        public void setTag_id(long tag_id) {
            this.tag_id = tag_id;
        }

        public long getTag_id() {
            return tag_id;
        }

        public void setTag_name(String tag_name) {
            this.tag_name = tag_name;
        }

        public String getTag_name() {
            return tag_name;
        }

    }

}