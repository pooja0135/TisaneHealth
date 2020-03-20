package com.tisanehealth.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tisanehealth.Model.TeamModelClass;
import com.tisanehealth.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<TeamModelClass>teamlist;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMemberName, tvMemberId,tvSponsorId,tvRegistrationDate,tvActiveDate,tvStatus,tvPackage;
        public ImageView ivStatus;

        LinearLayout llPackage;

        public MyViewHolder(View view) {
            super(view);

            tvSponsorId         =view.findViewById(R.id.tvSponsorId);
          tvMemberId         =view.findViewById(R.id.tvMemberId);
          tvMemberName        =view.findViewById(R.id.tvTeamMemberName);
          tvRegistrationDate  =view.findViewById(R.id.tvRegistrationDate);
          tvActiveDate        =view.findViewById(R.id.tvActivationDate);
          tvStatus            =view.findViewById(R.id.tvStatus);
          tvPackage           =view.findViewById(R.id.tvPackage);

          ivStatus            =view.findViewById(R.id.ivStatus);

          llPackage           =view.findViewById(R.id.llPackage);

        }
    }


    public TeamAdapter(Context mContext, ArrayList<TeamModelClass>teamlist) {
        this.mContext = mContext;
        this.teamlist=teamlist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.team_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

          holder.tvMemberId.setText(teamlist.get(position).getMemberid());
          holder.tvMemberName.setText(teamlist.get(position).getName());
          holder.tvSponsorId.setText(teamlist.get(position).getSponsorid()+" (Sponsor ID)");
          if (teamlist.get(position).getActiveDate().isEmpty())
          {
              holder.tvActiveDate.setText("Not Activated  (Activation Date)");
          }
          else
          {
              holder.tvActiveDate.setText(parseDateToddMMyyyy(teamlist.get(position).getActiveDate())+" (Activation Date)");

          }
          holder.tvRegistrationDate.setText(parseDateToddMMyyyy(teamlist.get(position).getRegistrationDate())+" (Registration Date)");
          holder.tvPackage.setText(teamlist.get(position).getPackage_value().trim()+" (Package Amount)");

          if (teamlist.get(position).getStatus().equals("Registration"))
          {
              holder.ivStatus.setImageDrawable(mContext.getResources().getDrawable(R.drawable.group));
              holder.ivStatus.setColorFilter(mContext.getResources().getColor(R.color.yellow_800));
              holder.tvStatus.setText("Registration");

          }
         else if (teamlist.get(position).getStatus().equals("Active"))
          {
            holder.ivStatus.setImageDrawable(mContext.getResources().getDrawable(R.drawable.group));
            holder.ivStatus.setColorFilter(mContext.getResources().getColor(R.color.green_500));
            holder.tvStatus.setText("Active");

        }

          else if (teamlist.get(position).getStatus().equals("In Active"))
          {
              holder.ivStatus.setImageDrawable(mContext.getResources().getDrawable(R.drawable.group));
              holder.ivStatus.setColorFilter(mContext.getResources().getColor(R.color.yellow_800));
              holder.tvStatus.setText("In Active");

          }



         if (teamlist.get(position).getPackage_value().equals(null)||teamlist.get(position).getPackage_value().equals("null")||teamlist.get(position).getPackage_value().equals(""))
          {
              holder.llPackage.setVisibility(View.GONE);
          }
          else
          {
              holder.llPackage.setVisibility(View.VISIBLE);
          }

    }


    @Override
    public int getItemCount() {
        return teamlist.size();
    }


    public String parseDateToddMMyyyy(String time) {
      //  String inputPattern = "MM/dd/yyyy HH:mm:ss a";
        String inputPattern = "MM/dd/yyyy HH:mm:ss a";
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

