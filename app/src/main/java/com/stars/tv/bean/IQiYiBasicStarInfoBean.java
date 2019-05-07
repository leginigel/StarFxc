
package com.stars.tv.bean;

import java.util.List;

public class IQiYiBasicStarInfoBean {

    private String id;
    private String description;
    private String starImg;
    private String name;
    private Birthday birthday;
    private String gender;
    private String height;
    private String weight;
    private String bloodType;
    private List<String> region;
    private String birthPlace;
    private List<StarWorks> starWorks;

    @Override
    public String toString() {
        return "IQiYiBasicStarInfo{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", starImg='" + starImg + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", gender='" + gender + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", bloodType='" + bloodType + '\'' +
                ", region=" + region +
                ", birthPlace='" + birthPlace + '\'' +
                ", starWorks=" + starWorks +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStarImg() {
        return starImg;
    }

    public void setStarImg(String starImg) {
        this.starImg = starImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Birthday getBirthday() {
        return birthday;
    }

    public void setBirthday(Birthday birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public List<String> getRegion() {
        return region;
    }

    public void setRegion(List<String> region) {
        this.region = region;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public List<StarWorks> getStarWorks() {
        return starWorks;
    }

    public void setStarWorks(List<StarWorks> starWorks) {
        this.starWorks = starWorks;
    }

    public class Birthday{
        private String year;
        private String month;
        private String day;

        @Override
        public String toString() {
            return "Birthday{" +
                    "year='" + year + '\'' +
                    ", month='" + month + '\'' +
                    ", day='" + day + '\'' +
                    '}';
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }
    }

    public class DayTime{
        private int year;
        private int month;
        private int day;

        @Override
        public String toString() {
            return "{" +
                    "year=" + year +
                    ", month=" + month +
                    ", day=" + day +
                    '}';
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }
    }

    public class RoleInfo {
        private String role;
        private List<String> character;

        @Override
        public String toString() {
            return "{" +
                    "role='" + role + '\'' +
                    ", character=" + character +
                    '}';
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public List<String> getCharacter() {
            return character;
        }

        public void setCharacter(List<String> character) {
            this.character = character;
        }
    }
    public class StarWorks {

        private String title;
        private long qipu_id;
        private int channel_id;
        private long tvid;
        private long qitan_id;
        private String thumbnail_url;
        private int duration;
        private int available_episodes;
        private int total_episodes;
        private DayTime last_issue_date;
        private DayTime publish_time;
        private double relevance_score;
        private List<RoleInfo> role_info;
        private long corresponding_id;
        private double sns_score;
        private long ad_publish_date;
        private String prompt_description;
        private long kv_album_id;
        private String poster_url;
        private List<String> site_name;
        private String qiyu_picture_url;
        private boolean qiyu_is_focus_valid;

        @Override
        public String toString() {
            return "StarWorks{" +
                    "title='" + title + '\'' +
                    ", qipu_id=" + qipu_id +
                    ", channel_id=" + channel_id +
                    ", tvid=" + tvid +
                    ", qitan_id=" + qitan_id +
                    ", thumbnail_url='" + thumbnail_url + '\'' +
                    ", duration=" + duration +
                    ", available_episodes=" + available_episodes +
                    ", total_episodes=" + total_episodes +
                    ", last_issue_date=" + last_issue_date +
                    ", publish_time=" + publish_time +
                    ", relevance_score=" + relevance_score +
                    ", role_info=" + role_info +
                    ", corresponding_id=" + corresponding_id +
                    ", sns_score=" + sns_score +
                    ", ad_publish_date=" + ad_publish_date +
                    ", prompt_description='" + prompt_description + '\'' +
                    ", kv_album_id=" + kv_album_id +
                    ", poster_url='" + poster_url + '\'' +
                    ", site_name=" + site_name +
                    ", qiyu_picture_url='" + qiyu_picture_url + '\'' +
                    ", qiyu_is_focus_valid=" + qiyu_is_focus_valid +
                    '}';
        }

        public void setTitle(String title) {
            this.title = title;
        }
        public String getTitle() {
            return title;
        }

        public void setQipu_id(long qipu_id) {
            this.qipu_id = qipu_id;
        }
        public long getQipu_id() {
            return qipu_id;
        }

        public void setChannel_id(int channel_id) {
            this.channel_id = channel_id;
        }
        public int getChannel_id() {
            return channel_id;
        }

        public void setTvid(long tvid) {
            this.tvid = tvid;
        }
        public long getTvid() {
            return tvid;
        }

        public void setQitan_id(long qitan_id) {
            this.qitan_id = qitan_id;
        }
        public long getQitan_id() {
            return qitan_id;
        }

        public void setThumbnail_url(String thumbnail_url) {
            this.thumbnail_url = thumbnail_url;
        }
        public String getThumbnail_url() {
            return thumbnail_url;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }
        public int getDuration() {
            return duration;
        }

        public void setAvailable_episodes(int available_episodes) {
            this.available_episodes = available_episodes;
        }
        public int getAvailable_episodes() {
            return available_episodes;
        }

        public void setTotal_episodes(int total_episodes) {
            this.total_episodes = total_episodes;
        }
        public int getTotal_episodes() {
            return total_episodes;
        }

        public void setLast_issue_date(DayTime last_issue_date) {
            this.last_issue_date = last_issue_date;
        }
        public DayTime getLast_issue_date() {
            return last_issue_date;
        }

        public void setPublish_time(DayTime publish_time) {
            this.publish_time = publish_time;
        }
        public DayTime getPublish_time() {
            return publish_time;
        }

        public void setRelevance_score(double relevance_score) {
            this.relevance_score = relevance_score;
        }
        public double getRelevance_score() {
            return relevance_score;
        }

        public void setRole_info(List<RoleInfo> role_info) {
            this.role_info = role_info;
        }
        public List<RoleInfo> getRole_info() {
            return role_info;
        }

        public void setCorresponding_id(long corresponding_id) {
            this.corresponding_id = corresponding_id;
        }
        public long getCorresponding_id() {
            return corresponding_id;
        }

        public void setSns_score(double sns_score) {
            this.sns_score = sns_score;
        }
        public double getSns_score() {
            return sns_score;
        }

        public void setAd_publish_date(long ad_publish_date) {
            this.ad_publish_date = ad_publish_date;
        }
        public long getAd_publish_date() {
            return ad_publish_date;
        }

        public void setPrompt_description(String prompt_description) {
            this.prompt_description = prompt_description;
        }
        public String getPrompt_description() {
            return prompt_description;
        }

        public void setKv_album_id(long kv_album_id) {
            this.kv_album_id = kv_album_id;
        }
        public long getKv_album_id() {
            return kv_album_id;
        }

        public void setPoster_url(String poster_url) {
            this.poster_url = poster_url;
        }
        public String getPoster_url() {
            return poster_url;
        }

        public void setSite_name(List<String> site_name) {
            this.site_name = site_name;
        }
        public List<String> getSite_name() {
            return site_name;
        }

        public void setQiyu_picture_url(String qiyu_picture_url) {
            this.qiyu_picture_url = qiyu_picture_url;
        }
        public String getQiyu_picture_url() {
            return qiyu_picture_url;
        }

        public void setQiyu_is_focus_valid(boolean qiyu_is_focus_valid) {
            this.qiyu_is_focus_valid = qiyu_is_focus_valid;
        }
        public boolean getQiyu_is_focus_valid() {
            return qiyu_is_focus_valid;
        }

    }

}