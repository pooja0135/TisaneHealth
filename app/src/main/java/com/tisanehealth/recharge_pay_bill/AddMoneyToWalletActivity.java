package com.tisanehealth.recharge_pay_bill;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.medialablk.easytoast.EasyToast;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.R;
import com.tisanehealth.recharge_pay_bill.mobile_recharge.RechargeActivity;

import org.json.JSONException;
import org.json.JSONObject;
import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.CreateOrder;
import static com.tisanehealth.Helper.AppUrls.GetMoneyWallet;
import static com.tisanehealth.Helper.AppUrls.OrderStatusUpdate;
import static com.tisanehealth.Helper.AppUrls.WalletRequestByUserPayment;
import static com.tisanehealth.Helper.AppUrls.WalletUpdateByUserPayment;
import static com.tisanehealth.Helper.Utils.myUnsafeHttpClient;

public class AddMoneyToWalletActivity extends AppCompatActivity implements View.OnClickListener , PaymentResultWithDataListener {

    ImageView ivBack;
    Button btnAddMoney;
    EditText etAmount;
    TextView tvWalletBalance;
    Preferences pref;
    CustomLoader loader;
    String OrderId;
    String online_order_id;
    String online_transaction_id;
    double money_wallet=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money_to_wallet);

        ivBack            =findViewById(R.id.ivBack);
        etAmount          =findViewById(R.id.etAmount);
        tvWalletBalance   =findViewById(R.id.tvWalletBalance);
        btnAddMoney       =findViewById(R.id.btnAddMoney);
        
        pref              =new Preferences(this);
        
        loader            =new CustomLoader(this,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        
        //setOnClickListener
        ivBack.setOnClickListener(this);
        btnAddMoney.setOnClickListener(this);

        AndroidNetworking.initialize(this, myUnsafeHttpClient());

        if (Utils.isNetworkConnectedMainThred(this)) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);
            GetWalletAPI();
        } else {
            EasyToast.error(this, "No Internet Connnection");
        }

    }

    @Override
    public void onClick(View view) {
      switch (view.getId())
      {
          case R.id.ivBack:
              finish();
              break;
          case R.id.btnAddMoney:

              if (etAmount.getText().toString().isEmpty())
              {
                  EasyToast.warning(this,"Enter amount");
              }
              else
              {
                  if (Utils.isNetworkConnectedMainThred(AddMoneyToWalletActivity.this)) {
                      loader.show();
                      loader.setCanceledOnTouchOutside(true);
                      loader.setCancelable(false);

                      CreateOrderForWalletApi(etAmount.getText().toString(),pref.get(AppSettings.UserId),pref.get(AppSettings.UserMobile),pref.get(AppSettings.UserName));

                  } else {
                      EasyToast.error(AddMoneyToWalletActivity.this, "No Internet Connnection");
                  }
              }

              break;
      }    
    }


    //==========================================API===================================================//

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
                                tvWalletBalance.setText("\u20B9"+String.valueOf(money_wallet));

                                loader.cancel();

                            }
                            else
                            {
                                loader.cancel();

                                money_wallet=0.0;
                                tvWalletBalance.setText("\u20B9"+String.valueOf(money_wallet));

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


    public void CreateOrderForWalletApi(final String Amount, String MemberId, String MobileNo, String Name) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("Amount", Amount);
            jsonObject.put("BillStatus","Online Payment");
            jsonObject.put("MemberId",MemberId);
            jsonObject.put("MobileNo",MobileNo);
            jsonObject.put("Name",Name);
            jsonObject.put("PaymentMode","Online");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+CreateOrder)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //Log.v("hjfhjhfjhfjfhfhufh",response.toString());

                            boolean Status=   response.getBoolean("Status");
                            if (Status)
                            {
                                online_transaction_id=response.getString("TransactionId");
                                online_order_id=response.getString("OrderId");
                                loader.cancel();

                                try {
                                    AddMoneytoWalletApi(Amount,online_transaction_id,online_order_id,"Pending","Online");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                EasyToast.error(AddMoneyToWalletActivity.this, "This UserId or Password is not valid.");
                                loader.dismiss();
                            }

                            loader.cancel();

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


    public void AddMoneytoWalletApi(String Amount, final String transaction_id, final String order_id, final String payment_status, final String payment_mode) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("Address", "");
            jsonObject.put("CurrentWallet","0");
            jsonObject.put("Member_Id",pref.get(AppSettings.UserId));
            jsonObject.put("Member_Name",pref.get(AppSettings.UserName));
            jsonObject.put("MobileNo",pref.get(AppSettings.UserMobile));
            jsonObject.put("PayStatus","Pending");
            jsonObject.put("ReqWallet",Amount);

            //Log.v("hjfhjhfjhfjfhfhufh",jsonObject.toString());
            //Log.v("hjfhjhfjhfjfhfhufh",BaseUrl+WalletRequestByUserPayment);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+WalletRequestByUserPayment)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //Log.v("hjfhjhfjhfjfhfhufh",response.toString());
                            boolean Status=   response.getBoolean("Status");
                            if (Status)
                            {
                                loader.cancel();
                                try {
                                    OrderStatusApi("",order_id,payment_status,"",payment_mode,transaction_id,"","","","","","Online Payment");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                EasyToast.error(AddMoneyToWalletActivity.this, response.getString("Msg"));
                                loader.dismiss();
                            }

                            loader.cancel();

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


    public void OrderStatusApi(String Myhpayid, String OrderId, String PayStatus, String PaymentMethod, String PaymentMode, final String TransactionId,
                               String credit_used, String razorpay_amt, String razorpay_order_id, String razorpay_payment_id, String razorpay_signature, String service ) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("Myhpayid", Myhpayid);
            jsonObject.put("OrderId", OrderId);
            jsonObject.put("PayStatus", PayStatus);
            jsonObject.put("PaymentMethod", PaymentMethod);
            jsonObject.put("PaymentMode", PaymentMode);
            jsonObject.put("TransactionId", TransactionId);
            jsonObject.put("credit_used", credit_used);
            jsonObject.put("razorpay_amt", razorpay_amt);
            jsonObject.put("razorpay_order_id", razorpay_order_id);
            jsonObject.put("razorpay_payment_id", razorpay_payment_id);
            jsonObject.put("razorpay_signature", razorpay_signature);
            jsonObject.put("rech_mobile", "");
            jsonObject.put("rech_order_id", "");
            jsonObject.put("rech_status", "");
            jsonObject.put("rech_type", "");
            jsonObject.put("service", "");
            jsonObject.put("Amount", etAmount.getText().toString());
            jsonObject.put("MemberId", pref.get(AppSettings.UserId));

            //Log.v("hjfhjhfjhfjfhfhufh",jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+OrderStatusUpdate)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //Log.v("hjfhjhfjhfjfhfhufh",response.toString());

                            boolean Status=   response.getBoolean("Status");
                            if (Status)
                            {
                                startPayment(TransactionId);
                            }
                            else
                            {
                                EasyToast.error(AddMoneyToWalletActivity.this,response.getString("Msg") );
                                loader.dismiss();
                            }

                            loader.cancel();

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


    public void OrderStatusUpdateApi(String Myhpayid, String OrderId, String PayStatus, String PaymentMethod, String PaymentMode, final String TransactionId,
                                     String credit_used, final String razorpay_amt, String razorpay_order_id, String razorpay_payment_id, String razorpay_signature, String service,
                                     String rech_mobile, String rech_order_id, String rech_status, String rech_type) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("Myhpayid", Myhpayid);
            jsonObject.put("OrderId", OrderId);
            jsonObject.put("PayStatus", PayStatus);
            jsonObject.put("PaymentMethod", PaymentMethod);
            jsonObject.put("PaymentMode", PaymentMode);
            jsonObject.put("TransactionId", TransactionId);
            jsonObject.put("credit_used", credit_used);
            jsonObject.put("razorpay_amt", razorpay_amt);
            jsonObject.put("razorpay_order_id", razorpay_order_id);
            jsonObject.put("razorpay_payment_id", razorpay_payment_id);
            jsonObject.put("razorpay_signature", razorpay_signature);
            jsonObject.put("rech_mobile", "");
            jsonObject.put("rech_order_id", "");
            jsonObject.put("rech_status", "");
            jsonObject.put("rech_type", "");
            jsonObject.put("service", service);
            jsonObject.put("Amount", etAmount.getText().toString());
            jsonObject.put("MemberId", pref.get(AppSettings.UserId));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+OrderStatusUpdate)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //Log.v("hjfhjhfjhfjfhfhufh",response.toString());

                            boolean Status=   response.getBoolean("Status");
                            if (Status)
                            {
                                etAmount.setText("");
                                UpdateWalletApi(pref.get(AppSettings.UserId),razorpay_amt,"Success");
                            }
                            else
                            {
                                EasyToast.error(AddMoneyToWalletActivity.this,response.getString("Msg") );
                                loader.dismiss();
                            }

                            loader.cancel();

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


    public void UpdateWalletApi(String MemberId,String ReqWallet,String PayStatus) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("PayStatus", PayStatus);
            jsonObject.put("ReqWallet",ReqWallet);
            jsonObject.put("Member_Id",MemberId);

           //Log.v("recharge_status5",jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+WalletUpdateByUserPayment)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //Log.v("recharge_status6",response.toString());

                            boolean Status=   response.getBoolean("Status");
                            if (Status)
                            {
                               GetWalletAPI();
                            }
                            else
                            {
                                EasyToast.error(AddMoneyToWalletActivity.this, response.getString("Msg"));
                                loader.dismiss();
                            }

                            loader.cancel();

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


    //======================================================Payment gateway================================//
    public void startPayment(String orderId) {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        String amt = etAmount.getText().toString().replace("\u20b9", "").trim();
        Double amount = Double.parseDouble(amt) * 100;

        try {
            JSONObject options = new JSONObject();
            options.put("name", "e-Tisane");
            options.put("description", "Online Payment");
            int image = R.drawable.logo;
            options.put("currency", "INR");
            options.put("amount", amount);
            options.put("order_id", orderId);
            JSONObject preFill = new JSONObject();
            if (pref.get(AppSettings.UserEmail).isEmpty())
            {
                preFill.put("email", "test@gmail.com");
            }
            else
            {
                preFill.put("email", pref.get(AppSettings.UserEmail));
            }
            preFill.put("contact", pref.get(AppSettings.UserMobile));
            options.put("prefill", preFill);
            co.setImage(image);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();

            //Log.v("dkhkhgkhghuguguhdfuuduu",e.getMessage());
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData data) {
        final String paymentId = data.getPaymentId();
        String signature = data.getSignature();
        String orderId = data.getOrderId();
        String contact = data.getUserContact();
        String email = data.getUserEmail();


        //Log.v("dkhkhgkhghuguguhdfuuduu",paymentId);
        //Log.v("dkhkhgkhghuguguhdfuuduu",signature);
        //Log.v("dkhkhgkhghuguguhdfuuduu",orderId);
        //Log.v("dkhkhgkhghuguguhdfuuduu",contact);
        //Log.v("dkhkhgkhghuguguhdfuuduu",email);

        if (Utils.isNetworkConnectedMainThred(this)) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);

            OrderStatusUpdateApi(paymentId,online_order_id,"Success","Online","Online",online_transaction_id,"",
                    etAmount.getText().toString(),orderId,paymentId,signature,"Online Payment","","","","");



        } else {
            EasyToast.error(this, "No Internet Connnection");
        }

    }

    @Override
    public void onPaymentError(int i, String s, PaymentData data) {
        final String paymentId = data.getPaymentId();
        String signature = data.getSignature();
        String orderId = data.getOrderId();
        String contact = data.getUserContact();
        String email = data.getUserEmail();


        //Log.v("dkhkhgkhghuguguhdfuuduu",paymentId);
        //Log.v("dkhkhgkhghuguguhdfuuduu",signature);
        //Log.v("dkhkhgkhghuguguhdfuuduu",orderId);
        //Log.v("dkhkhgkhghuguguhdfuuduu",contact);
        //Log.v("dkhkhgkhghuguguhdfuuduu",email);

        if (Utils.isNetworkConnectedMainThred(this)) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);

            OrderStatusUpdateApi(paymentId,online_order_id,"Success","Online","Online",online_transaction_id,"",
                    etAmount.getText().toString(),orderId,paymentId,signature,"Online Payment","","","","");



        } else {
            EasyToast.error(this, "No Internet Connnection");
        }
    }


}
