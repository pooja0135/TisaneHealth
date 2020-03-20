package com.tisanehealth.Fragment.Team;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tisanehealth.Adapter.TeamAdapter;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.Model.TeamModelClass;
import com.tisanehealth.R;
import com.medialablk.easytoast.EasyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.tisanehealth.Activity.DashBoardActivity.ivLogo;
import static com.tisanehealth.Activity.DashBoardActivity.rlHeader;
import static com.tisanehealth.Activity.DashBoardActivity.tvHeader;
import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.GetMyTeam;

public class MyTeamFragment extends Fragment {
    //RecyclerView
    RecyclerView recyclerView;
    //TeamAdapter
    TeamAdapter teamAdapter;
    //Preferences
    Preferences pref;
    //CustomLoader
    CustomLoader loader;
    //ArrayList
    ArrayList<TeamModelClass>teamlist=new ArrayList<>();
    //ModelClass
    TeamModelClass teamModelClass;
    //ImageView
    ImageView ivFilter,ivNoDatafound;

    TextView tvTotalMember,tvActiveMember,tvInActiveMember;

    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendar2 = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog.OnDateSetListener date1;

    int status=0;

    String fromdate,todate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_teamlist_fragment, container, false);

        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("My Team");
        ivLogo.setVisibility(View.GONE);
        tvHeader.setVisibility(View.VISIBLE);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        loadFragment(new TeamFragment());
                        return true;
                    }
                }
                return false;
            }
        });

        //Initialise Value
        initialise(view);


        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);
            GetMyTeamAPI("","","","2","--Select--");

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
        ivNoDatafound        =view.findViewById(R.id.ivNoDatafound);

        //Recyclerview
        recyclerView        =view.findViewById(R.id.recylerview);

        //TextView
        tvTotalMember        =view.findViewById(R.id.tvTotalMember);
        tvActiveMember        =view.findViewById(R.id.tvActiveMember);
        tvInActiveMember        =view.findViewById(R.id.tvInActiveMember);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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

        TextView tvClear;
        RelativeLayout rlCancel,rlApply;
        final Spinner spinnerStatus,spinnerPosition;
        final EditText etMemeberID,etFromDate,etToDate;


        tvClear =dialog.findViewById(R.id.tvClear);
        rlApply=dialog.findViewById(R.id.rlApply);
        rlCancel=dialog.findViewById(R.id.rlCancel);
        etMemeberID=dialog.findViewById(R.id.etMemeberId);
        etFromDate=dialog.findViewById(R.id.etFromDate);
        etToDate=dialog.findViewById(R.id.etToDate);
        spinnerStatus=dialog.findViewById(R.id.spinnerStatus);
        spinnerPosition=dialog.findViewById(R.id.spinnerPosition);

        ArrayList<String>statuslist=new ArrayList<>();
        ArrayList<String>positionlist=new ArrayList<>();

        statuslist.add("Select");
        statuslist.add("Active");
        statuslist.add("InActive");


        positionlist.add("Select");
        positionlist.add("Left");
        positionlist.add("Right");

        ArrayAdapter genderAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,statuslist);
        spinnerStatus.setAdapter(genderAdapter);


        ArrayAdapter positionAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,positionlist);
        spinnerPosition.setAdapter(positionAdapter);



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
                    updateLabel(etFromDate,myCalendar, todate);
                }
                else
                {
                    updateLabel(etToDate,myCalendar,todate);
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
                    updateLabel(etFromDate,myCalendar2,fromdate);
                }
                else
                {
                    updateLabel(etToDate,myCalendar2,fromdate);
                }
            }

        };



        rlApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                        if (spinnerStatus.getSelectedItem().equals("Select"))
                        {
                            if (spinnerPosition.getSelectedItem().equals("Select"))
                            {
                                GetMyTeamAPI(member_id,etFromDate.getText().toString(),etToDate.getText().toString(), "2","--Select--");

                            }
                           else if (spinnerPosition.getSelectedItem().equals("Left"))
                            {
                                GetMyTeamAPI(member_id,etFromDate.getText().toString(),etToDate.getText().toString(), "2","L");

                            }
                            else
                            {
                                GetMyTeamAPI(member_id,etFromDate.getText().toString(),etToDate.getText().toString(), "2","R");

                            }

                        }

                        else if (spinnerStatus.getSelectedItem().equals("Active"))
                        {

                            if (spinnerPosition.getSelectedItem().equals("Select"))
                            {
                                GetMyTeamAPI(member_id,etFromDate.getText().toString(),etToDate.getText().toString(), "1","--Select--");

                            }
                           else if (spinnerPosition.getSelectedItem().equals("Left"))
                            {
                                GetMyTeamAPI(member_id,etFromDate.getText().toString(),etToDate.getText().toString(), "1","L");

                            }
                            else
                            {
                                GetMyTeamAPI(member_id,etFromDate.getText().toString(),etToDate.getText().toString(), "1","R");

                            }

                        }
                        else
                        {

                            if (spinnerPosition.getSelectedItem().equals("Select"))
                            {
                                GetMyTeamAPI(member_id,etFromDate.getText().toString(),etToDate.getText().toString(), "0","--Select--");

                            }

                           else if (spinnerPosition.getSelectedItem().equals("Left"))
                            {
                                GetMyTeamAPI(member_id,etFromDate.getText().toString(),etToDate.getText().toString(), "0","L");

                            }
                            else
                            {
                                GetMyTeamAPI(member_id,etFromDate.getText().toString(),etToDate.getText().toString(), "0","R");

                            }


                        }




                    } else {
                        EasyToast.error(getActivity(), "No Internet Connnection");
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
                {

                    status=1;
                    String getfromdate = etFromDate.getText().toString().trim();
                    String getfrom[] = getfromdate.split("-");
                    int year,month,day;

                    day= Integer.parseInt(getfrom[0]);
                    month = Integer.parseInt(getfrom[1]);
                    year = Integer.parseInt(getfrom[2]);

                    final Calendar myCalendar1 = Calendar.getInstance();
                    myCalendar1.set(Calendar.YEAR, year);
                    myCalendar1.set(Calendar.MONTH, month-1);
                    myCalendar1.set(Calendar.DAY_OF_MONTH, day);


                    DatePickerDialog datePickerDialog1=  new DatePickerDialog(getActivity(), date1, myCalendar2
                            .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                            myCalendar2.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog1.getDatePicker().setMinDate(myCalendar1.getTimeInMillis());
                    datePickerDialog1.show();

                }

            }
        });

        dialog.show();

    }


    private void updateLabel(EditText editText,Calendar calendar,String datevalue) {
       String myFormat = "dd-MM-yyyy"; //In which you need put here
       // String myFormat1 = "YYYY-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
       // SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);

        editText.setText(sdf.format(calendar.getTime()));



    }


    //======================================Load Fragment=====================================//
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
    public void GetMyTeamAPI(String user_id,String from_date,String to_date,String status,String position)
    {
        if (to_date.isEmpty())
        {

        }
        else
        {
            Date date = null;
            Date date1 = null;
            try {
                date = new SimpleDateFormat("dd-MM-yyyy").parse(to_date);
                date1 = new SimpleDateFormat("dd-MM-yyyy").parse(from_date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            to_date = new SimpleDateFormat("dd-MMM-yyyy").format(date);
            from_date = new SimpleDateFormat("dd-MMM-yyyy").format(date1);


        }



        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MemberId",pref.get(AppSettings.UserId));
            jsonObject.put("SMemberId","");
            jsonObject.put("Position",position);
            jsonObject.put("frmDate",from_date);
            jsonObject.put("status",status);
            jsonObject.put("toDate",to_date);

        } catch (JSONException e) {
            e.printStackTrace();
        }
         Log.v("hfghghghghgh",jsonObject.toString());

        AndroidNetworking.post(BaseUrl+GetMyTeam)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            teamlist.clear();
                            Log.v("jyiujojjujujujujuujujuj",response.toString());
                            String Message=   response.getString("Message");

                            int active=0;
                            int inactive=0;

                            if (Message.equals("Success"))
                            {
                                JSONArray jsonArray=response.getJSONArray("ProfileList");
                                recyclerView.setVisibility(View.VISIBLE);
                                ivFilter.setVisibility(View.VISIBLE);
                                ivNoDatafound.setVisibility(View.GONE);

                                for(int i=0;i<jsonArray.length();i++)
                                {

                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    String ActiveDate=jsonObject.getString("ActiveDate")  ;
                                    String MemberId=jsonObject.getString("MemberId")  ;
                                    String Name=jsonObject.getString("Name")  ;
                                    String Package=jsonObject.getString("Package")  ;
                                    String RegDate=jsonObject.getString("RegDate")  ;
                                    String SponserId=jsonObject.getString("SponserId")  ;
                                    String Status=jsonObject.getString("Status")  ;

                                    if (Status.equals("Active"))
                                    {
                                        active=active+1;
                                    }
                                    else
                                    {
                                        inactive=inactive+1;
                                    }
                                    teamModelClass=new TeamModelClass(MemberId,Name,Package,RegDate,ActiveDate,Status,SponserId);
                                    teamlist.add(teamModelClass);
                                }

                                teamAdapter=new TeamAdapter(getActivity(),teamlist);
                                recyclerView.setAdapter(teamAdapter);
                                loader.cancel();


                                tvTotalMember.setText("" +teamlist.size());
                                tvActiveMember.setText("" +active);
                                tvInActiveMember.setText("" +inactive);

                            }
                            else
                            {
                                recyclerView.setVisibility(View.GONE);
                                ivFilter.setVisibility(View.VISIBLE);
                                ivNoDatafound.setVisibility(View.VISIBLE);
                                tvTotalMember.setText("" +teamlist.size());
                                tvActiveMember.setText("" +active);
                                tvInActiveMember.setText("" +inactive);

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