package com.tisanehealth.recharge_pay_bill.broadband;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.tisanehealth.Adapter.recharge.Adapter_broadband;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.Model.recharge.RechargeListModel;
import com.tisanehealth.R;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class BroadbandListActivity extends AppCompatActivity {

    RecyclerView rvBroadband;
    Adapter_broadband adapter_broadband;
    List<RechargeListModel> broadbandlist=new ArrayList<>();
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadband_list);
        Paper.init(this);

        ivBack=findViewById(R.id.ivBack);
        rvBroadband=findViewById(R.id.rvBroadband);
        broadbandlist= Utils.GetRechargeValue("Broadband");
        Log.v("fhjfhfhjfhfhfhhf", String.valueOf(broadbandlist.size()));

        adapter_broadband=new Adapter_broadband(this,broadbandlist);
        rvBroadband.setLayoutManager(new LinearLayoutManager(this));
        rvBroadband.setAdapter(adapter_broadband);


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
