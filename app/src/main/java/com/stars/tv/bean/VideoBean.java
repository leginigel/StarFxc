package com.stars.tv.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 影视数据
 *
 * @Author: Dicks.yang
 * @Date: 2019.04.15
 */
public class VideoBean implements Serializable {

    int id;
    String name = "未知";
    String introduction; // 描述
    String director; // 导演
    String actor; //演员
    String released_time;
    String area;
    String poster_url;
    String cover_url;
    String sort;
    String type;

    @Override
    public String toString() {
        return "VideoBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", introduction='" + introduction + '\'' +
                ", director='" + director + '\'' +
                ", actor='" + actor + '\'' +
                ", released_time='" + released_time + '\'' +
                ", area='" + area + '\'' +
                ", poster_url='" + poster_url + '\'' +
                ", cover_url='" + cover_url + '\'' +
                ", sort='" + sort + '\'' +
                ", type=" + type +
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

    public String getIntroduction() {
        return !TextUtils.isEmpty(introduction) ? introduction : "未知";
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getDirector() {
        return !TextUtils.isEmpty(director) ? director : "未知";
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return !TextUtils.isEmpty(actor) ? actor : "未知";
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getReleased_time() {
        return released_time;
    }

    public void setReleased_time(String released_time) {
        this.released_time = released_time;
    }

    public String getArea() {
        return !TextUtils.isEmpty(area) ? area : "未知";
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getSort() {
        return !TextUtils.isEmpty(sort) ? sort : "未知";
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getType() {
        return !TextUtils.isEmpty(type) ? type: "未知";
    }

    public void setType(String type) {
        this.type = type;
    }

}
