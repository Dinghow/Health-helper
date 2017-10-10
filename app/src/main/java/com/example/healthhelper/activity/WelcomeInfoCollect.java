package com.example.healthhelper.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.healthhelper.R;
import com.example.healthhelper.function.WaterCounter;

/**
 * 初次登录收集用户信息
 */
public class WelcomeInfoCollect extends AppCompatActivity {

    //性别选项
    public RadioGroup mRadioGroup1;
    public RadioButton mRadio1, mRadio2;
    public EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_info_collect);
        mRadioGroup1 = (RadioGroup) findViewById(R.id.gender_group);
        mRadio1 = (RadioButton) findViewById(R.id.female_button);
        mRadio2 = (RadioButton) findViewById(R.id.male_button);
        mRadioGroup1.setOnCheckedChangeListener(radiogpchange);
        editText = (EditText)findViewById(R.id.weight_enter);
        Button enter_btn = (Button)findViewById(R.id.btn_enter_main);
        enter_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int gender = 0; //0代表男性 1代表女性
                for(int i = 0;i<mRadioGroup1.getChildCount();i++){
                    RadioButton radioButton = (RadioButton)mRadioGroup1.getChildAt(i);
                    if(radioButton.isChecked()) gender = i;
                }
                SharedPreferences.Editor editor = getSharedPreferences("user_data",MODE_PRIVATE).edit();
                if(gender!=0&&gender!=1) gender = 0;
                String weight = editText.getText().toString();
                if(weight != null &&weight.length()>0){
                    if(Integer.parseInt(editText.getText().toString())>0)
                        editor.putString("weight",editText.getText().toString());
                    else
                        editor.putString("weight","0");
                }
                editor.putInt("gender",gender);
                editor.putBoolean("sport_model",false);
                editor.putInt("drink_amount",0);
                editor.apply();
                WaterCounter.saveAmount();
                Intent intent = new Intent(WelcomeInfoCollect.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private RadioGroup.OnCheckedChangeListener radiogpchange = new RadioGroup.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(RadioGroup group,int checkedId){
            if(checkedId == mRadio1.getId()){

             }
            else if(checkedId == mRadio2.getId()){
                //To do
            }
        }
    };
}
