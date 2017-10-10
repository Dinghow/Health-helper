package com.example.healthhelper.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.healthhelper.app.HealthHelperApp;

/**
 * SharedPreference工具集合
 * Created by Dinghow on 2017/10/7.
 */

public class PreUtils {
    private static final int DEF_INT_VALUE = 2;
    private static final String DEF_STR_VALUE = "0";
    private static final boolean DEF_BOOL_VALUE = false;

    private static SharedPreferences getSharedPreferences(String name){
        return HealthHelperApp.getContext().getSharedPreferences(name,Context.MODE_PRIVATE);
    }

    public static int getInt(String name,String key){
        return getSharedPreferences(name).getInt(key,DEF_INT_VALUE);
    }

    public static void putInt(String name,String key,int value){
        SharedPreferences.Editor editor = getSharedPreferences(name).edit();
        editor.putInt(key,value);
        editor.commit();
        return;
    }

    public static String getString(String name,String key){
        return getSharedPreferences(name).getString(key,DEF_STR_VALUE);
    }

    public static void putString(String name,String key,String value){
        SharedPreferences.Editor editor = getSharedPreferences(name).edit();
        editor.putString(key,value);
        editor.commit();
        return;
    }

    public static boolean getBoolean(String name,String key){
        return getSharedPreferences(name).getBoolean(key,DEF_BOOL_VALUE);
    }

    public static void putBoolean(String name,String key,boolean value){
        SharedPreferences.Editor editor = getSharedPreferences(name).edit();
        editor.putBoolean(key,value);
        editor.commit();
        return;
    }
}
