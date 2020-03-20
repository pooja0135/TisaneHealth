package com.tisanehealth.Adapter.recharge;


import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tisanehealth.recharge_pay_bill.broadband.BroadbandPayActivity;
import com.tisanehealth.Model.recharge.RechargeListModel;
import com.tisanehealth.R;

import java.util.List;


public class Adapter_broadband extends RecyclerView.Adapter<Adapter_broadband.MyViewHolder> {

    Context mContext;
    List<RechargeListModel> broadbandlist;



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
    public Adapter_broadband(Context mContext, List<RechargeListModel> broadbandlist) {
        this.mContext = mContext;
        this.broadbandlist = broadbandlist;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_electricity_operator_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tvOperatorName.setText(broadbandlist.get(position).getService());
    //    holder.ivDth.setImageResource(broadbandlist.get(position).getImage());


        holder.linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             mContext.startActivity(new Intent(mContext, BroadbandPayActivity.class).putExtra("data_value", (Parcelable) broadbandlist.get(position)));
            }
        });




    }

    @Override
    public int getItemCount() {
        return broadbandlist.size();
    }



}
