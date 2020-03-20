package com.tisanehealth.Adapter.payout;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tisanehealth.Model.payout.LeaderIncomeModel;
import com.tisanehealth.Model.payout.LeaderIncomeModel;
import com.tisanehealth.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LeaderIncomeAdapter extends RecyclerView.Adapter<LeaderIncomeAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<LeaderIncomeModel> directincomelist;

    public class MyViewHolder extends RecyclerView.ViewHolder {

       TextView tvMemberId;
       TextView tvName;
       TextView tvPayableAmount;
       TextView tvLeftTeamMBP;
       TextView tvRightTeamMBP;
       TextView tvGeneratedDate;

        public MyViewHolder(View view) {
            super(view);


            tvMemberId               =view.findViewById(R.id.tvMemberId);
            tvName                   =view.findViewById(R.id.tvName);
            tvGeneratedDate          =view.findViewById(R.id.tvGeneratedDate);
            tvPayableAmount       =view.findViewById(R.id.tvPayableAmount);
            tvLeftTeamMBP         =view.findViewById(R.id.tvLeftTeamMBP);
            tvRightTeamMBP         =view.findViewById(R.id.tvRightTeamMBP);

        }
    }


    public LeaderIncomeAdapter(Context mContext, ArrayList<LeaderIncomeModel> directincomelist) {
        this.mContext = mContext;
        this.directincomelist=directincomelist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_leader_income_report, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


          holder.tvPayableAmount.setText(directincomelist.get(position).getPayableAmount());
          holder.tvMemberId.setText(directincomelist.get(position).getMemberId());
          holder.tvName.setText(directincomelist.get(position).getName());
          holder.tvLeftTeamMBP.setText(directincomelist.get(position).getLeftTeamMBP());
          holder.tvRightTeamMBP.setText(directincomelist.get(position).getRightTeamMBP());

        DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
        DateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = null;
        try {
            date = originalFormat.parse(directincomelist.get(position).getPaidDate());
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

