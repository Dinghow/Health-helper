package com.example.healthhelper.app;

/**
 * Created by Dinghow on 2017/10/7.
 */
import android.app.Application;
import android.content.Context;

/**
 * 全局应用程序上下文
 * 方便 Preference 或 Sqlite 获取 Context
 */

public class HealthHelperApp extends Application {


    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}

