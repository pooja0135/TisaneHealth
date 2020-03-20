package com.tisanehealth.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tisanehealth.R;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<String> newslist;

    public class MyViewHolder extends RecyclerView.ViewHolder {

       TextView tvNews;

        public MyViewHolder(View view) {
            super(view);


            tvNews               =view.findViewById(R.id.tvNews);







        }
    }


    public NewsAdapter(Context mContext, ArrayList<String> newslist) {
        this.mContext = mContext;
        this.newslist=newslist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_viewpager1, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.tvNews.setText(newslist.get(position));


    }


    @Override
    public int getItemCount() {
        return newslist.size();
    }





}

