package com.tisanehealth.Fragment.Team;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.tisanehealth.R;

import static com.tisanehealth.Activity.DashBoardActivity.ivLogo;
import static com.tisanehealth.Activity.DashBoardActivity.rlHeader;
import static com.tisanehealth.Activity.DashBoardActivity.tvHeader;


public class TreeviewFragment extends Fragment implements View.OnClickListener {


    //View
    View v;




    //fragment
    public static   Class fragmentClass;
    public static Fragment fragment;

    LinearLayout linearLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.tree_view2, container, false);





        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("Tree View Associate");
        ivLogo.setVisibility(View.GONE);
        tvHeader.setVisibility(View.VISIBLE);

        //Back
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        return true;
                    }
                }
                return false;
            }
        });


        linearLayout=v.findViewById(R.id.linearLayout);
        int width=getScreenWidth(getActivity());

        int childCount=linearLayout.getChildCount();
//        for (int i=0;i<childCount;i++){
//            RelativeLayout button= (RelativeLayout) linearLayout.getChildAt(i);
//            button.setScrollBarSize(width);
//
//        }
//

        return v;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {

        }

    }



    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
}


  /*  public void link(TextView textView, final Class frgmnt,final int clr)
    {

        textView.setMovementMethod(LinkMovementMethod.getInstance());

        Spannable spans = (Spannable) textView.getText();
        ClickableSpan clickSpan = new ClickableSpan() {

            @Override
            public void onClick(View widget)
            {
//
                try {
                    fragmentClass=frgmnt;

                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (fragment != null) {
                   // FragmentManager fragmentManager = getFragmentManager();
                 //   fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_left, R.anim.slide_right).replace(R.id.frameLayout, fragment).commit();
                }

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                // ds.setColor(Color.RED);

                int color = ContextCompat.getColor(getActivity(), clr);
                ds.setColor(color);
                ds.setUnderlineText(true);
            }
        };

        spans.setSpan(clickSpan, 0, spans.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


    }*/


