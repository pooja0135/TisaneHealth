package com.tisanehealth.Activity;

import android.app.Dialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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

import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.ForgotPassword;
import static com.tisanehealth.Helper.AppUrls.Login;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //Button
    Button btnLogin;
    //EditText
    EditText etUsername,etPassword;
    //TextView
    TextView tvForgotPassword;
    TextView tvGuestLogin;
    //Preferences
    Preferences pref;
    //CustomLoader
    CustomLoader loader;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_in_left);

        //Button
        btnLogin         =findViewById(R.id.btnLogin);

        //Edittext
        etUsername       =findViewById(R.id.etUsername);
        etPassword       =findViewById(R.id.etPassword);

        //Textview
        tvForgotPassword =findViewById(R.id.tvForgotpassword);
        tvGuestLogin     =findViewById(R.id.tvGuestLogin);

        //Preferences
        pref             = new Preferences(this);

        //loader
        loader           = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        //setOnClickListener
        tvForgotPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tvGuestLogin.setOnClickListener(this);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    //====================setOnClickListener=====================================//
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tvForgotpassword:
                forgotDialog();
                break;

            case R.id.tvGuestLogin:
                startActivity(new Intent(this,GuestLoginActivity.class));
                break;
            case R.id.btnLogin:
                if (etUsername.getText().toString().isEmpty())
                {
                    EasyToast.warning(this,"Please enter your UserId");
                }
                else if (etPassword.getText().toString().isEmpty())
                {
                    EasyToast.warning(this,"Please enter your password.");
                }
                else
                {
                    if (Utils.isNetworkConnectedMainThred(this)) {
                        loader.show();
                        loader.setCanceledOnTouchOutside(true);
                        loader.setCancelable(false);
                        LoginApi();

                    } else {
                        EasyToast.error(this, "No Internet Connnection");
                    }
                }


              //  Intent i=new Intent(LoginActivity.this,DashBoardActivity.class);
              //  startActivity(i);
                break;
        }
    }

   //===================================================DialogBox===========================================//
    public void forgotDialog()
    {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.forgot_password);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        CardView cardView            =dialog.findViewById(R.id.cardview);
        RelativeLayout rlPassword    = dialog.findViewById(R.id.rlPassword);
        Button btnSubmit             =dialog.findViewById(R.id.btnSubmit);
        final EditText etUserId      =dialog.findViewById(R.id.etUserId);

        Animation animSlide = AnimationUtils.loadAnimation(this,
                R.anim.bottom_to_top);
        Animation animSlide1 = AnimationUtils.loadAnimation(this,
                R.anim.up_from_bottom);
        cardView.setAnimation(animSlide);
        rlPassword.setAnimation(animSlide1);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (etUserId.getText().toString().isEmpty())
                {
                    EasyToast.warning(LoginActivity.this,"Please enter your UserId");
                }
                else{
                    if (Utils.isNetworkConnectedMainThred(LoginActivity.this)) {
                        loader.show();
                        loader.setCanceledOnTouchOutside(true);
                        loader.setCancelable(false);
                        ForgotPasswordApi(etUserId.getText().toString());

                    } else {
                        EasyToast.error(LoginActivity.this, "No Internet Connnection");
                    }
                }

            }
        });


        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
    }

    //==========================================API===================================================//
    public void LoginApi() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Password", etPassword.getText().toString());
            jsonObject.put("Userid", etUsername.getText().toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+Login)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String message=   response.getString("Message");
                            if (message.equals("Login Success"))
                            {
                                JSONArray jsonArray=response.getJSONArray("LoginRes");
                                JSONObject jsonObject=jsonArray.getJSONObject(0);
                                pref.set(AppSettings.UserName,jsonObject.getString("UserName"));
                                pref.set(AppSettings.UserId,jsonObject.getString("UserId"));
                                pref.set(AppSettings.GroupName,jsonObject.getString("GroupName"));
                                pref.set(AppSettings.UserMobile,jsonObject.getString("MobileNo"));
                                pref.set(AppSettings.UserType,"Member");
                                pref.commit();

                                Intent i=new Intent(LoginActivity.this,DashBoardActivity.class);
                                startActivity(i);
                                finish();
                                loader.dismiss();
                            }
                            else
                            {

                                EasyToast.error(LoginActivity.this, "This UserId or Password is not valid.");
                                loader.dismiss();
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

    public void ForgotPasswordApi( String user_id)
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Userid",user_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+ForgotPassword)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message=   response.getString("Message");
                            if (message.equals("Failed"))
                            {
                                EasyToast.error(LoginActivity.this, "This UserId is not valid..");
                                loader.dismiss();
                            }
                            else
                            {
                                EasyToast.success(LoginActivity.this,"Your password has been sent ot your registered mobile number.");
                                loader.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                    }
                });
    }
}
