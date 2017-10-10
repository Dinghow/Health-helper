package com.example.healthhelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.healthhelper.activity.MainActivity;
import com.example.healthhelper.adapter.MedicineAdapter;
import com.example.healthhelper.component.NewNote;
import com.getbase.floatingactionbutton.AddFloatingActionButton;

import java.util.ArrayList;

public class MedicineList extends AppCompatActivity {

    private AddFloatingActionButton addFab;
    ListView medicineList;
    LayoutInflater inflater;
    ArrayList<medicine> array;
    MyDataBase myDataBase;
    Button medicineListBackup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_note_list);
        medicineList = (ListView) findViewById(R.id.medicine_list);
        addFab = (AddFloatingActionButton) findViewById(R.id.add_medicine);
        medicineListBackup = (Button) findViewById(R.id.medicine_list_backup);
        inflater = getLayoutInflater();

        myDataBase = new MyDataBase(this);

        array = myDataBase.getArray();
        MedicineAdapter adapter = new MedicineAdapter(inflater, array);
        medicineList.setAdapter(adapter);

        //设置返回键
        medicineListBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedicineList.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //设置字体
        final TextView leftWaterText = (TextView) findViewById(R.id.medicine_list_title);
        leftWaterText.setTypeface(Typeface.createFromAsset(getAssets(),"font/AdobeKaitiStd-Regular.otf"));
        leftWaterText.getPaint().setFakeBoldText(true);

        /*
         * 点击listView里面的item,用来修改日记
         */
        medicineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),NewNote.class);
                intent.putExtra("id", array.get(position).getId());
                startActivity(intent);
                MedicineList.this.finish();
            }
        });

        /*
         * 长点后来判断是否删除数据
         */
        medicineList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MedicineList.this)
                        .setTitle("删除")
                        .setMessage("是否删除提醒")
                        .setNegativeButton("取消",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myDataBase.Delete(array.get(position).getId());
                                array = myDataBase.getArray();
                                MedicineAdapter adapter1 = new MedicineAdapter(inflater,array);
                                medicineList.setAdapter(adapter1);
                            }
                        })
                        .create().show();
                return true;
            }
        });

        /*
         * 按钮点击事件，用来新建日记
         */
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),NewNote.class);
                startActivity(intent);
            }
        });
    }
}
