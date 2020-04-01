package com.tisanehealth.Fragment.PayoutReport;

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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tisanehealth.Adapter.payout.RoiIncomeAdapter;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.Model.payout.RoiIncomeModel;
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
import static com.tisanehealth.Helper.AppUrls.GetRoiIncomeReport;

public class RoiIncomeFragment extends Fragment implements View.OnClickListener {


    RoiIncomeModel roiIncomeModel;
    ArrayList<RoiIncomeModel>roiincomelist=new ArrayList<>();

    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendar1 = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog.OnDateSetListener date1;

    RoiIncomeAdapter roiIncomeAdapter;

    RecyclerView rvRoiIncome;

    TextView tvFromDate,tvToDate,tvSearch;

    ImageView ivNoDatafound;

    //Preferences
    Preferences pref;
    //CustomLoader
    CustomLoader loader;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.roi_income_fragment, container, false);

        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("ROI Income Report");
        ivLogo.setVisibility(View.GONE);
        tvHeader.setVisibility(View.VISIBLE);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        loadFragment(new PayoutReportFragment());
                        return true;
                    }
                }
                return false;
            }
        });


        //Initialise Value
        initialise(view);

        tvFromDate.setOnClickListener(this);
        tvToDate.setOnClickListener(this);
        tvSearch.setOnClickListener(this);


        return view;
    }

    //Initialise Value
    public void initialise(View view)
    {



        //TextView
        tvFromDate            =view.findViewById(R.id.tvFromDate);
        tvToDate              =view.findViewById(R.id.tvToDate);


        ivNoDatafound              =view.findViewById(R.id.ivNoDatafound);
        rvRoiIncome               =view.findViewById(R.id.rvRoiIncome);
        tvSearch                 =view.findViewById(R.id.tvSearch);


        rvRoiIncome.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Preferences
        pref                = new Preferences(getActivity());

        //loader
        loader              = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);




        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(tvFromDate,myCalendar);
            }

        };

        date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(tvToDate,myCalendar1);
            }

        };





        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);
            RoiIncomeReportAPI("","");

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

    private void updateLabel(TextView textView, Calendar calendar) {
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        textView.setText(sdf.format(calendar.getTime()));
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
                new DatePickerDialog(getActivity(), date1, myCalendar1
                        .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.tvSearch:

                if (tvFromDate.getText().toString().isEmpty())
                {
                 EasyToast.warning(getActivity(),"Select From Date");
                }
                if (tvToDate.getText().toString().isEmpty())
                {
                    EasyToast.warning(getActivity(),"Select To Date");
                }

                else
                {
                    if (Utils.isNetworkConnectedMainThred(getActivity())) {
                        loader.show();
                        loader.setCanceledOnTouchOutside(true);
                        loader.setCancelable(false);
                        RoiIncomeReportAPI(tvFromDate.getText().toString(),tvToDate.getText().toString());

                    } else {
                        EasyToast.error(getActivity(), "No Internet Connnection");
                    }
                }



                break;
        }
    }



    //==========================================API===================================================//
    public void RoiIncomeReportAPI(String fromDate,String toDate)
    {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("UserId",pref.get(AppSettings.UserId));
            jsonObject.put("FromDate",fromDate);
            jsonObject.put("ToDate",toDate);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+GetRoiIncomeReport)
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

                                JSONArray jsonArray=response.getJSONArray("RoiIncomeDetail");

                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    loader.cancel();
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);

                                    String EnteredDate=jsonObject.getString("EnteredDate")  ;
                                    String MemberID=jsonObject.getString("MemberID")  ;
                                    String PaidAmount=jsonObject.getString("PaidAmount")  ;
                                    String PaidMonth=jsonObject.getString("PaidMonth")  ;
                                    String PaymentMonth=jsonObject.getString("PaymentMonth")  ;
                                    String PaymentPerMonth=jsonObject.getString("PaymentPerMonth")  ;

                                    roiIncomeModel=new RoiIncomeModel(MemberID,PaymentMonth,PaymentPerMonth,PaidMonth,PaidAmount,EnteredDate);
                                    roiincomelist.add(roiIncomeModel);
                                }

                                roiIncomeAdapter=new RoiIncomeAdapter(getActivity(),roiincomelist);
                                rvRoiIncome.setAdapter(roiIncomeAdapter);

                                loader.cancel();

                                ivNoDatafound.setVisibility(View.GONE);
                                rvRoiIncome.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                loader.cancel();
                                roiincomelist.clear();


                                ivNoDatafound.setVisibility(View.VISIBLE);
                                rvRoiIncome.setVisibility(View.GONE);
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
