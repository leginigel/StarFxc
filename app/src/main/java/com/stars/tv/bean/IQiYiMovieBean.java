package com.stars.tv.bean;


import java.io.Serializable;
import java.util.List;


public class IQiYiMovieBean implements Serializable {

    private String qPuId;
    private String name;
    private String url;
    private String score;
    private String time;
    private String posterUrl;
    private String posterLdUrl;
    private List<Role> roles;
    /**
     * 显示VIP或者用券的URL，为空时不显示
     */
    private String ltUrl;
    /**
     * 是否显示独播
     */
    private boolean isDJ;

    @Override
    public String toString() {
        return "Movie{" +
                "qPuId='" + qPuId + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", score='" + score + '\'' +
                ", time='" + time + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", posterLdUrl='" + posterLdUrl + '\'' +
                ", ltUrl='" + ltUrl + '\'' +
                ", isDJ='" + isDJ + '\'' +
                ", roles=" + roles.toString() +
                '}';
    }

    public String getqPuId() {
        return qPuId;
    }

    public void setqPuId(String qPuId) {
        this.qPuId = qPuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosterLdUrl() {
        return posterLdUrl;
    }

    public void setPosterLdUrl(String posterLdUrl) {
        this.posterLdUrl = posterLdUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLtUrl(String ltUrl) {
        this.ltUrl = ltUrl;
    }
    public String getLtUrl() {
        return ltUrl;
    }

    public void setDJ(boolean isDJ) {
        this.isDJ = isDJ;
    }
    public boolean isDJ() {
        return isDJ;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }


    public class Role{

        public Role() {}

        private String name;
        private String url;

        @Override
        public String toString() {
            return "Role{" +
                    "name='" + name + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }

        public Role(String name, String url) {
            this.name = name;
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
