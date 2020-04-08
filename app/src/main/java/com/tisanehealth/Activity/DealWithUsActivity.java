package com.tisanehealth.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.tisanehealth.R;

public class DealWithUsActivity extends AppCompatActivity {

    ImageView ivBack;
    //RecyclerView rvDealwithus;
    //List<Integer> arraylist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_with_us);

        ivBack = findViewById(R.id.ivBack);
        /*rvDealwithus  =findViewById(R.id.rvDealwithus);

        arraylist.add( R.drawable.flipkart);
        arraylist.add( R.drawable.amazon);
        arraylist.add( R.drawable.myntra);
        arraylist.add( R.drawable.swiggy);
        arraylist.add( R.drawable.zomato);

        rvDealwithus.setLayoutManager(new GridLayoutManager(this,2));
        rvDealwithus.setAdapter(new Adapter_dealwithus(this,arraylist));*/


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.llFlipkart).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.flipkart.com/"));
            startActivity(browserIntent);
        });
        findViewById(R.id.llZomato).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.zomato.com/"));
            startActivity(browserIntent);
        });

        findViewById(R.id.llSwiggy).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.swiggy.com/"));
            startActivity(browserIntent);
        });

        findViewById(R.id.llAmazon).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.amazon.in/"));
            startActivity(browserIntent);
        });
        findViewById(R.id.llSnapdeal).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.snapdeal.com/"));
            startActivity(browserIntent);
        });
        findViewById(R.id.llMyntra).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.myntra.com/"));
            startActivity(browserIntent);
        });
        findViewById(R.id.llOyo).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.oyorooms.com/"));
            startActivity(browserIntent);
        });

        findViewById(R.id.llGoIbibo).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.goibibo.com/"));
            startActivity(browserIntent);
        });

        findViewById(R.id.llMakeMyTrip).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.makemytrip.com/"));
            startActivity(browserIntent);
        });
        findViewById(R.id.Trivago).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.trivago.in/"));
            startActivity(browserIntent);
        });
        findViewById(R.id.Trivago).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.trivago.in/"));
            startActivity(browserIntent);
        });
        findViewById(R.id.llEasyMyTrip).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.easemytrip.com/"));
            startActivity(browserIntent);
        });
    }
}
