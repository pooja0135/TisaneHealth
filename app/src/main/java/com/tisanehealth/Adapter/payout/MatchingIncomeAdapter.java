package com.tisanehealth.Adapter.payout;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tisanehealth.Model.payout.MatchIncomeModel;
import com.tisanehealth.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MatchingIncomeAdapter extends RecyclerView.Adapter<MatchingIncomeAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<MatchIncomeModel> matchincomelist;

    public class MyViewHolder extends RecyclerView.ViewHolder {

       TextView tvMemberId;
       TextView tvMatchingBusiness;
       TextView tvMatchingIncome;
       TextView tvGeneratedDate;
       TextView tvLeftTeamBV;
       TextView tvRightTeamBV;
       TextView tvCFLeftBV;
       TextView tvCFRightBV;
       TextView tvPayableAmount;
       TextView tvOverFlow;

        public MyViewHolder(View view) {
            super(view);


            tvMemberId               =view.findViewById(R.id.tvMemberId);
            tvGeneratedDate          =view.findViewById(R.id.tvGeneratedDate);
            tvMatchingBusiness       =view.findViewById(R.id.tvMatchingBusiness);
            tvMatchingIncome         =view.findViewById(R.id.tvMatchingIncome);
            tvLeftTeamBV            =view.findViewById(R.id.tvLeftTeamBV);
            tvRightTeamBV           =view.findViewById(R.id.tvRightTeamBV);
            tvCFLeftBV              =view.findViewById(R.id.tvCFLeftBV);
            tvCFRightBV             =view.findViewById(R.id.tvCFRightBV);
            tvOverFlow              =view.findViewById(R.id.tvOverFlow);
            tvPayableAmount         =view.findViewById(R.id.tvPayableAmount);

        }
    }


    public MatchingIncomeAdapter(Context mContext, ArrayList<MatchIncomeModel> matchincomelist) {
        this.mContext = mContext;
        this.matchincomelist=matchincomelist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_matching_report, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tvCFLeftBV.setText(matchincomelist.get(position).getCf_left_bv());
        holder.tvCFRightBV.setText(matchincomelist.get(position).getCf_right_bv());
        holder.tvMemberId.setText(matchincomelist.get(position).getMember_id());
        holder.tvMatchingBusiness.setText(matchincomelist.get(position).getMatching_business());
        holder.tvMatchingIncome.setText(matchincomelist.get(position).getMatching_incentive());
        holder.tvLeftTeamBV.setText(matchincomelist.get(position).getLeft_team_bv());
        holder.tvRightTeamBV.setText(matchincomelist.get(position).getRight_team_bv());
        holder.tvOverFlow.setText(matchincomelist.get(position).getOverflow());
        holder.tvPayableAmount.setText(matchincomelist.get(position).getPayable_amount());


        DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = null;
        try {
            date = originalFormat.parse(matchincomelist.get(position).getEntered_date());
        } catch (Exception e) {

            //Log.v("hhhhhhhhhhhhh",e.toString());
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);
        holder.tvGeneratedDate.setText(formattedDate);


    }


    @Override
    public int getItemCount() {
        return matchincomelist.size();
    }





}

