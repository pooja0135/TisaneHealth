package com.tisanehealth.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.medialablk.easytoast.EasyToast;
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

public class DashBoardGuestFragment extends Fragment {



    ArrayList<String>newslist=new ArrayList<>();
    ArrayList<HashMap<String,String>>rewardlist=new ArrayList<>();

    int count = 0;
    final long DELAY_MS = 600;
    final long PERIOD_MS = 2000;
    Timer timer;

    CustomPagerAdapter customPagerAdapter;
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
            R.drawable.ic_broadband
    }   ;
    ArrayList<RechargeModel> rechargelist=new ArrayList<>();

    Adapter_recharge adapter_recharge;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dashboard_guest_fragment, container, false);
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


        return view;


    }



    public void initialise(View view)
    {
        rvRecharge               =view.findViewById(R.id.rvRecharge);

        RechargeModel a = new RechargeModel( "Mobile Prepaid", covers[0]);
        rechargelist.add(a);

        a = new RechargeModel( "Mobile Postpaid", covers[1]);
        rechargelist.add(a);

        a = new RechargeModel( "Electricity", covers[2]);
        rechargelist.add(a);

        a = new RechargeModel( "DTH", covers[3]);
        rechargelist.add(a);

        a = new RechargeModel( "Landline", covers[4]);
        rechargelist.add(a);

        a = new RechargeModel( "DataCard", covers[5]);
        rechargelist.add(a);

        a = new RechargeModel( "Insurance", covers[6]);
        rechargelist.add(a);

        a = new RechargeModel( "Broadband", covers[7]);
        rechargelist.add(a);




        adapter_recharge=new Adapter_recharge(getActivity(),rechargelist);
        rvRecharge.setLayoutManager(new GridLayoutManager(getActivity(),2));
        rvRecharge.setAdapter(adapter_recharge);



        //Preferences
        pref                = new Preferences(getActivity());

        //loader
        loader              = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


    }
    //==================================================Load Fragment=============================================//
    public void loadFragment(Fragment fragment)
    {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.card_flip_right_in, R.anim.card_flip_right_out,
                R.anim.card_flip_left_in, R.anim.card_flip_left_out);
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }





}