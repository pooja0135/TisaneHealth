package com.tisanehealth.Fragment.PayoutReport;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tisanehealth.Fragment.DashBoardFragment;
import com.tisanehealth.R;

import static com.tisanehealth.Activity.DashBoardActivity.ivLogo;
import static com.tisanehealth.Activity.DashBoardActivity.rlHeader;
import static com.tisanehealth.Activity.DashBoardActivity.tvHeader;

public class PayoutReportFragment extends Fragment implements View.OnClickListener{

    LinearLayout llSponsorIncome,llBusinessPrint,llTurnoverReport,llLeaderReport,llRewardReport,llCrownReport,llTotalIncome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.purchase_fragment, container, false);
        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("Payout Report");
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


        llSponsorIncome=view.findViewById(R.id.llSponsorIncome);
        llBusinessPrint=view.findViewById(R.id.llBusinessPrint);
        llTurnoverReport=view.findViewById(R.id.llTurnoverReport);
        llRewardReport=view.findViewById(R.id.llRewardReport);
        llLeaderReport=view.findViewById(R.id.llLeaderReport);
        llTotalIncome=view.findViewById(R.id.llTotalIncome);
        llCrownReport=view.findViewById(R.id.llCrownReport);



        llSponsorIncome.setOnClickListener(this);
        llBusinessPrint.setOnClickListener(this);
        llTurnoverReport.setOnClickListener(this);
        llRewardReport.setOnClickListener(this);
        llLeaderReport.setOnClickListener(this);
        llTotalIncome.setOnClickListener(this);
        llCrownReport.setOnClickListener(this);

        return view;


    }

    public void loadFragment(Fragment fragment) {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(
//                R.anim.card_flip_right_in, R.anim.card_flip_right_out,
//                R.anim.card_flip_left_in, R.anim.card_flip_left_out);
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.llSponsorIncome:
                loadFragment(new DirectIncomeFragment());
                break;
            case R.id.llBusinessPrint:
                loadFragment(new MatchingIncomeFragment());
                break;
            case R.id.llTurnoverReport:
                loadFragment(new RoiIncomeHistoryFragment());
                break;
            case R.id.llRewardReport:
                loadFragment(new RewardReportFragment());
                break;
            case R.id.llLeaderReport:
                loadFragment(new LeaderIncomeFragment());
                break;
            case R.id.llTotalIncome:
                loadFragment(new BankWalletSummaryFragment());
                break;
            case R.id.llCrownReport:
                //   loadFragment(new BankWalletSummaryFragment());
                break;
        }

    }
}