package com.tisanehealth.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tisanehealth.Model.DailyBusinessListModel;
import com.tisanehealth.R;

import java.util.ArrayList;

public class DailyBusinessReportAdapter extends RecyclerView.Adapter<DailyBusinessReportAdapter.DailyBussRepViewHolder> {

    ArrayList<DailyBusinessListModel> banklist;

    public class DailyBussRepViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate;
        TextView tvSno;
        TextView tvLeftBp;
        TextView tvRightBp;
        TextView tvTotal;

        public DailyBussRepViewHolder(View view) {
            super(view);

            tvDate = view.findViewById(R.id.tvDate);
            tvSno = view.findViewById(R.id.tvSno);
            tvLeftBp = view.findViewById(R.id.tvLeftBp);
            tvRightBp = view.findViewById(R.id.tvRightBp);
            tvTotal = view.findViewById(R.id.tvTotal);
        }
    }


    public DailyBusinessReportAdapter(ArrayList<DailyBusinessListModel> banklist) {
        this.banklist = banklist;
    }

    @Override
    public DailyBussRepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_business_report, parent, false);

        return new DailyBussRepViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DailyBussRepViewHolder holder, final int position) {

        holder.tvDate.setText(banklist.get(position).getDate());
        holder.tvSno.setText(banklist.get(position).getSno());
        holder.tvLeftBp.setText(banklist.get(position).getLeft());
        holder.tvRightBp.setText(banklist.get(position).getRight());
        holder.tvTotal.setText(banklist.get(position).getTotal());

    }


    @Override
    public int getItemCount() {
        return banklist.size();
    }


}
