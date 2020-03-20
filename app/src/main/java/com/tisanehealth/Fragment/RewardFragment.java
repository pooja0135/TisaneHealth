package com.tisanehealth.Fragment;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static com.tisanehealth.Activity.DashBoardActivity.ivLogo;
import static com.tisanehealth.Activity.DashBoardActivity.rlHeader;
import static com.tisanehealth.Activity.DashBoardActivity.tvHeader;
import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.GetReward;

public class RewardFragment extends Fragment {

    RecyclerView recyclerView;
    ImageView ivNoDataFound,ivFilter;
    RewardAdapter rewardAdapter;

    ArrayList<HashMap<String,String>> rewardlist=new ArrayList<>();

    //Preferences
    Preferences pref;
    //CustomLoader
    CustomLoader loader;


    Spinner spinnerMonth,spinnerYear;
    LinearLayout llSearch;

    ArrayList<String>monthlist=new ArrayList<>();
    ArrayList<String>yearlist=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.reward_fragment, container, false);
        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("Reward");
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


        //Preferences
        pref                = new Preferences(getActivity());

        //loader
        loader              = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        ivNoDataFound=view.findViewById(R.id.ivNoDatafound);



        ivFilter=view.findViewById(R.id.ivFilter);
        spinnerMonth=view.findViewById(R.id.spinnerMonth);
        spinnerYear=view.findViewById(R.id.spinnerYear);

        monthlist.add("All");
        monthlist.add("Jan");
        monthlist.add("Feb");
        monthlist.add("Mar");
        monthlist.add("Apr");
        monthlist.add("May");
        monthlist.add("Jun");
        monthlist.add("Jul");
        monthlist.add("Aug");
        monthlist.add("Sep");
        monthlist.add("Oct");
        monthlist.add("Nov");
        monthlist.add("Dec");


        yearlist.add("All");
        yearlist.add("2019");
        yearlist.add("2020");
        yearlist.add("2021");
        yearlist.add("2022");
        yearlist.add("2023");
        yearlist.add("2024");



        ArrayAdapter monthAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,monthlist);
        spinnerMonth.setAdapter(monthAdapter);

        ArrayAdapter yearAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,yearlist);
        spinnerYear.setAdapter(yearAdapter);





        llSearch=view.findViewById(R.id.llSearch);

        recyclerView=view.findViewById(R.id.recylerviewPayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkConnectedMainThred(getActivity())) {
                    loader.show();
                    loader.setCanceledOnTouchOutside(true);
                    loader.setCancelable(false);
                    if (spinnerYear.getSelectedItem().equals("All"))
                    {
                        GetPayoutAPI(String.valueOf(spinnerMonth.getSelectedItemPosition()),"0");
                    }
                    else
                    {
                        GetPayoutAPI(String.valueOf(spinnerMonth.getSelectedItemPosition()),String.valueOf(spinnerYear.getSelectedItem()));

                    }

                } else {
                    EasyToast.error(getActivity(), "No Internet Connnection");
                }
            }
        });

        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);
            GetPayoutAPI("0","0");

        } else {
            EasyToast.error(getActivity(), "No Internet Connnection");
        }


        return view;


    }




    //========================================LoadFragment===============================//

    public void loadFragment(Fragment fragment) {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.card_flip_right_in, R.anim.card_flip_right_out,
                R.anim.card_flip_left_in, R.anim.card_flip_left_out);
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }


    //==========================================API===================================================//
    public void GetPayoutAPI(String FromDate, String ToDate)
    {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("DSDID",pref.get(AppSettings.UserId));
            jsonObject.put("DSDName","");
            jsonObject.put("FromDate",FromDate);
            jsonObject.put("ToDate",ToDate);

            Log.v("yearvalues",jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+GetReward)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.v("jyiujojjujujujujuujujuj",response.toString());
                            String Message=   response.getString("Message");

                            rewardlist.clear();
                            if (Message.equals("Success"))
                            {
                                JSONArray jsonArray=response.getJSONArray("RewardRec");

                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);



                                    String CFLeftUnit="0.00";
                                    String CFRightUnit="0.00";

                                    String LeftBussiness="0.00";
                                    String LeftUnit="0.00";
                                    String PointValue="0.00";

                                    String RightBussiness="0.00";
                                    String RightUnit="0.00";
                                    String TotalUnit="0.00";


                                    if (jsonObject.getString("CFLeftUnit").isEmpty()||jsonObject.getString("CFLeftUnit").equals("")||jsonObject.getString("CFLeftUnit").equals(null))
                                    {
                                        CFLeftUnit="0.00";
                                    }
                                    else
                                    {
                                        CFLeftUnit=jsonObject.getString("CFLeftUnit")  ;
                                    }

                                    if (jsonObject.getString("CFRightUnit").isEmpty()||jsonObject.getString("CFRightUnit").equals("")||jsonObject.getString("CFRightUnit").equals(null))
                                    {
                                        CFRightUnit="0.00";
                                    }
                                    else
                                    {
                                        CFRightUnit=jsonObject.getString("CFRightUnit")  ;
                                    }

                                    if (jsonObject.getString("LeftBussiness").isEmpty()||jsonObject.getString("LeftBussiness").equals("")||jsonObject.getString("LeftBussiness").equals(null))
                                    {
                                        LeftBussiness="0.00";
                                    }
                                    else
                                    {
                                        LeftBussiness=jsonObject.getString("LeftBussiness")  ;
                                    }

                                    if (jsonObject.getString("LeftUnit").isEmpty()||jsonObject.getString("LeftUnit").equals("")||jsonObject.getString("LeftUnit").equals(null))
                                    {
                                        LeftUnit="0.00";
                                    }
                                    else
                                    {
                                        LeftUnit=jsonObject.getString("LeftUnit")  ;
                                    }


                                    if (jsonObject.getString("PointValue").isEmpty()||jsonObject.getString("PointValue").equals("")||jsonObject.getString("PointValue").equals(null))
                                    {
                                        PointValue="0.00";
                                    }
                                    else
                                    {
                                        PointValue=jsonObject.getString("PointValue")  ;
                                    }

                                    if (jsonObject.getString("RightBussiness").isEmpty()||jsonObject.getString("RightBussiness").equals("")||jsonObject.getString("RightBussiness").equals(null))
                                    {
                                        RightBussiness="0.00";
                                    }
                                    else
                                    {
                                        RightBussiness=jsonObject.getString("RightBussiness")  ;
                                    }

                                    if (jsonObject.getString("RightUnit").isEmpty()||jsonObject.getString("RightUnit").equals("")||jsonObject.getString("RightUnit").equals(null))
                                    {
                                        RightUnit="0.00";
                                    }
                                    else
                                    {
                                        RightUnit=jsonObject.getString("RightUnit")  ;
                                    }

                                    if (jsonObject.getString("TotalUnit").isEmpty()||jsonObject.getString("TotalUnit").equals("")||jsonObject.getString("TotalUnit").equals(null))
                                    {
                                        TotalUnit="0.00";
                                    }
                                    else
                                    {
                                        TotalUnit=jsonObject.getString("TotalUnit")  ;
                                    }

                                    HashMap<String,String>map=new HashMap<>();
                                    map.put("TotalUnit",TotalUnit);
                                    map.put("RightUnit",RightUnit);
                                    map.put("RightBussiness",RightBussiness);
                                    map.put("PointValue",PointValue);
                                    map.put("LeftUnit",LeftUnit);
                                    map.put("LeftBussiness",LeftBussiness);
                                    map.put("CFRightUnit",CFRightUnit);
                                    map.put("CFLeftUnit",CFLeftUnit);
                                    map.put("Reward",jsonObject.getString("Reward"));

                                 rewardlist.add(map);

                                }
                                ivNoDataFound.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                rewardAdapter=new RewardAdapter(getActivity(),rewardlist);
                                recyclerView.setAdapter(rewardAdapter);
                                loader.cancel();

                            }
                            else
                            {
                                loader.cancel();
                                ivNoDataFound.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
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



    public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.MyViewHolder> {

        private Context mContext;
        ArrayList<HashMap<String,String>>teamlist;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tvLeftunit,tvRightUnit,tvTotalUnit,tvleftCarry,tvRightCarry,tvPointValue,tvleftLeg,tvRightLeg,tvReward;


            public MyViewHolder(View view) {
                super(view);

                tvLeftunit=view.findViewById(R.id.tvLeftUnit);
                tvRightUnit=view.findViewById(R.id.tvRightUnit);
                tvTotalUnit=view.findViewById(R.id.tvTotalUnit);
                tvleftCarry=view.findViewById(R.id.tvCarryLeft);
                tvRightCarry=view.findViewById(R.id.tvCarryRight);
                tvPointValue=view.findViewById(R.id.tvPointValue);
                tvleftLeg=view.findViewById(R.id.tvLeftLeg);
                tvRightLeg=view.findViewById(R.id.tvRightLeg);
                tvReward=view.findViewById(R.id.tvReward);





            }
        }


        public RewardAdapter(Context mContext, ArrayList<HashMap<String,String>>teamlist) {
            this.mContext = mContext;
            this.teamlist=teamlist;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.inflate_reward_list, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final RewardAdapter.MyViewHolder holder, int position) {


            Log.v("leftbusiness", String.valueOf(Double.valueOf(teamlist.get(position).get("LeftBussiness")+2).longValue()));


            holder.tvLeftunit.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(teamlist.get(position).get("LeftUnit")))));
            holder.tvPointValue.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(teamlist.get(position).get("PointValue")))));
            holder.tvRightUnit.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(teamlist.get(position).get("RightUnit")))));
            holder.tvTotalUnit.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(teamlist.get(position).get("TotalUnit")))));
            holder.tvRightLeg.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.valueOf(teamlist.get(position).get("RightBussiness")+2).longValue())));
            holder.tvleftLeg.setText("\u20B9 " + String.valueOf((Double.valueOf(teamlist.get(position).get("LeftBussiness")+2).longValue())));
            holder.tvleftCarry.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(teamlist.get(position).get("CFLeftUnit")))));
            holder.tvRightCarry.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(teamlist.get(position).get("CFRightUnit")))));
            holder.tvReward.setText(teamlist.get(position).get("Reward"));



        }


        @Override
        public int getItemCount() {
            return teamlist.size();
        }


        public String parseDateToddMMyyyy(String time) {
            //  String inputPattern = "MM/dd/yyyy HH:mm:ss a";
            String inputPattern = "MM/dd/yyyy HH:mm:ss a";
            String outputPattern = "dd-MMM-yyyy";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.ENGLISH);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern,Locale.ENGLISH);

            Date date = null;
            String str = null;

            try {
                date = inputFormat.parse(time);
                str = outputFormat.format(date);
            } catch (Exception e) {


                e.printStackTrace();
            }
            return str;
        }

        double roundTwoDecimals(double d)
        {
            DecimalFormat twoDForm = new DecimalFormat("##.##");
            return Double.valueOf(twoDForm.format(Double.valueOf(d).longValue()));
        }


    }




}
