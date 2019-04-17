package com.stars.tv.bean;

/**
 * 影视标题栏
 *
 * @Author: Dicks.yang
 * @Date: 2019.04.15
 */
public class TvTitle {

    int id;
    String name;

    public TvTitle(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public TvTitle(String name) {
        this.name = name;
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
}
