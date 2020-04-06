package com.tisanehealth.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tisanehealth.Activity.DashBoardActivity;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.R;
import com.tisanehealth.recharge_pay_bill.AddMoneyToWalletActivity;
import com.tisanehealth.recharge_pay_bill.RechargeHistoryActivity;
import com.tisanehealth.recharge_pay_bill.broadband.BroadbandListActivity;
import com.tisanehealth.recharge_pay_bill.dth_recharge.DTHRechargeActivity;
import com.tisanehealth.recharge_pay_bill.electricitybill.ElectricityBillActivity;
import com.tisanehealth.recharge_pay_bill.gas.GasBillActivity;
import com.tisanehealth.recharge_pay_bill.insurance.InsuranceListActivity;
import com.tisanehealth.recharge_pay_bill.landline.LandlineListActivity;
import com.tisanehealth.recharge_pay_bill.metro.MetroBillActivity;
import com.tisanehealth.recharge_pay_bill.mobile_recharge.ContactActivity;
import com.tisanehealth.recharge_pay_bill.money_transfer.MoneyTransferActivity;
import com.tisanehealth.recharge_pay_bill.waterbill.WaterBillActivity;

import static com.tisanehealth.Activity.DashBoardActivity.ivLogo;
import static com.tisanehealth.Activity.DashBoardActivity.rlHeader;
import static com.tisanehealth.Activity.DashBoardActivity.tvHeader;

public class DashBoardGuestFragment extends Fragment {

    Preferences pref;

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
        view.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().finishAffinity();
                    return true;
                }
            }
            return false;
        });


        initialise(view);
        //Preferences
        pref = new Preferences(getActivity());


        return view;


    }


    public void initialise(View view) {
        Context mContext = getContext();
        view.findViewById(R.id.llWallet).setOnClickListener(v -> {
            startActivity(new Intent(mContext, AddMoneyToWalletActivity.class));
        });

        view.findViewById(R.id.llRechargeHistory).setOnClickListener(v -> {
            startActivity(new Intent(mContext, RechargeHistoryActivity.class));
        });

        view.findViewById(R.id.llMobPrepaid).setOnClickListener(v -> {
            DashBoardActivity.mobile_type = "Prepaid";
            mContext.startActivity(new Intent(mContext, ContactActivity.class));
        });
        view.findViewById(R.id.llMobPost).setOnClickListener(v -> {
            DashBoardActivity.mobile_type = "Postpaid";
            mContext.startActivity(new Intent(mContext, ContactActivity.class));
        });
        view.findViewById(R.id.llDth).setOnClickListener(v -> {
            startActivity(new Intent(mContext, DTHRechargeActivity.class));
        });
        view.findViewById(R.id.llDataCard).setOnClickListener(v -> {
            DashBoardActivity.mobile_type = "Datacard";
            mContext.startActivity(new Intent(mContext, ContactActivity.class));
        });

        view.findViewById(R.id.llElectricity).setOnClickListener(v -> {
            startActivity(new Intent(mContext, ElectricityBillActivity.class));
        });

        view.findViewById(R.id.llLandline).setOnClickListener(v -> {
            startActivity(new Intent(mContext, LandlineListActivity.class));
        });
        view.findViewById(R.id.llBroadband).setOnClickListener(v -> {
            startActivity(new Intent(mContext, BroadbandListActivity.class));
        });
        view.findViewById(R.id.llWater).setOnClickListener(v -> {
            startActivity(new Intent(mContext, WaterBillActivity.class));
        });
        view.findViewById(R.id.llInsurance).setOnClickListener(v -> {
            startActivity(new Intent(mContext, InsuranceListActivity.class));
        });

        view.findViewById(R.id.llGas).setOnClickListener(v -> {
            startActivity(new Intent(mContext, GasBillActivity.class));
        });

        view.findViewById(R.id.llMetro).setOnClickListener(v -> {
            startActivity(new Intent(mContext, MetroBillActivity.class));
        });
        view.findViewById(R.id.llMoneyTrans).setOnClickListener(v -> {
            if (!pref.get(AppSettings.BankAccountNumber).isEmpty() && !pref.get(AppSettings.Bankname).isEmpty() && !pref.get(AppSettings.PayeeName).isEmpty()
                    && !pref.get(AppSettings.BankIfsc).isEmpty() && !pref.get(AppSettings.BankAccountNumber).isEmpty()) {
                mContext.startActivity(new Intent(mContext, MoneyTransferActivity.class));
            } else {
                Toast.makeText(mContext, "Please update your bank details.", Toast.LENGTH_SHORT).show();
            }
        });
        view.findViewById(R.id.llFlipkart).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.flipkart.com/"));
            mContext.startActivity(browserIntent);
        });
        view.findViewById(R.id.llZomato).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.zomato.com/"));
            mContext.startActivity(browserIntent);
        });

        view.findViewById(R.id.llSwiggy).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.swiggy.com/"));
            mContext.startActivity(browserIntent);
        });

        view.findViewById(R.id.llAmazon).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.amazon.in/"));
            mContext.startActivity(browserIntent);
        });
        view.findViewById(R.id.llSnapdeal).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.snapdeal.com/"));
            mContext.startActivity(browserIntent);
        });
        view.findViewById(R.id.llMyntra).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.myntra.com/"));
            mContext.startActivity(browserIntent);
        });
        view.findViewById(R.id.llOyo).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.oyorooms.com/"));
            mContext.startActivity(browserIntent);
        });

        view.findViewById(R.id.llGoIbibo).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.goibibo.com/"));
            mContext.startActivity(browserIntent);
        });

        view.findViewById(R.id.llMakeMyTrip).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.makemytrip.com/"));
            mContext.startActivity(browserIntent);
        });
        view.findViewById(R.id.Trivago).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.trivago.in/"));
            mContext.startActivity(browserIntent);
        });
        view.findViewById(R.id.Trivago).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.trivago.in/"));
            mContext.startActivity(browserIntent);
        });
        view.findViewById(R.id.llEasyMyTrip).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.easemytrip.com/"));
            mContext.startActivity(browserIntent);
        });

    }

    //==================================================Load Fragment=============================================//
    public void loadFragment(Fragment fragment) {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }


}