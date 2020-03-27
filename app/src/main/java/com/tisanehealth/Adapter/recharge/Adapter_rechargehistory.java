package com.tisanehealth.Adapter.recharge;


import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tisanehealth.Model.recharge.DTHModel;
import com.tisanehealth.R;
import com.tisanehealth.recharge_pay_bill.dth_recharge.DTHListActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class Adapter_rechargehistory extends RecyclerView.Adapter<Adapter_rechargehistory.MyViewHolder> {

    Context mContext;
    ArrayList<HashMap<String,String>> rechargehistorylist;



    public class MyViewHolder extends RecyclerView.ViewHolder {


        public ImageView imageview,ivStatus;
        TextView tvPaid,tvAmount,tvRechargeType,tvRechMobile,tvRechOrderId,tvDate,tvRechargestatus;
        public MyViewHolder(View view) {
            super(view);

            imageview                =view.findViewById(R.id.imageview);
            ivStatus                 =view.findViewById(R.id.ivStatus);
            tvPaid                   =view.findViewById(R.id.tvPaid);
            tvAmount                 =view.findViewById(R.id.tvAmount);
            tvRechargeType           =view.findViewById(R.id.tvRechargeType);
            tvRechMobile             =view.findViewById(R.id.tvRechMobile);
            tvRechOrderId            =view.findViewById(R.id.tvRechOrderId);
            tvDate                   =view.findViewById(R.id.tvDate);
            tvRechargestatus         =view.findViewById(R.id.tvRechargestatus);


        }
    }
    public Adapter_rechargehistory(Context mContext,  ArrayList<HashMap<String,String>> rechargehistorylist) {
        this.mContext = mContext;
        this.rechargehistorylist = rechargehistorylist;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_recharge_history, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
       if (rechargehistorylist.get(position).get("bill_status").equals("Recharge"))
       {
           holder.imageview.setImageResource(R.drawable.ic_mobile1);
       }
       else if (rechargehistorylist.get(position).get("bill_status").equals("Electricity"))
        {
            holder.imageview.setImageResource(R.drawable.ic_electricity);
        }
       else if (rechargehistorylist.get(position).get("bill_status").equals("DTH"))
       {
           holder.imageview.setImageResource(R.drawable.ic_satellite);
       }
       else if (rechargehistorylist.get(position).get("bill_status").equals("Landline"))
       {
           holder.imageview.setImageResource(R.drawable.ic_telephone);
       }
       else if (rechargehistorylist.get(position).get("bill_status").equals("Data_Card"))
       {
           holder.imageview.setImageResource(R.drawable.ic_datacard);
       }
       else if (rechargehistorylist.get(position).get("bill_status").equals("Insurance"))
       {
           holder.imageview.setImageResource(R.drawable.ic_insurance);
       }
       else if (rechargehistorylist.get(position).get("bill_status").equals("Broadband"))
       {
           holder.imageview.setImageResource(R.drawable.ic_broadband);
       }








        if (rechargehistorylist.get(position).get("bill_status").equals("Recharge"))
        {
            holder.tvPaid.setText("Mobile Recharge");
        }
        else if (rechargehistorylist.get(position).get("bill_status").equals("Electricity"))
        {
            holder.tvPaid.setText("Electricity Bill");
        }
        else if (rechargehistorylist.get(position).get("bill_status").equals("DTH"))
        {
            holder.tvPaid.setText("DTH Recharge");
        }
        else if (rechargehistorylist.get(position).get("bill_status").equals("Landline"))
        {
            holder.tvPaid.setText("Landline Bill");
        }
        else if (rechargehistorylist.get(position).get("bill_status").equals("Data_Card"))
        {
            holder.tvPaid.setText("Datacard Recharge");
        }
        else if (rechargehistorylist.get(position).get("bill_status").equals("Insurance"))
        {
            holder.tvPaid.setText("Insurance Bill");
        }
        else if (rechargehistorylist.get(position).get("bill_status").equals("Broadband"))
        {
            holder.tvPaid.setText("Broadband Recharge");
        }

       holder.tvAmount.setText("\u20B9"+rechargehistorylist.get(position).get("Amount"));
       holder.tvRechargeType.setText(rechargehistorylist.get(position).get("rech_type"));
       holder.tvRechMobile.setText(rechargehistorylist.get(position).get("rech_mobile"));
       holder.tvRechOrderId.setText(rechargehistorylist.get(position).get("rech_order_id"));


       if(rechargehistorylist.get(position).get("rech_status").equals("SUCCESS"))
       {
           holder.tvRechargestatus.setText("Debited from");
           holder.ivStatus.setImageResource(R.drawable.ic_credit_wallet);
       }
       else
       {
           holder.tvRechargestatus.setText("Failed");
           holder.ivStatus.setImageResource(R.drawable.ic_error);
       }


        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = null;
        try {
            date = originalFormat.parse(rechargehistorylist.get(position).get("Entrydate"));
        } catch (Exception e) {

            //Log.v("hhhhhhhhhhhhh",e.toString());
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);
        holder.tvDate.setText(formattedDate);

    }

    @Override
    public int getItemCount() {
        return rechargehistorylist.size();
    }



}
