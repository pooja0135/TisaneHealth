package com.tisanehealth.Fragment.Bank;

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

public class BankManagement extends Fragment implements View.OnClickListener{
    LinearLayout llMyBank,llFundRequest,llPinPurchase,llTransferWithdrawal;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bank_management_fragment, container, false);
        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("Bank Management");
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

        llMyBank=view.findViewById(R.id.llMyBank);
        llFundRequest=view.findViewById(R.id.llFundRequest);
        llPinPurchase=view.findViewById(R.id.llPinPurchase);
        llTransferWithdrawal=view.findViewById(R.id.llTransferWithdrawal);


        llMyBank.setOnClickListener(this);
        llFundRequest.setOnClickListener(this);
        llPinPurchase.setOnClickListener(this);
        llTransferWithdrawal.setOnClickListener(this);

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
            case R.id.llMyBank:
                loadFragment(new MyBankFragment());
                break;
            case R.id.llFundRequest:
                loadFragment(new FundTermsFragment());
                break;
            case R.id.llPinPurchase:
                loadFragment(new PurchasePinFragment());
                break;
            case R.id.llTransferWithdrawal:
                loadFragment(new TransferWithdrawalFragment());
                break;
        }
    }




}