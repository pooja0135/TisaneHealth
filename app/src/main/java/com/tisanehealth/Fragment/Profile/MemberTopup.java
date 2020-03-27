package com.tisanehealth.Fragment.Profile;

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
import static com.tisanehealth.Helper.AppUrls.CheckAllMemberId;
import static com.tisanehealth.Helper.AppUrls.GetMemberPinDetails;
import static com.tisanehealth.Helper.AppUrls.MemberTopUp;

public class MemberTopup extends Fragment {
    //Edittext
    EditText etUserID,etName;
    //Button
    Button btnContinue;
    //Preferences
    Preferences pref;
    //CustomLoader
    CustomLoader loader;

    Spinner spinnerPin;

    String sponsor_name="";

    ArrayList<HashMap<String,String>>pinlist=new ArrayList<>();

    ArrayList<String>pinvaluelist=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.member_topup_fragment, container, false);
        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("Member Topup");
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

        //Edittext
        etUserID  =view.findViewById(R.id.etUserID);
        etName      =view.findViewById(R.id.etName);
        spinnerPin      =view.findViewById(R.id.spinnerPin);

        //Button
        btnContinue        =view.findViewById(R.id.btnSubmit);

        //Preferences
        pref                = new Preferences(getActivity());

        //loader
        loader              = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etUserID.getText().toString().isEmpty())
                {
                    EasyToast.warning(getActivity(),"Please enter your associate id");
                }

                else if (etName.getText().toString().trim().isEmpty())
                {
                    EasyToast.warning(getActivity(),"Please enter your associate name");
                }

                else if (!etName.getText().toString().equals(sponsor_name))
                {
                    EasyToast.warning(getActivity(),"Associate name is not correct");
                }



                else  if (spinnerPin.getSelectedItem().equals("Select"))
                {
                    EasyToast.warning(getActivity(),"Select Pin");
                }

                else
                {
                    if (Utils.isNetworkConnectedMainThred(getActivity())) {
                        loader.show();
                        loader.setCanceledOnTouchOutside(true);
                        loader.setCancelable(false);
                        MemberTopupAPI();
                    } else {
                        EasyToast.error(getActivity(), "No Internet Connnection");
                    }
                }
            }
        });



        etUserID.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                etName.setText("");

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                etName.setText("");

            }
        });


        etUserID.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    GetSponserNameAPI();
                }

            }
        });


        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);
            GetPinDetailAPI();

        } else {
            EasyToast.error(getActivity(), "No Internet Connnection");
        }


        return view;

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
    public void GetPinDetailAPI()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MemberId",pref.get(AppSettings.UserId));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+GetMemberPinDetails)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String Message=   response.getString("Message");
                            if (Message.equals("Success"))
                            {
                                HashMap<String,String>map=new HashMap<>();
                                map.put("Pin","Select");
                                map.put("PinNumber","Select");

                                pinlist.add(map);
                                pinvaluelist.add("Select");

                                JSONArray jsonArray=response.getJSONArray("PinDetail");

                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);

                                    HashMap<String,String>map1=new HashMap<>();
                                    map1.put("Pin",jsonObject.getString("Pin"));
                                    map1.put("PinNumber",jsonObject.getString("PinNumber"));

                                    pinlist.add(map1);
                                    pinvaluelist.add(jsonObject.getString("Pin"));
                                }

                                ArrayAdapter genderAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,pinvaluelist);
                                spinnerPin.setAdapter(genderAdapter);
                            }
                            else
                            {
                                loader.cancel();
                                pinvaluelist.add("Select");
                                ArrayAdapter genderAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,pinvaluelist);
                                spinnerPin.setAdapter(genderAdapter);
                            }

                            loader.cancel();

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
            jsonObject.put("MemberId",etUserID.getText().toString().trim());
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

                                    sponsor_name=Name;
                                    etName.setText(Name);


                                }

                                loader.cancel();

                            }
                            else
                            {
                                loader.cancel();
                                etName.setText("");

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


    public void MemberTopupAPI()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MemberId",etUserID.getText().toString().trim());
            jsonObject.put("AadharNo","");
            jsonObject.put("AccountNo","");
            jsonObject.put("Address","");
            jsonObject.put("BankBranch","");
            jsonObject.put("BankName","");
            jsonObject.put("City","");
            jsonObject.put("DOBDay","");
            jsonObject.put("DOBMonth","");
            jsonObject.put("DOBYear","");
            jsonObject.put("Direction","");
            jsonObject.put("District","");
            jsonObject.put("EmailId","");
            jsonObject.put("EntryBy","");
            jsonObject.put("FatherName","");
            jsonObject.put("Gender","");
            jsonObject.put("IFSCCode","");
            jsonObject.put("Landmark","");
            jsonObject.put("MemType","");
            jsonObject.put("MemberName","");
            jsonObject.put("MobileNo","");
            jsonObject.put("NomineeAddress","");
            jsonObject.put("NomineeAge","");
            jsonObject.put("NomineeFath_HusbName","");
            jsonObject.put("NomineeName","");
            jsonObject.put("NomineeRelation","");
            jsonObject.put("PANNo","");
            jsonObject.put("Password","");
            jsonObject.put("PayeeName","");
            jsonObject.put("PinNumber",pinlist.get(spinnerPin.getSelectedItemPosition()).get("pinlist"));
            jsonObject.put("SessionId",pref.get(AppSettings.UserId));
            jsonObject.put("SponsorID","");
            jsonObject.put("SponsorName","");
            jsonObject.put("State","");
            jsonObject.put("TransPassword","");
            jsonObject.put("ZipCode","");

        } catch (JSONException e) {
            e.printStackTrace();
        }



        AndroidNetworking.post(BaseUrl+MemberTopUp)
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

                                    etName.setText(Name);


                                }

                                loader.cancel();

                            }
                            else
                            {
                                loader.cancel();
                                etName.setText("");

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