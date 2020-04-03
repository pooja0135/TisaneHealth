package com.tisanehealth.recharge_pay_bill.gas;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tisanehealth.Adapter.recharge.Adapter_gas;
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

public class GasBillActivity extends AppCompatActivity {
    MovingViewPager vpGas;
    RecyclerView rvGas;
    RelativeLayout rlBack;
    //Adapter
    Adapter_viewpager adapter_viewpager;
    Adapter_gas adaptergas;

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
    List<RechargeListModel> gasList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_bill);

        Paper.init(this);

        rlBack = findViewById(R.id.rlBack);
        vpGas = findViewById(R.id.vpGas);
        rvGas = findViewById(R.id.rvGas);


        adapter_viewpager = new Adapter_viewpager(this, image);


        vpGas.setAdapter(adapter_viewpager);
        vpGas.setCurrentItem(1, true);
        vpGas.setClipToPadding(false);

        NUM_PAGES = image.length;

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES - 1) {
                    currentPage = 1;
                }
                vpGas.setCurrentItem(currentPage++, true);
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


        gasList = Utils.GetRechargeValue("Gas");

        adaptergas = new Adapter_gas(this, gasList);
        rvGas.setLayoutManager(new LinearLayoutManager(this));
        rvGas.setAdapter(adaptergas);


    }


}
