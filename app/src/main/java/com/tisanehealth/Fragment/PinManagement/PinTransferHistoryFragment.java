package com.tisanehealth.Fragment.PinManagement;

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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tisanehealth.Adapter.pin.PinTransferHistoryAdapter;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.R;
import com.medialablk.easytoast.EasyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.tisanehealth.Activity.DashBoardActivity.ivLogo;
import static com.tisanehealth.Activity.DashBoardActivity.rlHeader;
import static com.tisanehealth.Activity.DashBoardActivity.tvHeader;
import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.GetTrasnferPinLog;

public class PinTransferHistoryFragment extends Fragment{
    Preferences pref;
    CustomLoader loader;
    ArrayList<HashMap<String,String>>transferlist=new ArrayList<>();
    RecyclerView rvPinHistory;
    ImageView ivNoDatafound;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pin_transfer_history_fragment, container, false);
        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("Pin Transfer Log");
        ivLogo.setVisibility(View.GONE);
        tvHeader.setVisibility(View.VISIBLE);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        loadFragment(new PinManagement());
                        return true;
                    }
                }
                return false;
            }
        });


        pref             =new Preferences(getActivity());
        //loader
        loader           = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        ivNoDatafound     =view.findViewById(R.id.ivNoDatafound);

        rvPinHistory     =view.findViewById(R.id.rvPinHistory);

        rvPinHistory.setLayoutManager(new LinearLayoutManager(getActivity()));


        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);
            PinTransferHistoryApi();

        } else {
            EasyToast.error(getActivity(), "No Internet Connnection");
        }

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



    //==========================================API===================================================//
    public void PinTransferHistoryApi()
    {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("UserId",pref.get(AppSettings.UserId));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+GetTrasnferPinLog)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String message=   response.getString("Message");
                            if (message.equals("Success"))
                            {
                                JSONArray jsonArray=response.getJSONArray("PinTransferLog");
                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    HashMap<String,String>map=new HashMap<>();
                                    map.put("Date",jsonObject.getString("Date"));
                                    map.put("Particulars",jsonObject.getString("Particulars"));
                                    map.put("Received",jsonObject.getString("Received"));
                                    map.put("Send",jsonObject.getString("Send"));
                                    transferlist.add(map);
                                }


                                PinTransferHistoryAdapter pinTransferHistoryAdapter=new PinTransferHistoryAdapter(getActivity(),transferlist);
                                rvPinHistory.setAdapter(pinTransferHistoryAdapter);
                                loader.dismiss();

                                ivNoDatafound.setVisibility(View.GONE);
                                rvPinHistory.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                loader.dismiss();
                                ivNoDatafound.setVisibility(View.VISIBLE);
                                rvPinHistory.setVisibility(View.GONE);
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