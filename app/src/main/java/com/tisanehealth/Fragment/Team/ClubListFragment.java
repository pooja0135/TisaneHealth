package com.tisanehealth.Fragment.Team;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import static com.tisanehealth.Helper.AppUrls.GetClubMember;

public class ClubListFragment extends Fragment {

    //RecyclerView
    RecyclerView recyclerView;
    //TeamAdapter
    ClubAdapter teamAdapter;
    //Preferences
    Preferences pref;
    //CustomLoader
    CustomLoader loader;
    //ArrayList
    ArrayList<HashMap<String,String>> teamlist=new ArrayList<>();

    EditText etSearch;
    RelativeLayout rlSearch;
    ImageView ivNoDatafound;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.club_list_fragment, container, false);
        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("");
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


        etSearch=view.findViewById(R.id.etSearch);
        rlSearch=view.findViewById(R.id.rlSearch);

        ivNoDatafound=view.findViewById(R.id.ivNoDatafound);


        recyclerView=view.findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        if (getArguments().getString("type").equals("1"))
        {
            tvHeader.setText("Diamond Club");
        }

        else if (getArguments().getString("type").equals("2"))
        {
            tvHeader.setText("Platinum Club");
        }
        else if (getArguments().getString("type").equals("3"))
        {
            tvHeader.setText("Gold Club");
        }
        else if (getArguments().getString("type").equals("4"))
        {
            tvHeader.setText("Silver Club");
        }

        else if (getArguments().getString("type").equals("5"))
        {
            tvHeader.setText("Quick Silver Club");
        }



        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);
            GetClubTeamAPI(getArguments().getString("type"),pref.get(AppSettings.UserId));

        } else {
            EasyToast.error(getActivity(), "No Internet Connnection");
        }


        rlSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etSearch.getText().toString().isEmpty())
                {
                    EasyToast.warning(getActivity(),"Enter DSD ID for searching.");
                }
                else
                {
                    if (Utils.isNetworkConnectedMainThred(getActivity())) {
                        loader.show();
                        loader.setCanceledOnTouchOutside(true);
                        loader.setCancelable(false);

                        GetClubTeamAPI(getArguments().getString("type"),etSearch.getText().toString());

                    } else {
                        EasyToast.error(getActivity(), "No Internet Connnection");
                    }
                }


            }
        });




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
    public void GetClubTeamAPI(String action,String DsdId)
    {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("DSDID",DsdId);
            jsonObject.put("Action",action);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+GetClubMember)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.v("jyiujojjujujujujuujujuj",response.toString());
                            String Message=   response.getString("Message");

                            teamlist.clear();
                            if (Message.equals("Success"))
                            {


                                JSONArray jsonArray=response.getJSONArray("ClubMemberRec");

                                for(int i=0;i<jsonArray.length();i++)
                                {

                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    HashMap<String,String>map=new HashMap<>();
                                    map.put("ActivationDate",jsonObject.getString("ActivationDate"));
                                    map.put("CurrentBussiness",jsonObject.getString("CurrentBussiness"));
                                    map.put("CurrentUnit",jsonObject.getString("CurrentUnit"));
                                    map.put("DSDId",jsonObject.getString("DSDId"));
                                    map.put("DSDName",jsonObject.getString("DSDName"));
                                    map.put("DiamondIncome",jsonObject.getString("DiamondIncome"));
                                    map.put("DirectMember",jsonObject.getString("DirectMember"));
                                    map.put("PlatinumIncome",jsonObject.getString("PlatinumIncome"));
                                    map.put("SelfBussiness",jsonObject.getString("SelfBussiness"));
                                    map.put("TeamBussiness",jsonObject.getString("TeamBussiness"));
                                    map.put("sponsorID",jsonObject.getString("sponsorID"));

                                    teamlist.add(map);
                                }

                                teamAdapter=new ClubAdapter(getActivity(),teamlist);
                                recyclerView.setAdapter(teamAdapter);
                                loader.cancel();

                            }
                            else
                            {
                                loader.cancel();
                                ivNoDatafound.setVisibility(View.VISIBLE);
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



    public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.MyViewHolder> {

        private Context mContext;
        ArrayList<HashMap<String,String>>teamlist;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvDsdId,tvDsdName,tvSelfBusiness,tvTeamBusiness,tvCurrentbusiness,tvDiamondIncome,tvPlatinumIncome,tvCurrentUnit,tvActivationDate,tvDirectMember;

            LinearLayout llDiamondIncome,llPlatinum;

            public MyViewHolder(View view) {
                super(view);

                tvDsdId=view.findViewById(R.id.tvDsdId);
                tvDsdName=view.findViewById(R.id.tvDsdname);
                tvSelfBusiness=view.findViewById(R.id.tvSelfBusiness);
                tvTeamBusiness=view.findViewById(R.id.tvTeamBusiness);
                tvCurrentbusiness=view.findViewById(R.id.tvCurrentbusiness);
                tvDiamondIncome=view.findViewById(R.id.tvDiamondIncome);
                tvPlatinumIncome=view.findViewById(R.id.tvPlatinumIncome);
                tvCurrentUnit=view.findViewById(R.id.tvCurrentUnit);
                tvActivationDate=view.findViewById(R.id.tvActivationDate);
                tvDirectMember=view.findViewById(R.id.tvDirectMember);


                llDiamondIncome=view.findViewById(R.id.llDiamondIncome);
                llPlatinum=view.findViewById(R.id.llPlatinum);





            }
        }


        public ClubAdapter(Context mContext, ArrayList<HashMap<String,String>>teamlist) {
            this.mContext = mContext;
            this.teamlist=teamlist;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.inflate_club_list, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {

            holder.tvActivationDate.setText(parseDateToddMMyyyy(teamlist.get(position).get("ActivationDate")));
            holder.tvCurrentbusiness.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(teamlist.get(position).get("CurrentBussiness")))));
            holder.tvCurrentUnit.setText(teamlist.get(position).get("CurrentUnit"));
            holder.tvDsdId.setText(teamlist.get(position).get("DSDId"));
            holder.tvDsdName.setText(teamlist.get(position).get("DSDName"));
            holder.tvDiamondIncome.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(teamlist.get(position).get("DiamondIncome")))));
            holder.tvPlatinumIncome.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(teamlist.get(position).get("PlatinumIncome")))));
            holder.tvDirectMember.setText(teamlist.get(position).get("DirectMember"));
            holder.tvSelfBusiness.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(teamlist.get(position).get("SelfBussiness")))));
            holder.tvTeamBusiness.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(teamlist.get(position).get("TeamBussiness")))));



            if (getArguments().getString("type").equals("1"))
            {
               holder.llPlatinum.setVisibility(View.GONE);
            }
            else if (getArguments().getString("type").equals("2"))
            {
                holder.llDiamondIncome.setVisibility(View.GONE);
            }
            else
            {
                holder.llPlatinum.setVisibility(View.GONE);
                holder.llDiamondIncome.setVisibility(View.GONE);
            }




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
            DecimalFormat twoDForm = new DecimalFormat("#.##");
            return Double.valueOf(twoDForm.format(d));
        }


    }





}
