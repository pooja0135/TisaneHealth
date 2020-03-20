package com.tisanehealth.Fragment.Profile;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tisanehealth.Fragment.SettingFragment;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.R;
import com.medialablk.easytoast.EasyToast;

import org.json.JSONException;
import org.json.JSONObject;

import static com.tisanehealth.Activity.DashBoardActivity.ivLogo;
import static com.tisanehealth.Activity.DashBoardActivity.rlHeader;
import static com.tisanehealth.Activity.DashBoardActivity.tvHeader;
import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.ChangePasswordAPi;

public class ChangeTranscationPassword  extends Fragment {
    //Edittext
    EditText etOldPassword,etNewPassword,etConfirmPassword;
    //Button
    Button btnUpdate;
    //Preferences
    Preferences pref;
    //CustomLoader
    CustomLoader loader;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.change_password, container, false);
        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("Change Transaction Password");
        ivLogo.setVisibility(View.GONE);
        tvHeader.setVisibility(View.VISIBLE);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        loadFragment(new SettingFragment());
                        return true;
                    }
                }
                return false;
            }
        });

        //Edittext
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        etOldPassword = view.findViewById(R.id.etOldPassword);
        etNewPassword = view.findViewById(R.id.etNewPassword);

        //Button
        btnUpdate = view.findViewById(R.id.btnUpdate);

        //Preferences
        pref                = new Preferences(getActivity());

        //loader
        loader              = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);



        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etOldPassword.getText().toString().isEmpty())
                {
                    EasyToast.warning(getActivity(),"Please enter your old password");
                }

                else if (etNewPassword.getText().toString().isEmpty())
                {
                    EasyToast.warning(getActivity(),"Please enter your new password");
                }

                else if (etConfirmPassword.getText().toString().isEmpty())
                {
                    EasyToast.warning(getActivity(),"Please enter your confirm password");
                }
                else  if (!etNewPassword.getText().toString().equals(etConfirmPassword.getText().toString()))
                {
                    EasyToast.warning(getActivity(),"Confirm password is not match with new password.");
                }

                else
                {
                    if (Utils.isNetworkConnectedMainThred(getActivity())) {
                        loader.show();
                        loader.setCanceledOnTouchOutside(true);
                        loader.setCancelable(false);
                        ChangeTranscatoionPasswordAPI();
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
    public void ChangeTranscatoionPasswordAPI()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MemberId",pref.get(AppSettings.UserId));
            jsonObject.put("Action","T");
            jsonObject.put("NewPassword",etNewPassword.getText().toString());
            jsonObject.put("OldPassword",etOldPassword.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+ChangePasswordAPi)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String Message=   response.getString("Message");
                            if (Message.equals("Password Update Successfully"))
                            {
                                EasyToast.warning(getActivity(),"Your Password update Successfully.");
                                loader.cancel();
                            }
                            else
                            {
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