package com.tisanehealth.Fragment.PinManagement;

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

public class PinManagement extends Fragment implements View.OnClickListener{
    LinearLayout llPinBank,llPinTransfer,llTransferLog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.repurchase_fragment, container, false);
        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("Pin Management");
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

        llPinBank=view.findViewById(R.id.llPinBank);
        llPinTransfer=view.findViewById(R.id.llPinTransfer);
        llTransferLog=view.findViewById(R.id.llTransferLog);


        llPinBank.setOnClickListener(this);
        llPinTransfer.setOnClickListener(this);
        llTransferLog.setOnClickListener(this);

        return view;


    }

    public void loadFragment(Fragment fragment) {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.card_flip_right_in, R.anim.card_flip_right_out,
                R.anim.card_flip_left_in, R.anim.card_flip_left_out);
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.llPinBank:
                loadFragment(new PinBankFragment());
                break;
            case R.id.llPinTransfer:
                loadFragment(new PinTransferFragment());
                break;
            case R.id.llTransferLog:
                loadFragment(new PinTransferHistoryFragment());
                break;
        }
    }
}