package com.tisanehealth.Fragment.Team;

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
import android.widget.ImageView;
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

import java.util.ArrayList;

import static com.tisanehealth.Activity.DashBoardActivity.ivLogo;
import static com.tisanehealth.Activity.DashBoardActivity.rlHeader;
import static com.tisanehealth.Activity.DashBoardActivity.tvHeader;
import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.GetDirectTeam;

public class DirectTeamFragment extends Fragment {
    //RecyclerView
    RecyclerView recyclerViewLeft,recyclerViewRight;
    //TeamAdapter
    TeamAdapter teamAdapter;
    //Preferences
    Preferences pref;
    //CustomLoader
    CustomLoader loader;
    //ArrayList
    ArrayList<TeamModelClass> leftteamlist=new ArrayList<>();
    ArrayList<TeamModelClass> rightteamlist=new ArrayList<>();
    //ModelClass
    TeamModelClass teamModelClass;

    TextView tvNoDataLeft,tvNoDataRight;

    ImageView ivNoDatafound;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.direct_teamlist_fragment, container, false);
        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("Direct Team");
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



        //Preferences
        pref                = new Preferences(getActivity());

        //loader
        loader              = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);




        tvNoDataLeft       =view.findViewById(R.id.tvNoDataLeft);
        tvNoDataRight       =view.findViewById(R.id.tvNoDataRight);


        recyclerViewLeft=view.findViewById(R.id.recyclerviewLeft);
        recyclerViewRight=view.findViewById(R.id.recyclerviewRight);


        recyclerViewLeft.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewRight.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);
            GetDirectTeamAPI();

        } else {
            EasyToast.error(getActivity(), "No Internet Connnection");
        }

        return view;
    }


    //Load Fragment
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
    public void GetDirectTeamAPI()
    {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("DSDID",pref.get(AppSettings.UserId));
            jsonObject.put("Action","");
            jsonObject.put("FromDate","");
            jsonObject.put("ToDate","");



        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+GetDirectTeam)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            leftteamlist.clear();
                            rightteamlist.clear();

                            String Message=   response.getString("Message");
                            if (Message.equals("Success"))
                            {
                                JSONArray jsonArray=response.getJSONArray("DirectTeamBussRec");

                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    String ActiveDate=jsonObject.getString("Topupdate")  ;
                                    String MemberId=jsonObject.getString("DSDId")  ;
                                    String Name=jsonObject.getString("DSDName")  ;
                                    String Package=jsonObject.getString("Bussiness")  ;
                                    String Leg=jsonObject.getString("Leg")  ;
                                    String RegDate=jsonObject.getString("RegDate")  ;
                                    String SponserId=jsonObject.getString("SponsorId")  ;
                                    String Status=jsonObject.getString("Status")  ;
                                    teamModelClass=new TeamModelClass(MemberId,Name,Package,RegDate,ActiveDate,Status,SponserId);

                                   if (Leg.equals("L"))
                                   {
                                       leftteamlist.add(teamModelClass);
                                   }
                                   else
                                   {
                                       rightteamlist.add(teamModelClass);
                                   }

                                }

                                 if (rightteamlist.isEmpty())
                                {

                                    recyclerViewRight.setVisibility(View.GONE);
                                    tvNoDataRight.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    recyclerViewRight.setVisibility(View.VISIBLE);
                                    tvNoDataRight.setVisibility(View.GONE);
                                    teamAdapter=new TeamAdapter(getActivity(),rightteamlist);
                                    recyclerViewRight.setAdapter(teamAdapter);
                                }

                                if (leftteamlist.isEmpty())
                                {

                                    recyclerViewLeft.setVisibility(View.GONE);
                                    tvNoDataLeft.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    recyclerViewLeft.setVisibility(View.VISIBLE);
                                    tvNoDataLeft.setVisibility(View.GONE);
                                    teamAdapter=new TeamAdapter(getActivity(),leftteamlist);
                                    recyclerViewLeft.setAdapter(teamAdapter);
                                }


                                loader.cancel();

                            }
                            else
                            {
                                tvNoDataLeft.setVisibility(View.VISIBLE);
                                tvNoDataRight.setVisibility(View.VISIBLE);
                                recyclerViewLeft.setVisibility(View.GONE);
                                recyclerViewRight.setVisibility(View.GONE);
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