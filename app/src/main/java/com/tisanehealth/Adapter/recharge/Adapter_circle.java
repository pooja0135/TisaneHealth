package com.tisanehealth.Adapter.recharge;

import android.app.Dialog;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.tisanehealth.Activity.DashBoardActivity;
import com.tisanehealth.recharge_pay_bill.datacard.DataCardActivity;
import com.tisanehealth.recharge_pay_bill.mobile_recharge.RechargeActivity;
import com.tisanehealth.recharge_pay_bill.mobile_recharge.RechargePostpaidActivity;
import com.tisanehealth.Model.recharge.CircleModel;
import com.tisanehealth.R;

import java.util.ArrayList;


public class Adapter_circle extends RecyclerView.Adapter<Adapter_circle.MyViewHolder> {

    Context mContext;
    ArrayList<CircleModel> circlelist;
    Dialog dialog;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView tvCirclename;
        public MyViewHolder(View view) {
            super(view);


            tvCirclename=view.findViewById(R.id.tvCirclename);
            linearLayout=view.findViewById(R.id.linearlayout);

        }
    }
    public Adapter_circle(Context mContext, ArrayList<CircleModel> circlelist, Dialog dialog) {
        this.mContext = mContext;
        this.circlelist = circlelist;
        this.dialog = dialog;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_circle_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


       holder.tvCirclename.setText(circlelist.get(position).getState_name());
       holder.linearLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               dialog.dismiss();

               if (DashBoardActivity.mobile_type.equals("Prepaid"))
               {
                   RechargeActivity.tvCircleName.setText(circlelist.get(position).getState_name());
                   RechargeActivity.circle_id=circlelist.get(position).getCircle_code();
               }

               else if (DashBoardActivity.mobile_type.equals("Postpaid"))
               {
                   RechargePostpaidActivity.tvCircleName.setText(circlelist.get(position).getState_name());
                   RechargePostpaidActivity.circle_id=circlelist.get(position).getCircle_code();
               }

               else
               {
                   DataCardActivity.tvCircleName.setText(circlelist.get(position).getState_name());
                   DataCardActivity.circle_id=circlelist.get(position).getCircle_code();
               }

           }
       });
    }

    @Override
    public int getItemCount() {
        return circlelist.size();



    }
}
