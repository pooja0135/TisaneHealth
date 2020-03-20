package com.tisanehealth.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tisanehealth.Model.PayoutDetailModel;
import com.tisanehealth.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PayoutDetailAdapter extends RecyclerView.Adapter<PayoutDetailAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<PayoutDetailModel> payoutlist;

    public class MyViewHolder extends RecyclerView.ViewHolder {

       TextView tvDSDID,tvBV,tvDate;

        public MyViewHolder(View view) {
            super(view);


            tvDSDID               =view.findViewById(R.id.tvDSDID);
            tvBV                 =view.findViewById(R.id.tvBV);
            tvDate           =view.findViewById(R.id.tvDate);





        }
    }


    public PayoutDetailAdapter(Context mContext, ArrayList<PayoutDetailModel>payoutlist) {
        this.mContext = mContext;
        this.payoutlist=payoutlist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_payout_detail, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


    }


    @Override
    public int getItemCount() {
        return 6;
    }


    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern,Locale.ENGLISH);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (Exception e) {


            e.printStackTrace();
        }
        return str;
    }




}

