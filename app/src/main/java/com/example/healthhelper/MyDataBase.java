package com.example.healthhelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.healthhelper.function.MyOpenHelper;

import java.util.ArrayList;

/**
 * Created by user on 2017/10/7.
 */

public class MyDataBase {

    Context context;
    MyOpenHelper myHelper;
    SQLiteDatabase myDatabase;

    /*
     * 实例化这个类的同时，创建数据库
     */
    public MyDataBase(Context con){
        this.context=con;
        myHelper = new MyOpenHelper(context);
    }

    /*
     * 得到ListView的数据，从数据库里查找后解析
     */
    public ArrayList<medicine> getArray(){
        ArrayList<medicine> array = new ArrayList<medicine>();
        ArrayList<medicine> array1 = new ArrayList<medicine>();
        try{
            myDatabase = myHelper.getWritableDatabase();
            if(myDatabase != null){
                Cursor cursor = myDatabase.rawQuery("select id, medicineName, takenTimes,annotation from myMedicine",null);
                cursor.moveToFirst();
                while(!cursor.isAfterLast()){
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String medicineName = cursor.getString(
                            cursor.getColumnIndex("medicineName"));
                    int takenTimes = cursor.getInt(
                            cursor.getColumnIndex("takenTimes"));
                    String annotation = cursor.getString(
                            cursor.getColumnIndex("annotation"));
                    medicine med = new medicine(id,medicineName,takenTimes,annotation);
                    array.add(med);
                    cursor.moveToNext();
                }
                myDatabase.close();
                for(int i = array.size(); i > 0; i--){
                    array1.add(array.get(i-1));
                }
            }

        }catch (Exception e){
            Log.i("error", e.getMessage());
            //e.printStackTrace();
        }

        return array1;
    }

    /*
     * 返回可能要修改的数据
     */
    public medicine getNameandTimes(int id){
        myDatabase = myHelper.getWritableDatabase();
        Cursor cursor = myDatabase.rawQuery(
                "select id,medicineName,takenTimes,annotation from myMedicine where id='"+id+"'",null);
        cursor.moveToFirst();
        String medicineName = cursor.getString(
                cursor.getColumnIndex("medicineName"));
        int takenTimes = cursor.getInt(
                cursor.getColumnIndex("takenTimes"));
        String annotation = cursor.getString(
                cursor.getColumnIndex("annotation"));
        medicine med = new medicine(id,medicineName,takenTimes,annotation);
        myDatabase.close();
        return med;
    }

    /*
     * 用来修改日记
     */
    public void toUpdate(medicine med){
        myDatabase = myHelper.getWritableDatabase();
        try{
            myDatabase.execSQL(
                    "update myMedicine set medicineName= '"+med.getMedicineName() +
                            "', takenTimes= "+ med.getTakenTimes()+
                            " , annotation= '"+ med.getAnnotation()+
                            " ' where id = "+ med.getId());
        }catch (Exception e){
            Log.i("TAG", e.getMessage());
        }


        myDatabase.close();
    }

    /*
     * 用来增加新的日记
     */
    public void toInsert(medicine med){
        myDatabase = myHelper.getWritableDatabase();
        try{
            String sql = "insert into myMedicine(medicineName,takenTimes,annotation)"
                    +" values('"+ med.getMedicineName()+"', "
                    + med.getTakenTimes()+" ,'"
                    + med.getAnnotation()+"')";
            myDatabase.execSQL(sql);
            Log.i("TAG", sql);
        }catch (Exception e){
            Log.i("TAG", e.getMessage());
        }
        myDatabase.close();
    }
    /*
     * 选择删除日记
     */
    public void Delete(int id){
        myDatabase = myHelper.getWritableDatabase();
        myDatabase.execSQL("delete from myMedicine where id = " + id);
        myDatabase.close();
    }
}
