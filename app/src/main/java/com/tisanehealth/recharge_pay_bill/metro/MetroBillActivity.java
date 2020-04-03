package com.tisanehealth.recharge_pay_bill.metro;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tisanehealth.Adapter.recharge.Adapter_metro;
import com.tisanehealth.Adapter.recharge.Adapter_viewpager;
import com.tisanehealth.Helper.MovingViewPager;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.Model.recharge.RechargeListModel;
import com.tisanehealth.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

public class MetroBillActivity extends AppCompatActivity {
    MovingViewPager vpMetro;
    RecyclerView rvMetro;
    RelativeLayout rlBack;
    //Adapter
    Adapter_viewpager adapter_viewpager;
    Adapter_metro adapterMetro;

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
    List<RechargeListModel> metroList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metro_bill);

        Paper.init(this);

        rlBack = findViewById(R.id.rlBack);
        vpMetro = findViewById(R.id.vpMetro);
        rvMetro = findViewById(R.id.rvMetro);


        adapter_viewpager = new Adapter_viewpager(this, image);


        vpMetro.setAdapter(adapter_viewpager);
        vpMetro.setCurrentItem(1, true);
        vpMetro.setClipToPadding(false);

        NUM_PAGES = image.length;

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES - 1) {
                    currentPage = 1;
                }
                vpMetro.setCurrentItem(currentPage++, true);
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


        metroList = Utils.GetRechargeValue("Metro");

        adapterMetro = new Adapter_metro(this, metroList);
        rvMetro.setLayoutManager(new LinearLayoutManager(this));
        rvMetro.setAdapter(adapterMetro);


    }


}
