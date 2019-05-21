package com.stars.tv.bean;

import java.util.List;


public class IQiYiMovieSimplifiedBean {

        private int result_num;
        private int total;
        private int pageTotal;
        private List<IQiYiMovieBean> list;

    @Override
    public String toString() {
        return "{" +
                "result_num=" + result_num +
                ", total=" + total +
                ", pageTotal=" + pageTotal +
                ", list=" + list +
                '}';
    }

    public int getResult_num() {
            return result_num;
        }

        public void setResult_num(int result_num) {
            this.result_num = result_num;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPageTotal() {
            return pageTotal;
        }

        public void setPageTotal(int pageTotal) {
            this.pageTotal = pageTotal;
        }

        public List<IQiYiMovieBean> getList() {
            return list;
        }

        public void setList(List<IQiYiMovieBean> list) {
            this.list = list;
        }
}
