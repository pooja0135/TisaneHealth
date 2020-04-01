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

import static com.tisanehealth.Activity.DashBoardActivity.rlHeader;

public class AddAssociateAddressFragment extends Fragment implements View.OnClickListener{

    //Button
    Button btnContinue;

    //Edittext
    EditText etFlatNo,etLandmark,etCity,etDistrict,etState,etZipcode;

    //Textview
    TextView tvStep1,tvStep2,tvStep3,tvStep4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_associate_address_fragment, container, false);
        rlHeader.setVisibility(View.GONE);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        loadFragment(new AddAssociateProfileFragment());
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
        btnContinue         =view.findViewById(R.id.btnContinue);

        //Edittext
        etFlatNo            =view.findViewById(R.id.etFlatNo);
        etLandmark          =view.findViewById(R.id.etLandmark);
        etCity              =view.findViewById(R.id.etCity);
        etDistrict          =view.findViewById(R.id.etDistrict);
        etState             =view.findViewById(R.id.etState);
        etZipcode           =view.findViewById(R.id.etZipcode);

        //Textview
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
    public void loadFragment(Fragment fragment)
    {

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
            bundle.putString("address",etFlatNo.getText().toString());
            bundle.putString("landmark",etLandmark.getText().toString());
            bundle.putString("city",etCity.getText().toString());
            bundle.putString("state",etState.getText().toString());
            bundle.putString("zipcode",etZipcode.getText().toString());
            fragment.setArguments(bundle);
        }
        catch(Exception e)
        {

        }


//        transaction.setCustomAnimations(
//                R.anim.card_flip_right_in, R.anim.card_flip_right_out,
//                R.anim.card_flip_left_in, R.anim.card_flip_left_out);
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tvStep1:
                //loadFragment(new AddAssociateProfileFragment());
                break;
            case R.id.tvStep2:
               // loadFragment(new AddAssociateAddressFragment());
                break;
            case R.id.tvStep3:
             //  loadFragment(new AddAssociateBankFragment());
               break;
            case R.id.tvStep4:
             //   loadFragment(new AddAssociateNomineeFragment());
                break;
            case R.id.btnContinue:
              /*  if (validation()==true)
                {
                    loadFragment(new AddAssociateBankFragment());
                }
*/
                loadFragment(new AddAssociateBankFragment());

                break;

        }
    }


    //========================Check Validation=====================================//
    public boolean validation()
    {
        if (etFlatNo.getText().toString().isEmpty())
        {
            EasyToast.error(getActivity(), "Please enter  Flat/House Number");
        }
        else if (etCity.getText().toString().isEmpty())
        {
            EasyToast.error(getActivity(),"Please enter city");
        }
        else if(etDistrict.getText().toString().isEmpty())
        {
            EasyToast.error(getActivity(),"Please enter district  name ");
        }
        else if (etState.getText().toString().isEmpty())
        {
            EasyToast.error(getActivity(),"Please enter state");
        }
        else if (etZipcode.getText().toString().length()<10)
        {
            EasyToast.error(getActivity(),"Please enter zipcode ");
        }

        else
        {
            return true;
        }

        return  false;
    }
}
