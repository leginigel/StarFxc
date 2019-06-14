
package com.stars.tv.bean;

import java.util.List;

public class IQiYiSearchMovieBean {

    private int result_num;
    private int page_num;
    private int page_size;
    private int max_result_number;
    private List<IQiYiMovieBean> movieList;

    @Override
    public String toString() {
        return "IQiYiSearchMovieBean{" +
                "result_num=" + result_num +
                ", page_num=" + page_num +
                ", page_size=" + page_size +
                ", max_result_number=" + max_result_number +
                ", movieList=" + movieList +
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

    public List<IQiYiMovieBean> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<IQiYiMovieBean> movieList) {
        this.movieList = movieList;
    }
}