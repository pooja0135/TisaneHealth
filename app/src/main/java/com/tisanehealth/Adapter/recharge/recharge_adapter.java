package com.tisanehealth.Adapter.recharge;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.tisanehealth.recharge_pay_bill.mobile_recharge.RechargeActivity;
import com.tisanehealth.Model.recharge.RechargePlanModel;
import com.tisanehealth.R;

import java.util.ArrayList;

public class recharge_adapter extends RecyclerView.Adapter<recharge_adapter.MyViewHolder> {

    Context mContext;
    ArrayList<RechargePlanModel> rechargelist;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView tvTalktime;
        TextView tvData;
        TextView tvValidity;
        TextView tvPrice;
        TextView tvDetail;
        public MyViewHolder(View view) {
            super(view);

            tvTalktime=view.findViewById(R.id.tvTalktime);
            tvData=view.findViewById(R.id.tvData);
            tvValidity=view.findViewById(R.id.tvValidity);
            tvPrice=view.findViewById(R.id.tvPrice);
            tvDetail=view.findViewById(R.id.tvDetail);

        }
    }
    public recharge_adapter(Context mContext, ArrayList<RechargePlanModel> rechargelist) {
        this.mContext = mContext;
        this.rechargelist = rechargelist;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_recharge_plan, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        RechargePlanModel rechargePlanModel=rechargelist.get(position);
        holder.tvTalktime.setText(rechargePlanModel.getTalktime());
        holder.tvData.setText(rechargePlanModel.getValidity());
        holder.tvValidity.setText(rechargePlanModel.getValidity());
        holder.tvPrice.setText("\u20B9"+rechargePlanModel.getAmount());
        holder.tvDetail.setText(rechargePlanModel.getDetail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RechargeActivity.etAmount.setText(rechargelist.get(position).getAmount());
                ((Activity)mContext).finish();
            }
        });


    }

    @Override
    public int getItemCount() {
        return rechargelist.size();



    }
}
