package com.tisanehealth.Fragment.AddAssociate;

import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tisanehealth.Fragment.DashBoardFragment;
import com.tisanehealth.Fragment.Team.TeamFragment;
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
import java.util.Calendar;

import static com.tisanehealth.Activity.DashBoardActivity.rlHeader;
import static com.tisanehealth.Activity.DashBoardActivity.tvHeader;
import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.CheckAllMemberId;
import static com.tisanehealth.Helper.AppUrls.MemberRegistration;

public class AddAssociateFragment extends Fragment implements View.OnClickListener {
    Button btnContinue;
    EditText etName,etEmail,etMobile,etSponserID,etSponserName;
    Spinner spinnerGender,spinnerDirection;
    ArrayList<String>genderlist=new ArrayList<>();
    ArrayList<String>directionlist=new ArrayList<>();
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    //Preferences
    Preferences pref;
    //CustomLoader
    CustomLoader loader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_associate_fragment, container, false);

        rlHeader.setVisibility(View.VISIBLE);


        tvHeader.setText("Add Member");

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

        //Initialise Value
        initialise(view);


        //SetOnCLickListener

        btnContinue.setOnClickListener(this);

        return view;
    }

    //Initialise Value
    public void initialise(View view)
    {
        //Button
        btnContinue         =view.findViewById(R.id.btnContinue);

        //Edittext
        etName               =view.findViewById(R.id.etName);
        etEmail              =view.findViewById(R.id.etEmail);
        etMobile             =view.findViewById(R.id.etMobile);
        etSponserID          =view.findViewById(R.id.etSponserID);
        etSponserName        =view.findViewById(R.id.etSponserName);


        //Spinner
        spinnerGender         =view.findViewById(R.id.spinnerGender);
        spinnerDirection         =view.findViewById(R.id.spinnerDirection);

        //Preferences
        pref                = new Preferences(getActivity());

        //loader
        loader              = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


        genderlist.add("Select Gender");
        genderlist.add("Male");
        genderlist.add("Female");
        genderlist.add("Others");



        directionlist.add("Select Direction");
        directionlist.add("Left");
        directionlist.add("Right");

        ArrayAdapter genderAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,genderlist);
        spinnerGender.setAdapter(genderAdapter);

        ArrayAdapter directionAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,directionlist);
        spinnerDirection.setAdapter(directionAdapter);


        etSponserID.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                etSponserName.setText("");

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                etSponserName.setText("");

            }
        });


        etSponserID.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    GetSponserNameAPI();
                }

            }
        });





    }


    public void loadFragment(Fragment fragment)
    {
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

            case R.id.btnContinue:
                if (validation()==true)
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
                break;

        }
    }

    //Check Validation
    public boolean validation()
    {
        if (etName.getText().toString().isEmpty())
        {
            EasyToast.error(getActivity(), "Please enter  member name");
        }
        else if (spinnerGender.getSelectedItem().equals("Select Gender"))
        {
            EasyToast.error(getActivity(),"Please select member gender");
        }

        else if(etEmail.getText().toString().isEmpty())
        {
            EasyToast.error(getActivity(),"Please enter member email id ");
        }
        else if(!etEmail.getText().toString().trim().matches(emailPattern))
        {
            EasyToast.error(getActivity(),"Please enter valid email address. ");
        }
        else if (etMobile.getText().toString().isEmpty())
        {
            EasyToast.error(getActivity(),"Please enter member mobile number. ");
        }
        else if (etMobile.getText().toString().length()<10)
        {
            EasyToast.error(getActivity(),"Please enter 10 digit Mobile number. ");
        }
        else if (spinnerDirection.getSelectedItem().equals("Select Direction"))
        {
            EasyToast.error(getActivity(),"Please select member direction");
        }
        else
        {
            return true;
        }

        return  false;
    }



    //==========================================API===================================================//
    public void AddAssoicateAPI()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("AadharNo","");
            jsonObject.put("AccountNo", "");
            jsonObject.put("Address", "");
            jsonObject.put("BankBranch", "");
            jsonObject.put("BankName", "");
            jsonObject.put("City", "");
            jsonObject.put("DOBDay", "");
            jsonObject.put("DOBMonth", "");
            jsonObject.put("DOBYear", "");
            jsonObject.put("FatherName", "");
            if(spinnerDirection.getSelectedItem().equals("Left"))
            {
                jsonObject.put("Direction", "L");
            }

            else
            {
                jsonObject.put("Direction", "R");
            }


            jsonObject.put("District", "");
            jsonObject.put("EmailId", etEmail.getText().toString());
            jsonObject.put("EntryBy",pref.get(AppSettings.UserId));
            if (!spinnerGender.getSelectedItem().equals("Select Gender"))
            {
                jsonObject.put("Gender",spinnerGender.getSelectedItem());
            }
            else
            {
                jsonObject.put("Gender","");
            }

            jsonObject.put("IFSCCode", "");
            jsonObject.put("Landmark", "");
            jsonObject.put("MemType", "");
            jsonObject.put("MemberId","");
            jsonObject.put("MemberName", etName.getText().toString());
            jsonObject.put("MobileNo", etMobile.getText().toString());
            jsonObject.put("NomineeAddress", "");
            jsonObject.put("NomineeFath_HusbName","");
            jsonObject.put("NomineeName","");
            jsonObject.put("NomineeAge","");
            jsonObject.put("NomineeRelation","");
            jsonObject.put("PANNo","");
            jsonObject.put("Password","");
            jsonObject.put("PayeeName", "");
            jsonObject.put("SponsorID", pref.get(AppSettings.UserId));
            jsonObject.put("SponsorName",pref.get(AppSettings.UserName));
            jsonObject.put("State", "");
            jsonObject.put("TransPassword","");
            jsonObject.put("PinNumber","");
            jsonObject.put("SessionId","");
            jsonObject.put("ZipCode", "");


            //Log.v("hgjhghgjhhgghgh",jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

                        //Log.v("gjfihhgghghg",error.toString());
                        loader.cancel();
                    }
                });
    }

    public void GetSponserNameAPI()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MemberId",etSponserID.getText().toString().trim());
            jsonObject.put("SessionId","");

        } catch (JSONException e) {
            e.printStackTrace();
        }



        AndroidNetworking.post(BaseUrl+CheckAllMemberId)
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

                                    String Name=jsonObject.getString("Name")  ;

                                    etSponserName.setText(Name);


                                }

                                loader.cancel();

                            }
                            else
                            {
                                loader.cancel();
                                etSponserName.setText("");

                                EasyToast.warning(getActivity(),"Enter valid SponsorId ");
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
