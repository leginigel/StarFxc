
package com.stars.tv.bean;

public class IQiYiHotQueryBean {

    private String query;
    private int impression_count;
    private int click_count;
    private int search_trend;
    private int order;


    @Override
    public String toString() {
        return "{" +
                "query='" + query + '\'' +
                ", impression_count=" + impression_count +
                ", click_count=" + click_count +
                ", search_trend=" + search_trend +
                ", order=" + order +
                '}';
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getImpression_count() {
        return impression_count;
    }

    public void setImpression_count(int impression_count) {
        this.impression_count = impression_count;
    }

    public int getClick_count() {
        return click_count;
    }

    public void setClick_count(int click_count) {
        this.click_count = click_count;
    }

    public int getSearch_trend() {
        return search_trend;
    }

    public void setSearch_trend(int search_trend) {
        this.search_trend = search_trend;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}