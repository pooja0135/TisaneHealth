package com.tisanehealth.Fragment.Profile;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.StringTokenizer;

import static com.tisanehealth.Activity.DashBoardActivity.ivLogo;
import static com.tisanehealth.Activity.DashBoardActivity.rlHeader;
import static com.tisanehealth.Activity.DashBoardActivity.tvHeader;
import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.EditProfile;
import static com.tisanehealth.Helper.AppUrls.GetStateByPinCode;

public class EditProfileFragment extends Fragment implements View.OnClickListener{
    //EditText
    EditText etMemberName,etRegistrationDate,etDob,etEmail,etMobile,etFlatNo,etLandmark,etCity,etDistrict,etState,etZipcode;
    EditText etAadharNumber,etPanNumber;
    //Button
    Button   btnUpdate;
    //Spinner
    Spinner spinnerGender;
    //Preferences
    Preferences pref;
    //CustomLoader
    CustomLoader loader;
    //ArrayList
    ArrayList<String>genderlist=new ArrayList<>();

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;

    ArrayList<String>statelist=new ArrayList<>();
    ArrayList<String>districtlist=new ArrayList<>();
    ArrayList<String>citylist=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.edit_profile_fragment, container, false);
        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("Edit Profile");
        ivLogo.setVisibility(View.GONE);
        tvHeader.setVisibility(View.VISIBLE);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        loadFragment(new MyProfileFragment());
                        return true;
                    }
                }
                return false;
            }
        });

        //Initialise Value
        initialise(view);

        //setOnCLickListener
        btnUpdate.setOnClickListener(this);
        etDob.setOnClickListener(this);


        return view;
    }

    //Initialise Value
    public void initialise(View view) {


        //Edittext
        etMemberName       =view.findViewById(R.id.etMemberNAme);
        etRegistrationDate =view.findViewById(R.id.etRegistrationDate);
        etDob              =view.findViewById(R.id.etDob);
        etEmail            =view.findViewById(R.id.etEmail);
        etMobile           =view.findViewById(R.id.etMobile);
        etFlatNo           =view.findViewById(R.id.etFlatNo);
        etLandmark         =view.findViewById(R.id.etLandmark);
        etCity             =view.findViewById(R.id.etCity);
        etDistrict         =view.findViewById(R.id.etDistrict);
        etState            =view.findViewById(R.id.etState);
        etZipcode          =view.findViewById(R.id.etZipcode);
        etAadharNumber     =view.findViewById(R.id.etAadharNumber);
        etPanNumber        =view.findViewById(R.id.etPanNumber);

        //Button
        btnUpdate          =view.findViewById(R.id.btnUpdate);

        //Preferences
        pref                = new Preferences(getActivity());

        //loader
        loader              = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


        //Spinner
        spinnerGender         =view.findViewById(R.id.spinnerGender);

        genderlist.add("Select");
        genderlist.add("Male");
        genderlist.add("Female");
        genderlist.add("Others");

        ArrayAdapter genderAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,genderlist);
        spinnerGender.setAdapter(genderAdapter);



        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        //set Values
        etMemberName.setText(getArguments().getString("name"));
        //etRegistrationDate.setText(getArguments().getString("father_name"));
        etEmail.setText(getArguments().getString("email"));
        etDob.setText(getArguments().getString("dob"));
        etMobile.setText(getArguments().getString("mobile_number"));
        etFlatNo.setText(getArguments().getString("flat_number"));
        etLandmark.setText(getArguments().getString("landmark"));
        etCity.setText(getArguments().getString("city"));
        etDistrict.setText(getArguments().getString("district"));
        etState.setText(getArguments().getString("state"));
        etZipcode.setText(getArguments().getString("zipcode"));
        etAadharNumber.setText(getArguments().getString("Aadhar_number"));
        etPanNumber.setText(getArguments().getString("pan_number"));

        if (getArguments().getString("gender").equals("Male"))
        {
            spinnerGender.setSelection(1);
        }
        else if (getArguments().getString("gender").equals("Female"))
        {
            spinnerGender.setSelection(2);
        }
        else if (getArguments().getString("gender").equals("Others"))
        {
            spinnerGender.setSelection(3);
        }
        else
        {
            spinnerGender.setSelection(0);
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.etDob:
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.btnUpdate:
                if (Utils.isNetworkConnectedMainThred(getActivity())) {
                    loader.show();
                    loader.setCanceledOnTouchOutside(true);
                    loader.setCancelable(false);
                   EditProfileAPI();
                } else {
                    EasyToast.error(getActivity(), "No Internet Connnection");
                }


                break;
        }
    }

    private void updateLabel() {
        //String myFormat = "dd-MMMM-yyyy";
        String myFormat = "MM-dd-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etDob.setText(sdf.format(myCalendar.getTime()));
    }

    //Load Fragment
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
    public void EditProfileAPI()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("AadharNo",etAadharNumber.getText().toString());
            jsonObject.put("Address",etFlatNo.getText().toString());
            jsonObject.put("City",etCity.getText().toString());
            jsonObject.put("District",etDistrict.getText().toString());
            jsonObject.put("EmailId",etEmail.getText().toString());
            jsonObject.put("EntryBy",pref.get(AppSettings.UserId));
            jsonObject.put("FatherName","");
            jsonObject.put("Gender",spinnerGender.getSelectedItem());
            jsonObject.put("Landmark",etLandmark.getText().toString());
            jsonObject.put("MemberId",pref.get(AppSettings.UserId));
            jsonObject.put("MemberName",etMemberName.getText().toString());
            jsonObject.put("MobileNo",etMobile.getText().toString());
            jsonObject.put("PANNo",etPanNumber.getText().toString());
            jsonObject.put("State",etState.getText().toString());
            jsonObject.put("ZipCode",etZipcode.getText().toString());
            jsonObject.put("AccountNo",getArguments().getString("bankaccount"));
            jsonObject.put("BankBranch",getArguments().getString("BankBranch"));
            jsonObject.put("BankName",getArguments().getString("BankName"));
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

            jsonObject.put("IFSCCode",getArguments().getString("IFSCCode"));
            jsonObject.put("NomineeAddress",getArguments().getString("NomineeAddress"));
            jsonObject.put("NomineeFath_HusbName",getArguments().getString("NomineeFath_HusbName"));
            jsonObject.put("NomineeRelation",getArguments().getString("NomineeRelation"));
            jsonObject.put("NomineeName",getArguments().getString("NomineeName"));
            jsonObject.put("NomineeAge",getArguments().getString("NomineeName"));
            jsonObject.put("PayeeName",getArguments().getString("PayeeName"));
            jsonObject.put("SponsorID",getArguments().getString("SponsorID"));
            jsonObject.put("SponsorName",getArguments().getString("SponsorName"));
            jsonObject.put("Direction","");
            jsonObject.put("MemType","");
            jsonObject.put("Password","");
            jsonObject.put("PinNumber","");
            jsonObject.put("SessionId","");
            jsonObject.put("TransPassword","");


            //Log.v("jkhfhgjjhggghhg",jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+EditProfile)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            boolean Status=   response.getBoolean("Status");
                            if (Status)
                            {
                                loadFragment(new MyProfileFragment());
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


    public void GetStateAPI()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PinCode",etZipcode.getText().toString());


            //Log.v("jkhfhgjjhggghhg",jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+GetStateByPinCode)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            boolean Status=   response.getBoolean("Status");
                            if (Status)
                            {
                                JSONArray jsonArray= response.getJSONArray("Detail");

                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    statelist.add(jsonObject.getString("Name"));
                                }


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