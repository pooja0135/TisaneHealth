package com.tisanehealth.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tisanehealth.Activity.DashBoardActivity;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.recharge_pay_bill.broadband.BroadbandListActivity;
import com.tisanehealth.recharge_pay_bill.dth_recharge.DTHRechargeActivity;
import com.tisanehealth.recharge_pay_bill.electricitybill.ElectricityBillActivity;
import com.tisanehealth.recharge_pay_bill.insurance.InsuranceListActivity;
import com.tisanehealth.recharge_pay_bill.landline.LandlineListActivity;
import com.tisanehealth.recharge_pay_bill.mobile_recharge.ContactActivity;
import com.tisanehealth.Model.RechargeModel;
import com.tisanehealth.R;
import com.tisanehealth.recharge_pay_bill.money_transfer.MoneyTransferActivity;

import java.util.List;


public class Adapter_recharge extends RecyclerView.Adapter<Adapter_recharge.MyViewHolder> {

    Context mContext;
    List<RechargeModel> dashboardlist;
    Preferences pref;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageview;
        TextView textview;
        LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            imageview=view.findViewById(R.id.imageview);
            textview=view.findViewById(R.id.textview);
            linearLayout=view.findViewById(R.id.linearlayout);


        }
    }
    public Adapter_recharge(Context mContext, List<RechargeModel> dashboardlist) {
        this.mContext = mContext;
        this.dashboardlist = dashboardlist;
        pref=new Preferences(mContext);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_recharge_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        RechargeModel RechargeModel = dashboardlist.get(position);
    //    holder.textview.setText(RechargeTypeModel.getName());
     //   holder.imageview.setImageResource(RechargeTypeModel.getImage());

          holder.textview.setText(RechargeModel.getName());
            holder.imageview.setImageResource(RechargeModel.getImage());



        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position==0)
                {
                    DashBoardActivity.mobile_type="Prepaid";
                    mContext.startActivity(new Intent(mContext, ContactActivity.class));
                }
                else if (position==1)
                {
                    DashBoardActivity.mobile_type="Postpaid";
                  mContext.startActivity(new Intent(mContext, ContactActivity.class));
                }
                else if (position==2)
                {
                  mContext.startActivity(new Intent(mContext, ElectricityBillActivity.class));
                }
                else if (position==3)
                {

                    mContext.startActivity(new Intent(mContext, DTHRechargeActivity.class));
                }
                else if (position==4)
                {
                    mContext.startActivity(new Intent(mContext, LandlineListActivity.class));
                }
                else if (position==5)
                {
                    DashBoardActivity.mobile_type="Datacard";
                    mContext.startActivity(new Intent(mContext, ContactActivity.class));
                }
                else if (position==6)
                {
                    mContext.startActivity(new Intent(mContext, InsuranceListActivity.class));
                }
                else if (position==7)
                {
                    mContext.startActivity(new Intent(mContext, BroadbandListActivity.class));
                }

                else if (position==8)
                {
                    if (!pref.get(AppSettings.BankAccountNumber).isEmpty()&&!pref.get(AppSettings.Bankname).isEmpty()&&!pref.get(AppSettings.PayeeName).isEmpty()
                        &&!pref.get(AppSettings.BankIfsc).isEmpty()&&!pref.get(AppSettings.BankAccountNumber).isEmpty())
                    {
                        mContext.startActivity(new Intent(mContext, MoneyTransferActivity.class));
                    }
                    else
                    {
                        Toast.makeText(mContext, "Please update your bank details.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });






    }

    @Override
    public int getItemCount() {

        return dashboardlist.size();


    }
}
