package com.tisanehealth.Adapter;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tisanehealth.Model.TeamRepurchaseModel;
import com.tisanehealth.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TeamRepurchaseAdapter extends RecyclerView.Adapter<TeamRepurchaseAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<TeamRepurchaseModel>repurchaselist;
    int selectedPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate, tvInvoiceNumber,tvCenter,tvAmount,tvBusinessvalue,tvMemberId,tvMemberName,tvGrossAmount,tvSGStAmount,tvCGSTAmount,tvTotalAmount;
        ImageView ivInformation;
        CardView cardView;
        RelativeLayout relativeLayout;

        public MyViewHolder(View view) {
            super(view);

            tvDate                 =view.findViewById(R.id.tvDate);
            tvInvoiceNumber        =view.findViewById(R.id.tvInvoice);
            tvCenter               =view.findViewById(R.id.tvCenter);
            tvAmount               =view.findViewById(R.id.tvAmount);
            tvBusinessvalue        =view.findViewById(R.id.tvBusinessValue);
            tvMemberId             =view.findViewById(R.id.tvMemberId);
            tvMemberName           =view.findViewById(R.id.tvMemberName);
            tvGrossAmount          =view.findViewById(R.id.tvGrossAmount);
            tvSGStAmount           =view.findViewById(R.id.tvSgstAmount);
            tvCGSTAmount           =view.findViewById(R.id.tvCGSTAmount);
            tvTotalAmount           =view.findViewById(R.id.tvTotalAmount);

            ivInformation          =view.findViewById(R.id.ivInformation);
            cardView               =view.findViewById(R.id.cardview);

            relativeLayout         =view.findViewById(R.id.relativelayout);

        }
    }


    public TeamRepurchaseAdapter(Context mContext, ArrayList<TeamRepurchaseModel>repurchaselist) {
        this.mContext = mContext;
        this.repurchaselist=repurchaselist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_team_repurchase_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if(selectedPosition == position){
            holder.cardView.setVisibility(View.VISIBLE);
        }
        else{
            holder.cardView.setVisibility(View.GONE);
        }

        holder.tvAmount.setText("\u20B9"+repurchaselist.get(position).getAmount());
        holder.tvTotalAmount.setText("\u20B9"+repurchaselist.get(position).getAmount());
        holder.tvGrossAmount.setText("\u20B9"+repurchaselist.get(position).getGrossAmt());
        holder.tvSGStAmount.setText("\u20B9"+repurchaselist.get(position).getSGSTAmt());
        holder.tvCGSTAmount.setText("\u20B9"+repurchaselist.get(position).getCGSTAmt());


        holder.tvBusinessvalue.setText("\u20B9"+repurchaselist.get(position).getTotalBV());
        holder.tvInvoiceNumber.setText(repurchaselist.get(position).getInvoiceNo());
        holder.tvCenter.setText(repurchaselist.get(position).getCenterId());
        holder.tvMemberId.setText(repurchaselist.get(position).getMemberId());
        holder.tvMemberName.setText(repurchaselist.get(position).getMemberName());
        holder.tvDate.setText(parseDateToddMMyyyy(repurchaselist.get(position).getDate()));

        holder.ivInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cardView.setVisibility(View.VISIBLE);
                selectedPosition=position;
                notifyDataSetChanged();
            }
        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cardView.setVisibility(View.GONE);
                selectedPosition=-1;
                notifyDataSetChanged();
            }
        });

    }


    @Override
    public int getItemCount() {
        return repurchaselist.size();
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

