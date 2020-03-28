package com.tisanehealth.recharge_pay_bill.datacard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.medialablk.easytoast.EasyToast;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.tisanehealth.Activity.DashBoardActivity;
import com.tisanehealth.Adapter.recharge.Adapter_circle;
import com.tisanehealth.Adapter.recharge.Adapter_operator;
import com.tisanehealth.Adapter.recharge.Adapter_viewpager;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.MovingViewPager;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.Model.recharge.CircleModel;
import com.tisanehealth.Model.recharge.OperatorModel;
import com.tisanehealth.recharge_pay_bill.mobile_recharge.ContactActivity;
import com.tisanehealth.Model.recharge.RechargeListModel;
import com.tisanehealth.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import io.paperdb.Paper;
import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.CreateOrder;
import static com.tisanehealth.Helper.AppUrls.GetMoneyWallet;
import static com.tisanehealth.Helper.AppUrls.OrderStatusUpdate;
import static com.tisanehealth.Helper.AppUrls.WalletRequestByUserPayment;
import static com.tisanehealth.Helper.AppUrls.WalletUpdateByUserPayment;
import static com.tisanehealth.Helper.Utils.myUnsafeHttpClient;

public class DataCardActivity extends AppCompatActivity implements View.OnClickListener, PaymentResultWithDataListener {

    //Arraylist
    ArrayList<RechargeListModel> operatorlist = new ArrayList<>();
    ArrayList<CircleModel> circlelist = new ArrayList<>();
    ArrayList<String> imagelist = new ArrayList<>();

    //Model Class
    OperatorModel operatorModel;
    CircleModel circleModel;
    //Imageview
    ImageView ivUser, ivBack;
    //LinearLayout
    LinearLayout layoutBottomSheet;
    //RecyclerView
    RecyclerView recyclerViewOperator;
    //Adapter
    Adapter_operator adapter_operator;
    Adapter_circle adapter_circle;
    Adapter_viewpager adapter_viewpager;
    //Textview
    public static TextView tvOperatorName, tvCircleName;
    TextView tvContactName, tvContactNumber, tvAlphabet, tvChange, tvPlan;
    //RelativeLayout
    RelativeLayout rlOperatorName, rlCircleName;
    //ViewPager
    MovingViewPager viewpagerOffer;

    //Edittext
    EditText etAmount;

    //Button
    Button btnProceed;

    //Preference
    Preferences pref;

    //Customloader
    CustomLoader loader;

    public static String operator_id,circle_id,type;

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


    String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};
    ///int PERMISSION_ALL = 2;

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
    
    private static final int PERMISSION_REQUEST_CODE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_card);
        
        
        initialise();
    }


    //Initialise Value
    public void initialise() {

        Paper.init(this);

        //Imageview
        ivUser = findViewById(R.id.ivUser);
        ivBack = findViewById(R.id.ivBack);
        //Recylerview
        recyclerViewOperator = findViewById(R.id.recyclerviewOperator);
        //textview
        tvOperatorName      = findViewById(R.id.tvOperatorName);
        tvCircleName        = findViewById(R.id.tvCircleName);
        tvContactName       = findViewById(R.id.tvContactName);
        tvContactNumber     = findViewById(R.id.tvContactNumber);
        tvAlphabet          = findViewById(R.id.tvAlphabet);
        tvChange            = findViewById(R.id.tvChange);
        tvPlan              = findViewById(R.id.tvPlan);
        //RelativeLayout
        rlOperatorName      = findViewById(R.id.rlOperatorName);
        rlCircleName        = findViewById(R.id.rlCircleName);
        //Edittext
        etAmount            = findViewById(R.id.etAmount);
        //Button
        btnProceed            = findViewById(R.id.btnProceed);

        //Preferences
        pref                =new Preferences(this);

        //CustomLoader
        loader              =new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        //Arraylist
        operatorlist        = Utils.GetRechargeValue("Data_Card");
        circlelist          = Paper.book().read("circlelist");


        ivUser.setColorFilter(ContextCompat.getColor(this, R.color.color_list1), android.graphics.PorterDuff.Mode.MULTIPLY);


        //ViewPager
        viewpagerOffer      = findViewById(R.id.viewpagerOffer);
        adapter_viewpager = new Adapter_viewpager(this, image);
        viewpagerOffer.setAdapter(adapter_viewpager);
        viewpagerOffer.setCurrentItem(1, true);
        viewpagerOffer.setClipToPadding(false);

        NUM_PAGES = imagelist.size();

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES - 1) {
                    currentPage = 1;
                }
                viewpagerOffer.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);


        try {
            tvContactName.setText(getIntent().getExtras().getString("name"));
            tvContactNumber.setText(getIntent().getExtras().getString("number"));

            String str = tvContactName.getText().toString();
            String[] strArray = str.split(" ");
            StringBuilder builder = new StringBuilder();
            String str1 = strArray[0];
            for (String s : strArray) {
                String cap = str1.substring(0, 1).toUpperCase();
                tvAlphabet.setText(cap);
                builder.append(cap + " ");
            }
        } catch (Exception e) {

            String str = tvContactName.getText().toString();
            String[] strArray = str.split(" ");
            StringBuilder builder = new StringBuilder();
            String str1 = strArray[0];
            for (String s : strArray) {
                String cap = str1.substring(0, 1).toUpperCase();
                tvAlphabet.setText(cap);
                builder.append(cap + " ");
            }
        }

        //setOnClickListener
        rlOperatorName.setOnClickListener(this);
        rlCircleName.setOnClickListener(this);
        tvChange.setOnClickListener(this);
        tvPlan.setOnClickListener(this);
        ivBack.setOnClickListener(this);
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


    //=====================================BottomSheetDialog============================================//
    public void showBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.operator_list, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(DataCardActivity.this);
        dialog.setContentView(view);

        RecyclerView recyclerview = dialog.findViewById(R.id.recyclerviewOperator);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter_operator = new Adapter_operator(this, operatorlist, dialog,2);

        recyclerview.setAdapter(adapter_operator);
        ImageView ivCross = dialog.findViewById(R.id.ivCross);
        ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();


    }

    public void showBottomSheetCircleDialog() {
        View view = getLayoutInflater().inflate(R.layout.circle_list, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(DataCardActivity.this);
        dialog.setContentView(view);

        RecyclerView recyclerviewCircle = dialog.findViewById(R.id.recyclerviewCircle);
        recyclerviewCircle.setLayoutManager(new LinearLayoutManager(this));
        adapter_circle = new Adapter_circle(this, circlelist, dialog);
        recyclerviewCircle.setAdapter(adapter_circle);
        ImageView ivCross = dialog.findViewById(R.id.ivCross);
        ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlOperatorName:
                showBottomSheetDialog();
                break;
            case R.id.rlCircleName:
                showBottomSheetCircleDialog();
                break;
            case R.id.tvChange:
                if (!hasPermissions(DataCardActivity.this, PERMISSIONS)) {

                    ActivityCompat.requestPermissions(DataCardActivity.this, PERMISSIONS, PERMISSION_REQUEST_CODE);
                } else {
                    startActivity(new Intent(this, ContactActivity.class));

                }
                break;
            case R.id.tvPlan:
                break;
            case R.id.ivBack:
                startActivity(new Intent(this, DashBoardActivity.class));
                break;
            case R.id.btnProceed:
                if (tvOperatorName.getText().toString().equals("Select Operator")&& tvCircleName.getText().toString().equals("Select State"))
                {
                    Toast.makeText(this, "Select Mobile Operator and State .", Toast.LENGTH_SHORT).show();
                }
                else if (tvOperatorName.getText().toString().equals("Select Operator"))
                {
                    Toast.makeText(this, "Select Mobile Operator", Toast.LENGTH_SHORT).show();
                }
                else if ( tvCircleName.getText().toString().equals("Select State"))
                {
                    Toast.makeText(this, "Select State.", Toast.LENGTH_SHORT).show();
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
                            CreateOrderApi(etAmount.getText().toString(),"Recharge",pref.get(AppSettings.UserId),tvContactNumber.getText().toString(),tvContactName.getText().toString());

                        } else {
                            EasyToast.error(this, "No Internet Connnection");
                        }
                    }

                    else
                    {

                       // EasyToast.error(this, "You have insufficent balance in your wallet .Please add money in your wallet.");
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
        }
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {

                //Log.v("permissionvalue", permission);

                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                    startActivity(new Intent(this, ContactActivity.class));
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
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
                                    RechargeOrderStatusApi("",OrderId,"Pending","Wallet","Wallet",TransactionId,"","","","","","Data_Card");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                EasyToast.error(DataCardActivity.this,  response.getString("Msg"));
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
                                RechargeApi(tvContactNumber.getText().toString(), etAmount.getText().toString(), recharge_value.getOperator_Code(), TransactionId, opvalue1, opvalue2, opvalue3);

                            }
                            else
                            {
                                EasyToast.error(DataCardActivity.this, response.getString("Msg"));
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
                                EasyToast.error(DataCardActivity.this,"Recharge Successful.You will receive confirmation message in few minutes." );
                                startActivity(new Intent(DataCardActivity.this,DashBoardActivity.class));



                            }
                            else
                            {
                                EasyToast.error(DataCardActivity.this,response.getString("Msg") );
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
                                if (Utils.isNetworkConnectedMainThred(DataCardActivity.this)) {
                                    loader.show();
                                    loader.setCanceledOnTouchOutside(true);
                                    loader.setCancelable(false);
                                    RechargeOrderStatusUpdateApi("",OrderId,"Success","Wallet","Wallet",TransactionId,"","","","","","Data_Card",   rech_mobile, rech_order_id, rech_status, rech_type);

                                } else {
                                    EasyToast.error(DataCardActivity.this, "No Internet Connnection");
                                    loader.cancel();
                                }
                            }
                            else if (resCode.equals("201"))
                            {
                                if (Utils.isNetworkConnectedMainThred(DataCardActivity.this)) {
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
                                    EasyToast.error(DataCardActivity.this, "No Internet Connnection");
                                }


                            }

                            else if (resCode.equals("112"))
                            {
                                EasyToast.error(DataCardActivity.this, response.getString("resText"));
                                if (Utils.isNetworkConnectedMainThred(DataCardActivity.this)) {
                                    loader.show();
                                    loader.setCanceledOnTouchOutside(true);
                                    loader.setCancelable(false);
                                    RechargeOrderStatusUpdateApi("",OrderId,"Pending","Wallet","Wallet",TransactionId,"","","","","","Data_Card",   rech_mobile, rech_order_id, rech_status, rech_type);

                                } else {
                                    EasyToast.error(DataCardActivity.this, "No Internet Connnection");
                                }
                            }
                            else
                            {
                                Toast.makeText(DataCardActivity.this, response.getString("resText"), Toast.LENGTH_SHORT).show();
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
                                if (Utils.isNetworkConnectedMainThred(DataCardActivity.this)) {
                                    loader.show();
                                    loader.setCanceledOnTouchOutside(true);
                                    loader.setCancelable(false);

                                    String rech_mobile=jsonObject.getString("mobile");
                                    String rech_order_id=jsonObject.getString("orderId");
                                    String rech_status=jsonObject.getString("status");
                                    String rech_type=jsonObject.getString("service");

                                    RechargeOrderStatusUpdateApi("",OrderId,"Success","Wallet","Wallet",TransactionId,"","","","","","Data_Card", rech_mobile  , rech_order_id, rech_status, rech_type);

                                } else {
                                    EasyToast.error(DataCardActivity.this, "No Internet Connnection");
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
                                Toast.makeText(DataCardActivity.this, jsonObject.getString("resText"), Toast.LENGTH_SHORT).show();

                                if (Utils.isNetworkConnectedMainThred(DataCardActivity.this)) {
                                    loader.show();
                                    loader.setCanceledOnTouchOutside(true);
                                    loader.setCancelable(false);

                                    String rech_mobile=jsonObject.getString("mobile");
                                    String rech_order_id=jsonObject.getString("orderId");
                                    String rech_status=jsonObject.getString("status");
                                    String rech_type=jsonObject.getString("service");

                                    RechargeOrderStatusUpdateApi("",OrderId,"Success","Wallet","Wallet",TransactionId,"","","","","","Data_Card", rech_mobile  , rech_order_id, rech_status, rech_type);
                                } else {
                                    EasyToast.error(DataCardActivity.this, "No Internet Connnection");
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

                                CreateOrderApi(etAmount.getText().toString(),"Recharge",pref.get(AppSettings.UserId),tvContactNumber.getText().toString(),tvContactName.getText().toString());

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
                                EasyToast.error(DataCardActivity.this, "This UserId or Password is not valid.");
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
                                EasyToast.error(DataCardActivity.this, response.getString("Msg"));
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
                                EasyToast.error(DataCardActivity.this,response.getString("Msg") );
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
                                EasyToast.error(DataCardActivity.this,response.getString("Msg") );
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
                                EasyToast.error(DataCardActivity.this, response.getString("Msg"));
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
