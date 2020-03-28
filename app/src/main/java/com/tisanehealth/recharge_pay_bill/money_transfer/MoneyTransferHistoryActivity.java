package com.tisanehealth.recharge_pay_bill.money_transfer;

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
import com.tisanehealth.Adapter.Adapter_money_transfer_history;
import com.tisanehealth.Adapter.recharge.Adapter_rechargehistory;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.R;
import com.tisanehealth.recharge_pay_bill.RechargeHistoryActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.GetMoneyTransferHistory;
import static com.tisanehealth.Helper.AppUrls.GetRechargeHistory;
import static com.tisanehealth.Helper.Utils.myUnsafeHttpClient;

public class MoneyTransferHistoryActivity extends AppCompatActivity {

    ImageView ivBack;
    RecyclerView rvMoneyTransferHistory;
    Preferences pref;
    CustomLoader loader;
    TextView tvNoDataFound;
    ArrayList<HashMap<String,String>> transferhistorylist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transfer_history);

        rvMoneyTransferHistory=findViewById(R.id.rvMoneyTransferHistory);
        ivBack                =findViewById(R.id.ivBack);
        tvNoDataFound         =findViewById(R.id.tvNoDataFound);
        pref               =new Preferences(this);
        loader             =new CustomLoader(this,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        rvMoneyTransferHistory.setLayoutManager(new LinearLayoutManager(this));

        //AndroidNetworking.initializethis, myUnsafeHttpClient());
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

        AndroidNetworking.post(BaseUrl+GetMoneyTransferHistory)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //Log.v("jyiujojjujujujujuujujuj",response.toString());
                            boolean Status=   response.getBoolean("Status");

                            if (Status)
                            {
                                JSONArray jsonArray=response.getJSONArray("TrasactionHis");
                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    HashMap<String,String>map=new HashMap<>();

                                    map.put("CurrentWallet",jsonObject.getString("CurrentWallet"));
                                    map.put("TransferAmount",jsonObject.getString("TransferAmount"));
                                    map.put("Status",jsonObject.getString("Status"));
                                    map.put("WalletType",jsonObject.getString("WalletType"));
                                    map.put("EntryDate",jsonObject.getString("EntryDate"));


                                    transferhistorylist.add(map);

                                }

                                rvMoneyTransferHistory.setAdapter(new Adapter_money_transfer_history(MoneyTransferHistoryActivity.this,transferhistorylist));

                                loader.cancel();
                                tvNoDataFound.setVisibility(View.GONE);
                                rvMoneyTransferHistory.setVisibility(View.VISIBLE);

                            }
                            else
                            {
                                loader.cancel();
                                tvNoDataFound.setVisibility(View.VISIBLE);
                                rvMoneyTransferHistory.setVisibility(View.GONE);

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
