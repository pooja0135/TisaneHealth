package com.tisanehealth.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.tisanehealth.Activity.DashBoardActivity;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Model.RechargeModel;
import com.tisanehealth.R;
import com.tisanehealth.recharge_pay_bill.broadband.BroadbandListActivity;
import com.tisanehealth.recharge_pay_bill.dth_recharge.DTHRechargeActivity;
import com.tisanehealth.recharge_pay_bill.electricitybill.ElectricityBillActivity;
import com.tisanehealth.recharge_pay_bill.insurance.InsuranceListActivity;
import com.tisanehealth.recharge_pay_bill.landline.LandlineListActivity;
import com.tisanehealth.recharge_pay_bill.mobile_recharge.ContactActivity;
import com.tisanehealth.recharge_pay_bill.money_transfer.MoneyTransferActivity;

import java.util.List;


public class Adapter_dealwithus extends RecyclerView.Adapter<Adapter_dealwithus.MyViewHolder> {

    Context mContext;
    List<Integer> dashboardlist;
    Preferences pref;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageview;

        LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            imageview=view.findViewById(R.id.imageview);
            linearLayout=view.findViewById(R.id.linearlayout);


        }
    }
    public Adapter_dealwithus(Context mContext, List<Integer> dashboardlist) {
        this.mContext = mContext;
        this.dashboardlist = dashboardlist;
        pref=new Preferences(mContext);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_deal_with_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {



            holder.imageview.setImageResource(dashboardlist.get(position));

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position==0)
                {
                   /* Intent i = mContext.getPackageManager().getLaunchIntentForPackage("com.flipkart.android");
                    if (i != null) {
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(i);
                    } else {
                        i = new Intent(Intent.ACTION_VIEW);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setData(Uri.parse("market://details?id=" + "com.flipkart.android"));
                        mContext.startActivity(i);
                    }*/

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.flipkart.com/"));
                    mContext.startActivity(browserIntent);
                }
                else if (position==1)
                {
                  /*  Intent i = mContext.getPackageManager().getLaunchIntentForPackage("in.amazon.mShop.android.shopping");
                    if (i != null) {
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(i);
                    } else {
                        i = new Intent(Intent.ACTION_VIEW);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setData(Uri.parse("market://details?id=" + "in.amazon.mShop.android.shopping"));
                        mContext.startActivity(i);
                    }*/

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.amazon.in/"));
                    mContext.startActivity(browserIntent);
                }
                else if (position==2)
                {
                   /* Intent i = mContext.getPackageManager().getLaunchIntentForPackage("com.myntra.android");
                    if (i != null) {
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(i);
                    } else {
                        i = new Intent(Intent.ACTION_VIEW);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setData(Uri.parse("market://details?id=" + "com.myntra.android"));
                        mContext.startActivity(i);
                    }*/

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.myntra.com/"));
                    mContext.startActivity(browserIntent);
                }
                else if (position==3)
                {

//                    Intent i = mContext.getPackageManager().getLaunchIntentForPackage("in.swiggy.android");
//                    if (i != null) {
//                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        mContext.startActivity(i);
//                    } else {
//                        i = new Intent(Intent.ACTION_VIEW);
//                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        i.setData(Uri.parse("market://details?id=" + "in.swiggy.android"));
//                        mContext.startActivity(i);
//                    }


                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.swiggy.com/"));
                    mContext.startActivity(browserIntent);
                }
                else if (position==4)
                {


                 /*   Intent i = mContext.getPackageManager().getLaunchIntentForPackage("com.application.zomato");
                    if (i != null) {
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(i);
                    } else {
                        i = new Intent(Intent.ACTION_VIEW);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setData(Uri.parse("market://details?id=" + "com.application.zomato"));
                        mContext.startActivity(i);
                    }*/
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.zomato.com/"));
                    mContext.startActivity(browserIntent);

                }

            }
        });






    }

    @Override
    public int getItemCount() {

        return dashboardlist.size();


    }
}
