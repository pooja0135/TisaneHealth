package com.tisanehealth.Adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tisanehealth.R;

import java.util.ArrayList;

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<String> newslist;
    LayoutInflater mLayoutInflater;

    public CustomPagerAdapter(Context context,ArrayList<String> newslist) {
        this.mContext = context;
        this.newslist = newslist;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView =LayoutInflater.from(container.getContext()).inflate(R.layout.inflate_viewpager1, container, false);

        TextView tvNews=itemView.findViewById(R.id.tvNews);

        Log.v("fgnjghjghjghgjh", String.valueOf(newslist.get(position)));
        tvNews.setText(newslist.get(position));


        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return newslist.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return newslist.get(position);
    }

}