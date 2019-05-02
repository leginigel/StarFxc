package com.stars.tv.bean;

import java.io.Serializable;

public class IQiYiListBean implements Serializable {

    /**
     channel:{电视剧:2, 电影:1, 综艺:6, 动漫:4, 纪录片:3, 游戏:8, 资讯:25, 娱乐:7,财经:24,
                网络电影:16, 片花:10, 音乐:5, 军事:28, 教育:12, 体育:17, 儿童:15,旅游:9, 时尚:13,
                生活:21, 汽车:26, 搞笑:22, 广告:20, 原创:27, 母婴:29, 科技:30, 脱口秀:31, 健康:32}
     charge:{全部:null, 免费:0, 付费:2}
     category(电影):{全部:null,华语:1, 香港28997,美国2, 欧洲3, 韩国14, 日本308, 泰国1115, 印度28999, 其它5}
     category(电视):{全部:null,内地:15, 香港16, 韩国17, 美国18, 日本309, 泰国1114, 台湾1117, 英国28916, 其它19}
     type:{全部:null,自制:11992, 古装:24, 言情:20, 武侠:23, 偶像:30, 家庭:1654, 青春:1653, 都市:24064,
            喜剧:135, 战争:27916, 军旅:1655, 谍战:290, 悬疑:32, 罪案:149, 穿越:148,宫廷:139, 历史:21,
            神话:145, 科幻:34, 年代:27, 农村:29, 商战:140, 剧情:24063, 奇幻:27881, 网剧:24065}
     times:{全部:null,2019 2018 2017 2011_2016 2000_2010 1990_1999 1980_1989 1964_1979}
     sort:{综合排序:24, 热门:11, 更新时间:4}
     searchScope：{爱奇艺:iqiyi,全网:null；}
    */

    /**
     * 全部：http://list.iqiyi.com/www/channel/category-type-publishCategory-brandArea-newType-style-thirdType-fourthType---charge-times--sort-page-videoType-searchScope-userUpload-serialState.html
     * 电视: http://list.iqiyi.com/www/channel/category-type---------charge-times--sort-page-1-searchScope--.html
     * 电影: http://list.iqiyi.com/www/channel/category-type-----standard----charge-times--sort-page-1-searchScope--.html
     * 综艺：http://list.iqiyi.com/www/channel/category-type------------sort-page-1-searchScope--.html
     * 动漫：http://list.iqiyi.com/www/channel/category--publishCategory--newType-style-----charge---sort-page-1-searchScope--serialState.html
     */

    private String channel ="1";        //频道
    private String category ="";       //地区 分类
    private String type ="";           //类型
    private String publishCategory ="";       //版本 出版分类  动漫/纪录片
    private String newType ="";       //新类型(动漫) 时长，片种分类（纪录片)
    private String brandArea ="";    //品牌  汽车
    private String style ="";      //风格 动漫专用
    private String thirdType ="";      //分类
    private String fourthType ="";       //分类
    private String charge ="";        //资费
    private String times ="";       //年代
    private String sort ="";        //排序
    private String page ="1";           //页码
    private String videoType ="1";       //专辑/视频/直播
    private String searchScope ="iqiyi";; //搜索范围
    private String userUpload ="";;  //用户上传
    private String serialState ="";;       //连载状态 动漫使用

    public IQiYiListBean() {
    }

    public IQiYiListBean(String channel, String category, String type,String publishCategory, String newType, String brandArea,
                         String style,String thirdType,String fourthType, String charge, String times, String sort,
                         String page, String videoType,String searchScope, String userUpload,String serialState) {
        this.channel =channel;
        this.category =category;
        this.type =type;
        this.publishCategory =publishCategory;
        this.brandArea =brandArea;
        this.newType =newType;
        this.style =style;
        this.thirdType =thirdType;
        this.fourthType =fourthType;
        this.charge =charge;
        this.times = times;
        this.sort= sort;
        this.page= page;
        this.videoType= videoType;
        this.searchScope = searchScope;
        this.userUpload = userUpload;
        this.serialState = serialState;
    }


    /**
     * 电影示例
     * 电影: http://list.iqiyi.com/www/channel/category-type-----publishCategory----charge-times--sort-page-1-searchScope--.html
     * @param channel   频道
     * @param category  地区
     * @param type  类型
     * @param publishCategory 规格
     * @param charge    资费
     * @param times 我的年代
     * @param sort  排序
     * @param page  页码
     * @param searchScope   搜索范围 默认"iqiyi"
     *
     */
    public IQiYiListBean(String channel, String category, String type,String publishCategory, String charge,
                         String times, String sort, String page, String searchScope) {
        this.channel =channel;
        this.category =category;
        this.type =type;
        this.publishCategory =publishCategory;
        this.charge =charge;
        this.times = times;
        this.sort= sort;
        this.page= page;
        this.searchScope = searchScope;
    }

    //电视剧ListBean
    //http://list.iqiyi.com/www/channel/category-type---------charge-times--sort-page-1-searchScope--.html

    /**
     * 电视剧示例
     * 电视剧 http://list.iqiyi.com/www/channel/category-type---------charge-times--sort-page-1-searchScope--.html
     * @param channel   频道
     * @param category  地区
     * @param type  类型
     * @param charge    资费
     * @param times 我的年代
     * @param sort 排序
     * @param page 页码
     * @param searchScope 搜索范围 默认"iqiyi"
     */
    public IQiYiListBean(String channel, String category, String type, String charge, String times, String sort, String page, String searchScope) {
        this.channel =channel;
        this.category =category;
        this.type =type;
        this.charge =charge;
        this.times = times;
        this.sort= sort;
        this.page= page;
        this.searchScope = searchScope;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "channel='" + channel + '\'' +
                ", category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", publishCategory='" + publishCategory + '\'' +
                ", newType='" + newType + '\'' +
                ", brandArea='" + brandArea + '\'' +
                ", style='" + style + '\'' +
                ", thirdType='" + thirdType + '\'' +
                ", fourthType='" + fourthType + '\'' +
                ", charge='" + charge + '\'' +
                ", times='" + times + '\'' +
                ", sort='" + sort + '\'' +
                ", page='" + page + '\'' +
                ", videoType='" + videoType + '\'' +
                ", searchScope='" + searchScope + '\''+
                ", userUpload='" + userUpload + '\''+
                ", serialState='" + serialState + '\''+
                '}';
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getPublishCategory() {
        return publishCategory;
    }

    public void setPublishCategory(String publishCategory) {
        this.publishCategory = publishCategory;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getNewType() {
        return newType;
    }

    public void setNewType(String newType) {
        this.newType = newType;
    }

    public String getVideoType() {
        return videoType;
    }
    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getBrandArea() {
        return brandArea;
    }
    public void setBrandArea(String brandArea) {
        this.brandArea = brandArea;
    }

    public String getStyle() {
        return style;
    }
    public void setStyle(String style) {
        this.style = style;
    }

    public String getUserUpload() {
        return userUpload;
    }
    public void setUserUpload(String userUpload) {
        this.userUpload = userUpload;
    }

    public String getSerialState() {
        return serialState;
    }
    public void setSerialState(String serialState) {
        this.serialState = serialState;
    }

    public String getCharge() {
        return charge;
    }
    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getPage() {
        return page;
    }
    public void setPage(String page) {
        this.page = page;
    }

    public String getThirdType() {
        return thirdType;
    }
    public void setThirdType(String thirdType) {
        this.thirdType = thirdType;
    }

    public String getFourthType() {
        return fourthType;
    }
    public void setFourthType(String fourthType) {
        this.fourthType = fourthType;
    }


    public String getTimes() {
        return times;
    }
    public void setTimes(String times) {
        this.times = times;
    }


    public String getSort() {
        return sort;
    }
    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSearchScope() {
        return searchScope;
    }

    public void setSearchScope(String searchScope) {
        this.searchScope = searchScope;
    }

}
