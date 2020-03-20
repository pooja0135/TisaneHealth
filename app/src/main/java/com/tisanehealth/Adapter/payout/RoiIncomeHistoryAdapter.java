package com.tisanehealth.Adapter.payout;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tisanehealth.Model.payout.RoiIncomeHistoryModel;
import com.tisanehealth.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RoiIncomeHistoryAdapter extends RecyclerView.Adapter<RoiIncomeHistoryAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<RoiIncomeHistoryModel> roiincomelist;

    public class MyViewHolder extends RecyclerView.ViewHolder {

       TextView tvMemberId;
       TextView tvPaidAmount;
       TextView tvPaidMonth;
       TextView tvPaymentMonth;
       TextView tvPaymentPerMonth;
       TextView tvGeneratedDate;

        public MyViewHolder(View view) {
            super(view);

            tvMemberId               =view.findViewById(R.id.tvMemberId);
            tvPaidAmount                   =view.findViewById(R.id.tvPaidAmount);
            tvGeneratedDate          =view.findViewById(R.id.tvGeneratedDate);
            tvPaidMonth                =view.findViewById(R.id.tvPaidMonth);
            tvPaymentMonth         =view.findViewById(R.id.tvPaymentMonth);
            tvPaymentPerMonth         =view.findViewById(R.id.tvPaymentPerMonth);

        }
    }


    public RoiIncomeHistoryAdapter(Context mContext, ArrayList<RoiIncomeHistoryModel> roiincomelist) {
        this.mContext = mContext;
        this.roiincomelist=roiincomelist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_roi_income_history, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


         holder.tvMemberId.setText(roiincomelist.get(position).getMember_id());
         holder.tvPaidAmount.setText(roiincomelist.get(position).getPaid_amount());
         holder.tvPaidMonth.setText(roiincomelist.get(position).getPaid_month());
         holder.tvPaymentMonth.setText(roiincomelist.get(position).getPayment_month());
         holder.tvPaymentMonth.setText(roiincomelist.get(position).getPayment_month());
         holder.tvPaymentPerMonth.setText(roiincomelist.get(position).getPayment_per_month());

        DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
        DateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = null;
        try {
            date = originalFormat.parse(roiincomelist.get(position).getGenerate_date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);
        holder.tvGeneratedDate.setText(formattedDate);


    }


    @Override
    public int getItemCount() {
        return roiincomelist.size();
    }





}

