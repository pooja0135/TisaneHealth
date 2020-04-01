package com.tisanehealth.Fragment.PinManagement;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tisanehealth.Adapter.PinBankListAdapter;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.Model.PinBankModel;
import com.tisanehealth.R;
import com.medialablk.easytoast.EasyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.tisanehealth.Activity.DashBoardActivity.ivLogo;
import static com.tisanehealth.Activity.DashBoardActivity.rlHeader;
import static com.tisanehealth.Activity.DashBoardActivity.tvHeader;
import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.MyPinDetails;

public class PinBankFragment extends Fragment implements View.OnClickListener {

    Spinner spinnerStatus;
    PinBankModel pinBankModel;
    ArrayList<PinBankModel>pinbanklist=new ArrayList<>();
    ArrayList<String>statuslist=new ArrayList<>();
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog.OnDateSetListener date1;

    PinBankListAdapter pinBankListAdapter;

    RecyclerView recyclerViewPin;

    TextView tvFromDate,tvToDate;

    //Preferences
    Preferences pref;
    //CustomLoader
    CustomLoader loader;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pin_bank_fragment, container, false);

        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("Pin Bank Details");
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

        //Initialise Value
        initialise(view);




        return view;
    }

    //Initialise Value
    public void initialise(View view)
    {

        //Spinner
        spinnerStatus         =view.findViewById(R.id.spinnerStatus);

        //TextView
        tvFromDate            =view.findViewById(R.id.tvFromDate);
        tvToDate              =view.findViewById(R.id.tvToDate);


        recyclerViewPin       =view.findViewById(R.id.recylerviewPayBank);


        recyclerViewPin.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Preferences
        pref                = new Preferences(getActivity());

        //loader
        loader              = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        statuslist.add("All");
        statuslist.add("DeActive");
        statuslist.add("Active");


        ArrayAdapter genderAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,statuslist);
        spinnerStatus.setAdapter(genderAdapter);





        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(tvFromDate);
            }

        };

        date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(tvToDate);
            }

        };



        tvFromDate.setOnClickListener(this);
        tvToDate.setOnClickListener(this);

        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);
            PayBankAPI("","");

        } else {
            EasyToast.error(getActivity(), "No Internet Connnection");
        }


    }

    //Load Fragment
    public void loadFragment(Fragment fragment)
    {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(
//            R.anim.card_flip_right_in, R.anim.card_flip_right_out,
//            R.anim.card_flip_left_in, R.anim.card_flip_left_out);
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }

    private void updateLabel(TextView textView) {
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        textView.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tvFromDate:
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.tvToDate:
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }



    //==========================================API===================================================//
    public void PayBankAPI(String fromDate,String toDate)
    {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("MemberId","ENT10605");
            jsonObject.put("frmDate",fromDate);
            jsonObject.put("toDate",toDate);
            jsonObject.put("status","2");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+MyPinDetails)
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
                                JSONArray jsonArray=response.getJSONArray("PinDetail");

                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    loader.cancel();
                                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                                    String AssignTo=jsonObject.getString("AssignTo")  ;
                                    String GenerateDate=jsonObject.getString("GenerateDate")  ;
                                    String Pin=jsonObject.getString("Pin")  ;
                                    String PinType=jsonObject.getString("PinType")  ;
                                    String Status=jsonObject.getString("Status")  ;
                                    String UsedDate=jsonObject.getString("UsedDate")  ;
                                    String UsedName=jsonObject.getString("UsedName")  ;

                                    pinBankModel=new PinBankModel(AssignTo,GenerateDate,Pin,PinType,Status,UsedDate,UsedName);
                                    pinbanklist.add(pinBankModel);
                                }

                                pinBankListAdapter=new PinBankListAdapter(getActivity(),pinbanklist);
                                recyclerViewPin.setAdapter(pinBankListAdapter);

                                loader.cancel();
                                // loadFragment(new TeamFragment());
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
