package com.tisanehealth.Adapter.pin;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tisanehealth.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PinTransferHistoryAdapter extends RecyclerView.Adapter<PinTransferHistoryAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<HashMap<String,String>> transferlist;

    public class MyViewHolder extends RecyclerView.ViewHolder {

       TextView tvParticular;
       TextView tvSend;
       TextView tvDate;


        public MyViewHolder(View view) {
            super(view);

            tvDate                 =view.findViewById(R.id.tvDate);
            tvSend                 =view.findViewById(R.id.tvSend);
            tvParticular           =view.findViewById(R.id.tvParticular);
        }
    }


    public PinTransferHistoryAdapter(Context mContext, ArrayList<HashMap<String,String>> transferlist) {
        this.mContext = mContext;
        this.transferlist=transferlist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_pin_transfer_history, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
        DateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = null;
        try {
            date = originalFormat.parse(transferlist.get(position).get("Date"));
        } catch (Exception e) {

            //Log.v("hhhhhhhhhhhhh",e.toString());
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);

        holder.tvParticular.setText(transferlist.get(position).get("Particulars"));
        holder.tvDate.setText(formattedDate);
        if (transferlist.get(position).get("Received").equals("0"))
        {
            holder.tvSend.setText("-"+transferlist.get(position).get("Send"));
            holder.tvSend.setTextColor(mContext.getResources().getColor(R.color.red_600));
        }
        else
        {
            holder.tvSend.setText("+"+transferlist.get(position).get("Received"));
            holder.tvSend.setTextColor(mContext.getResources().getColor(R.color.green_600));
        }

    }


    @Override
    public int getItemCount() {
        return transferlist.size();
    }





}

