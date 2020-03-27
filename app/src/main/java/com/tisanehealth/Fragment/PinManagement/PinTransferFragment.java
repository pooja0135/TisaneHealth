package com.tisanehealth.Fragment.PinManagement;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.HashMap;

import static com.tisanehealth.Activity.DashBoardActivity.ivLogo;
import static com.tisanehealth.Activity.DashBoardActivity.rlHeader;
import static com.tisanehealth.Activity.DashBoardActivity.tvHeader;
import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.CheckAllMemberId;
import static com.tisanehealth.Helper.AppUrls.GetNoOfPin;
import static com.tisanehealth.Helper.AppUrls.GetPinType;
import static com.tisanehealth.Helper.AppUrls.PinTransfer;

public class PinTransferFragment extends Fragment{
    Preferences pref;
    CustomLoader loader;
    Spinner spinnerPin;
    EditText etTransferPin;
    EditText etAvailablePin;
    EditText etTransferID;
    TextView tvInvalidData;
    Button btnSubmit;
    String sponsor_name="";
    String available_pin="";
    ArrayList<HashMap<String,String>>pinlist=new ArrayList<>();
    ArrayList<String>pinvaluelist=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pin_transfer_fragment, container, false);
        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("Pin Transfer");
        ivLogo.setVisibility(View.GONE);
        tvHeader.setVisibility(View.VISIBLE);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        loadFragment(new PinManagement());
                        return true;
                    }
                }
                return false;
            }
        });


        pref             =new Preferences(getActivity());
        //loader
        loader           = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        spinnerPin= view.findViewById(R.id.spinnerPin);
        etTransferPin= view.findViewById(R.id.etTransferPin);
        etAvailablePin= view.findViewById(R.id.etAvailablePin);
        etTransferID= view.findViewById(R.id.etTransferID);
        tvInvalidData= view.findViewById(R.id.tvInvalidData);
        btnSubmit= view.findViewById(R.id.btnSubmit);


        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);
            GetPinApi();


        } else {
            EasyToast.error(getActivity(), "No Internet Connnection");
        }


        spinnerPin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    if (item.equals("Select"))
                    {
                        etAvailablePin.setText("0");
                        available_pin="0";

                    }
                    else
                    {
                        GetNoOfPinAPI();
                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });



        etTransferID.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                GetSponserNameAPI();

            }
        });





        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerPin.getSelectedItem().equals("Select"))
                {
                    EasyToast.warning(getActivity(),"Please select pin type");
                }
                else  if (etTransferID.getText().toString().isEmpty())
                {
                    EasyToast.warning(getActivity(),"Enter Transfer Member ID");
                }
                else if (sponsor_name.isEmpty())
                {
                    EasyToast.warning(getActivity(),"Enter Valid Member ID");
                }

                else if (etTransferPin.getText().toString().trim().isEmpty())
                {
                    EasyToast.warning(getActivity(),"Enter no of transfer pin");
                }

                else if (etTransferPin.getText().toString().trim().equals("0"))
                {
                    EasyToast.warning(getActivity(),"Transfer Pin value should be greater than 0.");
                }
                else if (Integer.parseInt(available_pin)<Integer.parseInt((etTransferPin.getText().toString().trim())))
                {
                    EasyToast.warning(getActivity(),"Transfer Pin value is not greater than Available Pin.");

                }
                else
                {
                    if (Utils.isNetworkConnectedMainThred(getActivity())) {
                        loader.show();
                        loader.setCanceledOnTouchOutside(true);
                        loader.setCancelable(false);
                        PinTransferApi();
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
    public void GetPinApi()
    {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("Userid",pref.get(AppSettings.UserId));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+GetPinType)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String message=   response.getString("Message");
                            if (message.equals("Success"))
                            {
                                HashMap<String,String> map=new HashMap<>();
                                map.put("KitAmount","Select");
                                map.put("KitAmountName","Select");
                                map.put("KitCode","Select");
                                map.put("NoOfPin","Select");
                                map.put("pinType","Select");
                                pinlist.add(map);
                                pinvaluelist.add("Select");


                                JSONArray jsonArray=response.getJSONArray("PinRes");

                                for (int i=0;i<jsonArray.length();i++)
                                {


                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    HashMap<String,String> map1=new HashMap<>();

                                    map1.put("KitAmount",jsonObject.getString("KitAmount"));
                                    map1.put("KitAmountName",jsonObject.getString("KitAmountName"));
                                    map1.put("KitCode",jsonObject.getString("KitCode"));
                                    map1.put("NoOfPin",jsonObject.getString("NoOfPin"));
                                    map1.put("pinType",jsonObject.getString("pinType"));
                                    pinlist.add(map1);

                                    pinvaluelist.add(jsonObject.getString("KitAmountName"));
                                }

                                ArrayAdapter pinAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,pinvaluelist);
                                spinnerPin.setAdapter(pinAdapter);
                                loader.dismiss();
                            }
                            else
                            {
                                loader.dismiss();

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


    public void PinTransferApi()
    {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("Userid",pref.get(AppSettings.UserId));
            jsonObject.put("NoOfPins",etTransferPin.getText().toString());
            jsonObject.put("RecieveUserid",etTransferID.getText().toString());
            jsonObject.put("PackageID",pinlist.get(spinnerPin.getSelectedItemPosition()).get("KitCode"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+PinTransfer)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String message=   response.getString("Message");
                            if (message.equals("Success"))
                            {
                                loader.dismiss();
                                loadFragment(new PinManagement());
                            }
                            else
                            {
                                loader.dismiss();
                                EasyToast.error(getActivity(),message);
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
            jsonObject.put("MemberId",etTransferID.getText().toString());
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
                                    tvInvalidData.setVisibility(View.GONE);

                                }

                                loader.cancel();

                            }
                            else
                            {
                                loader.cancel();
                                sponsor_name="";
                                tvInvalidData.setVisibility(View.VISIBLE);
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

    public void GetNoOfPinAPI()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Userid",etTransferID.getText().toString());
            jsonObject.put("PinType",pinlist.get(spinnerPin.getSelectedItemPosition()).get("KitCode"));


        } catch (JSONException e) {
            e.printStackTrace();
        }



        AndroidNetworking.post(BaseUrl+GetNoOfPin)
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
                                    tvInvalidData.setVisibility(View.GONE);

                                }

                                loader.cancel();

                            }
                            else
                            {
                                loader.cancel();
                                sponsor_name="";
                                tvInvalidData.setVisibility(View.VISIBLE);
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