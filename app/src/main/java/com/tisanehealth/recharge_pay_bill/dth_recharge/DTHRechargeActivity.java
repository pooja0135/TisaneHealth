package com.tisanehealth.recharge_pay_bill.dth_recharge;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import com.tisanehealth.Activity.DashBoardActivity;
import com.tisanehealth.Adapter.recharge.Adapter_dth;
import com.tisanehealth.Adapter.recharge.Adapter_viewpager;
import com.tisanehealth.Helper.MovingViewPager;
import com.tisanehealth.Model.recharge.DTHModel;
import com.tisanehealth.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DTHRechargeActivity extends AppCompatActivity {
    //RelativeLayout
    RelativeLayout rlBack;
    //RecyclerView
    RecyclerView recyclerviewDTH;
    //ViewPager
    MovingViewPager viewpagerDTH;
    //Adapter
    Adapter_viewpager adapter_viewpager;
    Adapter_dth adapter_dth;
    //Arraylist
    List<DTHModel> dthlist = new ArrayList<>();
    //Model
    DTHModel dthModel;

    int[] image = new int[]{
            R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3,
            R.drawable.banner4,
    };


    int currentPage = 1;
    int NUM_PAGES = 0;
    Timer timer;
    final long DELAY_MS = 2000;
    final long PERIOD_MS = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dthrecharge);


        //RelativeLayout
        rlBack            =findViewById(R.id.rlBack);
        //Viewpager
        viewpagerDTH      =findViewById(R.id.viewpagerDTH);
        //Recylerview
        recyclerviewDTH   =findViewById(R.id.recylerviewDTH);

        adapter_viewpager = new Adapter_viewpager(this, image);
        viewpagerDTH.setAdapter(adapter_viewpager);
        viewpagerDTH.setCurrentItem(1, true);
        viewpagerDTH.setClipToPadding(false);

        NUM_PAGES = image.length;

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES - 1) {
                    currentPage = 1;
                }
                viewpagerDTH.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);



        dthModel=new DTHModel(R.drawable.airtel_dth,"Airtel Digital TV","23");
        dthlist.add(dthModel);

        dthModel=new DTHModel(R.drawable.dish_dth,"Dish TV","25");
        dthlist.add(dthModel);

        dthModel=new DTHModel(R.drawable.d2h_dth,"D2H","28");
        dthlist.add(dthModel);

        dthModel=new DTHModel(R.drawable.reliance_digital_dth,"Reliance Digital TV","24");
        dthlist.add(dthModel);

        dthModel=new DTHModel(R.drawable.sundirect_dth,"SUN Direct","26");
        dthlist.add(dthModel);

        dthModel=new DTHModel(R.drawable.tata_sky_dth,"Tata Sky","27");
        dthlist.add(dthModel);

        adapter_dth=new Adapter_dth(this,dthlist);
        recyclerviewDTH.setLayoutManager(new LinearLayoutManager(this));
        recyclerviewDTH.setAdapter(adapter_dth);


        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DTHRechargeActivity.this, DashBoardActivity.class));

            }
        });


    }
}
