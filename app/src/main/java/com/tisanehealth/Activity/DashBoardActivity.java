package com.tisanehealth.Activity;

import android.Manifest;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.tisanehealth.Adapter.Adapter_menu;
import com.tisanehealth.Fragment.AddAssociate.AddAssociateFragment;
import com.tisanehealth.Fragment.Bank.BankManagement;
import com.tisanehealth.Fragment.DashBoardFragment;
import com.tisanehealth.Fragment.DashBoardGuestFragment;
import com.tisanehealth.Fragment.PayoutReport.PayoutReportFragment;
import com.tisanehealth.Fragment.PinManagement.PinManagement;
import com.tisanehealth.Fragment.Profile.MemberTopup;
import com.tisanehealth.Fragment.Repurchase.BusinessFragment;
import com.tisanehealth.Fragment.SettingFragment;
import com.tisanehealth.Fragment.Team.TeamFragment;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.RecyclerTouchListener;
import com.tisanehealth.Model.recharge.ContactModel;
import com.tisanehealth.Model.tree.TreeResponse;
import com.tisanehealth.R;
import com.tisanehealth.recharge_pay_bill.AddMoneyToWalletActivity;
import com.tisanehealth.recharge_pay_bill.RechargeHistoryActivity;
import com.tisanehealth.recharge_pay_bill.money_transfer.MoneyTransferHistoryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.paperdb.Paper;
import needle.Needle;

import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.BindDashBoradTreeview;


public class DashBoardActivity extends AppCompatActivity implements View.OnClickListener {

    NavigationView navigationView;
    RecyclerView recyclerView;
    RelativeLayout WaveContainer;
    DrawerLayout mDrawer;
    Preferences pref;
    public static RelativeLayout rlHeader, rlMenu;
    public static TextView tvHeader, tvUserName, tvUserMobile;
    public static ImageView ivMenu, ivLogo;
    public static TreeResponse treeResponse;
    private int AnimateNumber = 1;

    String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};
    private static final int PERMISSION_REQUEST_CODE = 2;
    public static String mobile_type = "";

    Cursor cursor;
    String name, phonenumber, image;
    ArrayList<ContactModel> StoreContacts = new ArrayList<>();
    ArrayList<ContactModel> StoreContacts1 = new ArrayList<>();
    ContactModel contactModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_in_right);


        //RelativeLayout
        rlHeader = findViewById(R.id.rlHeader);
        rlMenu = findViewById(R.id.rlMenu);

        //Textview
        tvHeader = findViewById(R.id.tvHeader);
        tvUserName = findViewById(R.id.tvUsername);
        tvUserMobile = findViewById(R.id.tvUserMobile);
        //ImageView
        ivMenu = findViewById(R.id.ivMenu);
        ivLogo = findViewById(R.id.ivLogo);

        pref = new Preferences(this);

        try {
            tvUserName.setText(pref.get(AppSettings.UserName));
            tvUserMobile.setText(pref.get(AppSettings.UserMobile));
        } catch (Exception e) {

        }


        //DrawerLayout
        mDrawer = findViewById(R.id.drawer_layout);

        //Navigation Initialisation
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        recyclerView = navigationView.findViewById(R.id.nav_drawer_recycler_view);
        WaveContainer = navigationView.findViewById(R.id.WaveContainer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Adapter_menu adapter_menu = new Adapter_menu(this, new Adapter_menu.ListenerOnMenuItemClick() {
            @Override
            public void Item(int Position) {
                mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                mDrawer.closeDrawer(GravityCompat.START);
            }
        });

        recyclerView.setAdapter(adapter_menu);

        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                StartAnimation();
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        //SetOnClickListener
        rlMenu.setOnClickListener(this);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                if (pref.get(AppSettings.UserType).equals("Guest")) {
                    if (position == 0) {
                        loadFragment(new DashBoardGuestFragment());
                    } else if (position == 1) {
                        mDrawer.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(DashBoardActivity.this, AddMoneyToWalletActivity.class));
                    } else if (position == 2) {
                        mDrawer.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(DashBoardActivity.this, RechargeHistoryActivity.class));
                    } else if (position == 3) {
                        mDrawer.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(DashBoardActivity.this, DealWithUsActivity.class));
                    } else if (position == 4) {
                        mDrawer.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(DashBoardActivity.this, MoneyTransferHistoryActivity.class));
                    } else if (position == 5) {
                        logoutDialog();
                    }
                } else {
                    if (position == 0) {
                        loadFragment(new DashBoardFragment());
                    } else if (position == 1) {
                        loadFragment(new AddAssociateFragment());
                    } else if (position == 2) {
                        tvHeader.setText("Settings");
                        // getSupportActionBar().setTitle("Settings");
                        loadFragment(new SettingFragment());
                    } else if (position == 3) {
                        tvHeader.setText("My Team");
                        loadFragment(new TeamFragment());
                    } else if (position == 4) {

                        tvHeader.setText("Payout Report");
                        loadFragment(new PayoutReportFragment());
                    } else if (position == 5) {
                        tvHeader.setText("Member Topup");
                        loadFragment(new MemberTopup());
                    } else if (position == 6) {
                        tvHeader.setText("Pin Management");
                        loadFragment(new PinManagement());

                    } else if (position == 7) {
                        tvHeader.setText("Bank Management");
                        loadFragment(new BankManagement());

                    } else if (position == 8) {
                        mDrawer.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(DashBoardActivity.this, AddMoneyToWalletActivity.class));

                    } else if (position == 9) {
                        mDrawer.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(DashBoardActivity.this, RechargeHistoryActivity.class));
                    } else if (position == 10) {
                        mDrawer.closeDrawer(GravityCompat.START);
                        loadFragment(new BusinessFragment());
                    } else if (position == 11) {
                        mDrawer.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(DashBoardActivity.this, DealWithUsActivity.class));
                    } else if (position == 12) {
                        mDrawer.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(DashBoardActivity.this, MoneyTransferHistoryActivity.class));
                    } else if (position == 13) {
                        mDrawer.closeDrawer(GravityCompat.START);
                        logoutDialog();

                    }
                }


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        inAppUpdateController();

        if (!hasPermissions(DashBoardActivity.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(DashBoardActivity.this, PERMISSIONS, PERMISSION_REQUEST_CODE);
        } else {
            Paper.book().delete("contactlist");
            getContact();
        }


        if (pref.get(AppSettings.UserType).equals("Guest")) {
            loadFragment(new DashBoardGuestFragment());
        } else {
            loadFragment(new DashBoardFragment());
        }

        getData();
    }

    public void inAppUpdateController() {

        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                //  Toast.makeText(DashBoardActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                //Log.v("ghgghghghghghhg",e.toString());
            }
        });

        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                //     //Log.v("ghgghghghghghhg","434444444");

                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {

                    try {
                        Toast.makeText(DashBoardActivity.this, "New Version Available", Toast.LENGTH_SHORT).show();
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, DashBoardActivity.this, 1212);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(DashBoardActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        //      //Log.v("ghgghghghghghhg",e.toString());
                    }
                }
            }
        });
    }

    public void getData() {
//        loader.show();
//        loader.setCanceledOnTouchOutside(true);
//        loader.setCancelable(false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MemberId", pref.get(AppSettings.UserId));
            jsonObject.put("UserName", pref.get(AppSettings.UserId));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl + BindDashBoradTreeview)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        treeResponse = new Gson().fromJson(response, TreeResponse.class);
                    }


                    @Override
                    public void onError(ANError anError) {
                        //Toast.makeText(DashBoardActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }

                });
    }


    //Animation in DrawerLayout
    private void StartAnimation() {
        AnimatedVectorDrawableCompat drawable = null;
        switch (AnimateNumber) {
            case 1:
                drawable = AnimatedVectorDrawableCompat.create(this, R.drawable.animate_wave_1);
                break;
            case 2:
                drawable = AnimatedVectorDrawableCompat.create(this, R.drawable.animate_wave_2);
                break;
            case 3:
                drawable = AnimatedVectorDrawableCompat.create(this, R.drawable.animate_wave_3);
                break;
            case 4:
                drawable = AnimatedVectorDrawableCompat.create(this, R.drawable.animate_wave_4);
                break;
            case 5:
                drawable = AnimatedVectorDrawableCompat.create(this, R.drawable.animate_wave_5);
                AnimateNumber = 0;
                break;
            default:
                drawable = AnimatedVectorDrawableCompat.create(this, R.drawable.animate_wave_1);
        }


        AnimateNumber++;
        WaveContainer.setBackground(drawable);
        assert drawable != null;
        drawable.start();
    }

    //Load Fragment
    public void loadFragment(Fragment fragment) {
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(
//                R.anim.card_flip_right_in, R.anim.card_flip_right_out,
//                R.anim.card_flip_left_in, R.anim.card_flip_left_out);
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("SplashActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("SplashActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlMenu:
                mDrawer.openDrawer(Gravity.LEFT);
                break;
        }
    }


    public void GetContactsIntoArrayList() {
        StoreContacts.clear();
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1", null, "UPPER(" + ContactsContract.Contacts.DISPLAY_NAME + ") ASC");

        String lastPhoneName = " ";
        while (cursor.moveToNext()) {

            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            image = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            if (!name.equalsIgnoreCase(lastPhoneName)) {
                lastPhoneName = name;

                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                try {
                    Phonenumber.PhoneNumber swissNumberProto = phoneUtil.parse(phonenumber, "IN");

                    contactModel = new ContactModel(name, String.valueOf(swissNumberProto.getNationalNumber()));
                    //Log.v("fihihgihghghg",swissNumberProto.toString());
                } catch (NumberParseException e) {
                    System.err.println("NumberParseException was thrown: " + e.toString());
                }

                StoreContacts.add(contactModel);
                Paper.book().write("contactlist", StoreContacts);
            } else {

            }

        }


    }

    //======================================Dialog==============================================//
    public void logoutDialog() {
        final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);
        dialog.setContentView(R.layout.logout);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        Button btnOkay, btnCancel;

        btnOkay = dialog.findViewById(R.id.btnOkay);
        btnCancel = dialog.findViewById(R.id.btnCancel);

        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                pref.set(AppSettings.UserId, "");
                pref.set(AppSettings.UserName, "");
                pref.set(AppSettings.UserType, "");
                pref.commit();
                Intent i = new Intent(DashBoardActivity.this, LoginActivity.class);
                startActivity(i);

            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {

                //Log.v("permissionvalue", permission);

                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    //startActivity(new Intent(this, ContactActivity.class));

                    getContact();


                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    if (!hasPermissions(DashBoardActivity.this, PERMISSIONS)) {

                        ActivityCompat.requestPermissions(DashBoardActivity.this, PERMISSIONS, PERMISSION_REQUEST_CODE);
                    } else {
                        Paper.book().delete("contactlist");
                        getContact();
                    }
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    void getContact() {
        Needle.onBackgroundThread().execute(new Runnable() {
            @Override
            public void run() {
                GetContactsIntoArrayList();
            }
        });

    }
}
