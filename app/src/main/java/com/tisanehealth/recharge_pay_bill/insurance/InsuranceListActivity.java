package com.tisanehealth.recharge_pay_bill.insurance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.tisanehealth.Adapter.recharge.Adapter_insurance;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.Model.recharge.RechargeListModel;
import com.tisanehealth.R;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class InsuranceListActivity extends AppCompatActivity {

    RecyclerView rvInsurance;
    Adapter_insurance adapter_insurance;
    List<RechargeListModel> insurancelist=new ArrayList<>();
    ImageView ivBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_list);
        Paper.init(this);

        ivBack=findViewById(R.id.ivBack);
        rvInsurance=findViewById(R.id.rvInsurance);
        insurancelist= Utils.GetRechargeValue("Insurance");

        adapter_insurance=new Adapter_insurance(this,insurancelist);
        rvInsurance.setLayoutManager(new LinearLayoutManager(this));
        rvInsurance.setAdapter(adapter_insurance);


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
