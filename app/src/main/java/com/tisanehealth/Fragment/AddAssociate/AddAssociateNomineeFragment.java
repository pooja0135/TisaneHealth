package com.tisanehealth.Fragment.AddAssociate;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tisanehealth.Fragment.Team.TeamFragment;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.R;
import com.medialablk.easytoast.EasyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.StringTokenizer;

import static com.tisanehealth.Activity.DashBoardActivity.rlHeader;
import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.MemberRegistration;

public class AddAssociateNomineeFragment extends Fragment implements View.OnClickListener{

    //Button
    Button btnContinue;
    //Edittext
    EditText etNomineeName,etNomineeFather,etNomineeRelation,etNomineeAddress;
    //TextView
    TextView tvStep1,tvStep2,tvStep3,tvStep4;
    //Preferences
    Preferences pref;
    //CustomLoader
    CustomLoader loader;
    //CheckBox
    CheckBox checkBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_associate_nominee_fragment, container, false);
        rlHeader.setVisibility(View.GONE);

        //BackPressed of Fragment
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        loadFragment(new AddAssociateBankFragment());
                        return true;
                    }
                }
                return false;
            }
        });

        //Initialise Value
        initialise(view);

        return view;


    }
    //Initialise Value
    public void initialise(View view)
    {
        //Button
        btnContinue        = view.findViewById(R.id.btnContinue);

        //Edittext
        etNomineeAddress   =view.findViewById(R.id.etNomineeAddress);
        etNomineeName      =view.findViewById(R.id.etNomineeName);
        etNomineeFather    =view.findViewById(R.id.etNomineeFather);
        etNomineeRelation  =view.findViewById(R.id.etNomineeRelation);

        //TextView
        tvStep1             =view.findViewById(R.id.tvStep1);
        tvStep2             =view.findViewById(R.id.tvStep2);
        tvStep3             =view.findViewById(R.id.tvStep3);
        tvStep4             =view.findViewById(R.id.tvStep4);

        //Preferences
        pref                = new Preferences(getActivity());

        //loader
        loader              = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        //CheckBox
        checkBox             =view.findViewById(R.id.checkbox);

        SpannableString str = new SpannableString(getResources().getString(R.string.terms_and_condititons));
        str.setSpan(new ForegroundColorSpan(Color.RED), 11, 31, 0);
        checkBox.setText(str);


        //SetOnCLickListener
        tvStep1.setOnClickListener(this);
        tvStep2.setOnClickListener(this);
        tvStep3.setOnClickListener(this);
        tvStep4.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
        checkBox.setOnClickListener(this);

    }

    //Load Fragment
    public void loadFragment(Fragment fragment) {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.card_flip_right_in, R.anim.card_flip_right_out,
                R.anim.card_flip_left_in, R.anim.card_flip_left_out);
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tvStep1:
                loadFragment(new AddAssociateProfileFragment());
                break;
            case R.id.tvStep2:
                loadFragment(new AddAssociateAddressFragment());
                break;
            case R.id.tvStep3:
                loadFragment(new AddAssociateBankFragment());
                break;
            case R.id.tvStep4:
                loadFragment(new AddAssociateNomineeFragment());
                break;
            case R.id.btnContinue:
                if (checkBox.isChecked())
                {
                    if (Utils.isNetworkConnectedMainThred(getActivity())) {
                        loader.show();
                        loader.setCanceledOnTouchOutside(true);
                        loader.setCancelable(false);
                        AddAssoicateAPI();

                    } else {
                        EasyToast.error(getActivity(), "No Internet Connnection");
                    }
                }
                else
                {
                    EasyToast.warning(getActivity(),"Please select Terms and Conditons");
                }

             //   loadFragment(new DashBoardFragment());
                break;
            case R.id.checkbox:
                if (checkBox.isChecked())
                {
                  //  TermsandConditionDialog();



                    String url = "https://apnagullak.com/TERM%20AND%20CONDITIONS%20Apnagullak.pdf";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);


                }
                else
                {

                }
                break;
        }
    }

    //=========================Dialog==================================================//
    public void TermsandConditionDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnim);
        dialog.setContentView(R.layout.terms_and_conditions);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        dialog.show();

    }


    public boolean validation()
    {
        if (etNomineeName.getText().toString().isEmpty())
        {
            EasyToast.error(getActivity(),"Please enter nominee name");
        }
        else if (etNomineeFather.getText().toString().isEmpty())
        {
            EasyToast.error(getActivity(),"Please enter Noimneee Father/Husband name");
        }
        else if (etNomineeRelation.getText().toString().isEmpty())
        {
            EasyToast.error(getActivity(),"Please enter Noimneee Relation");
        }
        else if (etNomineeAddress.getText().toString().isEmpty())
        {
            EasyToast.error(getActivity(),"Please enter Noimneee Address");
        }
        return  false;
    }

    //==========================================API===================================================//
    public void AddAssoicateAPI()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("AadharNo", getArguments().getString("aadhar_number"));
            jsonObject.put("AccountNo", getArguments().getString("bank_account"));
            jsonObject.put("Address", getArguments().getString("address"));
            jsonObject.put("BankBranch", getArguments().getString("bank_branch"));
            jsonObject.put("BankName", getArguments().getString("bank_name"));
            jsonObject.put("City", getArguments().getString("city"));

            if (!getArguments().getString("dob").isEmpty()) {
                StringTokenizer st = new StringTokenizer(getArguments().getString("dob"), "-");
                String month = st.nextToken();
                String date = st.nextToken();
                String year = st.nextToken();
                jsonObject.put("DOBDay", date);
                jsonObject.put("DOBMonth", month);
                jsonObject.put("DOBYear", year);
            } else
            {
                jsonObject.put("DOBDay", "");
                jsonObject.put("DOBMonth", "");
                jsonObject.put("DOBYear", "");
            }
            jsonObject.put("District", getArguments().getString("city"));
            jsonObject.put("EmailId", getArguments().getString("email"));
            jsonObject.put("EntryBy",pref.get(AppSettings.UserId));
            if (!getArguments().getString("gender").equals("Select Gender"))
            {
                jsonObject.put("Gender",getArguments().getString("gender"));
            }
            else
            {
                jsonObject.put("Gender","");
            }

            jsonObject.put("IFSCCode", getArguments().getString("bank_ifsc"));
            jsonObject.put("Landmark",  getArguments().getString("landmark"));
            jsonObject.put("MemberName", getArguments().getString("name"));
            jsonObject.put("FatherName", getArguments().getString("father_name"));
            jsonObject.put("MobileNo", getArguments().getString("mobile"));
            jsonObject.put("NomineeAddress", etNomineeAddress.getText().toString());
            jsonObject.put("NomineeFath_HusbName", etNomineeFather.getText().toString());
            jsonObject.put("NomineeName", etNomineeName.getText().toString());
            jsonObject.put("NomineeRelation", etNomineeRelation.getText().toString());
            jsonObject.put("PANNo",getArguments().getString("pan_number"));
            jsonObject.put("Password","");
            jsonObject.put("PayeeName", getArguments().get("bank_payeename"));
            jsonObject.put("SponsorID", pref.get(AppSettings.UserId));
            jsonObject.put("SponsorName",pref.get(AppSettings.UserName));
            jsonObject.put("State", getArguments().getString("state"));
            jsonObject.put("TransPassword","");
            jsonObject.put("MemType","D");
            jsonObject.put("ZipCode", getArguments().getString("zipcode"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.v("jyiujojjujujujujuujujuj",jsonObject.toString());
        AndroidNetworking.post(BaseUrl+MemberRegistration)
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
                                loadFragment(new TeamFragment());
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