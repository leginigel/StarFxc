package com.stars.tv.sample;

import com.stars.tv.bean.VideoBean;
import com.stars.tv.fragment.DragTabCommonFragment;

import java.util.ArrayList;
import java.util.List;

public class DragFavoriteSampleDataList {
  public static List<VideoBean> setupMovies() {
    List<VideoBean> list = new ArrayList<>();
    String title[] = {
      "Phone", "Tablet", "NoteBook", "Server", "Apple",
      "Glasses", "Tablet", "NoteBook", "Server", "Apple",
      "Mac", "Phone", "Tablet", "Wear","Watches",
      "PC", "NoteBook", "Server", "Apple", "Mac",
      "Phone", "Tablet", "NoteBook", "Server", "Apple"
    };

    for ( int i = 0 ; i < title.length ; i++ ){
      list.add(buildMovieInfo(title[i], DragTabCommonFragment.FAVORITE_FRAGMENT_ID + i, "0", "0"));
    }

    return list;
  }

  private static VideoBean buildMovieInfo(String title, String description,
                                          String videoUrl, String bgImageUrl) {
    VideoBean videoBean = new VideoBean();
    videoBean.setName(title);
    videoBean.setIntroduction(description);
    videoBean.setCover_url(bgImageUrl);
    videoBean.setPoster_url(videoUrl);
    return videoBean;
  }
}
