package com.tisanehealth.Adapter.recharge;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tisanehealth.R;


public class Adapter_viewpager extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;

    int[] image;
    public static int LOOPS_COUNT = 1000;

    public Adapter_viewpager(Context context,  int[] image) {
        mContext = context;
        this. image = image;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
      //  return imagelist.size();

        if (image != null && image.length > 0)
        {
            return image.length*LOOPS_COUNT; // simulate infinite by big number of products
        }
        else
        {
            return 1;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.inflate_viewpager, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageview);
      /*  Picasso.get()
                .load(ImageUrl+imagelist.get(position))
                .placeholder(R.drawable.giant_swing)
                .error(R.drawable.giant_swing)
                .into( imageView);
        container.addView(itemView);*/


        if (image != null &&image.length > 0)
        {
            position = position % image.length; // use modulo for infinite cycling


           try{
               imageView.setImageResource(image[position]);
               container.addView(itemView);
           }
           catch(Exception e)
           {

           }

        }
        else
        {
          //  return MyFragment.newInstance(null);
        }

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
