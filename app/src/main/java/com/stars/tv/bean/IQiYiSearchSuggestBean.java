
package com.stars.tv.bean;

import java.util.List;

public class IQiYiSearchSuggestBean  extends IQiYiSearchBaseBean {

    private int aid;
    private String name;
    private String link;
    private String picture_url;
    private int cid;
    private String cname;
    private int is_purchase;
    private String region;
    private int year;
    private int duration;
    private long vid;
    private String normalize_query;
    private double final_score;
    private boolean is_album_log;
    private List<String> director;
    private List<String> main_actor;
    private List<String> show_title;
    private List<String> link_address;

    @Override
    public String toString() {
        return "{" +
                "aid=" + aid +
                ", name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", picture_url='" + picture_url + '\'' +
                ", cid=" + cid +
                ", cname='" + cname + '\'' +
                ", is_purchase=" + is_purchase +
                ", region='" + region + '\'' +
                ", year=" + year +
                ", duration=" + duration +
                ", vid=" + vid +
                ", normalize_query='" + normalize_query + '\'' +
                ", final_score=" + final_score +
                ", is_album_log=" + is_album_log +
                ", director=" + director +
                ", main_actor=" + main_actor +
                ", show_title=" + show_title +
                ", link_address=" + link_address +
                '}';
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public int getIs_purchase() {
        return is_purchase;
    }

    public void setIs_purchase(int is_purchase) {
        this.is_purchase = is_purchase;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getVid() {
        return vid;
    }

    public void setVid(long vid) {
        this.vid = vid;
    }

    public String getNormalize_query() {
        return normalize_query;
    }

    public void setNormalize_query(String normalize_query) {
        this.normalize_query = normalize_query;
    }

    public double getFinal_score() {
        return final_score;
    }

    public void setFinal_score(double final_score) {
        this.final_score = final_score;
    }

    public boolean isIs_album_log() {
        return is_album_log;
    }

    public void setIs_album_log(boolean is_album_log) {
        this.is_album_log = is_album_log;
    }

    public List<String> getDirector() {
        return director;
    }

    public void setDirector(List<String> director) {
        this.director = director;
    }

    public List<String> getMain_actor() {
        return main_actor;
    }

    public void setMain_actor(List<String> main_actor) {
        this.main_actor = main_actor;
    }

    public List<String> getShow_title() {
        return show_title;
    }

    public void setShow_title(List<String> show_title) {
        this.show_title = show_title;
    }

    public List<String> getLink_address() {
        return link_address;
    }

    public void setLink_address(List<String> link_address) {
        this.link_address = link_address;
    }

    @Override
    public String getQueryName() {
        return name;
    }
}