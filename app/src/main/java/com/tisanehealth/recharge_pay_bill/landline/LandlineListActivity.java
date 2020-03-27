package com.tisanehealth.recharge_pay_bill.landline;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tisanehealth.Adapter.recharge.Adapter_landline;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.Model.recharge.RechargeListModel;
import com.tisanehealth.R;

import java.util.ArrayList;
import java.util.List;

public class LandlineListActivity extends AppCompatActivity {

    ImageView ivBack;
    RecyclerView rvLandlineList;

    List<RechargeListModel> landlinelist=new ArrayList<>();
    Adapter_landline adapter_landline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landline_list);

        ivBack=findViewById(R.id.ivBack);

        rvLandlineList=findViewById(R.id.rvLandlineList);
        rvLandlineList.setLayoutManager(new LinearLayoutManager(this));


        landlinelist= Utils.GetRechargeValue("Landline");


        adapter_landline=new Adapter_landline(this,landlinelist);
        rvLandlineList.setLayoutManager(new LinearLayoutManager(this));
        rvLandlineList.setAdapter(adapter_landline);


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
