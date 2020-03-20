package com.tisanehealth.Adapter.recharge;


import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tisanehealth.recharge_pay_bill.dth_recharge.DTHListActivity;
import com.tisanehealth.Model.recharge.DTHModel;
import com.tisanehealth.R;

import java.util.List;


public class Adapter_dth extends RecyclerView.Adapter<Adapter_dth.MyViewHolder> {

    Context mContext;
    List<DTHModel> dthlist;



    public class MyViewHolder extends RecyclerView.ViewHolder {

       LinearLayout linearlayout;
        public ImageView ivDth;
        TextView tvDth;
        public MyViewHolder(View view) {
            super(view);

            ivDth        =view.findViewById(R.id.ivDth);
            tvDth        =view.findViewById(R.id.tvDth);
            linearlayout =view.findViewById(R.id.linearlayout);


        }
    }
    public Adapter_dth(Context mContext,  List<DTHModel> dthlist) {
        this.mContext = mContext;
        this.dthlist = dthlist;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_dth_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tvDth.setText(dthlist.get(position).getName());
        holder.ivDth.setImageResource(dthlist.get(position).getImage());


        holder.linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             mContext.startActivity(new Intent(mContext, DTHListActivity.class).putExtra("data_value",(Parcelable)dthlist.get(position)));
            }
        });




    }

    @Override
    public int getItemCount() {
        return dthlist.size();
    }



}
