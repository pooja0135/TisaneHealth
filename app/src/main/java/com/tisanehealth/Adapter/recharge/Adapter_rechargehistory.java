package com.tisanehealth.Adapter.recharge;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.medialablk.easytoast.EasyToast;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.OrderStatusUpdate;


public class Adapter_rechargehistory extends RecyclerView.Adapter<Adapter_rechargehistory.MyViewHolder> {

    Context mContext;
    ArrayList<HashMap<String, String>> rechargehistorylist;
    Preferences pref;
    ProgressDialog progressDialog;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        public ImageView imageview, ivStatus;
        TextView tvPaid, tvAmount, tvRechargeType, tvRechMobile, tvRechOrderId, tvDate, tvRechargestatus, btnRefresh;

        public MyViewHolder(View view) {
            super(view);

            imageview = view.findViewById(R.id.imageview);
            ivStatus = view.findViewById(R.id.ivStatus);
            tvPaid = view.findViewById(R.id.tvPaid);
            tvAmount = view.findViewById(R.id.tvAmount);
            tvRechargeType = view.findViewById(R.id.tvRechargeType);
            tvRechMobile = view.findViewById(R.id.tvRechMobile);
            tvRechOrderId = view.findViewById(R.id.tvRechOrderId);
            tvDate = view.findViewById(R.id.tvDate);
            tvRechargestatus = view.findViewById(R.id.tvRechargestatus);
            btnRefresh = view.findViewById(R.id.btnRefresh);


        }
    }

    public Adapter_rechargehistory(Context mContext, ArrayList<HashMap<String, String>> rechargehistorylist) {
        this.mContext = mContext;
        this.rechargehistorylist = rechargehistorylist;
        pref = new Preferences(mContext);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_recharge_history, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @SuppressWarnings("ConstantConditions")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        HashMap<String, String> model = rechargehistorylist.get(position);

        switch (model.get("bill_status")) {
            case "Recharge":
                holder.imageview.setImageResource(R.drawable.ic_mobile1);
                break;
            case "Electricity":
                holder.imageview.setImageResource(R.drawable.ic_electricity);
                break;
            case "DTH":
                holder.imageview.setImageResource(R.drawable.ic_satellite);
                break;
            case "Landline":
                holder.imageview.setImageResource(R.drawable.ic_telephone);
                break;
            case "Data_Card":
                holder.imageview.setImageResource(R.drawable.ic_datacard);
                break;
            case "Insurance":
                holder.imageview.setImageResource(R.drawable.ic_insurance);
                break;
            case "Broadband":
                holder.imageview.setImageResource(R.drawable.ic_broadband);
                break;
        }


        switch (model.get("bill_status")) {
            case "Recharge":
                holder.tvPaid.setText("Mobile Recharge");
                break;
            case "Electricity":
                holder.tvPaid.setText("Electricity Bill");
                break;
            case "DTH":
                holder.tvPaid.setText("DTH Recharge");
                break;
            case "Landline":
                holder.tvPaid.setText("Landline Bill");
                break;
            case "Data_Card":
                holder.tvPaid.setText("Datacard Recharge");
                break;
            case "Insurance":
                holder.tvPaid.setText("Insurance Bill");
                break;
            case "Broadband":
                holder.tvPaid.setText("Broadband Recharge");
                break;
        }

        holder.tvAmount.setText("\u20B9" + rechargehistorylist.get(position).get("Amount"));
        holder.tvRechargeType.setText(rechargehistorylist.get(position).get("rech_type"));
        holder.tvRechMobile.setText(rechargehistorylist.get(position).get("rech_mobile"));
        holder.tvRechOrderId.setText(rechargehistorylist.get(position).get("rech_order_id"));


        if (model.get("rech_status").equalsIgnoreCase("SUCCESS")) {
            holder.tvRechargestatus.setText("Debited from");
            holder.ivStatus.setImageResource(R.drawable.ic_credit_wallet);
        } else if (model.get("rech_status").equalsIgnoreCase("PENDING")) {
            holder.tvRechargestatus.setText("Pending");
            holder.ivStatus.setImageResource(R.drawable.ic_error);
            holder.btnRefresh.setVisibility(View.VISIBLE);
            holder.btnRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RechargeStatusApi(model.get("rech_order_id"), model.get("service"), model.get("OrderIdmodel"), model.get("TransactionId"), holder, model, position);
                }
            });
        } else {
            holder.tvRechargestatus.setText("Failed");
            holder.ivStatus.setImageResource(R.drawable.ic_error);
        }

        holder.btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RechargeStatusApi(model.get("rech_order_id"), model.get("service"), model.get("OrderIdmodel"), model.get("TransactionId"), holder, model, position);
            }
        });


        @SuppressLint("SimpleDateFormat") DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        @SuppressLint("SimpleDateFormat") DateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = null;
        try {
            date = originalFormat.parse(model.get("Entrydate"));
        } catch (Exception e) {

            //Log.v("hhhhhhhhhhhhh",e.toString());
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);
        holder.tvDate.setText(formattedDate);

    }

    @Override
    public int getItemCount() {
        return rechargehistorylist.size();
    }


    public void RechargeStatusApi(final String id, String service, String order_id, String TransactionId, MyViewHolder holder, HashMap<String, String> model, int position) {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String url = "https://api.rechapi.com/api_status.php?format=json&token=iYpga6msh23e1Bkqvovpid9xoFijuN&orderId=" + id;

        //Log.v("recharge_status2",url);

        AndroidNetworking.post(url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject object) {
                        progressDialog.dismiss();
                        try {

                            //Log.v("recharge_status2",object.toString());

                            JSONArray response = object.getJSONArray("data");
                            JSONObject jsonObject = response.getJSONObject(0);

                            String status = jsonObject.getString("error_code");
                            String rech_mobile = jsonObject.getString("mobile");
                            String rech_order_id = jsonObject.getString("orderId");
                            String rech_status = jsonObject.getString("status");
                            String rech_type = jsonObject.getString("service");
                            String txn_id = jsonObject.getString("TransId");
                            if (status.equals("200")) {
                                if (Utils.isNetworkConnectedMainThred(mContext)) {
                                    // loader.show();
                                    // loader.setCanceledOnTouchOutside(true);
                                    // loader.setCancelable(false);


                                } else {
                                    EasyToast.error(mContext, "No Internet Connnection");
                                }

                            } else if (status.equals("201")) {
                                /*Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        RechargeStatusApi(id, service, order_id, TransactionId, holder, model, position);
                                    }
                                }, 2000);*/
                            } else if (status.equals("105")) {
                               /* Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        RechargeStatusApi(id, service, order_id, TransactionId, holder, model, position);
                                    }
                                }, 2000);*/
                            } else {
                                Toast.makeText(mContext, jsonObject.getString("resText"), Toast.LENGTH_SHORT).show();

                                if (Utils.isNetworkConnectedMainThred(mContext)) {
                                    //    loader.show();
                                    //  loader.setCanceledOnTouchOutside(true);
                                    //loader.setCancelable(false);


                                } else {
                                    EasyToast.error(mContext, "No Internet Connnection");
                                }
                            }
                            RechargeOrderStatusUpdateApi("", order_id, "Success", "Wallet", "Wallet", TransactionId, "", "", "", "", "", service, rech_mobile, rech_order_id, rech_status, rech_type, holder, model, position);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }


                });
    }

    public void RechargeOrderStatusUpdateApi(String Myhpayid, String OrderId, String PayStatus, String PaymentMethod, String PaymentMode, final String TransactionId,
                                             String credit_used, String razorpay_amt, String razorpay_order_id, String razorpay_payment_id, String razorpay_signature, String service,
                                             String rech_mobile, String rech_order_id, final String rech_status, final String rech_type, MyViewHolder holder, HashMap<String, String> model, int position) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("Myhpayid", Myhpayid);
            jsonObject.put("OrderId", model.get("OrderId"));
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
            jsonObject.put("Amount", model.get("Amount"));
            jsonObject.put("MemberId", pref.get(AppSettings.UserId));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl + OrderStatusUpdate)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();

                            //Log.v("recharge_status4",response.toString());
                            boolean Status = response.getBoolean("Status");
                            if (Status) {
                                EasyToast.info(mContext, "Recharge Successful.You will receive confirmation message in few minutes.");
                                if (jsonObject.getString("Msg").equalsIgnoreCase("success")) {
                                    holder.tvRechargestatus.setText("Debited from");
                                    holder.ivStatus.setImageResource(R.drawable.ic_credit_wallet);
                                    holder.btnRefresh.setVisibility(View.GONE);
                                } else {
                                    EasyToast.info(mContext, jsonObject.getString("Msg"));
                                }


                                  /*  if (model.get("rech_status").equalsIgnoreCase("SUCCESS")) {

                                    } else if (model.get("rech_status").equalsIgnoreCase("PENDING")) {
                                        holder.tvRechargestatus.setText("Pending");
                                        holder.ivStatus.setImageResource(R.drawable.ic_error);
                                        holder.btnRefresh.setVisibility(View.VISIBLE);
                                        holder.btnRefresh.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                RechargeStatusApi(model.get("rech_order_id"), model.get("service"), model.get("OrderIdmodel"), model.get("TransactionId"), holder, model, position);
                                            }
                                        });
                                    } else {
                                        holder.tvRechargestatus.setText("Failed");
                                        holder.btnRefresh.setVisibility(View.GONE);
                                        holder.ivStatus.setImageResource(R.drawable.ic_error);
                                    }*/


                            } else {
                                EasyToast.error(mContext, response.getString("Msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                        //Log.v("gjfihhgghghg",error.toString());
                    }
                });
    }


}
