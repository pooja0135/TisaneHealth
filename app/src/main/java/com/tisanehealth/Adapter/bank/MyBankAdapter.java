package com.tisanehealth.Adapter.bank;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tisanehealth.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MyBankAdapter extends RecyclerView.Adapter<MyBankAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<HashMap<String,String>> banklist;

    public class MyViewHolder extends RecyclerView.ViewHolder {

       TextView tvParticular;
       TextView tvCredit;
       TextView tvDate;
       TextView tvTotalAmount;

        public MyViewHolder(View view) {
            super(view);

            tvDate                 =view.findViewById(R.id.tvDate);
            tvCredit               =view.findViewById(R.id.tvCredit);
            tvTotalAmount          =view.findViewById(R.id.tvTotalAmount);
            tvParticular           =view.findViewById(R.id.tvParticular);
        }
    }


    public MyBankAdapter(Context mContext,ArrayList<HashMap<String,String>> banklist) {
        this.mContext = mContext;
        this.banklist=banklist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_bank_detail, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tvParticular.setText(banklist.get(position).get("Particulars"));
        holder.tvTotalAmount.setText("Balance :\u20B9"+banklist.get(position).get("Balance"));
        holder.tvDate.setText("Date:"+banklist.get(position).get("TransactionDate"));
        if (banklist.get(position).get("Debit").equals("0.00"))
        {
            holder.tvCredit.setText("+\u20B9"+banklist.get(position).get("Credit"));
            holder.tvCredit.setTextColor(mContext.getResources().getColor(R.color.green_600));
        }
        else
        {
            holder.tvCredit.setText("-\u20B9"+banklist.get(position).get("Debit"));
            holder.tvCredit.setTextColor(mContext.getResources().getColor(R.color.red_600));
        }

    }


    @Override
    public int getItemCount() {
        return banklist.size();
    }





}

