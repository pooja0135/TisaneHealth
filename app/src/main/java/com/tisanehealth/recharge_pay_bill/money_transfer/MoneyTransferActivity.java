package com.tisanehealth.recharge_pay_bill.money_transfer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.medialablk.easytoast.EasyToast;
import com.tisanehealth.Adapter.recharge.recharge_adapter;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.AppUrls;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.Model.recharge.RechargePlanModel;
import com.tisanehealth.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.GetMemberDetailsById;
import static com.tisanehealth.Helper.Utils.myUnsafeHttpClient;

public class MoneyTransferActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSend,btnVerify,btnBankVerify;
    ImageView ivBack;
    TextView tvMobile;
    TextView tvResend;
    EditText etOtp,etBankOtp;
    LinearLayout llOtp;
    LinearLayout llBankOtp;
    CountDownTimer timer;
    Preferences pref;
    CustomLoader loader;
    String beneficiary_id="";
    String otp="";
    String resend_otp="";
    ArrayList<HashMap<String,String>>banklist=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transfer);

        //Tetxview
        tvMobile    =findViewById(R.id.tvMobile);
        tvResend    =findViewById(R.id.tvResend);

        //ImageView
        ivBack       =findViewById(R.id.ivBack);

        //Button
        btnSend          =findViewById(R.id.btnSend);
        btnVerify       =findViewById(R.id.btnVerify);
        btnBankVerify   =findViewById(R.id.btnBankVerify);

        //Edittext
        etOtp       =findViewById(R.id.etOtp);
        etBankOtp       =findViewById(R.id.etBankOtp);

        //LinearLayout
        llOtp       =findViewById(R.id.llOtp);

        //LinearLayout
        llBankOtp   =findViewById(R.id.llBankOtp);

        //Preferences
        pref=new Preferences(this);

        //loader
        loader              = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        tvMobile.setText(pref.get(AppSettings.UserMobile));

        //AndroidNetworking.initializethis, myUnsafeHttpClient());
        //setonClickListener
        ivBack.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnVerify.setOnClickListener(this);
        btnBankVerify.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
       switch (view.getId())
       {
           case R.id.btnSend:
               if (Utils.isNetworkConnectedMainThred(this)) {
                   loader.show();
                   loader.setCanceledOnTouchOutside(true);
                   loader.setCancelable(false);
                 //  CustomerDetailApi(pref.get(AppSettings.UserMobile));
                  // CustomerDetailApi("7752825521");
                   CustomerDetailApi("8707250558");

               } else {
                   EasyToast.error(this, "No Internet Connnection");
               }
               break;
           case R.id.btnVerify:
               if (resend_otp.equals("2"))
               {
                  if(etOtp.getText().toString().equals(otp))
                  {
                      if (banklist.isEmpty())
                      {
                          AddBeneficiaryApi(pref.get(AppSettings.UserMobile),pref.get(AppSettings.PayeeName),pref.get(AppSettings.UserMobile),pref.get(AppSettings.BankAccountNumber),pref.get(AppSettings.BankIfsc));
                      }
                      else
                      {
                          startActivity(new Intent(this,TransferMoneyActivity.class).putExtra("bank_detail", banklist));
                      }
                  }
                  else
                  {
                      Toast.makeText(this, "Enter Valid Otp.", Toast.LENGTH_SHORT).show();
                  }
               }
               else
               {
                   if (Utils.isNetworkConnectedMainThred(this)) {
                       loader.show();
                       loader.setCanceledOnTouchOutside(true);
                       loader.setCancelable(false);
                       VerifyOtpApi(pref.get(AppSettings.UserMobile),etOtp.getText().toString());

                   } else {
                       EasyToast.error(this, "No Internet Connnection");
                   }
               }

               break;

           case R.id.tvResend:
               if (resend_otp.equals("2"))
               {
                   if (Utils.isNetworkConnectedMainThred(this)) {
                       loader.show();
                       loader.setCanceledOnTouchOutside(true);
                       loader.setCancelable(false);
                       GetMemberDetailsById();

                   } else {
                       EasyToast.error(this, "No Internet Connnection");
                   }
               }
               else
               {
                   if (Utils.isNetworkConnectedMainThred(this)) {
                       loader.show();
                       loader.setCanceledOnTouchOutside(true);
                       loader.setCancelable(false);
                       CustomerRegistrationApi(pref.get(AppSettings.UserName),pref.get(AppSettings.UserPincode),"7752825521");
                   } else {
                       EasyToast.error(this, "No Internet Connnection");
                   }
               }

               break;

           case R.id.btnBankVerify:

               if (Utils.isNetworkConnectedMainThred(this)) {
                   loader.show();
                   loader.setCanceledOnTouchOutside(true);
                   loader.setCancelable(false);
                   BankVerifyOtpApi(pref.get(AppSettings.UserMobile),etBankOtp.getText().toString(),beneficiary_id);

               } else {
                   EasyToast.error(this, "No Internet Connnection");
               }

           case R.id.ivBack:
               finish();
               break;
       }
    }


    public static String convertSecondsToHMmSs(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;

        return String.format("%02d:%02d", m,s);
    }




    //===============================================API======================================================//

    public void CustomerDetailApi(String customerMobile) {

        String url="https://api.rechapi.com/moneyTransfer/cusDetails.php?format=json&token=iYpga6msh23e1Bkqvovpid9xoFijuN&customerMobile="+customerMobile+"&demo="+"&version=1.1";

        //Log.v("gfgfgfgfhgfhghfghf",url);

        AndroidNetworking.post(url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //Log.v("gfgfgfgfhgfhghfghf",response.toString());
                            JSONObject jsonObject=response.getJSONObject("data");

                            String resCode=   jsonObject.getString("error_code");

                            //Log.v("gfgfgfgfhgfhghfghf",resCode);
                            if (resCode.equals("123"))
                            {
                             resend_otp="1";
                             CustomerRegistrationApi(pref.get(AppSettings.UserName),pref.get(AppSettings.UserPincode),"7752825521");

                            }
                            else
                            {
                                Object jsonArray=jsonObject.get("beneficiaryList");

                                if(jsonArray instanceof JSONArray)
                                {
                                  JSONArray jsonArray1=jsonObject.getJSONArray("beneficiaryList");

                                  for (int i=0;i<jsonArray1.length();i++)
                                  {
                                    HashMap<String,String>map=new HashMap<>();
                                    JSONObject jsonObject1=jsonArray1.getJSONObject(i);

                                    map.put("beneficiaryName",jsonObject1.getString("beneficiaryName"));
                                    map.put("beneficiaryMobileNumber",jsonObject1.getString("beneficiaryMobileNumber"));
                                    map.put("beneficiaryAccountNumber",jsonObject1.getString("beneficiaryAccountNumber"));
                                    map.put("ifscCode",jsonObject1.getString("ifscCode"));
                                    map.put("beneficiaryId",jsonObject1.getString("beneficiaryId"));

                                    banklist.add(map);

                                  }

                                  //Log.v("banklist",banklist.toString());


                                }
                                else
                                {

                                }
                                resend_otp="2";

                                GetMemberDetailsById();




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


    public void GetMemberDetailsById() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MemberId",pref.get(AppSettings.UserId));
            jsonObject.put("MobileNo",tvMobile.getText().toString());



        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(BaseUrl+GetMemberDetailsById)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //Log.v("gfgfgfgfhgfhghfghf", String.valueOf(response));
                            boolean status=   response.getBoolean("Status");

                            if (status)
                            {
                               otp=response.getString("OTP");

                                Toast.makeText(MoneyTransferActivity.this, "Otp has been sent to your mobile number.", Toast.LENGTH_SHORT).show();

                                llOtp.setVisibility(View.VISIBLE);
                                btnSend.setVisibility(View.GONE);
                                timer=  new CountDownTimer(60000, 1000) {
                                    public void onTick(long millisUntilFinished) {
                                        tvResend.setText(convertSecondsToHMmSs(millisUntilFinished/1000));
                                    }

                                    public void onFinish() {
                                        tvResend.setText("Resend OTP");
                                    }
                                }.start();
                                loader.cancel();

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
                    public void onError(ANError anError) {

                    }


                });
    }


    public void CustomerRegistrationApi(String customerName,String customerPincode,String customerMobile) {

        String url="https://api.rechapi.com/moneyTransfer/customerRegistration.php?format=json&token=iYpga6msh23e1Bkqvovpid9xoFijuN&customerName="+customerName+"&customerPincode="+customerPincode+"&customerMobile="+customerMobile+"&demo="+"&version=1.1";

        //Log.v("gfgfgfgfhgfhghfghf",url);

        AndroidNetworking.post(url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject jsonObject=response.getJSONObject("data");

                            String resCode=   jsonObject.getString("error_code");

                            //Log.v("gfgfgfgfhgfhghfghf",resCode);
                            if (resCode.equals("178"))
                            {

                                Toast.makeText(MoneyTransferActivity.this, "Otp has been sent to your mobile number.", Toast.LENGTH_SHORT).show();

                                llOtp.setVisibility(View.VISIBLE);
                                btnSend.setVisibility(View.GONE);
                                timer=  new CountDownTimer(120000, 1000) {
                                    public void onTick(long millisUntilFinished) {
                                        tvResend.setText(convertSecondsToHMmSs(millisUntilFinished/1000));
                                    }

                                    public void onFinish() {
                                        tvResend.setText("Resend OTP");
                                    }
                                }.start();
                                loader.cancel();

                            }
                            else
                            {



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


    public void VerifyOtpApi(String customerMobile,String otp) {

        String url="https://api.rechapi.com/moneyTransfer/customerVerify.php?format=json&token=iYpga6msh23e1Bkqvovpid9xoFijuN&customerMobile="+customerMobile+"&otp="+otp+"&demo="+"&version=1.1";

        //Log.v("gfgfgfgfhgfhghfghf",url);

        AndroidNetworking.post(url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String resCode=   response.getString("error_code");
                            //Log.v("gfgfgfgfhgfhghfghf",resCode);
                            if (resCode.equals("200"))
                            {
                                AddBeneficiaryApi(pref.get(AppSettings.UserMobile),pref.get(AppSettings.PayeeName),pref.get(AppSettings.UserMobile),pref.get(AppSettings.BankAccountNumber),pref.get(AppSettings.BankIfsc));

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
                    public void onError(ANError anError) {

                    }


                });
    }

    public void AddBeneficiaryApi(String customerMobile,String beneficiaryName,String beneficiaryMobileNumber,String beneficiaryAccountNumber,String ifscCode) {

        String url="https://api.rechapi.com/moneyTransfer/addBeneficiary.php?format=json&token=iYpga6msh23e1Bkqvovpid9xoFijuN&customerMobile="+customerMobile+"&beneficiaryName="+beneficiaryName+"&beneficiaryMobileNumber="+beneficiaryMobileNumber+"&beneficiaryAccountNumber="+beneficiaryAccountNumber+"&ifscCode="+ifscCode+"&demo="+"&version=1.1";

        //Log.v("gfgfgfgfhgfhghfghf",url);

        AndroidNetworking.post(url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {


                            JSONObject jsonObject=response.getJSONObject("data");
                            String resCode=   jsonObject.getString("error_code");
                            //Log.v("gfgfgfgfhgfhghfghf",resCode);
                            if (resCode.equals("200"))
                            {

                                beneficiary_id=jsonObject.getString("beneficiaryId");

                                llBankOtp.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                loader.cancel();

                                Toast.makeText(MoneyTransferActivity.this, jsonObject.getString("resText"), Toast.LENGTH_SHORT).show();
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

    public void BankVerifyOtpApi(String customerMobile,String otp,String beneficiary_id) {

        String url="https://api.rechapi.com/moneyTransfer/beneficiaryVerifiy.php?format=json&token=iYpga6msh23e1Bkqvovpid9xoFijuN&customerMobile="+customerMobile+"&otp="+otp+"&beneficiaryId="+beneficiary_id+"&demo="+"&version=1.1";

        //Log.v("gfgfgfgfhgfhghfghf",url);

        AndroidNetworking.post(url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject jsonObject=response.getJSONObject("data");

                            String resCode=   jsonObject.getString("error_code");
                            //Log.v("gfgfgfgfhgfhghfghf",resCode);
                            if (resCode.equals("200"))
                            {

                                 startActivity(new Intent(MoneyTransferActivity.this,TransferMoneyActivity.class).putExtra("bank_detail", banklist));
                            }
                            else
                            {
                                loader.cancel();

                                Toast.makeText(MoneyTransferActivity.this, jsonObject.getString("resText"), Toast.LENGTH_SHORT).show();

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
