package com.tisanehealth.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tisanehealth.Fragment.Profile.BankDetailFragment;
import com.tisanehealth.Fragment.Profile.ChangePassword;
import com.tisanehealth.Fragment.Profile.ChangeTranscationPassword;
import com.tisanehealth.Fragment.Profile.EditProfileFragment;
import com.tisanehealth.Fragment.Profile.MyProfileFragment;
import com.tisanehealth.Fragment.Profile.WelcomeLetterActivity;
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
import static com.tisanehealth.Helper.AppUrls.ForgotTransPass;

public class SettingFragment extends Fragment implements View.OnClickListener{

    LinearLayout llProfile;
    LinearLayout llBankDetail;
    LinearLayout llEditProfile;
    LinearLayout llChangePassword;
    LinearLayout llWelcomeLetter;
    LinearLayout llChangeTranscationPassword;
    LinearLayout llForgotTranscationPassword;

    Preferences pref;
    CustomLoader loader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.setting_fragment, container, false);
        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("Settings");
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

        llProfile=view.findViewById(R.id.llProfile);
        llBankDetail=view.findViewById(R.id.llBankDetail);
        llEditProfile=view.findViewById(R.id.llEditProfile);
        llWelcomeLetter=view.findViewById(R.id.llWelcomeLetter);
        llChangePassword=view.findViewById(R.id.llChangePassword);
        llChangeTranscationPassword=view.findViewById(R.id.llChangeTranscationPassword);
        llForgotTranscationPassword=view.findViewById(R.id.llForgotTranscationPassword);



        pref=new Preferences(getActivity());

        //loader
        loader              = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


        llProfile.setOnClickListener(this);
        llBankDetail.setOnClickListener(this);
        llEditProfile.setOnClickListener(this);
        llWelcomeLetter.setOnClickListener(this);
        llChangePassword.setOnClickListener(this);
        llEditProfile.setOnClickListener(this);
        llChangeTranscationPassword.setOnClickListener(this);
        llForgotTranscationPassword.setOnClickListener(this);


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
            case R.id.llProfile:
                loadFragment(new MyProfileFragment());
                break;
            case R.id.llBankDetail:
                loadFragment(new BankDetailFragment());
                break;
            case R.id.llEditProfile:
                loadFragment(new EditProfileFragment());
                break;
            case R.id.llChangePassword:
                loadFragment(new ChangePassword());
                break;
            case R.id.llChangeTranscationPassword:
                loadFragment(new ChangeTranscationPassword());
                break;
            case R.id.llForgotTranscationPassword:
                 forgotDialog();
                break;
            case R.id.llWelcomeLetter:
                 startActivity(new Intent(getActivity(), WelcomeLetterActivity.class));
                break;

        }

    }

    public void successDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.success_popup);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();


        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);


        CardView  cardView     =dialog.findViewById(R.id.cardview);
        RelativeLayout rlTick  = dialog.findViewById(R.id.rlTick);
        Button btnOkay         =dialog.findViewById(R.id.btnOkay);

        TextView text=dialog.findViewById(R.id.text);

        Animation animSlide = AnimationUtils.loadAnimation(getActivity(),
                R.anim.bottom_to_top);
        Animation animSlide1 = AnimationUtils.loadAnimation(getActivity(),
                R.anim.up_from_bottom);
        cardView.setAnimation(animSlide);
        rlTick.setAnimation(animSlide1);

        text.setText("Your Transaction password has been sent to \n your registered mobile number.");


        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    public void forgotDialog()
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.forgot_password);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();


        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        CardView  cardView     =dialog.findViewById(R.id.cardview);
        RelativeLayout rlPassword  = dialog.findViewById(R.id.rlPassword);
        Button btnSubmit         =dialog.findViewById(R.id.btnSubmit);

        EditText etUserId=dialog.findViewById(R.id.etUserId);
        etUserId.setEnabled(false);

        etUserId.setText(pref.get(AppSettings.UserId));

        Animation animSlide = AnimationUtils.loadAnimation(getActivity(),
                R.anim.bottom_to_top);
        Animation animSlide1 = AnimationUtils.loadAnimation(getActivity(),
                R.anim.up_from_bottom);
        cardView.setAnimation(animSlide);
        rlPassword.setAnimation(animSlide1);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();


                if (Utils.isNetworkConnectedMainThred(getActivity())) {
                    loader.show();
                    loader.setCanceledOnTouchOutside(true);
                    loader.setCancelable(false);
                    ForgotTransactionPasswordAPI();
                } else {
                    EasyToast.error(getActivity(), "No Internet Connnection");
                }
            }
        });


        dialog.show();
    }



    //==========================================API===================================================//
    public void ForgotTransactionPasswordAPI()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Userid",pref.get(AppSettings.UserId));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+ForgotTransPass)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String Message=   response.getString("Message");
                            if (Message.equals("Your Transaction Password has been sent on your registered mobile no."))
                            {
                               // EasyToast.warning(getActivity(),"Your Transaction Password has been sent on your registered mobile number.");
                                loader.cancel();
                                successDialog();
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

                        //Log.v("gjfihhgghghg",error.toString());
                        loader.cancel();
                    }
                });
    }
}