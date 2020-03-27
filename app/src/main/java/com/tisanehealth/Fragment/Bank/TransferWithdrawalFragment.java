package com.tisanehealth.Fragment.Bank;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.medialablk.easytoast.EasyToast;
import com.tisanehealth.Activity.DashBoardActivity;
import com.tisanehealth.Fragment.DashBoardFragment;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.tisanehealth.Activity.DashBoardActivity.ivLogo;
import static com.tisanehealth.Activity.DashBoardActivity.rlHeader;
import static com.tisanehealth.Activity.DashBoardActivity.tvHeader;
import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.GetMoneyWallet;
import static com.tisanehealth.Helper.AppUrls.GetProfile;
import static com.tisanehealth.Helper.AppUrls.TransferWithdrawRequest;

public class TransferWithdrawalFragment extends Fragment {
    EditText etMemberId,etApplicantName,etAddress,etPayee,etBankAccountNumber,etBankName,etBankIfsc,etBankBranch,etTransferWallet,etDate,etMobile,etRequestWallet;
    Button btnSubmit;
    Preferences pref;
    CustomLoader loader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.transfer_withdrawal_request, container, false);
        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("Transfer Withdrawal Request");
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


        etMemberId=view.findViewById(R.id.etMemberId);
        etApplicantName=view.findViewById(R.id.etApplicantName);
        etAddress=view.findViewById(R.id.etAddress);
        etPayee=view.findViewById(R.id.etPayee);
        etBankAccountNumber=view.findViewById(R.id.etBankAccountNumber);
        etBankName=view.findViewById(R.id.etBankName);
        etBankIfsc=view.findViewById(R.id.etBankIfsc);
        etBankBranch=view.findViewById(R.id.etBankBranch);
        etTransferWallet=view.findViewById(R.id.etTransferWallet);
        etDate=view.findViewById(R.id.etDate);
        etMobile=view.findViewById(R.id.etMobile);
        etRequestWallet=view.findViewById(R.id.etRequestWallet);


        btnSubmit=view.findViewById(R.id.btnSubmit);

        pref=new Preferences(getActivity());

        //loader
        loader                  = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


        etMemberId.setText(pref.get(AppSettings.UserId));
        etApplicantName.setText(pref.get(AppSettings.UserName));
        etAddress.setText(pref.get(AppSettings.UserAddress));
        etMobile.setText(pref.get(AppSettings.UserMobile));

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        etDate.setText(formattedDate);

        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);
            GetProfileAPI();
            GetWalletAPI();

        } else {
            EasyToast.error(getActivity(), "No Internet Connnection");
        }


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etMemberId.getText().toString().isEmpty())
                {
                    Toast.makeText(getActivity(),"Enter Member Id",Toast.LENGTH_SHORT).show();
                }
                else if (etApplicantName.getText().toString().isEmpty())
                {
                    Toast.makeText(getActivity(),"Enter Member Name",Toast.LENGTH_SHORT).show();
                }
                else if (etPayee.getText().toString().isEmpty()||etBankAccountNumber.getText().toString().isEmpty()||etBankIfsc.getText().toString().isEmpty()||etBankName.getText().toString().isEmpty()||etBankBranch.getText().toString().isEmpty())
                {
                    Toast.makeText(getActivity(),"Update Bank Details",Toast.LENGTH_SHORT).show();
                }
                else if (etRequestWallet.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getActivity(),"Enter Request Amount",Toast.LENGTH_SHORT).show();
                }

                else if (Double.parseDouble(etRequestWallet.getText().toString())>Double.parseDouble(etTransferWallet.getText().toString()))
                {
                    Toast.makeText(getActivity(),"Request amount will be not greater than wallet amount.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (Utils.isNetworkConnectedMainThred(getActivity())) {
                        loader.show();
                        loader.setCanceledOnTouchOutside(true);
                        loader.setCancelable(false);
                        TransferWithdrawalAPI();

                    } else {
                        EasyToast.error(getActivity(), "No Internet Connnection");
                    }
                }
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
    public void GetProfileAPI()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MemberId",pref.get(AppSettings.UserId));
            jsonObject.put("SessionId","");

        } catch (JSONException e) {
            e.printStackTrace();
        }



        AndroidNetworking.post(BaseUrl+GetProfile)
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
                                JSONArray jsonArray=response.getJSONArray("ProfileList");

                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                                    String AadharNo=jsonObject.getString("AadharNo")  ;
                                    String BankBranch=jsonObject.getString("BankBranch")  ;
                                    String BankIFSC=jsonObject.getString("BankIFSC")  ;
                                    String BankName=jsonObject.getString("BankName")  ;
                                    String BankAccountNumber=jsonObject.getString("BankAcNo")  ;
                                    String MobileNo=jsonObject.getString("MobileNo")  ;
                                    String Name=jsonObject.getString("Name")  ;
                                    String PanNo=jsonObject.getString("PanNo")  ;
                                    String PayeeName=jsonObject.getString("PayeeName")  ;
                                    String SponsorId=jsonObject.getString("SponsorId")  ;
                                    String SponsorName=jsonObject.getString("SponsorName")  ;
                                    String UserImage=jsonObject.getString("ProfilePath")  ;

                                    String Address=jsonObject.getString("Address")  ;

                                    pref.set(AppSettings.PayeeName,PayeeName);
                                    pref.set(AppSettings.Bankname,BankName);
                                    pref.set(AppSettings.BankIfsc,BankIFSC);
                                    pref.set(AppSettings.BankBranch,BankBranch);
                                    pref.set(AppSettings.BankAccountNumber,BankAccountNumber);
                                    pref.set(AppSettings.PanNumber,PanNo);
                                    pref.set(AppSettings.UserMobile,MobileNo);
                                    pref.set(AppSettings.UserName,Name);
                                    pref.commit();


                                    etPayee.setText(pref.get(AppSettings.PayeeName));
                                    etBankAccountNumber.setText(pref.get(AppSettings.BankAccountNumber));
                                    etBankName.setText(pref.get(AppSettings.Bankname));
                                    etBankIfsc.setText(pref.get(AppSettings.BankIfsc));
                                    etBankBranch.setText(pref.get(AppSettings.BankBranch));


                                    etAddress.setText(Address);




                                }

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


                                double money_wallet=Double.valueOf(response.getString("MoneyWallet"));
                                etTransferWallet.setText(String.valueOf(money_wallet));
                                loader.cancel();

                            }
                            else
                            {
                                loader.cancel();
                                double  money_wallet=0.0;
                                etTransferWallet.setText(String.valueOf(money_wallet));
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

    public void TransferWithdrawalAPI()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Member_Id",pref.get(AppSettings.UserId));
            jsonObject.put("MobileNo",pref.get(AppSettings.UserMobile));
            jsonObject.put("ApplicantName",etApplicantName.getText().toString());
            jsonObject.put("Address",etAddress.getText().toString());
            jsonObject.put("BankAccountNumber",etBankAccountNumber.getText().toString());
            jsonObject.put("BankBranch",etBankBranch.getText().toString());
            jsonObject.put("BankIFSCCode",etBankIfsc.getText().toString());
            jsonObject.put("BankName",etBankName.getText().toString());
            jsonObject.put("Date",etDate.getText().toString());
            jsonObject.put("PayeeName",etPayee.getText().toString());
            jsonObject.put("RequestWallet",etRequestWallet.getText().toString());
            jsonObject.put("TransferWallet",etTransferWallet.getText().toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+TransferWithdrawRequest)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //Log.v("jyiujojjujujujujuujujuj",response.toString());
                            Boolean Status=   response.getBoolean("Status");
                            String message=   response.getString("ResponseMsg");

                            if (Status)
                            {
                                loader.cancel();
                                Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                loader.cancel();
                                Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();



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