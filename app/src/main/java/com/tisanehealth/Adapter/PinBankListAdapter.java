package com.tisanehealth.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tisanehealth.Model.PinBankModel;
import com.tisanehealth.R;

import java.util.ArrayList;

public class PinBankListAdapter extends RecyclerView.Adapter<PinBankListAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<PinBankModel> paybanklist;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvPin;
        TextView tvPinType;
        TextView tvStatus;
        TextView tvAssignTo;
        TextView tvGeneratedDate;
        TextView tvUsedName;
        TextView tvUsedDate;
        public MyViewHolder(View view) {
            super(view);


            tvPin=view.findViewById(R.id.tvPin);
            tvPinType=view.findViewById(R.id.tvPinType);
            tvStatus=view.findViewById(R.id.tvStatus);
            tvAssignTo=view.findViewById(R.id.tvAssignTo);
            tvGeneratedDate=view.findViewById(R.id.tvGeneratedDate);
            tvUsedName=view.findViewById(R.id.tvUsedName);
            tvUsedDate=view.findViewById(R.id.tvUsedDate);




        }
    }


    public PinBankListAdapter(Context mContext, ArrayList<PinBankModel>paybanklist) {
        this.mContext = mContext;
        this.paybanklist=paybanklist;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_pin_bank_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tvPin.setText(paybanklist.get(position).getPin());
      holder.tvPinType.setText(paybanklist.get(position).getPinType());
      holder.tvStatus.setText(paybanklist.get(position).getStatus());
      holder.tvAssignTo.setText(paybanklist.get(position).getAssignTo());
      holder.tvGeneratedDate.setText(paybanklist.get(position).getGenerateDate());
      holder.tvUsedName.setText(paybanklist.get(position).getUsedName());
      holder.tvUsedDate.setText(paybanklist.get(position).getUsedDate());

    }


    @Override
    public int getItemCount() {
        return paybanklist.size();
    }





}

