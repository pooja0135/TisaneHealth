package com.tisanehealth.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import static com.tisanehealth.Helper.AppUrls.MemberRegistration;

public class AddCustomerFragment extends Fragment implements View.OnClickListener {
    Button btnContinue;
    EditText etName,etFatherName,etDob,etEmail,etMobile,etSponserID,etSponserName;
    Spinner spinnerGender;
    ArrayList<String>genderlist=new ArrayList<>();
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    //Preferences
    Preferences pref;
    //CustomLoader
    CustomLoader loader;

    //CheckBox
    CheckBox checkBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_customer_fragment, container, false);

        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("Add Customer");
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

        //Initialise Value
        initialise(view);


        //SetOnCLickListener

        btnContinue.setOnClickListener(this);
        etDob.setOnClickListener(this);
        checkBox.setOnClickListener(this);

        return view;
    }

    //Initialise Value
    public void initialise(View view)
    {
        //Button
        btnContinue         =view.findViewById(R.id.btnContinue);

        //Edittext
        etName               =view.findViewById(R.id.etName);
        etFatherName         =view.findViewById(R.id.etFatherName);
        etDob                =view.findViewById(R.id.etDob);
        etEmail              =view.findViewById(R.id.etEmail);
        etMobile             =view.findViewById(R.id.etMobile);
        etSponserID          =view.findViewById(R.id.etSponserID);
        etSponserName        =view.findViewById(R.id.etSponserName);

        //Spinner
        spinnerGender         =view.findViewById(R.id.spinnerGender);

        //Preferences
        pref                = new Preferences(getActivity());

        //loader
        loader              = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


        etSponserID.setText(pref.get(AppSettings.UserId));
        etSponserName.setText(pref.get(AppSettings.UserName));


        genderlist.add("Select Gender");
        genderlist.add("Male");
        genderlist.add("Female");
        genderlist.add("Others");

        ArrayAdapter genderAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,genderlist);
        spinnerGender.setAdapter(genderAdapter);


        //CheckBox
        checkBox             =view.findViewById(R.id.checkbox);

        SpannableString str = new SpannableString(getResources().getString(R.string.terms_and_condititons));
        str.setSpan(new ForegroundColorSpan(Color.RED), 11, 31, 0);
        checkBox.setText(str);


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





    }

    //Load Fragment
    public void loadFragment(Fragment fragment)
    {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Bundle bundle=new Bundle();
        bundle.putString("name", etName.getText().toString());
        bundle.putString("father_name", etFatherName.getText().toString());
        bundle.putString("dob", etDob.getText().toString());
        bundle.putString("email", etEmail.getText().toString());
        bundle.putString("mobile", etMobile.getText().toString());
        bundle.putString("sponser_id", etSponserID.getText().toString());
        bundle.putString("sponser_name", etSponserName.getText().toString());
        bundle.putString("gender", spinnerGender.getSelectedItem().toString());
        fragment.setArguments(bundle);
//        transaction.setCustomAnimations(
//            R.anim.card_flip_right_in, R.anim.card_flip_right_out,
//            R.anim.card_flip_left_in, R.anim.card_flip_left_out);
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }

    private void updateLabel() {
        String myFormat = "MM-dd-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etDob.setText(sdf.format(myCalendar.getTime()));
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

    //Check Validation
    public boolean validation()
    {
        if (etName.getText().toString().isEmpty())
        {
            EasyToast.error(getActivity(), "Please enter  member name");
        }
     /*   else if (etFatherName.getText().toString().isEmpty())
        {
            EasyToast.error(getActivity(), "Please enter  member father/husband name");
        }*/
      /*  else if (etDob.getText().toString().isEmpty())
        {
            EasyToast.error(getActivity(),"Please select member date of birth");
        }
        else if (spinnerGender.getSelectedItem().equals("Select Gender"))
        {
            EasyToast.error(getActivity(),"Please select member gender");
        }*/
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
        else if (!checkBox.isChecked())
        {
            EasyToast.warning(getActivity(),"Please accept Terms and Conditions.");
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
            jsonObject.put("AccountNo","");
            jsonObject.put("Address","");
            jsonObject.put("BankBranch", "");
            jsonObject.put("BankName", "");
            jsonObject.put("City","");

            if (!etDob.getText().toString().isEmpty()) {
                StringTokenizer st = new StringTokenizer(etDob.getText().toString(), "-");
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
            jsonObject.put("District","");
            jsonObject.put("EmailId",  etEmail.getText().toString());
            jsonObject.put("EntryBy",pref.get(AppSettings.UserId));
            if (!spinnerGender.getSelectedItem().toString().equals("Select Gender"))
            {
                jsonObject.put("Gender", spinnerGender.getSelectedItem().toString());
            }
            else
            {
                jsonObject.put("Gender","");
            }

            jsonObject.put("IFSCCode", "");
            jsonObject.put("Landmark", "");
            jsonObject.put("MemberName",  etName.getText().toString());
            jsonObject.put("FatherName",etFatherName.getText().toString());
            jsonObject.put("MobileNo", etMobile.getText().toString());
            jsonObject.put("NomineeAddress","");
            jsonObject.put("NomineeFath_HusbName","");
            jsonObject.put("NomineeName", "");
            jsonObject.put("NomineeRelation", "");
            jsonObject.put("PANNo","");
            jsonObject.put("Password","");
            jsonObject.put("PayeeName", "");
            jsonObject.put("SponsorID", pref.get(AppSettings.UserId));
            jsonObject.put("SponsorName",pref.get(AppSettings.UserName));
            jsonObject.put("State", "");
            jsonObject.put("TransPassword","");
            jsonObject.put("MemType","C");
            jsonObject.put("ZipCode", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Log.v("jyiujojjujujujujuujujuj",jsonObject.toString());
        AndroidNetworking.post(BaseUrl+MemberRegistration)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //Log.v("hgghghghghhghg",response.toString());

                            String Status=   response.getString("Status");

                            if (Status.equals("true"))
                            {
                                loader.cancel();
                                EasyToast.success(getActivity(),response.getString("Message"),10);
                                loadFragment(new DashBoardFragment());
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
