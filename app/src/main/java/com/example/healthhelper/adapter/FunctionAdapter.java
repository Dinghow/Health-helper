package com.example.healthhelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.healthhelper.function.Function;
import com.example.healthhelper.MedicineList;
import com.example.healthhelper.R;

import java.util.List;

/**
 * Created by user on 2017/10/5.
 */

public class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.ViewHolder> {

    private Context mContext;
    private List<Function> mFunctionList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView functionImage;
        TextView functionName;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            functionImage = (ImageView) view.findViewById(R.id.function_image);
            functionName = (TextView) view.findViewById(R.id.function_name);
        }
    }

    public FunctionAdapter(List<Function> functionList){
        mFunctionList=functionList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.function_item,parent,false);

        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Function function = mFunctionList.get(position);
                Intent intent = new Intent(mContext,MedicineList.class);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Function function = mFunctionList.get(position);
        holder.functionName.setText(function.getName());
        Glide.with(mContext).load(function.getImageId()).into(holder.functionImage);
    }

    @Override
    public int getItemCount() {
        return mFunctionList.size();
    }
}
