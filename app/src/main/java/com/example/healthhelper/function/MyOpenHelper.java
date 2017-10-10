package com.example.healthhelper.function;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by user on 2017/10/7.
 */

public class MyOpenHelper extends SQLiteOpenHelper {

    private Context mContext;

    public MyOpenHelper(Context context){
        super(context, "mydata.db", null, 2);
        Log.i("TAG", "create db");
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("TAG", "create the database");
        db.execSQL("create table myMedicine( id integer primary key autoincrement, medicineName text,takenTimes integer, annotation text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
