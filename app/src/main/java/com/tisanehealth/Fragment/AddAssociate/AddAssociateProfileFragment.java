package com.tisanehealth.Fragment.AddAssociate;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tisanehealth.Fragment.DashBoardFragment;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.R;
import com.medialablk.easytoast.EasyToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.tisanehealth.Activity.DashBoardActivity.rlHeader;

public class AddAssociateProfileFragment extends Fragment implements View.OnClickListener {
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
    //TextView
    TextView tvStep1,tvStep2,tvStep3,tvStep4;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_associate_profile_fragment, container, false);

        rlHeader.setVisibility(View.GONE);

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
        tvStep1.setOnClickListener(this);
        tvStep2.setOnClickListener(this);
        tvStep3.setOnClickListener(this);
        tvStep4.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
        etDob.setOnClickListener(this);

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

        //TextView
        tvStep1             =view.findViewById(R.id.tvStep1);
        tvStep2             =view.findViewById(R.id.tvStep2);
        tvStep3             =view.findViewById(R.id.tvStep3);
        tvStep4             =view.findViewById(R.id.tvStep4);

        //Spinner
        spinnerGender         =view.findViewById(R.id.spinnerGender);

        //Preferences
        pref                = new Preferences(getActivity());

        //loader
        loader              = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


        genderlist.add("Select Gender");
        genderlist.add("Male");
        genderlist.add("Female");
        genderlist.add("Others");

        ArrayAdapter genderAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,genderlist);
        spinnerGender.setAdapter(genderAdapter);


        etSponserID.setText(pref.get(AppSettings.UserId));
        etSponserName.setText(pref.get(AppSettings.UserName));

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
        transaction.setCustomAnimations(
            R.anim.card_flip_right_in, R.anim.card_flip_right_out,
            R.anim.card_flip_left_in, R.anim.card_flip_left_out);
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
                    loadFragment(new AddAssociateAddressFragment());
                }
                break;
            case R.id.tvStep1:
               // loadFragment(new AddAssociateProfileFragment());
                break;
            case R.id.tvStep2:
               /* if (validation()==true)
                {
                    loadFragment(new AddAssociateAddressFragment());
                }*/
                break;
            case R.id.tvStep3:
              //  loadFragment(new AddAssociateBankFragment());
                break;
            case R.id.tvStep4:
                loadFragment(new AddAssociateNomineeFragment());
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
        else
        {
            return true;
        }

        return  false;
    }

}
