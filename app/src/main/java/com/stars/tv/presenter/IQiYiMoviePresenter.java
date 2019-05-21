package com.stars.tv.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stars.tv.bean.IQiYiMovieBean;
import com.stars.tv.bean.contract.IQiYiMovieContract;
import com.stars.tv.server.RxManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class IQiYiMoviePresenter extends IQiYiMovieContract.IQiYiMoviePresenter {

    @Override
    public void requestIQiYiMovie(String url) {
        RxManager.add(mModel.getIQiYiMovie(url).subscribe(responseBody -> {
            try {
                ArrayList<IQiYiMovieBean> simpleList;
                Document doc = Jsoup.parse(responseBody.string());
                if (null!=doc){
                    Elements block = doc.select("div[id=block-D]");
                    String result = block.attr(":first-search-list");
                    try {
                        JSONObject root = new JSONObject(result);
                        String movieList = root.getString("list");
                        Type listType = new TypeToken<List<IQiYiMovieBean>>() {}.getType();
                        simpleList = new Gson().fromJson(movieList, listType);
                        mView.returnIQiYiMovieList(simpleList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (IOException | StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }, Throwable -> mView.showError(Throwable.toString())));
    }
}
