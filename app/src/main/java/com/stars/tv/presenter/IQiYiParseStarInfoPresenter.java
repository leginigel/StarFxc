package com.stars.tv.presenter;

import com.stars.tv.bean.IQiYiStarInfoBean;
import com.stars.tv.server.RetrofitFactory;
import com.stars.tv.server.RetrofitService;
import com.stars.tv.server.RxManager;
import com.stars.tv.utils.CallBack;
import com.stars.tv.utils.Constants;
import com.stars.tv.utils.RxUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class IQiYiParseStarInfoPresenter {

    private Observable<ResponseBody> getIQiYiBaseUrl(String url) {
        return RetrofitFactory.createApi(RetrofitService.class, Constants.BASE_IQIYI_URL)
                .getIQiYiStarsInfo(url).compose(RxUtils.rxSchedulerHelper());
    }

    public void requestIQiYiStarsInfo(String url, CallBack<IQiYiStarInfoBean> listener) {

        RxManager.add(getIQiYiBaseUrl(url).subscribe(responseBody -> {
            Document doc = Jsoup.parse(responseBody.string());
            IQiYiStarInfoBean starInfo = new IQiYiStarInfoBean();
            Elements base = doc.select("div[itemtype = http://schema.org/Person]");
            Elements more = doc.select("div.site-main-outer");

            // 明星姓名和海报
            String imageUrl = "http:"+base.select("div.result_pic").select("img").attr("src");
            String name = base.select("div.result_detail").select("h1").text();
            String intro = more.select("p.introduce-info").text();
            starInfo.setName(name);
            starInfo.setImageUrl(imageUrl);
            starInfo.setIntro(intro);

            // 明星个人信息
            List<String> personalInfo = new ArrayList<>();
            Elements personalLis = base.select("div.mx_topic-item").select("li");
            for(Element element : personalLis)
            {
                personalInfo.add(element.text());
            }
            starInfo.setPersonalInfo(personalInfo);

            // 明星更多个人信息
            List<String> morePersonalInfo = new ArrayList<>();
            Elements mPerKeyLis = more.select("div.basic-info").select("dt");
            Elements mPerValueLis = more.select("div.basic-info").select("dd");
            for(int i = 0; i< mPerKeyLis.size();i++)
            {
                morePersonalInfo.add(mPerKeyLis.get(i).text()+" : "+mPerValueLis.get(i).text());
            }

            starInfo.setMorePersonalInfo(morePersonalInfo);

            // 明星推荐作品
            List<IQiYiStarInfoBean.Works> recommendList = new ArrayList<>();
            Elements recommendLis = base.select("div.wrapper-piclist").select("li");
            for(Element element : recommendLis)
            {
                IQiYiStarInfoBean.Works recWork = starInfo.new Works();
                String recName = element.select("p.site-piclist_info_title").select("a").text();
                String recImageUrl = "http:"+element.select("a.site-piclist_pic_link").select("img").attr("src");
                String recPlayUrl = "http:"+element.select("a.site-piclist_pic_link").attr("href");
                String recType = element.select("a.site-piclist_pic_link").select("span.mod-listTitle_left").text();
                recWork.setName(recName);
                recWork.setImageUrl(recImageUrl);
                recWork.setPlayUrl(recPlayUrl);
                recWork.setType(recType);
                recommendList.add(recWork);
            }
            starInfo.setRecommendList(recommendList);

            // 明星影视作品
            List<IQiYiStarInfoBean.Category> allWorksList = new ArrayList<>();
            Elements allLis = more.select("div[data-slider-wrap=works]");

            for(Element element : allLis)
            {
                IQiYiStarInfoBean.Category category = starInfo.new Category();
                String type = element.attr("data-works-type");
                category.setType(type);
                Elements typeLis = element.select("div.wrapper-piclist").select("li");
                List<IQiYiStarInfoBean.Works> typeWorksList = new ArrayList<>();
                for(Element typeElement : typeLis)
                {
                    IQiYiStarInfoBean.Works work = starInfo.new Works();
                    String typeName =typeElement.select("a.site-piclist_pic_link").select("img").attr("title");
                    String typePlayUrl ="http:"+typeElement.select("a.site-piclist_pic_link").attr("href");
                    String typeImageUrl = "http:"+typeElement.select("a.site-piclist_pic_link").select("img").attr("src");
                    String typeTime;
                    String typeRole = "";
                    if(type.equals("zongyi")||type.equals("dongman"))
                    {
                        typeTime =typeElement.select("p.site-piclist_info_describe").select("a").text();
                    }else
                    {
                        typeTime = typeElement.select("a.c999").text();
                        typeRole = typeElement.select("p.site-piclist_info_describe").text();
                    }
                    work.setType(type);
                    work.setName(typeName);
                    work.setPlayUrl(typePlayUrl);
                    work.setImageUrl(typeImageUrl);
                    work.setTime(typeTime);
                    work.setRole(typeRole);
                    typeWorksList.add(work);
//                    Log.v("tttt","work"+work);
                }
                category.setWorksList(typeWorksList);
                allWorksList.add(category);
            }
            starInfo.setAllWorksList(allWorksList);

            // 明星图片
            List<IQiYiStarInfoBean.StarPicture> picList = new ArrayList<>();
            Elements picLis = more.select("ul.piclist-flex-wrapper").select("a.piclist-flex-link");
            Elements picLikeNum = more.select("ul.piclist-flex-wrapper").select("a.thumb-link");
            for(int i = 0;i<picLis.size();i++)
            {
                IQiYiStarInfoBean.StarPicture picture = starInfo.new StarPicture();
                picture.setName(picLis.get(i).select("img").attr("title"));
                picture.setImageUrl(picLis.get(i).select("img").attr("data-paopao-picurl"));
                picture.setClickNum(picLikeNum.get(i).select("span.thumb-num").text());
                picList.add(picture);
            }
            starInfo.setStarPictureList(picList);

            // 明星关系图谱
            List<IQiYiStarInfoBean.RelateStar> relateList = new ArrayList<>();
            Elements relate = more.select("div.center-star");
            Elements relatedNameList = relate.select("p");
            Elements relateLis = more.select("a.sub-star-img");
            IQiYiStarInfoBean.RelateStar relateStar0 = starInfo.new RelateStar();
            relateStar0.setName(relate.select("img").attr("alt"));
            relateStar0.setImageUrl(relate.select("img").attr("src"));
            relateList.add(relateStar0);

            for(int i = 0;i<relateLis.size();i++)
            {
                IQiYiStarInfoBean.RelateStar relateStar = starInfo.new RelateStar();
                relateStar.setTitle(relatedNameList.get(i+1).text());
                relateStar.setName(relateLis.get(i).select("img").attr("alt"));
                relateStar.setImageUrl(relateLis.get(i).select("img").attr("src"));
                relateList.add(relateStar);
            }
            starInfo.setRelateStarList(relateList);

            if (listener != null) {
                listener.success(starInfo);
            }
        }, Throwable ->listener.error(Throwable.toString())));
    }
}
