package com.example.healthhelper.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.healthhelper.R;
import com.example.healthhelper.function.WaterCounter;
import com.example.healthhelper.utils.PreUtils;

/**
 * Created by Dinghow on 2017/10/7.
 */
public class SettingsFragment extends PreferenceFragment {
    ListPreference lp;
    EditTextPreference ep;
    CheckBoxPreference cp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

/* 性别设置 */
        //根据之前收集的数据设置摘要默认值
        lp = (ListPreference) findPreference(getString(R.string.list_key_1));
        CharSequence[] entries = lp.getEntries();
        int index = PreUtils.getInt("user_data","gender");
        lp.setValueIndex(index);
        lp.setSummary(entries[index]);
        lp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // 选项变更更新存储和摘要
                if (preference instanceof ListPreference) {
                    //把preference这个Preference强制转化为ListPreference类型
                    ListPreference listPreference = (ListPreference) preference;
                    //获取ListPreference中的实体内容
                    CharSequence[] entries = listPreference.getEntries();
                    //获取ListPreference中的实体内容的下标值
                    int index = listPreference.findIndexOfValue((String) newValue);
                    //把listPreference中的摘要显示为当前ListPreference的实体内容中选择的那个项目
                    listPreference.setSummary(entries[index]);
                    //将改变存入sharedPreference
                    PreUtils.putInt("user_data","gender",index);
                    WaterCounter.saveAmount();
                }
                return true;
            }
        });

/* 体重设置 */
        //根据之前收集的数据设置默认值
        ep = (EditTextPreference)findPreference(getString(R.string.edit_key_1));
        String defaultvalue = PreUtils.getString("user_data","weight");
        ep.setText(defaultvalue);
        ep.setSummary(defaultvalue);
        ep.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (preference instanceof EditTextPreference) {
                    EditTextPreference editTextPreference = (EditTextPreference) preference;
                    //将改变存入sharedPreference
                    PreUtils.putString("user_data","weight",newValue.toString());
                    //更改摘要显示
                    editTextPreference.setText((String)newValue);
                    editTextPreference.setSummary(newValue.toString());
                    WaterCounter.saveAmount();
                }
                return false;
            }
        });

/* 运动补水设置 */
        cp = (CheckBoxPreference)findPreference(getString(R.string.check_key_1));
        cp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.d("CheckBox",newValue.toString());
                CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preference;
                checkBoxPreference.setChecked((boolean)newValue);
                PreUtils.putBoolean("user_data","sport_model",(boolean)newValue);
                WaterCounter.saveAmount();
                return false;
            }
        });
    }
}