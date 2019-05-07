
package com.stars.tv.bean;

import java.util.List;

public class IQiYiStarInfoBean {

    private String name;
    private String imageUrl;
    private String intro;
    private List<String> personalInfo;
    private List<String> morePersonalInfo;
    private List<Works> recommendList;
    private List<Category> allWorksList;
    private List<StarPicture> starPictureList;
    private List<RelateStar> relateStarList;

    @Override
    public String toString() {
        return "IQiYiStarInfo{" +
                "name='" + name + '\'' +
                ", imageUrl='" + imageUrl+ '\'' +
                ", intro='" + intro+ '\'' +
                ", personalInfo='" + personalInfo + '\'' +
                ", morePersonalInfo='" + morePersonalInfo + '\'' +
                ", recommendList='" + recommendList + '\'' +
                ", allWorksList='" + allWorksList + '\'' +
                ", starPictureList='" + starPictureList + '\'' +
                ", relateStarList='" + relateStarList + '\''+
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(List<String>  personalInfo) {
        this.personalInfo = personalInfo;
    }

    public List<Works> getRecommendList() {
        return recommendList;
    }

    public void setRecommendList(List<Works> recommendList) {
        this.recommendList = recommendList;
    }

    public List<Category> getAllWorksList() {
        return allWorksList;
    }

    public void setAllWorksList(List<Category> allWorksList) {
        this.allWorksList = allWorksList;
    }

    public List<StarPicture> getStarPictureList() {
        return starPictureList;
    }

    public void setStarPictureList(List<StarPicture> starPictureList) {
        this.starPictureList = starPictureList;
    }

    public List<RelateStar> getRelateStarList() {
        return relateStarList;
    }

    public void setRelateStarList(List<RelateStar> relateStarList) {
        this.relateStarList = relateStarList;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public List<String> getMorePersonalInfo() {
        return morePersonalInfo;
    }

    public void setMorePersonalInfo(List<String> morePersonalInfo) {
        this.morePersonalInfo = morePersonalInfo;
    }

    public class Category {

        private String type;
        private List<Works> worksList;

        @Override
        public String toString() {
            return "{" +
                    "type='" + type + '\'' +
                    ", worksList='" + worksList + '\'' +
                    '}';
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Works> getWorksList() {
            return worksList;
        }

        public void setWorksList(List<Works> worksList) {
            this.worksList = worksList;
        }
    }

    public class Works{
        private String type;
        private String name;
        private String playUrl;
        private String imageUrl;
        private String time;
        private String role;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        @Override
        public String toString() {
            return "{" +
                    "type='" + type + '\'' +
                    "，name='" + name + '\'' +
                    "，playUrl='" + playUrl + '\'' +
                    "，imageUrl='" + imageUrl + '\'' +
                    "，time='" + time + '\'' +
                    ", role='" + role + '\'' +
                    '}';
        }

        public String getPlayUrl() {
            return playUrl;
        }

        public void setPlayUrl(String playUrl) {
            this.playUrl = playUrl;
        }
    }

    public class StarPicture{
        private String name;
        private String imageUrl;
        private String clickNum;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getClickNum() {
            return clickNum;
        }

        public void setClickNum(String clickNum) {
            this.clickNum = clickNum;
        }

        @Override
        public String toString() {
            return "{" +
                    "name='" + name + '\'' +
                    "，imageUrl='" + imageUrl + '\'' +
                    ", clickNum='" + clickNum + '\'' +
                    '}';
        }

    }

    public class RelateStar{
        private String name;
        private String title;
        private String imageUrl;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }


        @Override
        public String toString() {
            return "{" +
                    "title='" + title + '\'' +
                    ", name='" + name + '\'' +
                    ", imageUrl='" + imageUrl + '\'' +
                    '}';
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}