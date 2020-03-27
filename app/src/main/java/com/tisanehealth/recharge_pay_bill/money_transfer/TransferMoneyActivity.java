package com.tisanehealth.recharge_pay_bill.money_transfer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.medialablk.easytoast.EasyToast;
import com.tisanehealth.Activity.DashBoardActivity;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.GetMoneyWallet;
import static com.tisanehealth.Helper.AppUrls.SaveTermsConditionForFundRequest;
import static com.tisanehealth.Helper.AppUrls.TransactionFundInsert;
import static com.tisanehealth.Helper.Utils.myUnsafeHttpClient;

public class TransferMoneyActivity extends AppCompatActivity  implements View.OnClickListener {

    ImageView ivBack;
    TextView tvWalletAmount;
    CheckBox checkbox;
    EditText etPayeeName,etIFSc,etBankAccountNumber,etAmount;
    Button btnTransferMoney;
    CardView cardview;
    Preferences pref;
    CustomLoader loader;
    double money_wallet=0.0;
    ArrayList <HashMap<String, String>>items=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_money);

        ivBack                   =findViewById(R.id.ivBack);
        tvWalletAmount           =findViewById(R.id.tvWalletAmount);
        checkbox                 =findViewById(R.id.checkbox);
        etPayeeName              =findViewById(R.id.etPayeeName);
        etIFSc                   =findViewById(R.id.etIFSC);
        etBankAccountNumber      =findViewById(R.id.etAccountNumber);
        etAmount                 =findViewById(R.id.etAmount);
        btnTransferMoney         =findViewById(R.id.btnTransferMoney);
        cardview                 =findViewById(R.id.cardview);

        pref                     =new Preferences(this);
        loader                   =new CustomLoader(this,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        AndroidNetworking.initialize(this, myUnsafeHttpClient());

        if (Utils.isNetworkConnectedMainThred(TransferMoneyActivity.this)) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);
            GetWalletAPI();

        } else {
            EasyToast.error(TransferMoneyActivity.this, "No Internet Connnection");
        }


        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CompoundButton) view).isChecked()){

                    cardview.setVisibility(View.VISIBLE);

                } else {
                    cardview.setVisibility(View.GONE);
                }
            }
        });






        items = (ArrayList<HashMap<String,String>>)getIntent().getSerializableExtra("bank_detail");

       //Log.v("fjhffhjfhjfhfhjf", String.valueOf(items.size()));

        etBankAccountNumber.setText(items.get(0).get("beneficiaryAccountNumber"));
        etIFSc.setText(items.get(0).get("ifscCode"));
        etPayeeName.setText(items.get(0).get("beneficiaryName"));


        ivBack.setOnClickListener(this);
        btnTransferMoney.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnTransferMoney:
                if (money_wallet==0.0)
                {
                    Toast.makeText(this, "You have insufficent amount in wallet.", Toast.LENGTH_SHORT).show();
                }

                else if (money_wallet<Double.parseDouble(etAmount.getText().toString()))
                {
                    Toast.makeText(this, "Transfer amount should not be greater than wallet amount", Toast.LENGTH_SHORT).show();
                }
                else if (Double.parseDouble(etAmount.getText().toString())<250)
                {
                    Toast.makeText(this, "Transfer amount should be greater than or equal to \u20B9250.", Toast.LENGTH_SHORT).show();
                }

                else if (Double.parseDouble(etAmount.getText().toString())>2500)
                {
                    Toast.makeText(this, "Transfer amount should not be greater than  \u20B92500.", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    if (Utils.isNetworkConnectedMainThred(TransferMoneyActivity.this)) {
                        loader.show();
                        loader.setCanceledOnTouchOutside(true);
                        loader.setCancelable(false);
                        TransferMoneyApi(pref.get(AppSettings.UserMobile),pref.get(AppSettings.UserId), String.valueOf(money_wallet),items.get(0).get("beneficiaryId"),etAmount.getText().toString());

                    } else {
                        EasyToast.error(TransferMoneyActivity.this, "No Internet Connnection");
                    }
                }



                break;
        }
    }

    //==========================================API===================================================//
    public void FundRequesttAPI()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Terms","1");
            jsonObject.put("Userid",pref.get(AppSettings.UserId));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+SaveTermsConditionForFundRequest)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {


                            String Message=   response.getString("Message");

                            if (Message.equals("Success"))
                            {
                               cardview.setVisibility(View.VISIBLE);
                                loader.cancel();
                            }
                            else
                            {
                                loader.cancel();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        loader.cancel();
                    }
                });
    }

    public void GetWalletAPI()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Userid",pref.get(AppSettings.UserId));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+GetMoneyWallet)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //Log.v("jyiujojjujujujujuujujuj",response.toString());
                            String Message=   response.getString("Message");

                            if (Message.equals("Success"))
                            {


                                money_wallet=Double.valueOf(response.getString("MoneyWallet"));

                                tvWalletAmount.setText("Wallet Amount: \u20B9"+ String.valueOf(money_wallet));

                                loader.cancel();

                            }
                            else
                            {
                                loader.cancel();

                                money_wallet=0.0;
                                tvWalletAmount.setText("Wallet Amount: \u20B9"+String.valueOf(money_wallet));
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

    public void TransferMoneyApi(String MobileNo,String UserName,String WalletAmount,String beneficiary_id,String amount) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MobileNo",MobileNo);
            jsonObject.put("UserName",UserName);
            jsonObject.put("WalletAmount",WalletAmount);
            jsonObject.put("beneficiaryId",beneficiary_id);
            jsonObject.put("amount",amount);

            //Log.v("gfgfgfgfhgfhghfghf",jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        AndroidNetworking.post(BaseUrl+TransactionFundInsert)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //Log.v("gfgfgfgfhgfhghfghf",response.toString());


                            boolean status=   response.getBoolean("Status");

                            if (status)
                            {
                              loader.cancel();
                              Toast.makeText(TransferMoneyActivity.this, response.getString("Msg"), Toast.LENGTH_SHORT).show();
                              startActivity(new Intent(TransferMoneyActivity.this, DashBoardActivity.class));
                            }


                            else
                            {
                                loader.cancel();

                                Toast.makeText(TransferMoneyActivity.this, response.getString("Msg"), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(TransferMoneyActivity.this, DashBoardActivity.class));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }


                });
    }

}
