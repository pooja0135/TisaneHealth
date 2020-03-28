package com.tisanehealth.recharge_pay_bill.dth_recharge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import com.tisanehealth.Activity.DashBoardActivity;
import com.tisanehealth.Adapter.recharge.Adapter_viewpager;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.MovingViewPager;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.Model.recharge.DTHModel;
import com.tisanehealth.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Timer;
import java.util.TimerTask;
import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.CreateOrder;
import static com.tisanehealth.Helper.AppUrls.GetMoneyWallet;
import static com.tisanehealth.Helper.AppUrls.OrderStatusUpdate;
import static com.tisanehealth.Helper.AppUrls.WalletRequestByUserPayment;
import static com.tisanehealth.Helper.AppUrls.WalletUpdateByUserPayment;
import static com.tisanehealth.Helper.Utils.myUnsafeHttpClient;

public class DTHListActivity extends AppCompatActivity implements View.OnClickListener, PaymentResultWithDataListener {

    MovingViewPager viewpagerDTH;
    RelativeLayout rlBack;
    //Adapter
    Adapter_viewpager adapter_viewpager;

    TextView tvDthname;
    EditText etCustomerID;
    EditText etAmount;

    Button btnProceed;

    int[] image = new int[]{
            R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3,
            R.drawable.banner4,

    };

    int currentPage = 1;
    int NUM_PAGES = 0;
    Timer timer;
    final long DELAY_MS = 2000;
    final long PERIOD_MS = 5000;

    DTHModel dthModel;

    Preferences pref;
    CustomLoader loader;
    String OrderId;
    String TransactionId;
    double money_wallet=0.0;
    String mobile="";
    String opvalue1="";
    String opvalue2="";
    String opvalue3="";
    String online_order_id;
    String online_transaction_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dthlist);

        viewpagerDTH  =findViewById(R.id.viewpagerDTH);
        rlBack        =findViewById(R.id.rlBack);


        pref                   =new Preferences(this);

        loader                 =new CustomLoader(this,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


        adapter_viewpager = new Adapter_viewpager(this, image);
        viewpagerDTH.setAdapter(adapter_viewpager);
        viewpagerDTH.setCurrentItem(1, true);
        viewpagerDTH.setClipToPadding(false);

        NUM_PAGES = image.length;

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES - 1) {
                    currentPage = 1;
                }
                viewpagerDTH.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);


        dthModel = getIntent().getParcelableExtra("data_value");


        tvDthname        =findViewById(R.id.tvDthname);
        etCustomerID     =findViewById(R.id.etCustomerID);
        etAmount         =findViewById(R.id.etAmount);
        btnProceed       =findViewById(R.id.btnProceed);


        tvDthname.setText(dthModel.getName());



        //setonCLickListener
        rlBack.setOnClickListener(this);
        btnProceed.setOnClickListener(this);

        //AndroidNetworking.initializethis, myUnsafeHttpClient());


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
            case R.id.btnProceed:
                if(etCustomerID.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(this, "Enter customer id", Toast.LENGTH_SHORT).show();
                }
                else if (etAmount.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(this, "Enter Amount", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (money_wallet>=Double.valueOf(etAmount.getText().toString()))
                    {
                        if (Utils.isNetworkConnectedMainThred(this)) {
                            loader.show();
                            loader.setCanceledOnTouchOutside(true);
                            loader.setCancelable(false);
                            CreateOrderApi(etAmount.getText().toString(),"Recharge",pref.get(AppSettings.UserId),etCustomerID.getText().toString(), pref.get(AppSettings.UserName));

                        } else {
                            EasyToast.error(this, "No Internet Connnection");
                        }
                    }

                    else
                    {

                      //  EasyToast.error(this, "You have insufficent balance in your wallet .Please add money in your wallet.");
                        if (Utils.isNetworkConnectedMainThred(this)) {
                            loader.show();
                            loader.setCanceledOnTouchOutside(true);
                            loader.setCancelable(false);
                            CreateOrderForWalletApi(etAmount.getText().toString(),pref.get(AppSettings.UserId),pref.get(AppSettings.UserMobile),pref.get(AppSettings.UserName));

                        } else {
                            EasyToast.error(this, "No Internet Connnection");
                        }
                    }
                }
                break;
            case R.id.rlBack:
                finish();
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



                                loader.cancel();

                            }
                            else
                            {
                                loader.cancel();

                                money_wallet=0.0;

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

    public void CreateOrderApi(String Amount,String BillStatus,String MemberId,String MobileNo,String Name) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("Amount", Amount);
            jsonObject.put("BillStatus",BillStatus);
            jsonObject.put("MemberId",MemberId);
            jsonObject.put("MobileNo",MobileNo);
            jsonObject.put("Name",Name);
            jsonObject.put("PaymentMode","Wallet");


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
                                OrderId=response.getString("OrderId");
                                TransactionId=response.getString("TransactionId");

                                try {
                                    RechargeOrderStatusApi("",OrderId,"Pending","Wallet","Wallet",TransactionId,"","","","","","DTH");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                EasyToast.error(DTHListActivity.this,  response.getString("Msg"));
                                loader.dismiss();
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


    public void RechargeOrderStatusApi(String Myhpayid, String OrderId, String PayStatus, String PaymentMethod, String PaymentMode, final String TransactionId,
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
            jsonObject.put("Amount", etAmount.getText().toString().trim());
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

                            //Log.v("recharge_status3",response.toString());

                            boolean Status=   response.getBoolean("Status");
                            if (Status)
                            {

                                RechargeApi(etCustomerID.getText().toString(), etAmount.getText().toString(), dthModel.getId(), TransactionId, opvalue1, opvalue2, opvalue3);
                            }
                            else
                            {
                                EasyToast.error(DTHListActivity.this, response.getString("Msg"));
                                loader.dismiss();
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

    public void RechargeOrderStatusUpdateApi(String Myhpayid, String OrderId, String PayStatus, String PaymentMethod, String PaymentMode, final String TransactionId,
                                             String credit_used, String razorpay_amt, String razorpay_order_id, String razorpay_payment_id, String razorpay_signature, String service,
                                             String rech_mobile, String rech_order_id, final String rech_status, final String rech_type) {
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
            jsonObject.put("rech_mobile", rech_mobile);
            jsonObject.put("rech_order_id", rech_order_id);
            jsonObject.put("rech_status", rech_status);
            jsonObject.put("rech_type", rech_type);
            jsonObject.put("service", service);
            jsonObject.put("Amount", etAmount.getText().toString().trim());
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

                            //Log.v("recharge_status4",response.toString());
                            boolean Status=   response.getBoolean("Status");
                            if (Status)
                            {
                                EasyToast.error(DTHListActivity.this,"Recharge Successful.You will receive confirmation message in few minutes." );
                                startActivity(new Intent(DTHListActivity.this, DashBoardActivity.class));



                            }
                            else
                            {
                                EasyToast.error(DTHListActivity.this,response.getString("Msg") );
                                loader.dismiss();
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

    public void RechargeApi(String mobile,String amount,String operator_id,String urid,String opvalue1,String opvalue2,String opvalue3) {

        String url="https://api.rechapi.com/recharge.php?format=json&token=iYpga6msh23e1Bkqvovpid9xoFijuN&mobile="+mobile+"&amount="+amount+"&opid="+operator_id+"&urid="+urid+"&opvalue1="+opvalue1+"&opvalue2="+opvalue2+"&opvalue3="+opvalue3;

        //Log.v("gfgfgfgfhgfhghfghf",url);

        AndroidNetworking.post(url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject object) {
                        try {

                            //Log.v("recharge_status1",object.toString());
                            JSONObject response =object.getJSONObject("data");
                            String resCode=   response.getString("error_code");
                            String rech_mobile=response.getString("mobile");
                            final String rech_order_id=response.getString("orderId");
                            String rech_status=response.getString("status");
                            String rech_type=response.getString("service");

                            if (resCode.equals("200")) {
                                if (Utils.isNetworkConnectedMainThred(DTHListActivity.this)) {
                                    loader.show();
                                    loader.setCanceledOnTouchOutside(true);
                                    loader.setCancelable(false);
                                    RechargeOrderStatusUpdateApi("",OrderId,"Success","Wallet","Wallet",TransactionId,"","","","","","Broadband",   rech_mobile, rech_order_id, rech_status, rech_type);

                                } else {
                                    EasyToast.error(DTHListActivity.this, "No Internet Connnection");
                                    loader.cancel();
                                }
                            }
                            else if (resCode.equals("201"))
                            {
                                if (Utils.isNetworkConnectedMainThred(DTHListActivity.this)) {
                                    loader.show();
                                    loader.setCanceledOnTouchOutside(true);
                                    loader.setCancelable(false);


                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            RechargeStatusApi(rech_order_id);

                                        }
                                    }, 4000);

                                } else {
                                    EasyToast.error(DTHListActivity.this, "No Internet Connnection");
                                }


                            }


                            else if (resCode.equals("112"))
                            {
                                EasyToast.error(DTHListActivity.this, response.getString("resText"));
                                if (Utils.isNetworkConnectedMainThred(DTHListActivity.this)) {
                                    loader.show();
                                    loader.setCanceledOnTouchOutside(true);
                                    loader.setCancelable(false);
                                    RechargeOrderStatusUpdateApi("",OrderId,"Pending","Wallet","Wallet",TransactionId,"","","","","","DTH",   rech_mobile, rech_order_id, rech_status, rech_type);

                                } else {
                                    EasyToast.error(DTHListActivity.this, "No Internet Connnection");
                                }
                            }
                            else
                            {
                                EasyToast.error(DTHListActivity.this, response.getString("resText"));
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


    public void RechargeStatusApi(final String id) {
        String url="https://api.rechapi.com/api_status.php?format=json&token=iYpga6msh23e1Bkqvovpid9xoFijuN&orderId="+id;

        //Log.v("recharge_status2",url);

        AndroidNetworking.post(url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject object) {
                        try {

                            //Log.v("recharge_status2",object.toString());

                            JSONArray response =object.getJSONArray("data");
                            JSONObject jsonObject=response.getJSONObject(0);

                            String status=   jsonObject.getString("error_code");
                            if (status.equals("200")) {
                                if (Utils.isNetworkConnectedMainThred(DTHListActivity.this)) {
                                    loader.show();
                                    loader.setCanceledOnTouchOutside(true);
                                    loader.setCancelable(false);

                                    String rech_mobile=jsonObject.getString("mobile");
                                    String rech_order_id=jsonObject.getString("orderId");
                                    String rech_status=jsonObject.getString("status");
                                    String rech_type=jsonObject.getString("service");

                                    RechargeOrderStatusUpdateApi("",OrderId,"Success","Wallet","Wallet",TransactionId,"","","","","","DTH", rech_mobile  , rech_order_id, rech_status, rech_type);

                                } else {
                                    EasyToast.error(DTHListActivity.this, "No Internet Connnection");
                                }

                            }
                            else if (status.equals("201"))
                            {
                                RechargeStatusApi(id);
                            }

                            else if (status.equals("105"))
                            {
                                RechargeStatusApi(id);
                            }
                            else
                            {
                                Toast.makeText(DTHListActivity.this, jsonObject.getString("resText"), Toast.LENGTH_SHORT).show();

                                if (Utils.isNetworkConnectedMainThred(DTHListActivity.this)) {
                                    loader.show();
                                    loader.setCanceledOnTouchOutside(true);
                                    loader.setCancelable(false);

                                    String rech_mobile=jsonObject.getString("mobile");
                                    String rech_order_id=jsonObject.getString("orderId");
                                    String rech_status=jsonObject.getString("status");
                                    String rech_type=jsonObject.getString("service");

                                    RechargeOrderStatusUpdateApi("",OrderId,"Success","Wallet","Wallet",TransactionId,"","","","","","DTH", rech_mobile  , rech_order_id, rech_status, rech_type);
                                } else {
                                    EasyToast.error(DTHListActivity.this, "No Internet Connnection");
                                }
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


    public void GetUpdateWalletAPI()
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

                                CreateOrderApi(etAmount.getText().toString(),"Recharge",pref.get(AppSettings.UserId),etCustomerID.getText().toString(), pref.get(AppSettings.UserName));

                            }
                            else
                            {
                                loader.cancel();

                                money_wallet=0.0;

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
                                EasyToast.error(DTHListActivity.this, "This UserId or Password is not valid.");
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
                                EasyToast.error(DTHListActivity.this, response.getString("Msg"));
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
                                EasyToast.error(DTHListActivity.this,response.getString("Msg") );
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
                                // etAmount.setText("");
                                UpdateWalletApi(pref.get(AppSettings.UserId),razorpay_amt,"Success");
                            }
                            else
                            {
                                EasyToast.error(DTHListActivity.this,response.getString("Msg") );
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
                                GetUpdateWalletAPI();
                            }
                            else
                            {
                                EasyToast.error(DTHListActivity.this, response.getString("Msg"));
                                loader.dismiss();
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

            //Log.v("amount_value",options.toString());
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
