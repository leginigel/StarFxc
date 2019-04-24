package com.stars.tv.presenter;

import com.stars.tv.bean.IQiYiMovieBean;
import com.stars.tv.bean.contract.IQiYiMovieContract;
import com.stars.tv.server.RxManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IQiYiMoviePresenter extends IQiYiMovieContract.IQiYiMoviePresenter {

    @Override
    public void requestIQiYiMovie(String url) {
        RxManager.add(mModel.getIQiYiMovie(url).subscribe(responseBody -> {
            try {
                ArrayList<IQiYiMovieBean> simpleList = new ArrayList<>();
                Document doc = Jsoup.parse(responseBody.string());
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
                    }
                }
                mView.returnIQiYiMovieList(simpleList);
            } catch (IOException | StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }, Throwable -> mView.showError(Throwable.toString())));
    }
}
