package com.tisanehealth.recharge_pay_bill.mobile_recharge;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tisanehealth.Adapter.recharge.recharge_adapter;
import com.tisanehealth.Model.recharge.RechargePlanModel;
import com.tisanehealth.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;

import static com.tisanehealth.Helper.Utils.myUnsafeHttpClient;


public class RechargePlanFragment extends Fragment {

     RecyclerView rvRecharge;
     RechargePlanModel rechargePlanModel;
     ArrayList<RechargePlanModel>rechargelist=new ArrayList<>();
     Bundle bundle;

    public static RechargePlanFragment newInstance(int page, String title) {
        RechargePlanFragment fragmentFirst = new RechargePlanFragment();
        Bundle args = new Bundle();
        args.putString("someTitle", title);
        args.putString("page", String.valueOf(page));
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recharge_plan_fragment, container, false);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
      /*  view.setOnKeyListener(new View.OnKeyListener() {


            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        return true;
                    }
                }
                return false;
            }
        });*/


        rvRecharge=view.findViewById(R.id.rvRecharge);
        rvRecharge.setLayoutManager(new LinearLayoutManager(getActivity()));

         bundle=getArguments();

     //   //Log.v("gfgfgfgfhgfhghfghf",bundle.getString("someTitle"));

        //AndroidNetworking.initializegetActivity(), myUnsafeHttpClient());

        RechargeApi(bundle.getString("someTitle"),RechargeActivity.circle_id,RechargeActivity.operator_id);



        return view;
    }







    public void RechargeApi(String type,String circle_id,String operator_id) {

        rechargelist.clear();
        String url="https://api.rechapi.com/rech_plan.php?format=json&token=iYpga6msh23e1Bkqvovpid9xoFijuN&type="+type+"&cirid="+circle_id+"&opid="+operator_id;

        //Log.v("gfgfgfgfhgfhghfghf",url);

        AndroidNetworking.post(url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //Log.v("gfgfgfgfhgfhghfghf",response.toString());
                            String resCode=   response.getString("resCode");
                            if (resCode.equals("200"))
                            {
                                JSONObject jsonObject=response.getJSONObject("data");

                                if (bundle.getString("page").equals("0"))
                                {
                                    JSONArray jsonArray=jsonObject.getJSONArray("SPL");
                                    for (int i=0;i<jsonArray.length();i++)
                                    {
                                        JSONObject data=jsonArray.getJSONObject(i);

                                        rechargePlanModel=new RechargePlanModel(data.get("amount").toString(),data.get("detail").toString(),data.get("validity").toString(),data.get("talktime").toString());
                                        rechargelist.add(rechargePlanModel);
                                        rvRecharge.setAdapter(new recharge_adapter(getActivity(),rechargelist));
                                    }
                                }

                                else   if (bundle.getString("page").equals("1"))
                                {
                                    JSONArray jsonArray=jsonObject.getJSONArray("DATA");
                                    for (int i=0;i<jsonArray.length();i++)
                                    {
                                        JSONObject data=jsonArray.getJSONObject(i);

                                        rechargePlanModel=new RechargePlanModel(data.get("amount").toString(),data.get("detail").toString(),data.get("validity").toString(),data.get("talktime").toString());
                                        rechargelist.add(rechargePlanModel);
                                        rvRecharge.setAdapter(new recharge_adapter(getActivity(),rechargelist));
                                    }
                                }


                                else   if (bundle.getString("page").equals("2"))
                                {
                                    JSONArray jsonArray=jsonObject.getJSONArray("RMG");
                                    for (int i=0;i<jsonArray.length();i++)
                                    {
                                        JSONObject data=jsonArray.getJSONObject(i);

                                        rechargePlanModel=new RechargePlanModel(data.get("amount").toString(),data.get("detail").toString(),data.get("validity").toString(),data.get("talktime").toString());
                                        rechargelist.add(rechargePlanModel);
                                        rvRecharge.setAdapter(new recharge_adapter(getActivity(),rechargelist));
                                    }
                                }

                                else   if (bundle.getString("page").equals("3"))
                                {
                                    JSONArray jsonArray=jsonObject.getJSONArray("TUP");
                                    for (int i=0;i<jsonArray.length();i++)
                                    {
                                        JSONObject data=jsonArray.getJSONObject(i);

                                        rechargePlanModel=new RechargePlanModel(data.get("amount").toString(),data.get("detail").toString(),data.get("validity").toString(),data.get("talktime").toString());
                                        rechargelist.add(rechargePlanModel);
                                        rvRecharge.setAdapter(new recharge_adapter(getActivity(),rechargelist));
                                    }
                                }
                                else   if (bundle.getString("page").equals("4"))
                                {
                                    JSONArray jsonArray=jsonObject.getJSONArray("SPL");
                                    for (int i=0;i<jsonArray.length();i++)
                                    {
                                        JSONObject data=jsonArray.getJSONObject(i);

                                        rechargePlanModel=new RechargePlanModel(data.get("amount").toString(),data.get("detail").toString(),data.get("validity").toString(),data.get("talktime").toString());
                                        rechargelist.add(rechargePlanModel);
                                        rvRecharge.setAdapter(new recharge_adapter(getActivity(),rechargelist));
                                    }
                                }


                            }
                            else
                            {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                            //Log.v("gfgfgfgfhgfhghfghf",e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        //Log.v("gfgfgfgfhgfhghfghf",anError.toString());
                    }


                });
    }


}
