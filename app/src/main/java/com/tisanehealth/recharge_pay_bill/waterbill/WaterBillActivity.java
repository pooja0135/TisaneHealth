package com.tisanehealth.recharge_pay_bill.waterbill;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tisanehealth.Adapter.recharge.Adapter_viewpager;
import com.tisanehealth.Adapter.recharge.Adapter_water;
import com.tisanehealth.Helper.MovingViewPager;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.Model.recharge.RechargeListModel;
import com.tisanehealth.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

public class WaterBillActivity extends AppCompatActivity {
    MovingViewPager vpWater;
    RecyclerView rvWater;
    RelativeLayout rlBack;
    //Adapter
    Adapter_viewpager adapter_viewpager;
    Adapter_water adapterwater;

    Button btnConfirm;

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
    List<RechargeListModel> waterList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_bill);

        Paper.init(this);

        rlBack = findViewById(R.id.rlBack);
        vpWater = findViewById(R.id.vpWater);
        rvWater = findViewById(R.id.rvWater);


        adapter_viewpager = new Adapter_viewpager(this, image);


        vpWater.setAdapter(adapter_viewpager);
        vpWater.setCurrentItem(1, true);
        vpWater.setClipToPadding(false);

        NUM_PAGES = image.length;

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES - 1) {
                    currentPage = 1;
                }
                vpWater.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);


        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                // startActivity(new Intent(ElectricityBillActivity.this, DashBoardActivity.class));
            }
        });


        waterList = Utils.GetRechargeValue("Water");

        adapterwater = new Adapter_water(this, waterList);
        rvWater.setLayoutManager(new LinearLayoutManager(this));
        rvWater.setAdapter(adapterwater);


    }


}
