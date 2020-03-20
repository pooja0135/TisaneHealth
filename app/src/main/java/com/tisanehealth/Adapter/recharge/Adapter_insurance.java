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

import com.tisanehealth.recharge_pay_bill.insurance.InsurancePayActivity;
import com.tisanehealth.Model.recharge.RechargeListModel;
import com.tisanehealth.R;

import java.util.List;


public class Adapter_insurance extends RecyclerView.Adapter<Adapter_insurance.MyViewHolder> {

    Context mContext;
    List<RechargeListModel> insurancelist;



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
    public Adapter_insurance(Context mContext, List<RechargeListModel> insurancelist) {
        this.mContext = mContext;
        this.insurancelist = insurancelist;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_electricity_operator_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tvOperatorName.setText(insurancelist.get(position).getService());
    //    holder.ivDth.setImageResource(insurancelist.get(position).getImage());


        holder.linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             mContext.startActivity(new Intent(mContext, InsurancePayActivity.class).putExtra("data_value", (Parcelable) insurancelist.get(position)));
            }
        });




    }

    @Override
    public int getItemCount() {
        return insurancelist.size();
    }



}
