package com.stars.tv.utils;


import android.content.Context;
import android.content.SharedPreferences;

import java.time.format.DateTimeFormatterBuilder;
import java.util.Formatter;

import static android.content.Context.MODE_PRIVATE;

/**
 *
 * @Author: Dicks.yang
 * @Date: 2019.05.07
 */

public class Utils {

    public static Object getSharedValue(Context context, String key, Object defVal)
    {
        SharedPreferences shared = context.getSharedPreferences("share", MODE_PRIVATE);
        if(defVal instanceof String)
        {
            return shared.getString(key,(String)defVal);
        }else if(defVal instanceof Boolean)
        {
            return shared.getBoolean(key, (boolean)defVal);
        }else if(defVal instanceof Integer)
        {
            return shared.getInt(key, (int)defVal);
        }
        return defVal;
    }


    public static void setSharedValue(Context context, String key, Object value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences("share", MODE_PRIVATE).edit();

        if(value instanceof String)
        {
            editor.putString(key,(String)value);
        }else if(value instanceof Boolean)
        {
            editor.putBoolean(key, (boolean)value);
        }else if(value instanceof Integer)
        {
            editor.putInt(key, (int)value);
        }
        editor.apply();
    }
    public static String stringForTime(int timeMs) {
        StringBuilder FormatBuilder = new StringBuilder();
        Formatter Formatter = new Formatter();

        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;

        FormatBuilder.setLength(0);
        if (hours > 0) {
            return Formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return Formatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
}
