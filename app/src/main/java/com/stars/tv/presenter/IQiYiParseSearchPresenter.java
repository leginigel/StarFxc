package com.stars.tv.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stars.tv.bean.IQiYiHotQueryBean;
import com.stars.tv.bean.IQiYiSearchSuggestBean;
import com.stars.tv.server.RetrofitFactory;
import com.stars.tv.server.RetrofitService;
import com.stars.tv.server.RxManager;
import com.stars.tv.utils.CallBack;
import com.stars.tv.utils.Constants;
import com.stars.tv.utils.RxUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class IQiYiParseSearchPresenter {

    private Observable<ResponseBody> getIQiYiSearchQueryUrl(String queryType) {
        return RetrofitFactory.createApi(RetrofitService.class, Constants.BASE_IQIYI_SEARCH_QUERY_URL)
                .getIQiYiSearchHotQueryWord(queryType).compose(RxUtils.rxSchedulerHelper());
    }

    private Observable<ResponseBody> getIQiYiSearchSuggestWordUrl(String keyWord,int resultNum) {
        return RetrofitFactory.createApi(RetrofitService.class, Constants.BASE_IQIYI_SEARCH_SUGGEST_WORD_URL)
                .getIQiYiSearchSuggestWord(keyWord,resultNum).compose(RxUtils.rxSchedulerHelper());
    }


    public void requestIQiYiSearchHotQueryWord( CallBack<List<IQiYiHotQueryBean>> listener) {
        RxManager.add(getIQiYiSearchQueryUrl("hotQuery").subscribe(responseBody -> {
            List<IQiYiHotQueryBean> hotQueryList;
            try {
                JSONObject root = new JSONObject(responseBody.string());
                boolean isEmpty = root.getBoolean("is_empty");
                Log.v("ttt","isEmpty"+isEmpty);
                if (isEmpty) {
                    //error
                    listener.error("返回值错误");
                }

                String data = root.getString("data");
                Type listType = new TypeToken<List<IQiYiHotQueryBean>>() {}.getType();
                hotQueryList = new Gson().fromJson(data, listType);

                if (listener != null) {
                    listener.success(hotQueryList);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        },Throwable ->listener.error(Throwable.toString())));
    }

    public void requestIQiYiSearchSuggestWord(String keyWord,int resultNum, CallBack<List<IQiYiSearchSuggestBean>> listener) {
        RxManager.add(getIQiYiSearchSuggestWordUrl(keyWord,resultNum).subscribe(responseBody -> {
            List<IQiYiSearchSuggestBean> suggestList;
            try {
                JSONObject root = new JSONObject(responseBody.string());
                String code = root.getString("code");
                if (!code.equals("A00000")) {
                    //error
                    listener.error("返回值错误");
                }

                String data = root.getString("data");
                Type listType = new TypeToken<List<IQiYiSearchSuggestBean>>() {}.getType();
                suggestList = new Gson().fromJson(data, listType);

                if (listener != null) {
                    listener.success(suggestList);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        },Throwable ->listener.error(Throwable.toString())));
    }

}
