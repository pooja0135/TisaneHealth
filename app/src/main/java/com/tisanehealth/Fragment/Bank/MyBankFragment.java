package com.tisanehealth.Fragment.Bank;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tisanehealth.Adapter.bank.MyBankAdapter;
import com.tisanehealth.Fragment.DashBoardFragment;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.R;
import com.medialablk.easytoast.EasyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.tisanehealth.Activity.DashBoardActivity.ivLogo;
import static com.tisanehealth.Activity.DashBoardActivity.rlHeader;
import static com.tisanehealth.Activity.DashBoardActivity.tvHeader;
import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.MyBank;
import static com.tisanehealth.Helper.AppUrls.MyBankSummary;

public class MyBankFragment extends Fragment{

    RecyclerView rvBankDetail;
    TextView tvCredit,tvDebit,tvTotalBalance;
    //Preferences
    Preferences pref;
    //CustomLoader
    CustomLoader loader;

    ArrayList<HashMap<String,String>>bankdetaillist=new ArrayList<>();

    double credit,debit=0.0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_bank_fragment, container, false);
        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("My Bank");
        ivLogo.setVisibility(View.GONE);
        tvHeader.setVisibility(View.VISIBLE);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        loadFragment(new DashBoardFragment());
                        return true;
                    }
                }
                return false;
            }
        });


        rvBankDetail  =view.findViewById(R.id.rvBankDetail);
        rvBankDetail.setLayoutManager(new LinearLayoutManager(getActivity()));

        tvCredit      =view.findViewById(R.id.tvCredit);
        tvDebit       =view.findViewById(R.id.tvDebit);
        tvTotalBalance=view.findViewById(R.id.tvTotalBalance);

        //Preferences
        pref                = new Preferences(getActivity());

        //loader
        loader              = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);
            BankDetailListAPI();

        } else {
            EasyToast.error(getActivity(), "No Internet Connnection");
        }



        return view;


    }

    public void loadFragment(Fragment fragment) {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(
//                R.anim.card_flip_right_in, R.anim.card_flip_right_out,
//                R.anim.card_flip_left_in, R.anim.card_flip_left_out);
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }


    //==========================================API===================================================//
    public void BankDetailListAPI()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MemberId",pref.get(AppSettings.UserId));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+MyBank)
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
                                JSONArray jsonArray=response.getJSONArray("BankWalletSumm");


                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);

                                    HashMap<String,String>map=new HashMap<>();

                                    map.put("Balance",jsonObject.getString("Balance"));
                                    map.put("Credit",jsonObject.getString("Credit"));
                                    map.put("Debit",jsonObject.getString("Debit"));
                                    map.put("Particulars",jsonObject.getString("Particulars"));
                                    map.put("TransactionDate",jsonObject.getString("TransactionDate"));

                                    bankdetaillist.add(map);

                                }

                                MyBankAdapter myBankAdapter=new MyBankAdapter(getActivity(),bankdetaillist);
                                rvBankDetail.setAdapter(myBankAdapter);

                                BankSummaryAPI();

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

                        //Log.v("gjfihhgghghg",error.toString());
                        loader.cancel();
                    }
                });
    }


    public void BankSummaryAPI()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MemberId",pref.get(AppSettings.UserId));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+MyBankSummary)
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
                                JSONArray jsonArray=response.getJSONArray("BankWalletAllSumm");


                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);

                                    HashMap<String,String>map=new HashMap<>();

                                    credit = credit+Double.parseDouble(jsonObject.getString("TotalCredit"));
                                    debit = debit+Double.parseDouble(jsonObject.getString("TotalDebit"));

                                    tvTotalBalance.setText("\u20B9"+jsonObject.getString("TotalBalance"));
                                }

                                tvCredit.setText("\u20B9"+ String.valueOf(credit));
                                tvDebit.setText("\u20B9"+ String.valueOf(debit));


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

                        //Log.v("gjfihhgghghg",error.toString());
                        loader.cancel();
                    }
                });
    }

}