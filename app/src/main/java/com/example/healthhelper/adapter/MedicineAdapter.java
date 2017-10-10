package com.example.healthhelper.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.healthhelper.R;
import com.example.healthhelper.medicine;

import java.util.ArrayList;

/**
 * Created by user on 2017/10/7.
 */

public class MedicineAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<medicine> array;

    public MedicineAdapter(LayoutInflater inflater, ArrayList<medicine> array){
        this.inflater = inflater;
        this.array = array;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView=inflater.inflate(R.layout.medicine_list_item, null);
            viewHolder.medicineName = (TextView) convertView.findViewById(R.id.medicine_name_inList);
            viewHolder.takenTimes = (TextView) convertView.findViewById(R.id.medicine_times_inList);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.medicineName.setText(array.get(position).getMedicineName());
        viewHolder.takenTimes.setText("每日服用 "+
                String.format("%d", array.get(position).getTakenTimes())+" 次");

        return convertView;
    }

    class ViewHolder{
        TextView medicineName, takenTimes;
    }
}
