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

import com.tisanehealth.recharge_pay_bill.electricitybill.ElectricityPayActivity;
import com.tisanehealth.Model.recharge.RechargeListModel;
import com.tisanehealth.R;

import java.util.List;


public class Adapter_electricity extends RecyclerView.Adapter<Adapter_electricity.MyViewHolder> {

    Context mContext;
    List<RechargeListModel> electricitylist;



    public class MyViewHolder extends RecyclerView.ViewHolder {

       LinearLayout linearlayout;
        public ImageView ivDth;
        TextView tvOperatorName;
        public MyViewHolder(View view) {
            super(view);

         //   ivDth        =view.findViewById(R.id.ivDth);
            tvOperatorName        =view.findViewById(R.id.tvOperatorName);
            linearlayout =view.findViewById(R.id.linearlayout);


        }
    }
    public Adapter_electricity(Context mContext, List<RechargeListModel> electricitylist) {
        this.mContext = mContext;
        this.electricitylist = electricitylist;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_electricity_operator_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tvOperatorName.setText(electricitylist.get(position).getService());
    //    holder.ivDth.setImageResource(dthlist.get(position).getImage());


        holder.linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             mContext.startActivity(new Intent(mContext, ElectricityPayActivity.class).putExtra("data_value", (Parcelable) electricitylist.get(position)));
            }
        });




    }

    @Override
    public int getItemCount() {
        return electricitylist.size();
    }



}
