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

import com.tisanehealth.recharge_pay_bill.landline.LandlinePaidActivity;
import com.tisanehealth.Model.recharge.RechargeListModel;
import com.tisanehealth.R;

import java.util.List;


public class Adapter_landline extends RecyclerView.Adapter<Adapter_landline.MyViewHolder> {

    Context mContext;
    List<RechargeListModel> landlinelist;



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
    public Adapter_landline(Context mContext, List<RechargeListModel> landlinelist) {
        this.mContext = mContext;
        this.landlinelist = landlinelist;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_landline_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tvDth.setText(landlinelist.get(position).getService());

        holder.linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             mContext.startActivity(new Intent(mContext, LandlinePaidActivity.class).putExtra("data_value", (Parcelable) landlinelist.get(position)));
            }
        });




    }

    @Override
    public int getItemCount() {
        return landlinelist.size();
    }



}
