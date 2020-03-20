package com.tisanehealth.Adapter.payout;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tisanehealth.Model.payout.RewardIncomeModel;
import com.tisanehealth.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RewardReportAdapter extends RecyclerView.Adapter<RewardReportAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<RewardIncomeModel> rewardlist;

    public class MyViewHolder extends RecyclerView.ViewHolder {

       TextView tvMemberId;
       TextView tvName;
       TextView tvReward;
       TextView tvRewardBusiness;
       TextView tvRightBusiness;
       TextView tvLeftBusiness;
       TextView tvGeneratedDate;
       TextView tvPost;

        public MyViewHolder(View view) {
            super(view);


            tvMemberId               =view.findViewById(R.id.tvMemberId);
            tvName                   =view.findViewById(R.id.tvName);
            tvPost                   =view.findViewById(R.id.tvPost);
            tvGeneratedDate          =view.findViewById(R.id.tvGeneratedDate);
            tvReward                  =view.findViewById(R.id.tvReward);
            tvRewardBusiness         =view.findViewById(R.id.tvRewardBusiness);
            tvRightBusiness           =view.findViewById(R.id.tvRightBusiness);
            tvLeftBusiness            =view.findViewById(R.id.tvLeftBusiness);

        }
    }


    public RewardReportAdapter(Context mContext, ArrayList<RewardIncomeModel> rewardlist) {
        this.mContext = mContext;
        this.rewardlist=rewardlist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_reward_report, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.tvMemberId.setText(rewardlist.get(position).getMember_id());
        holder.tvName.setText(rewardlist.get(position).getName());
        holder.tvPost.setText(rewardlist.get(position).getPost());
        holder.tvReward.setText(rewardlist.get(position).getReward());
        holder.tvRewardBusiness.setText(rewardlist.get(position).getReward_business());
        holder.tvRightBusiness.setText(rewardlist.get(position).getRight_business());
        holder.tvLeftBusiness.setText(rewardlist.get(position).getLeft_business());

        DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
        DateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = null;
        try {
            date = originalFormat.parse(rewardlist.get(position).getGenerate_date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);
        holder.tvGeneratedDate.setText(formattedDate);

    }


    @Override
    public int getItemCount() {
        return rewardlist.size();
    }





}

