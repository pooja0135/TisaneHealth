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
import com.tisanehealth.Adapter.payout.BankSummaryAdapter;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.Model.payout.BankSummaryModel;
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
import static com.tisanehealth.Helper.AppUrls.GetBankSummarryReport;

public class BankWalletSummaryFragment extends Fragment implements View.OnClickListener {


    BankSummaryModel bankSummaryModel;
    ArrayList<BankSummaryModel>banksummarylist=new ArrayList<>();

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog.OnDateSetListener date1;

    BankSummaryAdapter bankSummaryAdapter;

    RecyclerView rvBankSummary;

    TextView tvFromDate,tvToDate,tvSearch;

    ImageView ivNoDatafound;

    //Preferences
    Preferences pref;
    //CustomLoader
    CustomLoader loader;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bank_walllet_summary_fragment, container, false);

        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("Total Income");
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
        rvBankSummary               =view.findViewById(R.id.rvBankSummary);


        rvBankSummary.setLayoutManager(new LinearLayoutManager(getActivity()));

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
        tvSearch.setOnClickListener(this);

        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);
            BankSummaryAPI();

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
    public void BankSummaryAPI()
    {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("UserId",pref.get(AppSettings.UserId));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+GetBankSummarryReport)
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
                                JSONArray jsonArray=response.getJSONArray("BankSummaryDetail");

                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    loader.cancel();
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);

                                    String AdminCharge=jsonObject.getString("AdminCharge")  ;
                                    String BeneficiaryACNo=jsonObject.getString("BeneficiaryACNo")  ;
                                    String BeneficiaryBank=jsonObject.getString("BeneficiaryBank")  ;
                                    String BeneficiaryName=jsonObject.getString("BeneficiaryName")  ;
                                    String BenificiaryIFSC=jsonObject.getString("BenificiaryIFSC")  ;
                                    String DirectIncome=jsonObject.getString("DirectIncome")  ;
                                    String GenerateDate=jsonObject.getString("GenerateDate")  ;
                                    String MatchingIncome=jsonObject.getString("MatchingIncome")  ;
                                    String MemberID=jsonObject.getString("MemberID")  ;
                                    String Name=jsonObject.getString("Name")  ;
                                    String PayableAmount=jsonObject.getString("PayableAmount")  ;
                                    String turnoverIncome=jsonObject.getString("ROIIncome")  ;
                                    String TDS=jsonObject.getString("TDS")  ;
                                    String CrawnShipIncome=jsonObject.getString("CrawnShipIncome")  ;
                                    String LeaderShipIncome=jsonObject.getString("LeaderShipIncome")  ;
                                    String PANNo=jsonObject.getString("PANNo")  ;

                                    bankSummaryModel=new BankSummaryModel(MemberID,Name,BeneficiaryName,BenificiaryIFSC,BeneficiaryACNo,BeneficiaryBank,GenerateDate,MatchingIncome,DirectIncome,turnoverIncome,AdminCharge,TDS,PayableAmount,CrawnShipIncome,LeaderShipIncome,PANNo);
                                    banksummarylist.add(bankSummaryModel);
                                }

                                bankSummaryAdapter=new BankSummaryAdapter(getActivity(),banksummarylist);
                                rvBankSummary.setAdapter(bankSummaryAdapter);

                                loader.cancel();
                                rvBankSummary.setVisibility(View.VISIBLE);
                                ivNoDatafound.setVisibility(View.GONE);
                            }
                            else
                            {
                                loader.cancel();
                                rvBankSummary.setVisibility(View.GONE);
                                ivNoDatafound.setVisibility(View.VISIBLE);
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
