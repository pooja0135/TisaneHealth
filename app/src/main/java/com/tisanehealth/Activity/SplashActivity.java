package com.tisanehealth.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.medialablk.easytoast.EasyToast;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.Model.recharge.CircleModel;
import com.tisanehealth.Model.recharge.RechargeListModel;
import com.tisanehealth.Model.recharge.RechargeTypeModel;
import com.tisanehealth.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.paperdb.Paper;

import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.GetAllStateCode;
import static com.tisanehealth.Helper.AppUrls.GetRechargeType;
import static com.tisanehealth.Helper.Utils.myUnsafeHttpClient;

public class SplashActivity extends AppCompatActivity {

    ImageView imageView, ivLogo;
    Preferences pref;
    CustomLoader loader;
    CircleModel circleModel;
    RechargeTypeModel rechargeTypeModel;
    RechargeListModel rechargeListModel;
    ArrayList<CircleModel> circlelist = new ArrayList<>();
    ArrayList<RechargeTypeModel> rechargeTypeModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY); // simply enable logging
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.HEADERS);
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BASIC);
        //AndroidNetworking.initializethis, myUnsafeHttpClient());

        //ImageView
        ivLogo = findViewById(R.id.ivLogo);

        pref = new Preferences(this);

        //loader
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


        Paper.init(this);

      /*  Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ivLogo.setVisibility(View.VISIBLE);
                Animation animSlide = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.fade_in);
                ivLogo.startAnimation(animSlide);

            }
        }, 500);*/


        Paper.book().delete("circlelist");


        if (Utils.isNetworkConnectedMainThred(this)) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);
            GetStateAPI();

        } else {
            EasyToast.error(this, "No Internet Connnection");
        }


    }


    public void GetStateAPI() {
        AndroidNetworking.post(BaseUrl + GetAllStateCode)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            boolean Status = response.getBoolean("Status");
                            if (Status) {
                                JSONArray jsonArray = response.getJSONArray("Scode");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    circleModel = new CircleModel(jsonObject.getString("Scode"), jsonObject.getString("StateName"));
                                    circlelist.add(circleModel);

                                }

                                Paper.book().write("circlelist", circlelist);


                                GetRechargeListAPI();

                            } else {
                                loader.cancel();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        //Log.v("gjfihhgghghg",error.toString());
                        loader.cancel();
                    }
                });
    }

    public void GetRechargeListAPI() {
        AndroidNetworking.post(BaseUrl + GetRechargeType)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            boolean Status = response.getBoolean("Status");
                            if (Status) {
                                JSONArray jsonArray = response.getJSONArray("Recharge");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    ArrayList<RechargeListModel> rechargeList = new ArrayList<>();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    JSONArray jsonArray1 = jsonObject.getJSONArray("RchDetail");

                                    for (int j = 0; j < jsonArray1.length(); j++) {

                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(j);

                                        String instruction = jsonObject1.getString("Instruction");
                                        String operator_code = jsonObject1.getString("Operator_Code");
                                        String service = jsonObject1.getString("Service");
                                        String state = jsonObject1.getString("State");

                                        rechargeListModel = new RechargeListModel(instruction, operator_code, service, state);
                                        rechargeList.add(rechargeListModel);
                                    }
                                    String rechargetype = jsonObject.getString("Recharge_type");

                                    rechargeTypeModel = new RechargeTypeModel(rechargetype, rechargeList);
                                    rechargeTypeModelArrayList.add(rechargeTypeModel);


                                }

                                Paper.book().write("rechargetype", rechargeTypeModelArrayList);

                                if (!pref.get(AppSettings.UserId).isEmpty()) {
                                    Intent i = new Intent(SplashActivity.this, DashBoardActivity.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                                    startActivity(i);
                                    finish();
                                }

                                loader.cancel();

                            } else {
                                loader.cancel();

                                if (!pref.get(AppSettings.UserId).isEmpty()) {
                                    Intent i = new Intent(SplashActivity.this, DashBoardActivity.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        //Log.v("gjfihhgghghg",error.toString());
                        loader.cancel();
                    }
                });
    }

}
