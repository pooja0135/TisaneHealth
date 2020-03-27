package com.tisanehealth.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.medialablk.easytoast.EasyToast;
import com.tisanehealth.Adapter.DailyBusinessReportAdapter;
import com.tisanehealth.Adapter.recharge.Adapter_rechargehistory;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.Model.DailyBusinessListModel;
import com.tisanehealth.R;
import com.tisanehealth.recharge_pay_bill.RechargeHistoryActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.DailyTopUpDetail;
import static com.tisanehealth.Helper.AppUrls.GetRechargeHistory;

public class DailyBusinessReport extends AppCompatActivity {

    Preferences pref;
    CustomLoader loader;
    EditText e1, e2;
    Button btnFilter;
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog.OnDateSetListener date1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_business_report);
        pref = new Preferences(this);
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        e1 = findViewById(R.id.e1);
        e2 = findViewById(R.id.e2);
        btnFilter = findViewById(R.id.btnFilter);
        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (e1.getText().toString().equals("")) {
                    Toast.makeText(DailyBusinessReport.this, "Enter Start Date", Toast.LENGTH_SHORT).show();
                } else if (e2.getText().toString().equals("")) {
                    Toast.makeText(DailyBusinessReport.this, "Enter End Date", Toast.LENGTH_SHORT).show();
                } else {
                    GetRechargeHistoryAPI(e1.getText().toString(), e2.getText().toString());
                }
            }
        });
        if (Utils.isNetworkConnectedMainThred(this)) {
            GetRechargeHistoryAPI("", "");
        } else {
            EasyToast.error(this, "No Internet Connnection");
        }

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(e1);
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
                updateLabel(e2);
            }

        };

        e1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(DailyBusinessReport.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        e2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(DailyBusinessReport.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



    }

    private void updateLabel(TextView textView) {
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        textView.setText(sdf.format(myCalendar.getTime()));
    }

    public void GetRechargeHistoryAPI(String s, String ee) {
        loader.show();
        loader.setCanceledOnTouchOutside(true);
        loader.setCancelable(false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MemberId", pref.get(AppSettings.UserId));
            if (!s.equals("") && !ee.equals("")) {
                jsonObject.put("Fromdate", s);
                jsonObject.put("Todate", ee);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl + DailyTopUpDetail)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        loader.cancel();
                        try {
                            boolean Status = response.getBoolean("Status");

                            if (Status) {
                                JSONArray jsonArray = response.getJSONArray("Response");
                                if (jsonArray.length() == 0) {
                                    findViewById(R.id.tvEmpty).setVisibility(View.VISIBLE);
                                    return;
                                }


                                ArrayList<DailyBusinessListModel> listModel = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    DailyBusinessListModel model = new DailyBusinessListModel();
                                    model.setDate(object.getString("Entered_date"));
                                    model.setLeft(object.getString("LPV"));
                                    model.setRight(object.getString("RPV"));
                                    model.setTotal(object.getString("TPV"));
                                    model.setSno("" + (i + 1));
                                    listModel.add(model);
                                }

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DailyBusinessReport.this);
                                DailyBusinessReportAdapter adapter = new DailyBusinessReportAdapter(listModel);
                                RecyclerView rv = findViewById(R.id.rvData);
                                rv.setLayoutManager(layoutManager);
                                rv.setAdapter(adapter);
                                rv.setVisibility(View.VISIBLE);
                            } else {
                                findViewById(R.id.tvEmpty).setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            findViewById(R.id.tvEmpty).setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        findViewById(R.id.tvEmpty).setVisibility(View.VISIBLE);
                        loader.cancel();
                    }
                });
    }

}
