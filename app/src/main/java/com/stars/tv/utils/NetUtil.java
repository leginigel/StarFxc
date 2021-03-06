package com.stars.tv.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.stars.tv.MyApplication;

import java.io.IOException;

/**
 * 网络工具类
 *
 * @Author: Dicks.yang
 * @Date: 2019.04.24
 */
public class NetUtil {
    private NetUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断网络是否连接
     */
    public static boolean isConnected() {

        ConnectivityManager connectivity = (ConnectivityManager) MyApplication.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                return info.getState() == NetworkInfo.State.CONNECTED;
            }
        }
        return false;
    }

    public static boolean isAvailableByPing() {
        String ip = "www.baidu.com";
        Runtime runtime = Runtime.getRuntime();
        Process ipProcess = null;
        try {
            //-c 后边跟随的是重复的次数，-w后边跟随的是超时的时间，单位是秒，不是毫秒，要不然也不会anr了
            ipProcess = runtime.exec("ping -c 3 -w 2 "+ip);
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (ipProcess != null) {
                ipProcess.destroy();
            }
            runtime.gc();
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifiConnected() {
        ConnectivityManager cm = (ConnectivityManager) MyApplication.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

    /**
     * 打开网络设置
     */
    public static void openWiFiSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }
}
