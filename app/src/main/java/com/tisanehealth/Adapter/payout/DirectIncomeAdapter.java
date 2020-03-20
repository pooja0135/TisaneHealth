package com.tisanehealth.Adapter.payout;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tisanehealth.Model.payout.DirectIncomeModel;
import com.tisanehealth.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DirectIncomeAdapter extends RecyclerView.Adapter<DirectIncomeAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<DirectIncomeModel> directincomelist;

    public class MyViewHolder extends RecyclerView.ViewHolder {

       TextView tvMemberId;
       TextView tvName;
       TextView tvDirectBusiness;
       TextView tvDirectIncome;
       TextView tvGeneratedDate;

        public MyViewHolder(View view) {
            super(view);


            tvMemberId               =view.findViewById(R.id.tvMemberId);
            tvName                   =view.findViewById(R.id.tvName);
            tvGeneratedDate          =view.findViewById(R.id.tvGeneratedDate);
            tvDirectBusiness       =view.findViewById(R.id.tvDirectBusiness);
            tvDirectIncome         =view.findViewById(R.id.tvDirectIncome);

        }
    }


    public DirectIncomeAdapter(Context mContext, ArrayList<DirectIncomeModel> directincomelist) {
        this.mContext = mContext;
        this.directincomelist=directincomelist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_direct_report, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


          holder.tvDirectBusiness.setText(directincomelist.get(position).getDirect_business());
          holder.tvDirectIncome.setText(directincomelist.get(position).getDirect_income());
          holder.tvName.setText(directincomelist.get(position).getName());
          holder.tvMemberId.setText(directincomelist.get(position).getMember_id());

        DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
        DateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = null;
        try {
            date = originalFormat.parse(directincomelist.get(position).getGenerate_date());
        } catch (Exception e) {

            Log.v("hhhhhhhhhhhhh",e.toString());
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);
        holder.tvGeneratedDate.setText(formattedDate);


    }


    @Override
    public int getItemCount() {
        return directincomelist.size();
    }





}

