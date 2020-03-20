package com.tisanehealth.Fragment.Repurchase;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.tisanehealth.Adapter.TeamRepurchaseAdapter;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.Model.TeamRepurchaseModel;
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
import static com.tisanehealth.Helper.AppUrls.TeamRepurchase;


public class TeamBusinessFragment extends Fragment {
    //RecyclerView
    RecyclerView recyclerView;
    //TeamAdapter
    TeamRepurchaseAdapter teamRepurchaseAdapter;
    //Preferences
    Preferences pref;
    //CustomLoader
    CustomLoader loader;
    //ArrayList
    ArrayList<TeamRepurchaseModel> repurchaselist=new ArrayList<>();
    //ModelClass
    TeamRepurchaseModel teamRepurchaseModel;
    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendar2 = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog.OnDateSetListener date1;

    int status=0;
    String date_format;
    //ImageView
    ImageView ivFilter,ivNoDatafound,ivDownload;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.self_repurchase_fragment, container, false);
        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("Team Repurchase");
        ivLogo.setVisibility(View.GONE);
        tvHeader.setVisibility(View.VISIBLE);


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        loadFragment(new BusinessFragment());
                        return true;
                    }
                }
                return false;
            }
        });



        initialise(view);

        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);
            GetSelfRepurchaseAPI(pref.get(AppSettings.UserId),"","","","");

        } else {
            EasyToast.error(getActivity(), "No Internet Connnection");
        }
        return view;


    }


    public void initialise(View view)
    {
        //Preferences
        pref                = new Preferences(getActivity());

        //loader
        loader              = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        //Imageview
        ivFilter            =view.findViewById(R.id.ivFilter);
        ivDownload            =view.findViewById(R.id.ivDownload);
        ivNoDatafound        =view.findViewById(R.id.ivNoDatafound);

        //Recyclerview
        recyclerView        =view.findViewById(R.id.recylerviewSelf);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        ivDownload.setVisibility(View.GONE);


        ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterDialog();
            }
        });




    }

    //=========================Dialog==================================================//
    public void filterDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnim);
        dialog.setContentView(R.layout.search_member_dialog);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        TextView tvClear,tvStatus,tvmemeberid;
        RelativeLayout rlCancel,rlApply,rlStatus;
        final Spinner spinnerStatus;
        final EditText etMemeberID,etFromDate,etToDate;



        tvClear =dialog.findViewById(R.id.tvClear);
        tvStatus =dialog.findViewById(R.id.tvstatus);
        tvmemeberid =dialog.findViewById(R.id.tvMemberId);
        rlApply=dialog.findViewById(R.id.rlApply);
        rlStatus=dialog.findViewById(R.id.rlStatus);
        rlCancel=dialog.findViewById(R.id.rlCancel);
        etMemeberID=dialog.findViewById(R.id.etMemeberId);
        etFromDate=dialog.findViewById(R.id.etFromDate);
        etToDate=dialog.findViewById(R.id.etToDate);
        spinnerStatus=dialog.findViewById(R.id.spinnerStatus);



        tvStatus.setVisibility(View.GONE);
        rlStatus.setVisibility(View.GONE);



        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if (status==0)
                {
                    updateLabel(etFromDate,myCalendar);
                }
                else
                {
                    updateLabel(etToDate,myCalendar);
                }
            }

        };

        date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, monthOfYear);
                myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if (status==0)
                {
                    updateLabel(etFromDate,myCalendar2);
                }
                else
                {
                    updateLabel(etToDate,myCalendar2);
                }
            }

        };



        rlApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etFromDate.getText().toString().isEmpty()&&etToDate.getText().toString().isEmpty())
                {
                    EasyToast.warning(getActivity(),"Please select your To Date");
                }
               /* else  if (etToDate.getText().toString().isEmpty())
                {
                    EasyToast.warning(getActivity(),"Please select your To Date");
                }*/
                else
                {
                    dialog.dismiss();
                    String member_id;
                    if (etMemeberID.getText().toString().isEmpty())
                    {
                        member_id= pref.get(AppSettings.UserId);
                    }
                    else
                    {
                        member_id= etMemeberID.getText().toString();
                    }
                    if (Utils.isNetworkConnectedMainThred(getActivity())) {
                        loader.show();
                        loader.setCanceledOnTouchOutside(true);
                        loader.setCancelable(false);
                        if (etMemeberID.getText().toString().isEmpty())
                        {
                            GetSelfRepurchaseAPI(pref.get(AppSettings.UserId),etFromDate.getText().toString(),etToDate.getText().toString(),"","");
                        }
                        else
                        {
                            GetSelfRepurchaseAPI(etMemeberID.getText().toString(),etFromDate.getText().toString(),etToDate.getText().toString(),"","");
                        }



                    } else {
                        EasyToast.error(getActivity(), "No Internet Connnection");
                    }

                }

            }
        });

        rlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etMemeberID.setText("");
                etFromDate.setText("");
                etToDate.setText("");
            }
        });

        etFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status=0;
                DatePickerDialog datePickerDialog=  new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });

        etToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etFromDate.getText().toString().isEmpty())
                {
                    EasyToast.warning(getActivity(),"Please select From Date");
                }
                else
                {   status=1;
                    String getfromdate = etFromDate.getText().toString().trim();
                    String getfrom[] = date_format.split("-");
                    int year,month,day;
                    year= Integer.parseInt(getfrom[0]);
                    month = Integer.parseInt(getfrom[1]);
                    day = Integer.parseInt(getfrom[2]);

                    final Calendar myCalendar1 = Calendar.getInstance();
                    myCalendar1.set(Calendar.YEAR, year);
                    myCalendar1.set(Calendar.MONTH, month-1);
                    myCalendar1.set(Calendar.DAY_OF_MONTH, day);


                    DatePickerDialog datePickerDialog1=  new DatePickerDialog(getActivity(), date1, myCalendar2
                            .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                            myCalendar2.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog1.getDatePicker().setMinDate(myCalendar1.getTimeInMillis());
                    datePickerDialog1.getDatePicker().setMaxDate(System.currentTimeMillis());
                    datePickerDialog1.show();

                }

            }
        });

        dialog.show();

    }


    private void updateLabel(EditText editText,Calendar calendar) {
        //String myFormat = "dd-MM-yyyy"; //In which you need put here
        String myFormat = "dd-MMM-yyyy";
        if (status==0)
        {
            String date_value="YYYY-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(date_value, Locale.US);
            date_format=sdf.format(calendar.getTime());
        }
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(sdf.format(calendar.getTime()));
    }

     //=====================================LoadFragment====================================//
    public void loadFragment(Fragment fragment) {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.card_flip_right_in, R.anim.card_flip_right_out,
                R.anim.card_flip_left_in, R.anim.card_flip_left_out);
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }

    //==========================================API===================================================//
    public void GetSelfRepurchaseAPI(String id,String from_date,String to_date,String status,String Position)
    {
        JSONObject jsonObject = new JSONObject();
        try {



            jsonObject.put("MemberId",pref.get(AppSettings.UserId));
            jsonObject.put("Position",Position);
            jsonObject.put("SMemberId",id);
            jsonObject.put("frmDate",from_date);
            jsonObject.put("toDate",to_date);
            jsonObject.put("status",status);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("hfghghghghgh",jsonObject.toString());

        AndroidNetworking.post(BaseUrl+TeamRepurchase)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            repurchaselist.clear();
                            Log.v("jyiujojjujujujujuujujuj",response.toString());
                            String Message=   response.getString("Message");


                            if (Message.equals("Success"))
                            {
                                JSONArray jsonArray=response.getJSONArray("SelfRepurchaseList");
                                recyclerView.setVisibility(View.VISIBLE);
                                ivFilter.setVisibility(View.VISIBLE);
                                ivNoDatafound.setVisibility(View.GONE);

                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    String Amount=jsonObject.getString("Amount")  ;
                                    String CGSTAmt=jsonObject.getString("CGSTAmt")  ;
                                    String CenterId=jsonObject.getString("CenterId")  ;
                                    String Date=jsonObject.getString("Date")  ;
                                    String GrossAmt=jsonObject.getString("GrossAmt")  ;
                                    String InvoiceNo=jsonObject.getString("InvoiceNo")  ;
                                    String SGSTAmt=jsonObject.getString("SGSTAmt")  ;
                                    String TotalBV=jsonObject.getString("TotalBV")  ;
                                    String MemberId=jsonObject.getString("MemberId")  ;
                                    String MemberName=jsonObject.getString("MemberName")  ;

                                    teamRepurchaseModel=new TeamRepurchaseModel(Amount,CGSTAmt,CenterId,Date,GrossAmt,InvoiceNo,SGSTAmt,TotalBV,MemberId,MemberName);
                                    repurchaselist.add(teamRepurchaseModel);
                                }

                                teamRepurchaseAdapter=new TeamRepurchaseAdapter(getActivity(),repurchaselist);
                                recyclerView.setAdapter(teamRepurchaseAdapter);
                                loader.cancel();

                            }
                            else
                            {
                                recyclerView.setVisibility(View.GONE);
                                ivFilter.setVisibility(View.VISIBLE);
                                ivNoDatafound.setVisibility(View.VISIBLE);
                                loader.cancel();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        Log.v("gjfihhgghghg",error.toString());
                        loader.cancel();
                    }
                });
    }

}