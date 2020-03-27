package com.tisanehealth.Fragment.Repurchase;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.medialablk.easytoast.EasyToast;
import com.opencsv.CSVWriter;
import com.tisanehealth.Adapter.SelfRepurchaseAdapter;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.Model.SelfRepurchaseModel;
import com.tisanehealth.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import static com.tisanehealth.Activity.DashBoardActivity.ivLogo;
import static com.tisanehealth.Activity.DashBoardActivity.rlHeader;
import static com.tisanehealth.Activity.DashBoardActivity.tvHeader;
import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.SelfRepurchase;


public class SelfBusinessFragment extends Fragment implements View.OnClickListener{
    //RecyclerView
    RecyclerView recyclerView;
    //TeamAdapter
    SelfRepurchaseAdapter selfRepurchaseAdapter;
    //Preferences
    Preferences pref;
    //CustomLoader
    CustomLoader loader;
    //ImageView
    ImageView ivFilter,ivNoDatafound,ivDownload;
    //ArrayList
    ArrayList<SelfRepurchaseModel> repurchaselist=new ArrayList<>();
    //ModelClass
    SelfRepurchaseModel selfRepurchaseModel;
    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendar2 = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog.OnDateSetListener date1;

    int status=0;

    String date_format;
    String formattedDate;
    JSONArray jsonArray;
    Double total_value=0.00,total_cgst=0.00,total_sgst=0.00,total_amount=0.00,total_bv=0.00;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.self_repurchase_fragment, container, false);
        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("Self Repurchase");
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


        //initialise value
        initialise(view);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);

        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);
            GetSelfRepurchaseAPI("","","","","");

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



        //setOnClickListener
        ivDownload.setOnClickListener(this);
        ivFilter.setOnClickListener(this);
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


        tvmemeberid.setVisibility(View.GONE);
        tvStatus.setVisibility(View.GONE);
        etMemeberID.setVisibility(View.GONE);
        rlStatus.setVisibility(View.GONE);

        etFromDate.setText(formattedDate);
        etToDate.setText(formattedDate);

        ArrayList<String>statuslist=new ArrayList<>();

        statuslist.add("Select Status");
        statuslist.add("Active");
        statuslist.add("Deactive");

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
                        if (spinnerStatus.getSelectedItem().equals("Select Status"))
                        {
                          //  GetMyTeamAPI(member_id,etFromDate.getText().toString(),etToDate.getText().toString(),"--Select--");

                          GetSelfRepurchaseAPI(etFromDate.getText().toString(),etToDate.getText().toString(),"","","");
                        }
                        else{
                         //   GetMyTeamAPI(member_id,etFromDate.getText().toString(),etToDate.getText().toString(),spinnerStatus.getSelectedItem().toString());
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
        //Log.v("hfghghghghgh",jsonObject.toString());

        AndroidNetworking.post(BaseUrl+SelfRepurchase)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            repurchaselist.clear();
                            //Log.v("jyiujojjujujujujuujujuj",response.toString());
                            String Message=   response.getString("Message");


                            if (Message.equals("Success"))
                            {
                                jsonArray=response.getJSONArray("SelfRepurchaseList");
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

                                    selfRepurchaseModel=new SelfRepurchaseModel(Amount,CGSTAmt,CenterId,Date,GrossAmt,InvoiceNo,SGSTAmt,TotalBV);
                                    repurchaselist.add(selfRepurchaseModel);
                                }

                                selfRepurchaseAdapter=new SelfRepurchaseAdapter(getActivity(),repurchaselist);
                                recyclerView.setAdapter(selfRepurchaseAdapter);
                                loader.cancel();
                            }
                            else
                            {
                                recyclerView.setVisibility(View.GONE);
                                ivFilter.setVisibility(View.VISIBLE);
                                ivDownload.setVisibility(View.GONE);
                                ivNoDatafound.setVisibility(View.VISIBLE);
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


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ivDownload:
                try {
                    saveCsv(jsonArray);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ivFilter:
                 filterDialog();
                break;
        }
    }


    public void saveCsv(JSONArray outerArray) throws IOException, JSONException {
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        String rootPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/ApnaGullak/";
        File dir = new File(rootPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file;
        Toast.makeText(getActivity(), "This file is saved in Apna Gullak folder in your File Manager.", Toast.LENGTH_LONG).show();
        file = new File(rootPath, "PurchaseReport"+ts+".csv");
        if(!file.exists()){
            file.createNewFile();
        }
        if (file.exists()) {
            CSVWriter writer = new CSVWriter(new FileWriter(file), ',');

            String[][] arrayOfArrays = new String[outerArray.length()+3][];

            String[] stringArray2 = new String[8];
            String[] stringArray3 = new String[8];
            stringArray2[0]= (String) "Date";
            stringArray2[1]= (String)"Invoice Number";
            stringArray2[2]= (String) "Center";
            stringArray2[3]= (String) "Taxable Amount";
            stringArray2[4]= (String) "SGST";
            stringArray2[5]= (String) "CGST";
            stringArray2[6]= (String) "Total Amount";
            stringArray2[7]= (String) "Total Business Value";
            arrayOfArrays[0] = stringArray2;
            writer.writeNext(arrayOfArrays[0]);



            for (int i = 0; i <outerArray.length(); i++) {
                JSONObject innerJsonArray =  (JSONObject) outerArray.get(i);


                String[] stringArray1 = new String[innerJsonArray.length()-2];

                stringArray1[0]= (String) innerJsonArray.getString("Date");
                stringArray1[1]= (String) innerJsonArray.getString("InvoiceNo");
                stringArray1[2]= (String) innerJsonArray.getString("CenterId");
                stringArray1[3]= (String) innerJsonArray.getString("GrossAmt");
                stringArray1[4]= (String) innerJsonArray.getString("SGSTAmt");
                stringArray1[5]= (String) innerJsonArray.getString("CGSTAmt");
                stringArray1[6]= (String) innerJsonArray.getString("Amount");
                stringArray1[7]= (String) innerJsonArray.getString("TotalBV");

                try{

                    total_value=total_value+Double.parseDouble(String.valueOf(stringArray1[3]));


                 total_sgst=total_sgst+Double.parseDouble(stringArray1[4]);
                 total_cgst=total_cgst+Double.parseDouble(stringArray1[5]);
                 total_amount=total_amount+Double.parseDouble(stringArray1[6]);
                 total_bv=total_bv+Double.parseDouble(stringArray1[7]);
                    //Log.v("value1234", String.valueOf(total_value));
                }
                catch(Exception e)
                {
                    //Log.v("gfgfggggggggg",e.toString());
                }


                arrayOfArrays[i+1] = stringArray1;
                writer.writeNext(arrayOfArrays[i+1]);
            }


            stringArray3[0]= (String) "";
            stringArray3[1]= (String)"";
            stringArray3[2]= (String) "Total";
            stringArray3[3]= (String)String.valueOf(total_value);
            stringArray3[4]= (String) String.valueOf(total_sgst);
            stringArray3[5]= (String)String.valueOf(total_cgst);
            stringArray3[6]= (String) String.valueOf(total_amount);
            stringArray3[7]= (String)String.valueOf(total_bv);
            arrayOfArrays[outerArray.length()+2] = stringArray3;
            writer.writeNext(arrayOfArrays[outerArray.length()+2]);





            writer.close();
        }
    }


}