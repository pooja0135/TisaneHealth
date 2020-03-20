package com.tisanehealth.Fragment.Bank;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
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
import static com.tisanehealth.Helper.AppUrls.GetAllPinType;
import static com.tisanehealth.Helper.AppUrls.GetMoneyBalanceWallet;
import static com.tisanehealth.Helper.AppUrls.GetMoneyWallet;
import static com.tisanehealth.Helper.AppUrls.Pinpurchasebymybank;

public class PurchasePinFragment extends Fragment{


    //Preferences
    Preferences pref;
    //CustomLoader
    CustomLoader loader;

    Button btnSubmit;

    EditText etMemberId,etMoneyBalance,etNoOfPin,etTotalCost;

    ArrayList<HashMap<String,String>>pinlist=new ArrayList<>();
    ArrayList<String>pinvaluelist=new ArrayList<>();

     Spinner spinnerPin;

     double pinvalue=0.0;
     double value=0.0;
     double money_wallet=0.0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pin_purchase_fragment, container, false);
        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("Purchase Pin");
        ivLogo.setVisibility(View.GONE);
        tvHeader.setVisibility(View.VISIBLE);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        loadFragment(new BankManagement());
                        return true;
                    }
                }
                return false;
            }
        });


        btnSubmit=view.findViewById(R.id.btnSubmit);

       etMemberId=view.findViewById(R.id.etMemberId);
       etMoneyBalance=view.findViewById(R.id.etMoneyBalance);
       etNoOfPin=view.findViewById(R.id.etNoOfPin);
       etTotalCost=view.findViewById(R.id.etTotalCost);

       spinnerPin=view.findViewById(R.id.spinnerPin);



        //Preferences
        pref                = new Preferences(getActivity());

        //loader
        loader              = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

      /*  if (Utils.isNetworkConnectedMainThred(getActivity())) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);
          //  BankDetailListAPI();

        } else {
            EasyToast.error(getActivity(), "No Internet Connnection");
        }*/


        etMemberId.setText(pref.get(AppSettings.UserId));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNoOfPin.getText().toString().trim().isEmpty())
                {
                    EasyToast.warning(getActivity(),"Enter No Of Pin");
                }
                else if (etNoOfPin.getText().toString().trim().equals("0"))
                {
                    EasyToast.warning(getActivity(),"Enter No Of Pin");
                }
                else if (money_wallet<value)
                {
                    EasyToast.warning(getActivity(),"Your Wallet has insufficent amount for purchasing Pin.");
                }
                else
                {
                    if (Utils.isNetworkConnectedMainThred(getActivity())) {
                        loader.show();
                        loader.setCanceledOnTouchOutside(true);
                        loader.setCancelable(false);
                        PinPurchaseAPI();

                    } else {
                        EasyToast.error(getActivity(), "No Internet Connnection");
                    }
                }



            }
        });

        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);
            AllPinListAPI();
            GetWalletAPI();


        } else {
            EasyToast.error(getActivity(), "No Internet Connnection");
        }



        spinnerPin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    if (item.equals("Select"))
                    {
                        pinvalue=0;
                    }
                    else
                    {
                        pinvalue= Double.parseDouble(pinlist.get(position).get("KitAmount"));
                        if (etNoOfPin.getText().toString().isEmpty())
                        {
                            etTotalCost.setText("0.00");
                        }
                        else
                        {
                            double value=Double.valueOf(etNoOfPin.getText().toString())*pinvalue;
                            etTotalCost.setText(String.valueOf(value));
                        }
                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });


        etNoOfPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (etNoOfPin.getText().toString().isEmpty())
                {
                    etTotalCost.setText("0");
                    value=0.0;
                }
                else
                {
                     value=Double.valueOf(etNoOfPin.getText().toString())*pinvalue;
                    etTotalCost.setText(String.valueOf(value));
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });

        return view;


    }

    public void loadFragment(Fragment fragment) {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.card_flip_right_in, R.anim.card_flip_right_out,
                R.anim.card_flip_left_in, R.anim.card_flip_left_out);
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }


    //==========================================API===================================================//
    public void AllPinListAPI()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("","");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+GetAllPinType)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.v("jyiujojjujujujujuujujuj",response.toString());
                            String Message=   response.getString("Message");

                            if (Message.equals("Success"))
                            {

                                HashMap<String,String>map1=new HashMap<>();

                                map1.put("KitAmount","Select");
                                map1.put("KitAmountName","Select");
                                map1.put("KitCode","Select");

                                pinlist.add(map1);
                                pinvaluelist.add("Select");

                                JSONArray jsonArray=response.getJSONArray("PinRes");

                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);

                                    HashMap<String,String>map=new HashMap<>();

                                    map.put("KitAmount",jsonObject.getString("KitAmount"));
                                    map.put("KitAmountName",jsonObject.getString("KitAmountName"));
                                    map.put("KitCode",jsonObject.getString("KitCode"));

                                    pinlist.add(map);
                                    pinvaluelist.add(jsonObject.getString("KitAmountName"));

                                }

                                ArrayAdapter pinAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,pinvaluelist);
                                spinnerPin.setAdapter(pinAdapter);

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

                        Log.v("gjfihhgghghg",error.toString());
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

        AndroidNetworking.post(BaseUrl+GetMoneyBalanceWallet)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.v("jyiujojjujujujujuujujuj",response.toString());
                            String Message=   response.getString("Message");

                            if (Message.equals("Success"))
                            {


                                money_wallet=Double.valueOf(response.getString("MoneyWallet"));

                                etMoneyBalance.setText(String.valueOf(money_wallet));

                                loader.cancel();

                            }
                            else
                            {
                                loader.cancel();

                                money_wallet=0.0;
                                etMoneyBalance.setText(String.valueOf(money_wallet));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        Log.v("gjfihhgghghg",error.toString());
                        loader.cancel();
                    }
                });
    }

    public void PinPurchaseAPI()
    {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("KitAmount",pinlist.get(spinnerPin.getSelectedItemPosition()).get("KitAmount"));
            jsonObject.put("KitAmountName",pinlist.get(spinnerPin.getSelectedItemPosition()).get("KitAmountName"));
            jsonObject.put("NOP",etNoOfPin.getText().toString());
            jsonObject.put("PinIssueUserId",pref.get(AppSettings.UserId));
            jsonObject.put("Userid",pref.get(AppSettings.UserId));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+Pinpurchasebymybank)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.v("jyiujojjujujujujuujujuj",response.toString());
                            String Message=   response.getString("Message");

                            if (Message.equals("Success"))
                            {

                                 loadFragment(new BankManagement());
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

                        Log.v("gjfihhgghghg",error.toString());
                        loader.cancel();
                    }
                });
    }


}