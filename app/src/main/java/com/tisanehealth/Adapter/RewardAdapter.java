package com.tisanehealth.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tisanehealth.R;

import java.util.ArrayList;
import java.util.HashMap;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<HashMap<String,String>> rewardlist;

    public class MyViewHolder extends RecyclerView.ViewHolder {

       TextView tvSNo,tvBusiness,tvRewards,tvStatus,tvQualifiedDate,tvPost,tvRewardAmount;

        public MyViewHolder(View view) {
            super(view);


            tvSNo               =view.findViewById(R.id.tvSNo);
            tvPost               =view.findViewById(R.id.tvPost);
            tvBusiness           =view.findViewById(R.id.tvBusiness);
            tvRewards            =view.findViewById(R.id.tvRewards);
            tvStatus            =view.findViewById(R.id.tvStatus);
            tvRewardAmount         =view.findViewById(R.id.tvRewardAmount);
            tvQualifiedDate      =view.findViewById(R.id.tvQualifiedDate);

        }
    }


    public RewardAdapter(Context mContext,  ArrayList<HashMap<String,String>> rewardlist) {
        this.mContext = mContext;
        this.rewardlist=rewardlist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_reward, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,  int position) {

        int pos=position+1;

        holder.tvSNo.setText(String.valueOf(pos));
        holder.tvPost.setText(rewardlist.get(position).get("Post"));
        holder.tvRewardAmount.setText(rewardlist.get(position).get("RewardAmount"));
        holder.tvBusiness.setText(rewardlist.get(position).get("Business"));
        holder.tvRewards.setText(rewardlist.get(position).get("Rewards"));

        if(rewardlist.get(position).get("Status").equals("True"))
        {
            holder.tvStatus.setText("Qualified");
        }
        else
        {
            holder.tvStatus.setText("Not Qualified");
        }

        holder.tvQualifiedDate.setText(rewardlist.get(position).get("QualifiedDate"));



        Log.v("ggghghbbbbggjhjhj",rewardlist.get(position).get("QualifiedDate"));


    }


    @Override
    public int getItemCount() {
        return rewardlist.size();
    }





}

