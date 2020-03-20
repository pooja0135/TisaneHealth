package com.tisanehealth.recharge_pay_bill.mobile_recharge;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tisanehealth.Adapter.Adapter_recharge;
import com.tisanehealth.Model.RechargeModel;
import com.tisanehealth.R;

import java.util.ArrayList;

public class Recharge extends AppCompatActivity {


    int[] covers = new int[]{
            R.drawable.ic_mobile,
            R.drawable.ic_mobile1,
            R.drawable.ic_electricity,
            R.drawable.ic_satellite,
            R.drawable.ic_telephone,
            R.drawable.ic_datacard,
            R.drawable.ic_insurance,
            R.drawable.ic_broadband,
            R.drawable.ic_money_transfer};

    ImageView ivBack;
    RecyclerView recyclerviewDashboard;
    Adapter_recharge adapter_recharge;
    ArrayList<RechargeModel> rechargelist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        ivBack=findViewById(R.id.ivBack);
        recyclerviewDashboard=findViewById(R.id.recyclerviewDashboard);


        RechargeModel a = new RechargeModel( "Mobile Prepaid", covers[0]);
        rechargelist.add(a);

        a = new RechargeModel( "Mobile Postpaid", covers[1]);
        rechargelist.add(a);

        a = new RechargeModel( "Electricity", covers[2]);
        rechargelist.add(a);

        a = new RechargeModel( "DTH", covers[3]);
        rechargelist.add(a);

        a = new RechargeModel( "Landline", covers[4]);
        rechargelist.add(a);

        a = new RechargeModel( "DataCard", covers[5]);
        rechargelist.add(a);

        a = new RechargeModel( "Insurence", covers[6]);
        rechargelist.add(a);

        a = new RechargeModel( "Broadband", covers[7]);
        rechargelist.add(a);

        a = new RechargeModel( "Money Transfer", covers[7]);
        rechargelist.add(a);


        adapter_recharge=new Adapter_recharge(this,rechargelist);
        recyclerviewDashboard.setLayoutManager(new GridLayoutManager(this,4));
        recyclerviewDashboard.setAdapter(adapter_recharge);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
