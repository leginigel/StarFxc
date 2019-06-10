package com.stars.tv.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 数据库管理类
 */
public class DBManager extends SQLiteOpenHelper {

    private static final String DB_NAME = "television.db";
    private static final String PACKAGE_NAME = "com.stars.tv";
    private static final int DATABASE_VERSION = 1;
    private static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" +
        PACKAGE_NAME+"/databases";
    private static DBManager sInstance;

    public DBManager(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DBManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBManager(context.getApplicationContext());
        }
        return sInstance;
    }

    public static boolean initDatabase(Context context) {

        try {

            File file = new File(DB_PATH + "/" + DB_NAME);
            File dbPath = new File(DB_PATH);
            if (!dbPath.exists())
                dbPath.mkdirs();
            if (file.exists())
                file.deleteOnExit();
            if (!file.exists())
                file.createNewFile();

            FileOutputStream out = new FileOutputStream(DB_PATH + "/" + DB_NAME);
            InputStream in = context.getResources().getAssets().open(DB_NAME);
            byte[] buffer = new byte[400000];
            int readBytes;
            while ((readBytes = in.read(buffer)) != -1)
                out.write(buffer, 0, readBytes);
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}


