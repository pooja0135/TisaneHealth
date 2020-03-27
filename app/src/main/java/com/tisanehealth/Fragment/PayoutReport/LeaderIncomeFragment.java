package com.tisanehealth.Fragment.PayoutReport;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.medialablk.easytoast.EasyToast;
import com.tisanehealth.Adapter.payout.DirectIncomeAdapter;
import com.tisanehealth.Adapter.payout.LeaderIncomeAdapter;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.Model.payout.DirectIncomeModel;
import com.tisanehealth.Model.payout.LeaderIncomeModel;
import com.tisanehealth.R;

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
import static com.tisanehealth.Helper.AppUrls.GetDirectIncomeReport;
import static com.tisanehealth.Helper.AppUrls.LeaderPerformanceIncome;

public class LeaderIncomeFragment extends Fragment implements View.OnClickListener {


    LeaderIncomeModel leaderIncomeModel;
    ArrayList<LeaderIncomeModel>leaderincomelist=new ArrayList<>();

    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendar1 = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog.OnDateSetListener date1;

    LeaderIncomeAdapter leaderIncomeAdapter;

    RecyclerView recylerviewDirect;

    TextView tvFromDate,tvToDate,tvSearch;

    ImageView ivNoDatafound;

    //Preferences
    Preferences pref;
    //CustomLoader
    CustomLoader loader;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.direct_income_fragment, container, false);

        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("Leader Performance Income");
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




        return view;
    }

    //Initialise Value
    public void initialise(View view)
    {



        //TextView
        tvFromDate            =view.findViewById(R.id.tvFromDate);
        tvToDate              =view.findViewById(R.id.tvToDate);
        tvSearch              =view.findViewById(R.id.tvSearch);


        ivNoDatafound              =view.findViewById(R.id.ivNoDatafound);
        recylerviewDirect       =view.findViewById(R.id.recylerviewDirect);


        recylerviewDirect.setLayoutManager(new LinearLayoutManager(getActivity()));

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



        tvFromDate.setOnClickListener(this);
        tvToDate.setOnClickListener(this);
        tvSearch.setOnClickListener(this);

        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);
            LeaderIncomeAPI("","");

        } else {
            EasyToast.error(getActivity(), "No Internet Connnection");
        }

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

    private void updateLabel(TextView textView,Calendar calendar) {
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
                        LeaderIncomeAPI(tvFromDate.getText().toString(),tvToDate.getText().toString());

                    } else {
                        EasyToast.error(getActivity(), "No Internet Connnection");
                    }
                }



                break;
        }
    }



    //==========================================API===================================================//
    public void LeaderIncomeAPI(String fromDate,String toDate)
    {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("MemberId",pref.get(AppSettings.UserId));
            jsonObject.put("FromDate",fromDate);
            jsonObject.put("ToDate",toDate);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+LeaderPerformanceIncome)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //Log.v("jyiujojjujujujujuujujuj",response.toString());
                            String Message=   response.getString("ResponseMessage");
                            leaderincomelist.clear();
                            if (Message.equals("success"))
                            {
                                JSONArray jsonArray=response.getJSONArray("lstLeaderPerformanceIncome");

                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    loader.cancel();
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);

                                    String LeftTeamMBP=jsonObject.getString("LeftTeamMBP")  ;
                                    String MemberId=jsonObject.getString("MemberId")  ;
                                    String Name=jsonObject.getString("Name")  ;
                                    String PaidDate=jsonObject.getString("PaidDate")  ;
                                    String PayableAmount=jsonObject.getString("PayableAmount")  ;
                                    String RightTeamMBP=jsonObject.getString("RightTeamMBP")  ;

                                    leaderIncomeModel=new LeaderIncomeModel(LeftTeamMBP,MemberId,Name,PaidDate,PayableAmount,RightTeamMBP);
                                    leaderincomelist.add(leaderIncomeModel);
                                }

                                leaderIncomeAdapter=new LeaderIncomeAdapter(getActivity(),leaderincomelist);
                                recylerviewDirect.setAdapter(leaderIncomeAdapter);

                                loader.cancel();


                                ivNoDatafound.setVisibility(View.GONE);
                                recylerviewDirect.setVisibility(View.VISIBLE);

                                // loadFragment(new TeamFragment());
                            }
                            else
                            {
                                loader.cancel();

                                ivNoDatafound.setVisibility(View.VISIBLE);
                                recylerviewDirect.setVisibility(View.GONE);
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
