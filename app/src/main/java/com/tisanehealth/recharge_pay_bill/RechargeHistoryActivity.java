package com.tisanehealth.recharge_pay_bill;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.medialablk.easytoast.EasyToast;
import com.tisanehealth.Adapter.recharge.Adapter_rechargehistory;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.GetMoneyWallet;
import static com.tisanehealth.Helper.AppUrls.GetRechargeHistory;

public class RechargeHistoryActivity extends AppCompatActivity {

    ImageView ivBack;
    RecyclerView rvRechargeHistory;
    Preferences pref;
    CustomLoader loader;
    TextView tvNoDataFound;
    ArrayList<HashMap<String,String>> rechargehistorylist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_history);

        ivBack             =findViewById(R.id.ivBack);
        tvNoDataFound      =findViewById(R.id.tvNoDataFound);
        rvRechargeHistory  =findViewById(R.id.rvRechargeHistory);
        pref               =new Preferences(this);
        loader             =new CustomLoader(this,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        rvRechargeHistory.setLayoutManager(new LinearLayoutManager(this));

        if (Utils.isNetworkConnectedMainThred(this)) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);
            GetRechargeHistoryAPI();
        } else {
            EasyToast.error(this, "No Internet Connnection");
        }


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public void GetRechargeHistoryAPI()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MemberId",pref.get(AppSettings.UserId));
            jsonObject.put("Type","wallet");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+GetRechargeHistory)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.v("jyiujojjujujujujuujujuj",response.toString());
                            boolean Status=   response.getBoolean("Status");

                            if (Status)
                            {
                                JSONArray jsonArray=response.getJSONArray("TrasactionHis");
                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    HashMap<String,String>map=new HashMap<>();
                                    map.put("Amount",jsonObject.getString("Amount"));
                                    map.put("Entrydate",jsonObject.getString("Entrydate"));
                                    map.put("MemberId",jsonObject.getString("MemberId"));
                                    map.put("Myhpayid",jsonObject.getString("Myhpayid"));
                                    map.put("OrderId",jsonObject.getString("OrderId"));
                                    map.put("PayStatus",jsonObject.getString("PayStatus"));
                                    map.put("PaymentDate",jsonObject.getString("PaymentDate"));
                                    map.put("PaymentMethod",jsonObject.getString("PaymentMethod"));
                                    map.put("PaymentMode",jsonObject.getString("PaymentMode"));
                                    map.put("rech_mobile",jsonObject.getString("rech_mobile"));
                                    map.put("rech_order_id",jsonObject.getString("rech_order_id"));
                                    map.put("rech_status",jsonObject.getString("rech_status"));
                                    map.put("rech_type",jsonObject.getString("rech_type"));
                                    map.put("service",jsonObject.getString("service"));
                                    map.put("bill_status",jsonObject.getString("BillStatus"));

                                    rechargehistorylist.add(map);

                                }

                                rvRechargeHistory.setAdapter(new Adapter_rechargehistory(RechargeHistoryActivity.this,rechargehistorylist));

                                loader.cancel();
                                tvNoDataFound.setVisibility(View.GONE);
                                rvRechargeHistory.setVisibility(View.VISIBLE);

                            }
                            else
                            {
                                loader.cancel();
                                tvNoDataFound.setVisibility(View.VISIBLE);
                                rvRechargeHistory.setVisibility(View.GONE);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        Log.v("gjfihhgghghg",error.toString());
                        loader.cancel();
                    }
                });
    }
}
