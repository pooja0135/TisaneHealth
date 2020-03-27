package com.tisanehealth.Fragment.Profile;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tisanehealth.Fragment.SettingFragment;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.R;
import com.medialablk.easytoast.EasyToast;

import org.json.JSONException;
import org.json.JSONObject;

import static com.tisanehealth.Activity.DashBoardActivity.ivLogo;
import static com.tisanehealth.Activity.DashBoardActivity.rlHeader;
import static com.tisanehealth.Activity.DashBoardActivity.tvHeader;
import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.UpdateBank;

public class BankDetailFragment extends Fragment implements View.OnClickListener{

    //LinearLayout
    LinearLayout llBankDetail,llEditBankDetail;
    //ImageView
    ImageView ivEdit;
    //Button
    Button btnUpdate;
    //Edittext
    EditText etBankName,etIFSC,etAccountNumber,etPanNumber,etBankBranch,etPayeeName,etTranscationPassword;
    //Textview
    TextView tvBankName,tvIFSC,tvAccountNumber,tvPanNumber,tvBankBranch,tvLastUpdate,tvPayeeName;
    //Preferences
    Preferences pref;
    //CustomLoader
    CustomLoader loader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bank_details_fragment, container, false);
        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("Bank Details");
        ivLogo.setVisibility(View.GONE);
        tvHeader.setVisibility(View.VISIBLE);


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        loadFragment(new SettingFragment());
                        return true;
                    }
                }
                return false;
            }
        });

        //Initialise Value
        initialise(view);

        //setOnCLickListener
        ivEdit.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);

        return view;


    }

    //Initialise Value
    public void  initialise(View view)
    {
        //LinearLayout
        llBankDetail        =view.findViewById(R.id.llBankDetail);
        llEditBankDetail    =view.findViewById(R.id.llEditBankDetail);

        //Button
        btnUpdate           =view.findViewById(R.id.btnUpdate);

        //Imageview
        ivEdit              =view.findViewById(R.id.ivEdit);

        //Textview
        tvBankBranch         =view.findViewById(R.id.tvBankBranch);
        tvBankName           =view.findViewById(R.id.tvBankName);
        tvPayeeName           =view.findViewById(R.id.tvUserName);
        tvIFSC               =view.findViewById(R.id.tvBankIfsc);
        tvAccountNumber      =view.findViewById(R.id.tvBankAccount);
        tvPanNumber          =view.findViewById(R.id.tvPanNumber);
        tvLastUpdate         =view.findViewById(R.id.tvLastUpdate);

        //Edittext
        etAccountNumber      =view.findViewById(R.id.etAccountNumber);
        etBankName           =view.findViewById(R.id.etBankName);
        etPayeeName           =view.findViewById(R.id.etPayeeName);
        etIFSC               =view.findViewById(R.id.etIFSC);
        etPanNumber          =view.findViewById(R.id.etPanNumber);
        etBankBranch         =view.findViewById(R.id.etBankBranch);
        etTranscationPassword=view.findViewById(R.id.etTranscationPassword);

        //Preferences
        pref                = new Preferences(getActivity());

        //loader
        loader              = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        tvBankBranch.setText(pref.get(AppSettings.BankBranch));
        tvBankName.setText(pref.get(AppSettings.Bankname));
        tvPayeeName.setText(pref.get(AppSettings.PayeeName));
        tvIFSC.setText(pref.get(AppSettings.BankIfsc));
        tvPanNumber.setText(pref.get(AppSettings.PanNumber));
        tvAccountNumber.setText(pref.get(AppSettings.BankAccountNumber));



        etAccountNumber.setText(pref.get(AppSettings.BankAccountNumber));
        etBankName.setText(pref.get(AppSettings.Bankname));
        etPayeeName.setText(pref.get(AppSettings.PayeeName));
        etIFSC.setText(pref.get(AppSettings.BankIfsc));
        etPanNumber.setText(pref.get(AppSettings.PanNumber));
        etAccountNumber.setText(pref.get(AppSettings.BankAccountNumber));



        if (!pref.get(AppSettings.PayeeName).isEmpty()&&!pref.get(AppSettings.PanNumber).isEmpty()&&!pref.get(AppSettings.BankAccountNumber).isEmpty()&&!pref.get(AppSettings.BankBranch).isEmpty()&&!pref.get(AppSettings.BankIfsc).isEmpty()&&!pref.get(AppSettings.Bankname).isEmpty())
        {
            ivEdit.setVisibility(View.GONE);
        }
        else
        {

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ivEdit:
                Animation aniFade1 = AnimationUtils.loadAnimation(getActivity(),R.anim.fadeout);
                llBankDetail.startAnimation(aniFade1);
                llBankDetail.setVisibility(View.GONE);
                llEditBankDetail.setVisibility(View.VISIBLE);
                Animation aniFade = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_in);
                llEditBankDetail.startAnimation(aniFade);
                tvHeader.setText("Update Bank Details");
                break;
            case R.id.btnUpdate:
            /*    Animation aniFade2 = AnimationUtils.loadAnimation(getActivity(),R.anim.fadeout);
                llEditBankDetail.startAnimation(aniFade2);
                llBankDetail.setVisibility(View.VISIBLE);
                llEditBankDetail.setVisibility(View.GONE);
                Animation aniFad3 = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_in);
                llBankDetail.startAnimation(aniFad3);
                tvHeader.setText("Bank Details");

*/
                if (Utils.isNetworkConnectedMainThred(getActivity())) {
                    loader.show();
                    loader.setCanceledOnTouchOutside(true);
                    loader.setCancelable(false);
                    UpdateBankAPI();

                } else {
                    EasyToast.error(getActivity(), "No Internet Connnection");
                }
                break;
        }
    }


    //===============================Load Fragment=================================================//
    public void loadFragment(Fragment fragment)
    {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.card_flip_right_in, R.anim.card_flip_right_out,
                R.anim.card_flip_left_in, R.anim.card_flip_left_out);
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }

    //==========================================API===================================================//
    public void UpdateBankAPI()
    {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("MemberId",pref.get(AppSettings.UserId));
            jsonObject.put("AccountNo",etAccountNumber.getText().toString());
            jsonObject.put("BankBranch",etBankBranch.getText().toString());
            jsonObject.put("BankName",etBankName.getText().toString());
            jsonObject.put("IFSCCode", etIFSC.getText().toString());
            jsonObject.put("PayeeName", etPayeeName.getText().toString());
            jsonObject.put("EntryBy",pref.get(AppSettings.UserId));
            jsonObject.put("PANNo",etPanNumber.getText().toString());
            jsonObject.put("TransPassword",etTranscationPassword.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.v("jyiujojjujujujujuujujuj",jsonObject.toString());
        AndroidNetworking.post(BaseUrl+UpdateBank)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String Status=   response.getString("Status");
                            if (Status.equals("true"))
                            {
                                loader.cancel();
                                Animation aniFade2 = AnimationUtils.loadAnimation(getActivity(),R.anim.fadeout);
                                llEditBankDetail.startAnimation(aniFade2);
                                llBankDetail.setVisibility(View.VISIBLE);
                                llEditBankDetail.setVisibility(View.GONE);
                                Animation aniFad3 = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_in);
                                llBankDetail.startAnimation(aniFad3);
                                tvHeader.setText("Bank Details");
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