
package com.stars.tv.bean;

import java.util.List;

public class IQiYiSearchResultBean {

    private String resultNum;
    private List<ResultItem> itemList;

    public String getResultNum() {
        return resultNum;
    }

    public void setResultNum(String resultNum) {
        this.resultNum = resultNum;
    }
    public List<ResultItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<ResultItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public String toString() {
        return "{" +
                "resultNum='" + resultNum + '\'' +
                ", itemList=" + itemList +
                '}';
    }

    public class ResultItem{

        private String tvname;
        private String tvid;
        private String albumid;
        private String catageory;
        private String pagesize;
        private String playUrl;
        private String imageUrl;
        private String times;
        private String description;
        private List<SubItem> subItem;

        @Override
        public String toString() {
            return "{" +
                    "tvname='" + tvname + '\'' +
                    ", tvid='" + tvid + '\'' +
                    ", albumid='" + albumid + '\'' +
                    ", catageory='" + catageory + '\'' +
                    ", pagesize='" + pagesize + '\'' +
                    ", playUrl='" + playUrl + '\'' +
                    ", imageUrl='" + imageUrl + '\'' +
                    ", times='" + times + '\'' +
                    ", description='" + description + '\'' +
                    ", subItem=" + subItem +
                    '}';
        }

        public String getTvname() {
            return tvname;
        }

        public void setTvname(String tvname) {
            this.tvname = tvname;
        }

        public String getTvid() {
            return tvid;
        }

        public void setTvid(String tvid) {
            this.tvid = tvid;
        }

        public String getAlbumid() {
            return albumid;
        }

        public void setAlbumid(String albumid) {
            this.albumid = albumid;
        }

        public String getCatageory() {
            return catageory;
        }

        public void setCatageory(String catageory) {
            this.catageory = catageory;
        }

        public String getPagesize() {
            return pagesize;
        }

        public void setPagesize(String pagesize) {
            this.pagesize = pagesize;
        }

        public String getPlayUrl() {
            return playUrl;
        }

        public void setPlayUrl(String playUrl) {
            this.playUrl = playUrl;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<SubItem> getSubItem() {
            return subItem;
        }

        public void setSubItem(List<SubItem> subItem) {
            this.subItem = subItem;
        }
    }

    public class SubItem{

        private String subTitle;
        private String subPlayUrl;

        @Override
        public String toString() {
            return "{" +
                    "subTitle='" + subTitle + '\'' +
                    ", subPlayUrl='" + subPlayUrl + '\'' +
                    '}';
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getSubPlayUrl() {
            return subPlayUrl;
        }

        public void setSubPlayUrl(String subPlayUrl) {
            this.subPlayUrl = subPlayUrl;
        }
    }

}