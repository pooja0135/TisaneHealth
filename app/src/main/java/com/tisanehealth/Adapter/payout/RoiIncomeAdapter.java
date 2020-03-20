package com.tisanehealth.Adapter.payout;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tisanehealth.Model.payout.RoiIncomeModel;
import com.tisanehealth.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RoiIncomeAdapter extends RecyclerView.Adapter<RoiIncomeAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<RoiIncomeModel> roiincomelist;

    public class MyViewHolder extends RecyclerView.ViewHolder {

       TextView tvMemberId;
       TextView tvPaymentMonth;
       TextView tvPaidMonth;
       TextView tvPaidAmount;
       TextView tvEnteredDate;
       TextView tvPaymentPerMonth;

        public MyViewHolder(View view) {
            super(view);


            tvMemberId               =view.findViewById(R.id.tvMemberId);
            tvPaymentMonth           =view.findViewById(R.id.tvPaymentMonth);
            tvPaymentPerMonth          =view.findViewById(R.id.tvPaymentPerMonth);
            tvPaidMonth       =view.findViewById(R.id.tvPaidMonth);
            tvPaidAmount         =view.findViewById(R.id.tvPaidAmount);
            tvEnteredDate         =view.findViewById(R.id.tvEnteredDate);

        }
    }


    public RoiIncomeAdapter(Context mContext, ArrayList<RoiIncomeModel> roiincomelist) {
        this.mContext = mContext;
        this.roiincomelist=roiincomelist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_roi_income, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {



        holder.tvMemberId.setText(roiincomelist.get(position).getMember_id());
        holder.tvPaymentMonth.setText(roiincomelist.get(position).getPayment_month());
        holder.tvPaymentPerMonth.setText("\u20B9"+roiincomelist.get(position).getPayment_per_month());
        holder.tvPaidMonth.setText(roiincomelist.get(position).getPaid_month());
        holder.tvPaidAmount.setText("\u20B9"+roiincomelist.get(position).getPaid_amount());


        DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
        DateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = null;
        try {
            date = originalFormat.parse(roiincomelist.get(position).getEntered_date());
        } catch (Exception e) {

            Log.v("hhhhhhhhhhhhh",e.toString());
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);
        holder.tvEnteredDate.setText(formattedDate);


    }


    @Override
    public int getItemCount() {
        return roiincomelist.size();
    }





}

