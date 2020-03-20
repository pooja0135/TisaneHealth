package com.tisanehealth.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.tisanehealth.Adapter.Adapter_dealwithus;
import com.tisanehealth.R;

import java.util.ArrayList;
import java.util.List;

public class DealWithUsActivity extends AppCompatActivity {

    ImageView ivBack;
    RecyclerView rvDealwithus;
    List<Integer> arraylist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_with_us);

        ivBack        =findViewById(R.id.ivBack);
        rvDealwithus  =findViewById(R.id.rvDealwithus);

        arraylist.add( R.drawable.flipkart);
        arraylist.add( R.drawable.amazon);
        arraylist.add( R.drawable.myntra);
        arraylist.add( R.drawable.swiggy);
        arraylist.add( R.drawable.zomato);

        rvDealwithus.setLayoutManager(new GridLayoutManager(this,2));
        rvDealwithus.setAdapter(new Adapter_dealwithus(this,arraylist));


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
