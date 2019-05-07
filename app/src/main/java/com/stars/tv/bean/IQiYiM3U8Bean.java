
package com.stars.tv.bean;

public class IQiYiM3U8Bean {

    private String m3utx;
    private int ists;
    private String m3u;
    private int vd;
    private String screenSize;

    public void setM3utx(String m3utx) {
         this.m3utx = m3utx;
     }
     public String getM3utx() {
         return m3utx;
     }

    public void setIsts(int ists) {
         this.ists = ists;
     }
     public int getIsts() {
         return ists;
     }

    public void setM3u(String m3u) {
         this.m3u = m3u;
     }
     public String getM3u() {
         return m3u;
     }

    public void setVd(int vd) {
         this.vd = vd;
     }
     public int getVd() {
         return vd;
     }

    public void setScreenSize(String screenSize) {
         this.screenSize = screenSize;
     }
     public String getScreenSize() {
         return screenSize;
     }

    @Override
    public String toString() {
        return "IQiYiM3U8Bean{" +
                "m3utx='" + m3utx + '\'' +
                ", ists='" + ists + '\'' +
                ", m3u='" + m3u + '\'' +
                ", vd='" + vd + '\'' +
                ", screenSize='" + screenSize + '\''+
                '}';
    }

}