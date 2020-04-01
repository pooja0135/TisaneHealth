package com.tisanehealth.Fragment;

import android.os.Bundle;
import android.os.Handler;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tisanehealth.Activity.DashBoardActivity;
import com.tisanehealth.Adapter.Adapter_recharge;
import com.tisanehealth.Adapter.CustomPagerAdapter;
import com.tisanehealth.Adapter.NewsAdapter;
import com.tisanehealth.Adapter.RewardAdapter;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.Model.RechargeModel;
import com.tisanehealth.R;
import com.medialablk.easytoast.EasyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static com.tisanehealth.Activity.DashBoardActivity.ivLogo;
import static com.tisanehealth.Activity.DashBoardActivity.rlHeader;
import static com.tisanehealth.Activity.DashBoardActivity.tvHeader;
import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.GetDashboardData;
import static com.tisanehealth.Helper.AppUrls.GetProfile;

public class DashBoardFragment extends Fragment {

    TextView tvTotalPin, tvTodayUsedPin, tvTotalUsedPin, tvTotalTransferUsedPin, tvTotalUnusedPin;
    TextView tvDirectSponsorIncome, tvSponsorshipIncome, tvRewardIncome, tvTurnoverProfitIncome, tvBusinessPointIncome, tvCrownDirectorshipIncome, tvLeaderPerformanceIncome, tvContextAmount;
    TextView tvTotalBusinessLeft, tvTotalBusinessRight, tvCFLeftBp, tvCFRightBp;
    TextView tvPaidIncome, tvPackage;
    TextView tvNoData, tvNoDataFound;
    TextView tvTotalMember, tvActiveDirectMember, tvTotalActiveMember, tvTotalLeftID, tvTotalRightID;
    TextView tvTotalPayout, tvCurrentPayout;

    ArrayList<String> newslist = new ArrayList<>();
    ArrayList<HashMap<String, String>> rewardlist = new ArrayList<>();

    int count = 0;
    final long DELAY_MS = 600;
    final long PERIOD_MS = 2000;
    Timer timer;

    CustomPagerAdapter customPagerAdapter;

    RecyclerView recyclerviewReward;
    RecyclerView recyclerviewNews;
    RecyclerView rvRecharge;


    //Preferences
    Preferences pref;
    //CustomLoader
    CustomLoader loader;

    int[] covers = new int[]{
            R.drawable.ic_mobile,
            R.drawable.ic_mobile1,
            R.drawable.ic_electricity,
            R.drawable.ic_satellite,
            R.drawable.ic_telephone,
            R.drawable.ic_datacard,
            R.drawable.ic_insurance,
            R.drawable.ic_broadband,
            R.drawable.ic_money_transfer,
            R.drawable.wallet,
            R.drawable.ic_history,
            R.drawable.ic_money_transfer};

    ArrayList<RechargeModel> rechargelist = new ArrayList<>();

    Adapter_recharge adapter_recharge;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dashboard_fragment, container, false);
        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("Dashboard");
        ivLogo.setVisibility(View.GONE);
        tvHeader.setVisibility(View.VISIBLE);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        getActivity().finishAffinity();
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
            GetDasboardAPI();
            GetProfileAPI();

        } else {
            EasyToast.error(getActivity(), "No Internet Connnection");
        }
        return view;


    }


    public void initialise(View view) {
        tvTotalPin = view.findViewById(R.id.tvTotalPin);
        tvTodayUsedPin = view.findViewById(R.id.tvTodayUsedPin);
        tvTotalUsedPin = view.findViewById(R.id.tvTotalUsedPin);
        tvTotalUnusedPin = view.findViewById(R.id.tvTotalUnusedPin);
        tvTotalTransferUsedPin = view.findViewById(R.id.tvTotalTransferUsedPin);


        tvTotalMember = view.findViewById(R.id.tvTotalMember);
        tvActiveDirectMember = view.findViewById(R.id.tvActiveDirectMember);
        tvTotalActiveMember = view.findViewById(R.id.tvTotalActiveMember);
        tvTotalLeftID = view.findViewById(R.id.tvTotalLeftID);
        tvTotalRightID = view.findViewById(R.id.tvTotalRightID);


        tvTotalPayout = view.findViewById(R.id.tvTotalPayout);
        tvCurrentPayout = view.findViewById(R.id.tvCurrentPayout);


        tvDirectSponsorIncome = view.findViewById(R.id.tvDirectSponsorIncome);
        tvSponsorshipIncome = view.findViewById(R.id.tvSponsorshipIncome);
        tvRewardIncome = view.findViewById(R.id.tvRewardIncome);
        tvContextAmount = view.findViewById(R.id.tvContextAmount);
        tvTurnoverProfitIncome = view.findViewById(R.id.tvTurnoverProfitIncome);
        tvBusinessPointIncome = view.findViewById(R.id.tvBusinessPointIncome);
        tvCrownDirectorshipIncome = view.findViewById(R.id.tvCrownDirectorshipIncome);
        tvLeaderPerformanceIncome = view.findViewById(R.id.tvLeaderPerformanceIncome);


        tvTotalBusinessLeft = view.findViewById(R.id.tvTotalBusinessLeft);
        tvTotalBusinessRight = view.findViewById(R.id.tvTotalBusinessRight);


        tvCFLeftBp = view.findViewById(R.id.tvCFLeftBp);
        tvCFRightBp = view.findViewById(R.id.tvCFRightBp);

        tvPackage = view.findViewById(R.id.tvPackage);


        tvNoData = view.findViewById(R.id.tvNoData);
        tvNoDataFound = view.findViewById(R.id.tvNoDataFound);


        recyclerviewReward = view.findViewById(R.id.recyclerviewReward);
        recyclerviewNews = view.findViewById(R.id.recyclerviewNews);
        rvRecharge = view.findViewById(R.id.rvRecharge);

        recyclerviewNews.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerviewReward.setLayoutManager(new LinearLayoutManager(getActivity()));


        RechargeModel a = new RechargeModel("Mobile Prepaid", covers[0]);
        rechargelist.add(a);

        a = new RechargeModel("Mobile Postpaid", covers[1]);
        rechargelist.add(a);

        a = new RechargeModel("Electricity", covers[2]);
        rechargelist.add(a);

        a = new RechargeModel("DTH", covers[3]);
        rechargelist.add(a);

        a = new RechargeModel("Landline", covers[4]);
        rechargelist.add(a);

        a = new RechargeModel("DataCard", covers[5]);
        rechargelist.add(a);

        a = new RechargeModel("Insurance", covers[6]);
        rechargelist.add(a);

        a = new RechargeModel("Broadband", covers[7]);
        rechargelist.add(a);

        a = new RechargeModel("Money Transfer", covers[8]);
        rechargelist.add(a);

        a = new RechargeModel("Wallet", covers[8]);
        rechargelist.add(a);

        a = new RechargeModel("Recharge History", covers[9]);
        rechargelist.add(a);

        a = new RechargeModel("Deal with us", covers[10]);
        rechargelist.add(a);


        adapter_recharge = new Adapter_recharge(getActivity(), rechargelist, false);
        rvRecharge.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        rvRecharge.setAdapter(adapter_recharge);


        //Preferences
        pref = new Preferences(getActivity());

        //loader
        loader = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


    }

    //==================================================Load Fragment=============================================//
    public void loadFragment(Fragment fragment) {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(
//                R.anim.card_flip_right_in, R.anim.card_flip_right_out,
//                R.anim.card_flip_left_in, R.anim.card_flip_left_out);
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }

    //==========================================API===================================================//
    public void GetDasboardAPI() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MemberId", pref.get(AppSettings.UserId));
            jsonObject.put("SessionId", pref.get(AppSettings.UserId));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl + GetDashboardData)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //Log.v("jyiujojjujujujujuujujuj",response.toString());
                            String Message = response.getString("Message");

                            if (Message.equals("Success")) {
                                JSONArray jsonArray = response.getJSONArray("DashboardData");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    loader.cancel();
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                                    String totalmember = jsonObject.getString("allmembers");
                                    String activemember = jsonObject.getString("Activemembers");
                                    String totaldirectmem = jsonObject.getString("totaldirectmem");
                                    String totalleftid = jsonObject.getString("TotalLeftId");
                                    String totalrighttid = jsonObject.getString("totalrightmem");


                                    String TotalPayout = jsonObject.getString("TotalPayout");
                                    String CurrentClosingPayout = jsonObject.getString("CurrentClosingPayout");


                                    String sponsorship_income = jsonObject.getString("roiincome");
                                    String businesspoint_matching_income = jsonObject.getString("binaryincome");
                                    String leader_performance_income = jsonObject.getString("LeaderPerformanceIncome");
                                    String reward_income = jsonObject.getString("Rewards");
                                    String context_income = jsonObject.getString("ContextIncome");
                                    String turnOverProfitIncome = jsonObject.getString("TurnOverProfitIncome");
                                    String crowndirectorship = jsonObject.getString("crowndirectorship");
                                    String directsponsorincome = jsonObject.getString("directincome");


                                    String totalleftbus = jsonObject.getString("totalleftbus");
                                    String totalrightbus = jsonObject.getString("totalrightbus");
                                    String totalCFLeftBP = jsonObject.getString("TotalCFLeftBP");
                                    String totalCFRightBP = jsonObject.getString("TotalCFRightBP");


                                    tvTotalMember.setText(totalmember);
                                    tvTotalActiveMember.setText(activemember);
                                    tvActiveDirectMember.setText(totaldirectmem);
                                    tvTotalLeftID.setText(totalleftid);
                                    tvTotalRightID.setText(totalrighttid);


                                    tvSponsorshipIncome.setText(sponsorship_income);
                                    tvBusinessPointIncome.setText(businesspoint_matching_income);
                                    tvLeaderPerformanceIncome.setText(leader_performance_income);
                                    tvRewardIncome.setText(reward_income);
                                    tvDirectSponsorIncome.setText(directsponsorincome);
                                    tvContextAmount.setText(context_income);
                                    tvCrownDirectorshipIncome.setText(crowndirectorship);
                                    tvTurnoverProfitIncome.setText(turnOverProfitIncome);


                                    tvTotalPayout.setText(TotalPayout);
                                    tvCurrentPayout.setText(CurrentClosingPayout);


                                    tvTotalBusinessLeft.setText(totalleftbus);
                                    tvTotalBusinessRight.setText(totalrightbus);
                                    tvCFLeftBp.setText(totalCFLeftBP);
                                    tvCFRightBp.setText(totalCFRightBP);


                                }

                              /*  JSONArray jsonArray1=response.getJSONArray("DashboardPinData");

                                for(int j=0;j<jsonArray1.length();j++)
                                {

                                    JSONObject jsonObject=jsonArray1.getJSONObject(0);
                                    String Todayusedpin=jsonObject.getString("Todayusedpin")  ;
                                    String TotalPin=jsonObject.getString("TotalPin")  ;
                                    String TotalTransfrUPin=jsonObject.getString("TotalTransfrUPin")  ;
                                    String totalleftmem=jsonObject.getString("totalleftmem")  ;
                                    String totalpaid=jsonObject.getString("totalpaid")  ;
                                    String totalrightmem=jsonObject.getString("totalrightmem")  ;
                                    String totalunusedpin=jsonObject.getString("totalunusedpin")  ;
                                    String totalusedpin=jsonObject.getString("totalusedpin")  ;
                                    String mypackage=jsonObject.getString("mypackage")  ;


                                    tvTotalPin.setText(TotalPin);
                                    tvTodayUsedPin.setText(Todayusedpin);
                                    tvTotalUsedPin.setText(totalusedpin);
                                    tvTotalTransferUsedPin.setText(TotalTransfrUPin);
                                    tvTotalUnusedPin.setText(totalunusedpin);




                                }
*/


                                JSONArray jsonArrayNews = response.getJSONArray("NewsList");

                                for (int k = 0; k < jsonArrayNews.length(); k++) {
                                    JSONObject jsonObject = jsonArrayNews.getJSONObject(k);
                                    String News = jsonObject.getString("News");
                                    newslist.add(News);
                                }
                                if (newslist.size() == 0) {
                                    tvNoData.setVisibility(View.VISIBLE);
                                    recyclerviewNews.setVisibility(View.GONE);
                                } else {
                                    tvNoData.setVisibility(View.GONE);
                                    recyclerviewNews.setVisibility(View.VISIBLE);
                                }

                                NewsAdapter newsAdapter = new NewsAdapter(getActivity(), newslist);
                                recyclerviewNews.setAdapter(newsAdapter);


                                final Handler handler = new Handler();
                                final Runnable runnable = new Runnable() {

                                    @Override
                                    public void run() {
                                        if (count == newslist.size() - 1) {
                                            count = 0;
                                            recyclerviewNews.scrollToPosition(++count);
                                            //  handler.postDelayed(this,speedScroll);
                                        } else {
                                            recyclerviewNews.scrollToPosition(++count);
                                            // handler.postDelayed(this,speedScroll);
                                        }


                                    }
                                };


                                timer = new Timer(); // This will create a new Thread
                                timer.schedule(new TimerTask() { // task to be scheduled
                                    @Override
                                    public void run() {
                                        handler.post(runnable);
                                    }
                                }, DELAY_MS, PERIOD_MS);


                                JSONArray jsonArrayReward = response.getJSONArray("RewardList");

                                for (int j = 0; j < jsonArrayReward.length(); j++) {
                                    JSONObject jsonObject = jsonArrayReward.getJSONObject(j);
                                    HashMap<String, String> map = new HashMap<>();

                                    map.put("Business", jsonObject.getString("Business"));
                                    map.put("QualifiedDate", jsonObject.getString("QualifiedDate"));
                                    map.put("Rewards", jsonObject.getString("Rewards"));
                                    map.put("Status", jsonObject.getString("Status"));
                                    map.put("Post", jsonObject.getString("Post"));
                                    map.put("RewardAmount", jsonObject.getString("RewardAmount"));
                                    rewardlist.add(map);

                                    //Log.v("jbjbjbjbjbbfjhfh", String.valueOf(rewardlist.size()));
                                    //Log.v("jbjbjbjbjbbfjhfh",jsonObject.toString());
                                }


                                if (rewardlist.size() == 0) {
                                    tvNoDataFound.setVisibility(View.VISIBLE);
                                    recyclerviewReward.setVisibility(View.GONE);
                                } else {
                                    tvNoDataFound.setVisibility(View.GONE);
                                    recyclerviewReward.setVisibility(View.VISIBLE);
                                }

                                RewardAdapter rewardAdapter = new RewardAdapter(getActivity(), rewardlist);
                                recyclerviewReward.setAdapter(rewardAdapter);


                                loader.cancel();
                                // loadFragment(new TeamFragment());
                            } else {
                                loader.cancel();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Log.v("gjfihhgghghg",e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        //Log.v("gjfihhgghghg",error.toString());
                        loader.cancel();
                    }
                });
    }

    public void GetProfileAPI() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MemberId", pref.get(AppSettings.UserId));
            jsonObject.put("SessionId", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        AndroidNetworking.post(BaseUrl + GetProfile)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //Log.v("jyiujojjujujujujuujujuj",response.toString());
                            String Message = response.getString("Message");

                            if (Message.equals("Success")) {
                                JSONArray jsonArray = response.getJSONArray("ProfileList");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    String AadharNo = jsonObject.getString("AadharNo");
                                    String BankBranch = jsonObject.getString("BankBranch");
                                    String BankIFSC = jsonObject.getString("BankIFSC");
                                    String BankName = jsonObject.getString("BankName");
                                    String BankAccountNumber = jsonObject.getString("BankAcNo");
                                    String MobileNo = jsonObject.getString("MobileNo");
                                    String Name = jsonObject.getString("Name");
                                    String PanNo = jsonObject.getString("PanNo");
                                    String PayeeName = jsonObject.getString("PayeeName");


                                    pref.set(AppSettings.PayeeName, PayeeName);
                                    pref.set(AppSettings.Bankname, BankName);
                                    pref.set(AppSettings.BankIfsc, BankIFSC);
                                    pref.set(AppSettings.BankBranch, BankBranch);
                                    pref.set(AppSettings.BankAccountNumber, BankAccountNumber);
                                    pref.set(AppSettings.PanNumber, PanNo);
                                    pref.set(AppSettings.UserMobile, MobileNo);
                                    pref.set(AppSettings.UserName, Name);
                                    pref.set(AppSettings.UserPincode, jsonObject.getString("ZipCode"));
                                    pref.commit();

                                    DashBoardActivity.tvUserName.setText(pref.get(AppSettings.UserName));
                                    DashBoardActivity.tvUserMobile.setText(pref.get(AppSettings.UserMobile));

                                }

                                loader.cancel();

                            } else {
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