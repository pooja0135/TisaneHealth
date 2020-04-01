package com.tisanehealth.Fragment.Bank;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tisanehealth.BuildConfig;
import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Helper.Utils;
import com.tisanehealth.R;
import com.medialablk.easytoast.EasyToast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import static com.tisanehealth.Activity.DashBoardActivity.ivLogo;
import static com.tisanehealth.Activity.DashBoardActivity.rlHeader;
import static com.tisanehealth.Activity.DashBoardActivity.tvHeader;
import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.FundRequest;
import static com.tisanehealth.Helper.AppUrls.GetAllBankName;
import static com.tisanehealth.Helper.AppUrls.getDepositType;

public class FundRequestFormFragment extends Fragment implements View.OnClickListener{
    //Preferences
    Preferences pref;
    //CustomLoader
    CustomLoader loader;
    //Edittext
    EditText etMemberId,etMemberName,etAmount,etIfsc,etAccountNumber,etAccountHolderName,etBranchName,etPaymentDate,etMobile,etRemark;

    EditText etTransactionId,etChequeNumber,etReferenceNo,etBankName,etBankAccountNumber,etIfscCode,etBankBranch;

    LinearLayout llTransaction,llChequeNo,llReferenceNo,llBankName,llBankAccountNumber,llIfsc,llBankBranch,llDepositType;

    Spinner spinnerCompanyBank,spinnerDeposit;


    ArrayList<HashMap<String,String>>bankdetaillist=new ArrayList<>();
    ArrayList<String>bankvaluelist=new ArrayList<>();
    ArrayList<String>depositlist=new ArrayList<>();

    Button btnSubmit,btnChooseImage;

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;


    private static final int SELECT_PICTURE = 1;
    public Uri fileUri;
    public Uri picUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String IMAGE_DIRECTORY_NAME = "Real Estate";
    public static final int MEDIA_TYPE_VIDEO = 2;

    String picturePath = "", filename = "", ext = "";
    public static Bitmap bitmap;
  //  String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
   // int PERMISSION_ALL = 4;

    String selectedImagePath;
    int serverResponseCode=0;
    File auxFile;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fund_request_form, container, false);
        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("Fund Request Form");
        ivLogo.setVisibility(View.GONE);
        tvHeader.setVisibility(View.VISIBLE);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        loadFragment(new BankManagement());
                        return true;
                    }
                }
                return false;
            }
        });


        //initialise value
        initialise(view);


        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(etPaymentDate);
            }

        };


        etPaymentDate.setOnClickListener(this);
        btnChooseImage.setOnClickListener(this);


      /*  if (Utils.isNetworkConnectedMainThred(getActivity())) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);
          //  BankDetailListAPI();

        } else {
            EasyToast.error(getActivity(), "No Internet Connnection");
        }*/


      /*  btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/


        return view;


    }


    public  void initialise(View view)
    {
        btnSubmit      =view.findViewById(R.id.btnSubmit);
        btnChooseImage =view.findViewById(R.id.btnChooseImage);

        etMemberId=view.findViewById(R.id.etMemberId);
        etMemberName=view.findViewById(R.id.etMemberName);
        etAmount=view.findViewById(R.id.etAmount);
        etIfsc=view.findViewById(R.id.etIfsc);
        etBranchName=view.findViewById(R.id.etBranchName);
        etAccountNumber=view.findViewById(R.id.etAccountNumber);
        etAccountHolderName=view.findViewById(R.id.etAccountHolderName);
        etPaymentDate=view.findViewById(R.id.etPaymentDate);
        etMobile=view.findViewById(R.id.etMobile);
        etRemark=view.findViewById(R.id.etRemark);




        etTransactionId=view.findViewById(R.id.etTransactionId);
        etChequeNumber=view.findViewById(R.id.etChequeNumber);
        etReferenceNo=view.findViewById(R.id.etReferenceNo);
        etBankName=view.findViewById(R.id.etBankName);
        etBankAccountNumber=view.findViewById(R.id.etBankAccountNumber);
        etIfscCode=view.findViewById(R.id.etIfscCode);
        etBankBranch=view.findViewById(R.id.etBankBranch);


        llDepositType=view.findViewById(R.id.llDepositType);

        llTransaction=view.findViewById(R.id.llTransaction);
        llChequeNo=view.findViewById(R.id.llChequeNo);
        llReferenceNo=view.findViewById(R.id.llReferenceNo);
        llBankName=view.findViewById(R.id.llBankName);
        llBankAccountNumber=view.findViewById(R.id.llBankAccountNumber);
        llIfsc=view.findViewById(R.id.llIfsc);
        llBankBranch=view.findViewById(R.id.llBankBranch);


        spinnerCompanyBank =view.findViewById(R.id.spinnerCompanyBank);
        spinnerDeposit    =view.findViewById(R.id.spinnerDeposit);

        //Preferences
        pref                = new Preferences(getActivity());

        //loader
        loader              = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        etMemberId.setText(pref.get(AppSettings.UserId));
        etMemberName.setText(pref.get(AppSettings.UserName));


        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);
            GetAllBankAPI();
            GetDepositAPI();

        } else {
            EasyToast.error(getActivity(), "No Internet Connnection");
        }





        spinnerCompanyBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {

                if (spinnerCompanyBank.getSelectedItem().equals("Select"))
                {
                    etAccountHolderName.setText("");
                    etIfsc.setText("");
                    etBranchName.setText("");
                    etAccountNumber.setText("");
                }

                else
                {
                    etIfsc.setText(bankdetaillist.get(spinnerCompanyBank.getSelectedItemPosition()).get("IFSCCODE"));
                    etBranchName.setText(bankdetaillist.get(spinnerCompanyBank.getSelectedItemPosition()).get("Branch"));
                    etAccountNumber.setText(bankdetaillist.get(spinnerCompanyBank.getSelectedItemPosition()).get("BankAcNo"));
                }

            }

            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        spinnerDeposit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                //Log.v("hdjghfjfgfgfgf", String.valueOf(spinnerDeposit.getSelectedItem()));

                if (spinnerDeposit.getSelectedItem().equals("Select")) {
                    llDepositType.setVisibility(View.GONE);
                    etTransactionId.setText("");
                    etChequeNumber.setText("");
                    etReferenceNo.setText("");
                    etBankName.setText("");
                    etBankAccountNumber.setText("");
                    etIfscCode.setText("");
                    etBankBranch.setText("");
                }


                else if(spinnerDeposit.getSelectedItem().equals("Cash")) {
                    llDepositType.setVisibility(View.VISIBLE);
                    etTransactionId.setText("");
                    etChequeNumber.setText("");
                    etReferenceNo.setText("");
                    etBankName.setText("");
                    etBankAccountNumber.setText("");
                    etIfscCode.setText("");
                    etBankBranch.setText("");

                    llTransaction.setVisibility(View.VISIBLE);
                    llChequeNo.setVisibility(View.GONE);
                    llReferenceNo.setVisibility(View.GONE);
                    llBankName.setVisibility(View.GONE);
                    llBankAccountNumber.setVisibility(View.GONE);
                    llIfsc.setVisibility(View.GONE);
                    llBankBranch.setVisibility(View.GONE);

                }

                else if(spinnerDeposit.getSelectedItem().equals("Cheque")) {
                    llDepositType.setVisibility(View.VISIBLE);
                    etTransactionId.setText("");
                    etChequeNumber.setText("");
                    etReferenceNo.setText("");
                    etBankName.setText("");
                    etBankAccountNumber.setText("");
                    etIfscCode.setText("");
                    etBankBranch.setText("");

                    llTransaction.setVisibility(View.GONE);
                    llChequeNo.setVisibility(View.VISIBLE);
                    llReferenceNo.setVisibility(View.GONE);
                    llBankName.setVisibility(View.VISIBLE);
                    llBankAccountNumber.setVisibility(View.VISIBLE);
                    llIfsc.setVisibility(View.VISIBLE);
                    llBankBranch.setVisibility(View.VISIBLE);

                }

                else if(spinnerDeposit.getSelectedItem().equals("Online Transfer")) {
                    llDepositType.setVisibility(View.VISIBLE);
                    etTransactionId.setText("");
                    etChequeNumber.setText("");
                    etReferenceNo.setText("");
                    etBankName.setText("");
                    etBankAccountNumber.setText("");
                    etIfscCode.setText("");
                    etBankBranch.setText("");

                    llTransaction.setVisibility(View.GONE);
                    llChequeNo.setVisibility(View.GONE);
                    llReferenceNo.setVisibility(View.VISIBLE);
                    llBankName.setVisibility(View.VISIBLE);
                    llBankAccountNumber.setVisibility(View.VISIBLE);
                    llIfsc.setVisibility(View.VISIBLE);
                    llBankBranch.setVisibility(View.VISIBLE);

                }

                else if(spinnerDeposit.getSelectedItem().equals("NEFT")) {
                    llDepositType.setVisibility(View.VISIBLE);
                    etTransactionId.setText("");
                    etChequeNumber.setText("");
                    etReferenceNo.setText("");
                    etBankName.setText("");
                    etBankAccountNumber.setText("");
                    etIfscCode.setText("");
                    etBankBranch.setText("");

                    llTransaction.setVisibility(View.VISIBLE);
                    llChequeNo.setVisibility(View.GONE);
                    llReferenceNo.setVisibility(View.GONE);
                    llBankName.setVisibility(View.VISIBLE);
                    llBankAccountNumber.setVisibility(View.VISIBLE);
                    llIfsc.setVisibility(View.GONE);
                    llBankBranch.setVisibility(View.GONE);

                }

                else if(spinnerDeposit.getSelectedItem().equals("ATM Transfer")) {
                    llDepositType.setVisibility(View.VISIBLE);
                    etTransactionId.setText("");
                    etChequeNumber.setText("");
                    etReferenceNo.setText("");
                    etBankName.setText("");
                    etBankAccountNumber.setText("");
                    etIfscCode.setText("");
                    etBankBranch.setText("");

                    llTransaction.setVisibility(View.VISIBLE);
                    llChequeNo.setVisibility(View.GONE);
                    llReferenceNo.setVisibility(View.GONE);
                    llBankName.setVisibility(View.VISIBLE);
                    llBankAccountNumber.setVisibility(View.VISIBLE);
                    llIfsc.setVisibility(View.GONE);
                    llBankBranch.setVisibility(View.GONE);

                }


            }

            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etAmount.getText().toString().isEmpty())
                {
                    EasyToast.warning(getActivity(),"Enter Amount.");
                }
                else if (spinnerCompanyBank.getSelectedItem().equals("Select"))
                {
                    EasyToast.warning(getActivity(),"Select Bank.");
                }
                else if (spinnerDeposit.getSelectedItem().equals("Select"))
                {
                    EasyToast.warning(getActivity(),"Select Deposit Type.");
                }
                else if (etPaymentDate.getText().toString().isEmpty())
                {
                    EasyToast.warning(getActivity(),"Select date");
                }
                else if (etMobile.getText().toString().isEmpty())
                {
                    EasyToast.warning(getActivity(),"Enter Mobile");
                }
                else
                {
                    FundRequestAPI();
                }
            }
        });



    }

    private void updateLabel(EditText textView) {
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        textView.setText(sdf.format(myCalendar.getTime()));
    }


    public void loadFragment(Fragment fragment) {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(
//                R.anim.card_flip_right_in, R.anim.card_flip_right_out,
//                R.anim.card_flip_left_in, R.anim.card_flip_left_out);
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }


    //==========================================API===================================================//
    public void GetAllBankAPI()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("","");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+GetAllBankName)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //Log.v("jyiujojjujujujujuujujuj",response.toString());
                            String Message=   response.getString("Message");

                            if (Message.equals("Success"))
                            {

                                HashMap<String,String>map1=new HashMap<>();
                                map1.put("BankAcNo","Select");
                                map1.put("BankName","Select");
                                map1.put("Branch","Select");
                                map1.put("IFSCCODE","Select");
                                map1.put("Id","Select");

                                bankdetaillist.add(map1);
                                bankvaluelist.add("Select");

                                JSONArray jsonArray=response.getJSONArray("BankDetail");


                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);

                                    HashMap<String,String>map=new HashMap<>();
                                    map.put("BankAcNo",jsonObject.getString("BankAcNo"));
                                    map.put("BankName",jsonObject.getString("BankName"));
                                    map.put("Branch",jsonObject.getString("Branch"));
                                    map.put("IFSCCODE",jsonObject.getString("IFSCCODE"));
                                    map.put("Id",jsonObject.getString("Id"));

                                    bankdetaillist.add(map);

                                    bankvaluelist.add(jsonObject.getString("BankName"));

                                }
                                ArrayAdapter genderAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,bankvaluelist);
                                spinnerCompanyBank.setAdapter(genderAdapter);

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

                        //Log.v("gjfihhgghghg",error.toString());
                        loader.cancel();
                    }
                });
    }

    public void GetDepositAPI()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("","");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+getDepositType)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //Log.v("jyiujojjujujujujuujujuj",response.toString());
                            String Message=   response.getString("Message");

                            if (Message.equals("Success"))
                            {


                                depositlist.add("Select");

                                JSONArray jsonArray=response.getJSONArray("DepositDetail");
                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);


                                   depositlist.add(jsonObject.getString("DipositType"));

                                }
                                ArrayAdapter genderAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,depositlist);
                                spinnerDeposit.setAdapter(genderAdapter);

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

                        //Log.v("gjfihhgghghg",error.toString());
                        loader.cancel();
                    }
                });
    }


    public void FundRequestAPI()
    {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("BankACNo",etAccountNumber.getText().toString());
            jsonObject.put("BankAccountHolderName","");
            jsonObject.put("BankBranch",etBranchName.getText().toString());
            jsonObject.put("BankName",spinnerCompanyBank.getSelectedItem());
            jsonObject.put("ChequeNo",etChequeNumber.getText().toString());
            jsonObject.put("DipositType",spinnerDeposit.getSelectedItem());
            jsonObject.put("FilePath","");
            jsonObject.put("Files","");
            jsonObject.put("MemberID",pref.get(AppSettings.UserId));
            jsonObject.put("MobileNo",etMobile.getText().toString());
            jsonObject.put("MoneyAmount",etAmount.getText().toString());
            jsonObject.put("NEFT","");
            jsonObject.put("PaymentDate",etPaymentDate.getText().toString());
            jsonObject.put("ReferenceNo",etReferenceNo.getText().toString());
            jsonObject.put("Remarks",etRemark.getText().toString());
            jsonObject.put("Time","");
            jsonObject.put("TransactionId",etTransactionId.getText().toString());
            jsonObject.put("iFSCCOde",etIfsc.getText().toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl+FundRequest)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //Log.v("jyiujojjujujujujuujujuj",response.toString());
                            String Message=   response.getString("Message");

                            if (Message.equals("Success"))
                            {




                                depositlist.add("Select");

                                JSONArray jsonArray=response.getJSONArray("DepositDetail");
                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);


                                    depositlist.add(jsonObject.getString("DipositType"));

                                }
                                ArrayAdapter genderAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,depositlist);
                                spinnerDeposit.setAdapter(genderAdapter);

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

                        //Log.v("gjfihhgghghg",error.toString());
                        loader.cancel();
                    }
                });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.etPaymentDate:
                DatePickerDialog datePickerDialog=  new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
                break;
            case R.id.btnChooseImage:
               /* if (!hasPermissions(getActivity(), PERMISSIONS)) {
                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
                } else {
                    imageDialog();
                }*/
                break;
            case R.id.btnSubmit:
                break;
        }
    }

    //=========================================Dialog===================================================//
    public void imageDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnim);
        dialog.setContentView(R.layout.image_dialog);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        ImageView ivCamera,ivGallery;
        TextView tvCancel;

        ivCamera    =dialog.findViewById(R.id.ivCamera);
        ivGallery   =dialog.findViewById(R.id.ivGallery);
        tvCancel   =dialog.findViewById(R.id.tvCancel);

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                captureImage();
                dialog.dismiss();
            }
        });

        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoPickerIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(photoPickerIntent, SELECT_PICTURE);
                dialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }



    //============================Code for Camera and Gallary===================================//

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);


        Uri photoURI = FileProvider.getUriForFile(getActivity(),
                BuildConfig.APPLICATION_ID + ".provider", getOutputMediaFile(MEDIA_TYPE_IMAGE)
        );




        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public  int getImageOrientation(String imagePath) {
        int rotate = 0;
        try {
            InputStream input = getActivity().getContentResolver().openInputStream(Uri.parse(imagePath));
            ExifInterface exif;


            File imageFile = new File(imagePath);


            if (Build.VERSION.SDK_INT > 23)
                exif = new ExifInterface(input);
            else
                exif = new ExifInterface(
                        imageFile.getAbsolutePath());

          /*  ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());*/
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate;
    }


    //method to convert string into base64
    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
    }

    public void setPictures(Bitmap b, String setPic, String base64) {

        //profile.setImageBitmap(b);
    }

    public static String getFileType(String path) {
        String fileType = null;
        fileType = path.substring(path.indexOf('.', path.lastIndexOf('/')) + 1).toLowerCase();
        return fileType;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                picturePath = fileUri.getPath().toString();

                filename = picturePath.substring(picturePath.lastIndexOf("/") + 1);
                Log.d("filename_camera", filename);

             //   selectedImagePath = SiliCompressor.with(getActivity()).compress(picturePath,  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));;
                Uri uri = Uri.parse(picturePath);
                ext = "jpg";
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFile(selectedImagePath, options);
                //ivUserImage.setImageBitmap(bitmap);
                Matrix matrix = new Matrix();
                matrix.postRotate(getImageOrientation(picturePath));
                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                byte[] ba = bao.toByteArray();

           //     ivUser.setImageBitmap(rotatedBitmap);

          //      image=getEncoded64ImageStringFromBitmap(rotatedBitmap);

                auxFile = new File(picturePath);

            }
        } else if (requestCode == SELECT_PICTURE) {
            if (data != null) {
                Uri contentURI = data.getData();
                //get the Uri for the captured image
                picUri = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(contentURI, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                filename = picturePath.substring(picturePath.lastIndexOf("/") + 1);

           //     selectedImagePath =SiliCompressor.with(getActivity()).compress(picturePath,  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+"/ApnaGullak/images"));;



                Log.d("path", picturePath);
                System.out.println("Image Path : " + picturePath);
                cursor.close();


                filename = picturePath.substring(picturePath.lastIndexOf("/") + 1);
                ext = getFileType(picturePath);
                //    String selectedImagePath = picturePath;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFile(selectedImagePath, options);
                //  ivUserImage.setImageBitmap(bitmap);

                Matrix matrix = new Matrix();
                matrix.postRotate(getImageOrientation(picturePath));
                try {
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                //    ivUser.setImageBitmap(rotatedBitmap);


                //    image=getEncoded64ImageStringFromBitmap(rotatedBitmap);

                    auxFile = new File(picturePath);

                    byte[] ba = bao.toByteArray();
                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                }


            } else {

                // Snackbar.make(getActivity(), "Please enter Username.", Snackbar.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Unable to Select the Image.", Toast.LENGTH_LONG).show();
            }

        }

    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}