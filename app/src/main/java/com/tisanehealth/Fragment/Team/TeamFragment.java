package com.tisanehealth.Fragment.Team;

import android.content.Intent;
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

public class TeamFragment extends Fragment implements View.OnClickListener {

   LinearLayout llDirectTeam,llTeam,llTree;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.team_fragment, container, false);
        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("Team");
        ivLogo.setVisibility(View.GONE);
        tvHeader.setVisibility(View.VISIBLE);

        llDirectTeam      =view.findViewById(R.id.llDirectTeam);
        llTeam            =view.findViewById(R.id.llTeam);
        llTree            =view.findViewById(R.id.llTree);

        llTeam.setOnClickListener(this);
        llDirectTeam.setOnClickListener(this);
        llTree.setOnClickListener(this);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        loadFragment(new DashBoardFragment(),"");
                        return true;
                    }
                }
                return false;
            }
        });
        return view;


    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.llTeam:
                loadFragment(new MyTeamFragment(),"");
                break;
            case R.id.llDirectTeam:
                loadFragment(new DirectTeamFragment(),"");
                break;
                case R.id.llTree:
               // loadFragment(new TreeviewFragment(),"");
                startActivity(new Intent(getActivity(),BuchheimWalkerActivity.class));
                break;
        }
    }


    public void loadFragment(Fragment fragment,String type) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);

        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.card_flip_right_in, R.anim.card_flip_right_out,
                R.anim.card_flip_left_in, R.anim.card_flip_left_out);
        transaction.replace(R.id.framelayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}