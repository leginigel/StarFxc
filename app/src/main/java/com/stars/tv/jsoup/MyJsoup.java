package com.stars.tv.jsoup;

import android.os.Message;
import android.util.Log;

import com.stars.tv.bean.IQiYiListBean;
import com.stars.tv.bean.IQiYiMovieBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MyJsoup {

    private String url = "http://list.iqiyi.com";

    private boolean isOK = false;

    private requestEndListener mRequestEndListener;
    private List<IQiYiMovieBean> simpleList=new ArrayList<>();


    /*
    * 启动下载线程
    * */
    public void start(){

        new LoadThread().start();
    }
    public void setUrl(IQiYiListBean listBean){
        url = "http://list.iqiyi.com/www/"+
                listBean.getChannel()+"/" +
                listBean.getCategory() + "-" +
                listBean.getType() + "-" +
                listBean.getPublishCategory() + "-" +
                listBean.getBrandArea() + "-" +
                listBean.getNewType() + "-" +
                listBean.getStyle() + "-" +
                listBean.getThirdType() + "-" +
                listBean.getFourthType() + "---" +
                listBean.getCharge() + "-" +
                listBean.getTimes() + "--" +
                listBean.getSort() + "-" +
                listBean.getPage() + "-" +
                listBean.getVideoType() + "-" +
                listBean.getSearchScope() + "-" +
                listBean.getUserUpload() + "-" +
                listBean.getSerialState() + ".html";
    }

  //新线程
    private class LoadThread extends Thread{

        @Override
        public void run() {
            super.run();
            //发起请求
            Log.v("url=",url);
            Document doc = getRequestAddHeader(url);
            if (null!=doc){

                Elements lis = doc.select("div.wrapper-piclist").select("li");
                if(lis.size()!=0)
                {
                    for (Element element : lis) {
                        IQiYiMovieBean movie = new IQiYiMovieBean();
                        String qPuId = element.select("a.site-piclist_pic_link").attr("data-qipuid");
                        String link = element.select("a.site-piclist_pic_link").attr("href");
                        String name = element.select("a.site-piclist_pic_link").attr("title");
                        String time = element.select("span.icon-vInfo").text().trim();
                        String posterUrl = "http:"+element.select("img").attr("src").replace("180_236","260_360");
                        String posterLdUrl = posterUrl.replace("260_360","480_270");
                        String score= element.select("span.score").text();

                        String ltUrl = element.select("span.icon-member-box").select("img").attr("src");
                        boolean isDj = !element.select("p.video_dj").isEmpty();
                        movie.setScore(score);
                        movie.setqPuId(qPuId);
                        movie.setName(name);
                        movie.setUrl(link);
                        movie.setTime(time);
                        movie.setPosterLdUrl(posterLdUrl);
                        movie.setPosterUrl(posterUrl);
                        movie.setLtUrl(ltUrl.isEmpty()?"":"http:"+ltUrl);
                        movie.setDJ(isDj);

                        Elements rolesEm = element.select("div.role_info").select("a");
                        List<IQiYiMovieBean.Role> roles = new ArrayList<>();
                        for (Element em : rolesEm) {
                            IQiYiMovieBean.Role role = movie.new Role();
                            String roleHref = em.attr("href").trim();
                            String roleName = em.text().trim();
                            role.setName(roleName);
                            role.setUrl(roleHref);
                            roles.add(role);
                        }
                        movie.setRoles(roles);
                        simpleList.add(movie);
                    }
                    isOK = true;
                }
            }

            Message message=new Message();
            message.obj=simpleList;
            message.what=1;
            mRequestEndListener.onRequestEnd(message.obj,isOK);
        }
    }

    /*
    * jsoup添加header
    * */
    private Document getRequestAddHeader(String url) {

        Document doc = null;
        try {

            doc = Jsoup.connect(url)
                    .header("Accept", "*/*")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                    .header("Referer", "https://www.baidu.com/")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
                    .timeout(60000)
                    .maxBodySize(0)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    /*
    * 请求完成后的监听
    * */
    public interface requestEndListener{
        void onRequestEnd(Object movieList, boolean isSuccess);
    }

    public void setRequestListener(requestEndListener listener){
        this.mRequestEndListener=listener;
    }


}
