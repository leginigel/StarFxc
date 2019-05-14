package com.stars.tv.bean;

/**
 *
 * @Author: Dicks.yang
 * @Date: 2019.05.07
 */
public class IQiYiListBean {


    /**
     * http://list.iqiyi.com/www/channel/order1-order2-order3-order4-order5-order6-order7-order8---payStatus-myYear--sortType-page-dataType-siteType-sourceType-comicsStatus.html
     */

    private int channel;             //频道
    private String[] orderList;     //地区,类型,版本,新类型,风格,规格等
    private String payStatus;           //资费
    private String myYear;              //年代
    private int sortType;            //排序
    private int pageNum;             //页码
    private int dataType;            //专辑/视频/直播
    private String siteType;            //搜索范围
    private int sourceType;          //用户上传
    private String comicsStatus;        //连载状态 动漫使用


    public IQiYiListBean(int channel, String[] orderList, String payStatus, String myYear, int sortType,
                         int pageNum, int dataType, String siteType, int sourceType, String comicsStatus) {
        this.channel =channel;
        this.orderList =orderList;
        this.payStatus = payStatus;
        this.myYear = myYear;
        this.sortType = sortType;
        this.pageNum = pageNum;
        this.dataType = dataType;
        this.siteType = siteType;
        this.sourceType = sourceType;
        this.comicsStatus = comicsStatus;
    }

    public int getChannel() {
        return channel;
    }

    public String[] getOrderList() {
        return orderList;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public String getMyYear() {
        return myYear;
    }

    public int getSortType() {
        return sortType;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getDataType() {
        return dataType;
    }

    public String getSiteType() {
        return siteType;
    }

    public int getSourceType() {
        return sourceType;
    }

    public String getComicsStatus() {
        return comicsStatus;
    }
}
