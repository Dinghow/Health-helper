package com.example.healthhelper.component;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.healthhelper.MedicineList;
import com.example.healthhelper.MyDataBase;
import com.example.healthhelper.R;
import com.example.healthhelper.activity.AlermReceiver;
import com.example.healthhelper.medicine;

import java.util.Calendar;

import static com.example.healthhelper.R.id.add_alarm;

public class NewNote extends AppCompatActivity {

    EditText medicineName;
    EditText timesTaken;
    EditText annotation;
    MyDataBase myDataBase;
    ImageButton saveBtn;
    Button newNoteBackup;
    medicine med;
    private Calendar calendar;
    private Button addAlarm;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_note);
        saveBtn = (ImageButton) findViewById(R.id.save_medicine);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSave();
            }
        });
        medicineName = (EditText) findViewById(R.id.medicine_name_edit);
        timesTaken = (EditText) findViewById(R.id.times_taken_edit);
        annotation = (EditText) findViewById(R.id.annotation_edit);
        newNoteBackup = (Button) findViewById(R.id.new_note_backup);
        addAlarm = (Button) findViewById(R.id.add_alarm);
        calendar = Calendar.getInstance();
        myDataBase = new MyDataBase(this);

        Intent intent = this.getIntent();
        id = intent.getIntExtra("id", 0);
        //默认为0，不为0,则为修改数据时跳转过来的
        if(id != 0){
            med = myDataBase.getNameandTimes(id);
            medicineName.setText(med.getMedicineName());
            timesTaken.setText(String.valueOf(med.getTakenTimes()));
            annotation.setText(med.getAnnotation());
        }

        newNoteBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //添加提醒按钮响应事件
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.setTimeInMillis(System.currentTimeMillis());
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                new TimePickerDialog(NewNote.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        //set(f, value) changes field f to value.
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);

                        Intent intent = new Intent(NewNote.this, AlermReceiver.class);
                        intent.putExtra("music", true);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(NewNote.this, 0, intent, 0);
                        AlarmManager am;
                        //获取系统进程
                        am = (AlarmManager)getSystemService(ALARM_SERVICE);
                        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        //设置周期！！
                        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+(10*1000), (24*60*60*1000), pendingIntent);
                    }
                },hour,minute,true).show();
            }
        });


    }

    /*
     * 返回按钮调用的方法。
     */


    @Override
    public void onBackPressed() {
        String name = medicineName.getText().toString();
        String anno = annotation.getText().toString();
        int time = Integer.parseInt(timesTaken.getText().toString());

        //设置字体
        final TextView leftWaterText = (TextView) findViewById(R.id.new_note_title);
        leftWaterText.setTypeface(Typeface.createFromAsset(getAssets(),"font/AdobeKaitiStd-Regular.otf"));
        leftWaterText.getPaint().setFakeBoldText(true);

        //是要修改数据
        if (id != 0) {
            med = new medicine(id, name, time, anno);
            myDataBase.toUpdate(med);
            Intent intent = new Intent(NewNote.this,MedicineList.class);
            startActivity(intent);
            finish();
        }
        //新建日志
        else{
            med = new medicine(id, name, time, anno);
            myDataBase.toInsert(med);
            Intent intent = new Intent(NewNote.this, MedicineList.class);
            startActivity(intent);
            finish();
        }
    }

    private void isSave(){
        String name = medicineName.getText().toString();
        String anno = annotation.getText().toString();
        int time = Integer.parseInt(timesTaken.getText().toString());

        Log.i("TAG", "id = " + id);
        //是要修改数据
        if (id != 0) {
            med = new medicine(id, name, time, anno);
            myDataBase.toUpdate(med);
            Intent intent = new Intent(NewNote.this,MedicineList.class);
            startActivity(intent);

            Log.i("TAG", "event = id = " + 1);
            finish();
        }
        //新建日志
        else{
            med = new medicine(id, name, time, anno);
            myDataBase.toInsert(med);
            Intent intent = new Intent(NewNote.this,MedicineList.class);
            startActivity(intent);
            Log.i("TAG", "event = id = " + 0);

            finish();
        }
    }


}
