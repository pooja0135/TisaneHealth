package com.tisanehealth.Fragment.AddAssociate;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tisanehealth.R;
import com.medialablk.easytoast.EasyToast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tisanehealth.Activity.DashBoardActivity.rlHeader;

public class AddAssociateBankFragment extends Fragment implements View.OnClickListener {
    //Button
    Button btnContinue;
    //EditText
    EditText etBankName,etBankBranch,etPanNumber,etIFSCcode,etAccountNumber,etPayeeName,etAadharNumber;
    //TextView
    TextView tvStep1,tvStep2,tvStep3,tvStep4;
    Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_associate_bank_fragment, container, false);
        rlHeader.setVisibility(View.GONE);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        loadFragment(new AddAssociateAddressFragment());
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
        btnContinue         = view.findViewById(R.id.btnContinue);

        //EditText
        etPayeeName           =view.findViewById(R.id.etPayeename);
        etAadharNumber        =view.findViewById(R.id.etAadharNumber);
        etBankName            =view.findViewById(R.id.etBankName);
        etBankBranch          =view.findViewById(R.id.etBankBranch);
        etPanNumber           =view.findViewById(R.id.etPanNumber);
        etIFSCcode            =view.findViewById(R.id.etIFSC);
        etAccountNumber       =view.findViewById(R.id.etBankAccountNumber);

        //TextView
        tvStep1             =view.findViewById(R.id.tvStep1);
        tvStep2             =view.findViewById(R.id.tvStep2);
        tvStep3             =view.findViewById(R.id.tvStep3);
        tvStep4             =view.findViewById(R.id.tvStep4);

        //SetOnCLickListener
        tvStep1.setOnClickListener(this);
        tvStep2.setOnClickListener(this);
        tvStep3.setOnClickListener(this);
        tvStep4.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
    }

    //Load Fragment
    public void loadFragment(Fragment fragment) {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();

        try{
            Bundle bundle=new Bundle();
            bundle.putString("name", getArguments().getString("name"));
            bundle.putString("father_name", getArguments().getString("father_name"));
            bundle.putString("dob",  getArguments().getString("dob"));
            bundle.putString("email", getArguments().getString("email"));
            bundle.putString("gender",  getArguments().getString("gender"));
            bundle.putString("mobile",getArguments().getString("mobile"));
            bundle.putString("sponser_id",getArguments().getString("sponser_id"));
            bundle.putString("sponser_name",getArguments().getString("sponser_name"));
            bundle.putString("address",getArguments().getString("address"));
            bundle.putString("landmark",getArguments().getString("landmark"));
            bundle.putString("city",getArguments().getString("city"));
            bundle.putString("state",getArguments().getString("state"));
            bundle.putString("zipcode",getArguments().getString("zipcode"));
            bundle.putString("bank_payeename",etPayeeName.getText().toString());
            bundle.putString("bank_name",etBankName.getText().toString());
            bundle.putString("bank_branch",etBankBranch.getText().toString());
            bundle.putString("bank_ifsc",etIFSCcode.getText().toString());
            bundle.putString("bank_account",etAccountNumber.getText().toString());
            bundle.putString("pan_number",etPanNumber.getText().toString());
            bundle.putString("aadhar_number",etAadharNumber.getText().toString());
            fragment.setArguments(bundle);
        }
        catch(Exception e)
        {

        }


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
                if (validation()==true)
                {
                    loadFragment(new AddAssociateNomineeFragment());
                }
                //loadFragment(new AddAssociateNomineeFragment());

                break;
        }
    }

    //Check Validation
    public boolean validation()
    {
        Matcher matcher = pattern.matcher(etPanNumber.getText().toString());


        if (etPayeeName.getText().toString().isEmpty())
        {
            EasyToast.error(getActivity(), "Please enter  Payee Name");
        }
        else if (etBankName.getText().toString().length()<6)
        {
            EasyToast.error(getActivity(),"Please enter Bank Name");
        }
        else if(etBankBranch.getText().toString().isEmpty())
        {
            EasyToast.error(getActivity(),"Please enter Bank Branch Name ");
        }
        else if (etIFSCcode.getText().toString().isEmpty())
        {
            EasyToast.error(getActivity(),"Please enter Bank IFSC code");
        }
        else if (etAccountNumber.getText().toString().isEmpty())
        {
            EasyToast.error(getActivity(),"Please enter Bank Account Number ");
        }
        else if (etPanNumber.getText().toString().isEmpty())
        {
            EasyToast.error(getActivity(),"Please Enter Pan Number of Member");
        }
        else if (!matcher.matches()) {
            EasyToast.warning(getActivity(),"Please enter valid pattern of pan Card.");
        }
        else if (etAadharNumber.getText().toString().isEmpty())
        {
            EasyToast.error(getActivity(),"Please Enter  Aadhar Number of Member");
        }
        else if (etAadharNumber.getText().toString().length()<12)
        {
            EasyToast.warning(getActivity(),"Please enter your 12 digit Aadhar Number.");
        }
        else
        {
            return true;
        }

        return  false;
    }
}