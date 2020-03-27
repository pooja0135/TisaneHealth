package com.tisanehealth.Adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class Adapter_money_transfer_history extends RecyclerView.Adapter<Adapter_money_transfer_history.MyViewHolder> {

    Context mContext;
    ArrayList<HashMap<String,String>> rechargehistorylist;
    Preferences pref;


    public class MyViewHolder extends RecyclerView.ViewHolder {



        TextView tvMemberId,tvWalletType,tvCurrentWallet,tvTransferAmount,tvClosingBalance,tvStatus,tvDate;
        public MyViewHolder(View view) {
            super(view);

            tvMemberId               =view.findViewById(R.id.tvMemberId);
            tvWalletType             =view.findViewById(R.id.tvWalletType);
            tvCurrentWallet          =view.findViewById(R.id.tvCurrentWallet);
            tvTransferAmount         =view.findViewById(R.id.tvTransferAmount);
            tvClosingBalance         =view.findViewById(R.id.tvClosingBalance);
            tvStatus                 =view.findViewById(R.id.tvStatus);
            tvDate                   =view.findViewById(R.id.tvDate);



        }
    }
    public Adapter_money_transfer_history(Context mContext, ArrayList<HashMap<String,String>> rechargehistorylist) {
        this.mContext = mContext;
        this.rechargehistorylist = rechargehistorylist;
        pref=new Preferences(mContext);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_money_trnasfer, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tvMemberId.setText(pref.get(AppSettings.UserId));
        holder.tvWalletType.setText(rechargehistorylist.get(position).get("WalletType"));
        holder.tvCurrentWallet.setText("\u20B9"+rechargehistorylist.get(position).get("CurrentWallet"));
        holder.tvTransferAmount.setText("\u20B9"+rechargehistorylist.get(position).get("TransferAmount"));

        double closing_balance=Double.valueOf(rechargehistorylist.get(position).get("CurrentWallet"))-Double.valueOf(rechargehistorylist.get(position).get("TransferAmount"));
        holder.tvClosingBalance.setText("\u20B9"+closing_balance);
        holder.tvStatus.setText(rechargehistorylist.get(position).get("Status"));

        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        DateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = null;
        try {
            date = originalFormat.parse(rechargehistorylist.get(position).get("EntryDate"));
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
