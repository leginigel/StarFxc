
package com.stars.tv.bean;

public class IQiYiHotSearchItemBean {

    private String id;
    private String name;
    private String searchUrl;
    private String yesterdayIndex;
    private String yesterdayStatus;
    private String weekIndex;
    private String weekStatus;
    private String monthIndex;

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ",name='" + name+ '\'' +
                ",searchUrl='" + searchUrl+ '\'' +
                ",yesterdayIndex='" + yesterdayIndex+ '\'' +
                ",yesterdayStatus='" + yesterdayStatus+ '\'' +
                ",weekIndex='" + weekIndex + '\'' +
                ",weekStatus='" + weekStatus + '\'' +
                ",monthIndex='" + monthIndex + '\''+
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYesterdayIndex() {
        return yesterdayIndex;
    }

    public void setYesterdayIndex(String yesterdayIndex) {
        this.yesterdayIndex = yesterdayIndex;
    }

    public String getWeekIndex() {
        return weekIndex;
    }

    public void setWeekIndex(String weekIndex) {
        this.weekIndex = weekIndex;
    }

    public String getMonthIndex() {
        return monthIndex;
    }

    public void setMonthIndex(String monthIndex) {
        this.monthIndex = monthIndex;
    }

    public String getYesterdayStatus() {
        return yesterdayStatus;
    }

    public void setYesterdayStatus(String yesterdayStatus) {
        this.yesterdayStatus = yesterdayStatus;
    }

    public String getWeekStatus() {
        return weekStatus;
    }

    public void setWeekStatus(String weekStatus) {
        this.weekStatus = weekStatus;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }
}