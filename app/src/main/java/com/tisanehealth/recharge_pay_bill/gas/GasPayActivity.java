package com.tisanehealth.recharge_pay_bill.gas;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import com.tisanehealth.Model.recharge.RechargeListModel;
import com.tisanehealth.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.CreateOrder;
import static com.tisanehealth.Helper.AppUrls.GetMoneyWallet;
import static com.tisanehealth.Helper.AppUrls.OrderStatusUpdate;
import static com.tisanehealth.Helper.AppUrls.WalletRequestByUserPayment;
import static com.tisanehealth.Helper.AppUrls.WalletUpdateByUserPayment;


public class GasPayActivity extends AppCompatActivity implements PaymentResultWithDataListener {
    ImageView ivBack;
    Button btnConfirm;
    MovingViewPager viewpagerGas;
    Adapter_viewpager adapter_viewpager;

    TextView tvHeader;
    TextView tvAmount;
    EditText etAmount;

    LinearLayout linearlayout;
    Preferences pref;
    CustomLoader loader;
    LinearLayout editTextLayout;

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

    String OrderId;

    String mobile="";
    String opvalue1="";
    String opvalue2="";
    String opvalue3="";
    String online_order_id;
    String online_transaction_id;
    RechargeListModel recharge_value;
    String TransactionId;
    double money_wallet=0.0;

    ArrayList<String> datalist = new ArrayList<>();
    List<View> allViewInstance = new ArrayList<View>();
    List<View> allViewInstance1 = new ArrayList<View>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_pay);

        Paper.init(this);

        ivBack               = findViewById(R.id.ivBack);
        tvHeader             = findViewById(R.id.tvHeader);
        tvAmount             = findViewById(R.id.tvAmount);
        etAmount             = findViewById(R.id.etAmount);
        btnConfirm           = findViewById(R.id.btnConfirm);
        linearlayout         = findViewById(R.id.linearlayout);
        viewpagerGas = findViewById(R.id.vpGas);


        //Preferences
        pref                 =new Preferences(this);

        //Loader
        loader               =new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


        adapter_viewpager    = new Adapter_viewpager(this, image);


        viewpagerGas.setAdapter(adapter_viewpager);
        viewpagerGas.setCurrentItem(1, true);
        viewpagerGas.setClipToPadding(false);

        NUM_PAGES = image.length;

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES - 1) {
                    currentPage = 1;
                }
                viewpagerGas.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recharge_value = getIntent().getParcelableExtra("data_value");

        tvHeader.setText(recharge_value.getService());


        String[] arrayString = recharge_value.getInstruction().trim().split("\\,");
        for (int k = 0; k < arrayString.length; k++) {
            System.out.println(arrayString[k]);
            datalist.add(arrayString[k]);
        }

        //add Dynamic form
        addEditTexts();


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int x = 0; x < allViewInstance.size(); x++) {
                    EditText editText = (EditText)allViewInstance.get(x);
                    TextView textView = (TextView)allViewInstance1.get(x);
                    if(editText.getText().toString().isEmpty()){
                        textView.setVisibility(View.VISIBLE);
                        return;
                    }
                    else
                        {
                            textView.setVisibility(View.GONE);
                        }
                }
                if (etAmount.getText().toString().trim().isEmpty())
                  {
                    tvAmount.setVisibility(View.VISIBLE);

                    //Log.v("amountvalue","123456");
                  }
                  else {
                    tvAmount.setVisibility(View.GONE);
                    //Log.v("amountvalue", "123");


                    if (money_wallet >= Double.valueOf(etAmount.getText().toString())) {

                        if (Utils.isNetworkConnectedMainThred(GasPayActivity.this)) {
                            loader.show();
                            loader.setCanceledOnTouchOutside(true);
                            loader.setCancelable(false);

                            EditText editText = (EditText) allViewInstance.get(0);

                            CreateOrderApi(etAmount.getText().toString(), "Recharge", pref.get(AppSettings.UserId), editText.getText().toString(), pref.get(AppSettings.UserName));

                        } else {
                            EasyToast.error(GasPayActivity.this, "No Internet Connnection");
                        }

                    } else {

                       // EasyToast.error(ElectricityPayActivity.this, "You have insufficent balance in your wallet .Please add money in your wallet.");
                        if (Utils.isNetworkConnectedMainThred(GasPayActivity.this)) {
                            loader.show();
                            loader.setCanceledOnTouchOutside(true);
                            loader.setCancelable(false);
                            CreateOrderForWalletApi(etAmount.getText().toString(),pref.get(AppSettings.UserId),pref.get(AppSettings.UserMobile),pref.get(AppSettings.UserName));

                        } else {
                            EasyToast.error(GasPayActivity.this, "No Internet Connnection");
                        }
                    }


                }
            }







        });


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

    private void addEditTexts() {
        editTextLayout = new LinearLayout(this);
        editTextLayout.setOrientation(LinearLayout.VERTICAL);
        linearlayout.addView(editTextLayout);

        for (int i = 0; i < datalist.size(); i++) {

            TextView textView = new TextView(this);
            textView.setText(datalist.get(i));
            textView.setTextColor(getResources().getColor(R.color.grey_700));
            textView.setId(0);
            textView.setBackground(null);
            textView.setTextSize(14);
            setTextViewAttributes(textView);
            editTextLayout.addView(textView);


            EditText editText = new EditText(this);
            editText.setHint(datalist.get(i));
            editText.setTextColor(getResources().getColor(R.color.black));
            editText.setId(0);
            editText.setBackground(null);
            editText.setTextSize(14);


            setEditTextAttributes(editText);
            editTextLayout.addView(editText);
            allViewInstance.add(editText);

            View view = new View(this);
            view.setBackgroundColor(getResources().getColor(R.color.grey_400));
            setViewAttributes(view);
            editTextLayout.addView(view);



            TextView textView1 = new TextView(this);
            textView1.setText("Enter "+datalist.get(i)+"*");
            textView1.setTextColor(getResources().getColor(R.color.red_600));
            textView1.setId(0);
            textView1.setBackground(null);
            textView1.setTextSize(12);
            textView1.setVisibility(View.GONE);
            setTextViewAttributes(textView1);
            editTextLayout.addView(textView1);
            allViewInstance1.add(textView1);
        }
    }


    private void setTextViewAttributes(TextView textview) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(convertDpToPixel(16),
                convertDpToPixel(5),
                convertDpToPixel(16),
                0
        );

        textview.setLayoutParams(params);
    }

    //This function to convert DPs to pixels
    private int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    private void setEditTextAttributes(EditText editText) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(convertDpToPixel(16),
                convertDpToPixel(0),
                convertDpToPixel(16),
                0
        );

        editText.setLayoutParams(params);
    }

    private void setViewAttributes(View view) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2);

        params.setMargins(convertDpToPixel(16),
                convertDpToPixel(0),
                convertDpToPixel(16),
                0
        );

        view.setLayoutParams(params);
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
                                    RechargeOrderStatusApi("",OrderId,"Pending","Wallet","Wallet",TransactionId,"","","","","","Gas");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                EasyToast.error(GasPayActivity.this,  response.getString("Msg"));
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
                                try {
                                    for (int x = 0; x < allViewInstance.size(); x++) {
                                        EditText editText = (EditText) allViewInstance.get(x);
                                        if (x == 0) {
                                            mobile = editText.getText().toString();
                                        } else if (x == 1) {
                                            opvalue1 = editText.getText().toString();
                                        } else if (x == 2) {
                                            opvalue2 = editText.getText().toString();
                                        } else if (x == 3) {
                                            opvalue3 = editText.getText().toString();
                                        }
                                    }

                                    RechargeApi(mobile, etAmount.getText().toString(), recharge_value.getOperator_Code(), TransactionId, opvalue1, opvalue2, opvalue3);
                                }
                                catch (Exception e)
                                {

                                }

                            }
                            else
                            {
                                EasyToast.error(GasPayActivity.this, response.getString("Msg"));
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
                                if (rech_status.equalsIgnoreCase("success")) {
                                    EasyToast.error(GasPayActivity.this, "Recharge Successful.You will receive confirmation message in few minutes.");
                                    startActivity(new Intent(GasPayActivity.this, DashBoardActivity.class));
                                }


                            }
                            else
                            {
                                EasyToast.error(GasPayActivity.this,response.getString("Msg") );
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
                                if (Utils.isNetworkConnectedMainThred(GasPayActivity.this)) {
                                    loader.show();
                                    loader.setCanceledOnTouchOutside(true);
                                    loader.setCancelable(false);
                                    RechargeOrderStatusUpdateApi("",OrderId,"Success","Wallet","Wallet",TransactionId,"","","","","","Gas",   rech_mobile, rech_order_id, rech_status, rech_type);

                                } else {
                                    EasyToast.error(GasPayActivity.this, "No Internet Connnection");
                                    loader.cancel();
                                }
                            }
                            else if (resCode.equals("201"))
                            {
                                if (Utils.isNetworkConnectedMainThred(GasPayActivity.this)) {
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
                                    EasyToast.error(GasPayActivity.this, "No Internet Connnection");
                                }


                            }

                            else if (resCode.equals("112"))
                            {
                                EasyToast.error(GasPayActivity.this, response.getString("resText"));
                                if (Utils.isNetworkConnectedMainThred(GasPayActivity.this)) {
                                    loader.show();
                                    loader.setCanceledOnTouchOutside(true);
                                    loader.setCancelable(false);
                                    RechargeOrderStatusUpdateApi("",OrderId,"Pending","Wallet","Wallet",TransactionId,"","","","","","Mobile_Postpaid",   rech_mobile, rech_order_id, rech_status, rech_type);

                                } else {
                                    EasyToast.error(GasPayActivity.this, "No Internet Connnection");
                                }
                            }
                            else
                            {
                                EasyToast.error(GasPayActivity.this, response.getString("resText"));
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
                            String rech_mobile=jsonObject.getString("mobile");
                            String rech_order_id=jsonObject.getString("orderId");
                            String rech_status=jsonObject.getString("status");
                            String rech_type=jsonObject.getString("service");
                            if (status.equals("200")) {
                                if (Utils.isNetworkConnectedMainThred(GasPayActivity.this)) {
                                    loader.show();
                                    loader.setCanceledOnTouchOutside(true);
                                    loader.setCancelable(false);





                                } else {
                                    EasyToast.error(GasPayActivity.this, "No Internet Connnection");
                                }

                            }
                            else if (status.equals("201"))
                            {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        RechargeStatusApi(id);
                                    }
                                }, 2000);
                            }

                            else if (status.equals("105"))
                            {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        RechargeStatusApi(id);
                                    }
                                }, 2000);
                            }
                            else
                            {
                                Toast.makeText(GasPayActivity.this, jsonObject.getString("resText"), Toast.LENGTH_SHORT).show();

                                if (Utils.isNetworkConnectedMainThred(GasPayActivity.this)) {
                                    loader.show();
                                    loader.setCanceledOnTouchOutside(true);
                                    loader.setCancelable(false);



                                } else {
                                    EasyToast.error(GasPayActivity.this, "No Internet Connnection");
                                }
                            }
                            RechargeOrderStatusUpdateApi("",OrderId,"Success","Wallet","Wallet",TransactionId,"","","","","","Gas", rech_mobile  , rech_order_id, rech_status, rech_type);
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
                                EditText editText = (EditText) allViewInstance.get(0);

                                CreateOrderApi(etAmount.getText().toString(), "Recharge", pref.get(AppSettings.UserId), editText.getText().toString(), pref.get(AppSettings.UserName));

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
                                EasyToast.error(GasPayActivity.this, "This UserId or Password is not valid.");
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
                                EasyToast.error(GasPayActivity.this, response.getString("Msg"));
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
                                EasyToast.error(GasPayActivity.this,response.getString("Msg") );
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
            jsonObject.put("rech_mobile", rech_mobile);
            jsonObject.put("rech_order_id", rech_order_id);
            jsonObject.put("rech_status", rech_status);
            jsonObject.put("rech_type", rech_type);
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
                                UpdateWalletApi(pref.get(AppSettings.UserId),razorpay_amt, response.getString("Msg"));
                            }
                            else
                            {
                                EasyToast.error(GasPayActivity.this,response.getString("Msg") );
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
                                if (response.getString("Msg").equalsIgnoreCase("success")||response.getString("Msg").equalsIgnoreCase("Amount Added into customer wallet")) {
                                    GetUpdateWalletAPI();
                                } else {
                                    EasyToast.error(GasPayActivity.this, response.getString("Msg"));
                                    loader.dismiss();
                                }
                            }
                            else
                            {
                                EasyToast.error(GasPayActivity.this, response.getString("Msg"));
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

            OrderStatusUpdateApi(paymentId,online_order_id,"Failed","Online","Online",online_transaction_id,"",
                    etAmount.getText().toString(),orderId,paymentId,signature,"Online Payment","","","","");



        } else {
            EasyToast.error(this, "No Internet Connnection");
        }
    }

}



