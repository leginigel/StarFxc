package com.stars.tv.server;

import com.stars.tv.MyApplication;
import com.stars.tv.utils.NetUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Retrofit 工具
 *
 * @Author: Dicks.yang
 * @Date: 2019.04.24
 */

public class RetrofitFactory {

    //设缓存有效期为两天
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 *2;

    //缓存路径
    private static File cacheFile = new File(MyApplication.getContext().getCacheDir(), "HttpCache");

    private static Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); // 指定缓存大小100Mb

    //日志拦截器
    private static final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC);

    //缓存拦截器
    private static Interceptor cacheInterceptor = chain -> {
        Request request = chain.request();
        if(!NetUtil.isConnected()){
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        String cacheControl = request.cacheControl().toString();
        if(cacheControl.equals("")){
            cacheControl = "public, only-if-cached, max-age=" + CACHE_STALE_SEC;
        }
            return response.newBuilder()
                    .removeHeader("User-Agent")
                    .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/18.17763")
                .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
    };

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            //SSL证书
            .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
            .addInterceptor(interceptor) //日志拦截器
            .connectTimeout(15, TimeUnit.SECONDS) //连接超时
            .readTimeout(20, TimeUnit.SECONDS)  //读取超时
            .writeTimeout(20, TimeUnit.SECONDS) //写入超时
            .addNetworkInterceptor(cacheInterceptor)//添加缓存拦截器
            .cache(cache)//把缓存添加进来
            .retryOnConnectionFailure(true) //失败重连
            .build();

    public static <T> T createApi(Class<T> clazz, String url) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(clazz);
    }

}
