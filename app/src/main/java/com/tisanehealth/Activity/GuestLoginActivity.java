package com.tisanehealth.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.medialablk.easytoast.EasyToast;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.ForgotPassword;
import static com.tisanehealth.Helper.AppUrls.GuestLogin;
import static com.tisanehealth.Helper.AppUrls.Login;

public class GuestLoginActivity extends AppCompatActivity implements View.OnClickListener {
    //Button
    Button btnLogin;
    //EditText
    EditText etMobile;
    //TextView
    TextView tvMemberLogin;
    //Preferences
    Preferences pref;
    //CustomLoader
    CustomLoader loader;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_in_left);

        //Button
        btnLogin         =findViewById(R.id.btnLogin);

        //Edittext
        etMobile         =findViewById(R.id.etMobile);


        //Textview

        tvMemberLogin     =findViewById(R.id.tvMemberLogin);

        //Preferences
        pref             = new Preferences(this);

        //loader
        loader           = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        //setOnClickListener
        btnLogin.setOnClickListener(this);
        tvMemberLogin.setOnClickListener(this);


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
            case R.id.btnLogin:
                if (etMobile.getText().toString().isEmpty())
                {
                    EasyToast.warning(this,"Please enter your Mobile Number");
                }
               else if (etMobile.getText().toString().length()<10)
                {
                    EasyToast.warning(this,"Enter valid number");
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
            case R.id.tvMemberLogin:
                startActivity(new Intent(this,LoginActivity.class));
                break;
        }
    }



    //==========================================API===================================================//
    public void LoginApi() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MobileNumber", etMobile.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+GuestLogin)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            boolean Status=   response.getBoolean("Status");
                            if (Status)
                            {
                                pref.set(AppSettings.UserId,response.getString("Member_Id"));
                                pref.set(AppSettings.UserMobile,etMobile.getText().toString());
                                pref.set(AppSettings.UserType,"Guest");
                                pref.commit();

                                Intent i=new Intent(GuestLoginActivity.this,DashBoardActivity.class);
                                startActivity(i);
                                finish();
                                loader.dismiss();
                            }
                            else
                            {

                                EasyToast.error(GuestLoginActivity.this, "This UserId or Password is not valid.");
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
                                EasyToast.error(GuestLoginActivity.this, "This UserId is not valid..");
                                loader.dismiss();
                            }
                            else
                            {
                                EasyToast.success(GuestLoginActivity.this,"Your password has been sent ot your registered mobile number.");
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
