package com.tisanehealth.Fragment.Profile;

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
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tisanehealth.Activity.DashBoardActivity;
import com.tisanehealth.BuildConfig;
import com.tisanehealth.Fragment.SettingFragment;
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
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import de.hdodenhof.circleimageview.CircleImageView;
import static com.tisanehealth.Activity.DashBoardActivity.ivLogo;
import static com.tisanehealth.Activity.DashBoardActivity.rlHeader;
import static com.tisanehealth.Activity.DashBoardActivity.tvHeader;
import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.GetProfile;
import static com.tisanehealth.Helper.AppUrls.UpdateImage;


public class MyProfileFragment extends Fragment implements View.OnClickListener {
    //ImageView
    ImageView ivEdit,ivCamera;
    //Imageview
    CircleImageView ivUser;
    //TextView
    TextView tvUserName,tvEmail,tvMobile,tvGender,tvFathername,tvDob,tvAddress;
    TextView tvBankname,tvBankIFSC,tvBankAccount,tvBankBranch,tvPanNumber,tvAadhar,tvPayeeName;
    TextView tvSponserId,tvSponserName,tvNomineeName,tvNomineeFathername,tvNomineeAddress,tvNomineeRelation;

    //Preferences
    Preferences pref;
    //CustomLoader
    CustomLoader loader;

    //String
    String City ;
    String District ;
    String Landmark ;
    String State;
    String ZipCode ;
    String Address ;

    //declaration for camera_gallery
    private static final int SELECT_PICTURE = 1;
    public Uri fileUri;
    public Uri picUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String IMAGE_DIRECTORY_NAME = "Real Estate";
    public static final int MEDIA_TYPE_VIDEO = 2;

    String picturePath = "", filename = "", ext = "";
    public static Bitmap bitmap;
 //   String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
   // int PERMISSION_ALL = 4;

    String selectedImagePath;
    int serverResponseCode=0;
    File auxFile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_profile, container, false);
        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("Profile");
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

        //IamegView
        ivEdit                 =view.findViewById(R.id.ivEdit);
        ivCamera               =view.findViewById(R.id.ivCamera);
        ivUser                 =view.findViewById(R.id.ivUser);

        //Textview
        tvUserName             =view.findViewById(R.id.tvUserName);
        tvEmail                =view.findViewById(R.id.tvEmail);
        tvMobile               =view.findViewById(R.id.tvMobile);
        tvGender               =view.findViewById(R.id.tvGender);
        tvFathername           =view.findViewById(R.id.tvFathername);
        tvDob                  =view.findViewById(R.id.tvDob);
        tvAddress              =view.findViewById(R.id.tvAddress);
        tvBankname             =view.findViewById(R.id.tvBankName);
        tvBankIFSC             =view.findViewById(R.id.tvBankIfsc);
        tvBankAccount          =view.findViewById(R.id.tvBankAccount);
        tvBankBranch           =view.findViewById(R.id.tvbankBranch);
        tvPanNumber            =view.findViewById(R.id.tvPanno);
        tvAadhar               =view.findViewById(R.id.tvAadharNumber);
        tvPayeeName            =view.findViewById(R.id.tvPayeeName);
        tvSponserId            =view.findViewById(R.id.tvSponserId);
        tvSponserName          =view.findViewById(R.id.tvSponserName);
        tvNomineeName          =view.findViewById(R.id.tvNomineename);
        tvNomineeFathername    =view.findViewById(R.id.tvNomineeFathername);
        tvNomineeAddress       =view.findViewById(R.id.tvNomineeAddress);
        tvNomineeRelation      =view.findViewById(R.id.tvNomineeRelation);

        //Preferences
        pref                   = new Preferences(getActivity());
        //loader
        loader                  = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);



        rlHeader.setVisibility(View.VISIBLE);
        tvHeader.setText("My Profile");

        ivEdit.setOnClickListener(this);
        ivCamera.setOnClickListener(this);


        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            loader.show();
            loader.setCanceledOnTouchOutside(true);
            loader.setCancelable(false);
            GetProfileAPI();

        } else {
            EasyToast.error(getActivity(), "No Internet Connnection");
        }


        return view;


    }


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

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ivCamera:
//                if (!hasPermissions(getActivity(), PERMISSIONS)) {
//                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
//                } else {
//                    imageDialog();
//                }

                break;
            case R.id.ivEdit:
                loadFragment(new EditProfileFragment(),tvUserName.getText().toString(),tvFathername.getText().toString(),tvEmail.getText().toString(),tvDob.getText().toString(),tvGender.getText().toString(),tvMobile.getText().toString(),Address,Landmark,City,District,State,ZipCode,tvAadhar.getText().toString(),tvPanNumber.getText().toString(),tvBankAccount.getText().toString(),tvBankBranch.getText().toString(),tvBankname.getText().toString(),tvBankIFSC.getText().toString(),tvNomineeAddress.getText().toString(),tvNomineeFathername.getText().toString(),tvNomineeName.getText().toString(),tvNomineeRelation.getText().toString(),tvPayeeName.getText().toString(),tvSponserId.getText().toString(), tvSponserName.getText().toString());
                break;

        }
    }


    public void loadFragment(Fragment fragment)
    {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.card_flip_right_in, R.anim.card_flip_right_out,
                R.anim.card_flip_left_in, R.anim.card_flip_left_out);
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }


    public void loadFragment(Fragment fragment,String name,String father_name,String email,String dob,String gender,String mobile_number,String flat_number,String landmark,String city,String district,String state,String zipcode,String Aadhar_number,String pan_number,String bankaccount,String  BankBranch,String BankName,String IFSCCode,String NomineeAddress,String NomineeFath_HusbName,String nominee_name,String  NomineeRelation,String PayeeName,String SponsorID,String SponsorName )
    {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Bundle bundle=new Bundle();
        bundle.putString("name",name);
        bundle.putString("father_name", father_name);
        bundle.putString("dob", dob);
        bundle.putString("email", email);
        bundle.putString("gender", gender);
        bundle.putString("mobile_number", mobile_number);
        bundle.putString("flat_number", flat_number);
        bundle.putString("landmark", landmark);
        bundle.putString("city", city);
        bundle.putString("district", district);
        bundle.putString("state", state);
        bundle.putString("zipcode", zipcode);
        bundle.putString("Aadhar_number", Aadhar_number);
        bundle.putString("pan_number", pan_number);
        bundle.putString("bankaccount", bankaccount);
        bundle.putString("BankBranch", BankBranch);
        bundle.putString("BankName", BankName);
        bundle.putString("IFSCCode", IFSCCode);
        bundle.putString("NomineeAddress", NomineeAddress);
        bundle.putString("NomineeFath_HusbName", NomineeFath_HusbName);
        bundle.putString("nominee_name", nominee_name);
        bundle.putString("NomineeRelation", NomineeRelation);
        bundle.putString("PayeeName", PayeeName);
        bundle.putString("SponsorID", SponsorID);
        bundle.putString("SponsorName", SponsorName);
        fragment.setArguments(bundle);
        transaction.setCustomAnimations(
                R.anim.card_flip_right_in, R.anim.card_flip_right_out,
                R.anim.card_flip_left_in, R.anim.card_flip_left_out);
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
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

    public static int getImageOrientation(String imagePath) {
        int rotate = 0;
        try {

            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
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

          //      selectedImagePath =SiliCompressor.with(getActivity()).compress(picturePath,  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));;
                Uri uri = Uri.parse(picturePath);
                ext = "jpg";
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 500;
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

                ivUser.setImageBitmap(rotatedBitmap);

                auxFile = new File(picturePath);

                if (Utils.isNetworkConnectedMainThred(getActivity())) {
                    loader.show();
                    loader.setCanceledOnTouchOutside(true);
                    loader.setCancelable(false);
                    new UploadFileAsync().execute("");
                } else {
                    EasyToast.error(getActivity(), "No Internet Connnection");
                }

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

               // selectedImagePath =SiliCompressor.with(getActivity()).compress(picturePath,  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+"/ApnaGullak/images"));;



                Log.d("path", picturePath);
                System.out.println("Image Path : " + picturePath);
                cursor.close();


                filename = picturePath.substring(picturePath.lastIndexOf("/") + 1);
                ext = getFileType(picturePath);
            //    String selectedImagePath = picturePath;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 500;
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
                    ivUser.setImageBitmap(rotatedBitmap);

                    auxFile = new File(picturePath);
                    if (Utils.isNetworkConnectedMainThred(getActivity())) {
                        loader.show();
                        loader.setCanceledOnTouchOutside(true);
                        loader.setCancelable(false);
                        new UploadFileAsync().execute("");


                    } else {
                        EasyToast.error(getActivity(), "No Internet Connnection");
                    }



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

    //==========================================API===================================================//
    public void GetProfileAPI()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MemberId",pref.get(AppSettings.UserId));
            jsonObject.put("SessionId","");

        } catch (JSONException e) {
            e.printStackTrace();
        }



        AndroidNetworking.post(BaseUrl+GetProfile)
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
                                JSONArray jsonArray=response.getJSONArray("ProfileList");

                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                                      String AadharNo=jsonObject.getString("AadharNo")  ;
                                      String BankBranch=jsonObject.getString("BankBranch")  ;
                                      String BankIFSC=jsonObject.getString("BankIFSC")  ;
                                      String BankName=jsonObject.getString("BankName")  ;
                                      String BankAccountNumber=jsonObject.getString("BankAcNo")  ;
                                      String DOBDay=jsonObject.getString("DOBDay")  ;
                                      String DOBMonth=jsonObject.getString("DOBMonth")  ;
                                      String DOBYear=jsonObject.getString("DOBYear")  ;
                                      String Email=jsonObject.getString("Email")  ;
                                      String FatherName=jsonObject.getString("FatherName")  ;
                                      String Gender=jsonObject.getString("Gender")  ;
                                      String MobileNo=jsonObject.getString("MobileNo")  ;
                                      String Name=jsonObject.getString("Name")  ;
                                      String NomineeAddress=jsonObject.getString("NomineeAddress")  ;
                                      String NomineeFather=jsonObject.getString("NomineeFather")  ;
                                      String NomineeName=jsonObject.getString("NomineeName")  ;
                                      String NomineeRelation=jsonObject.getString("NomineeRelation")  ;
                                      String NomineeAge=jsonObject.getString("NomineeAge")  ;
                                      String PanNo=jsonObject.getString("PanNo")  ;
                                      String PayeeName=jsonObject.getString("PayeeName")  ;
                                      String SponsorId=jsonObject.getString("SponsorId")  ;
                                      String SponsorName=jsonObject.getString("SponsorName")  ;
                                      String UserImage=jsonObject.getString("ProfilePath")  ;
                                      City=jsonObject.getString("City")  ;
                                      District=jsonObject.getString("District")  ;
                                      Landmark=jsonObject.getString("Landmark")  ;
                                      State=jsonObject.getString("State")  ;
                                      ZipCode=jsonObject.getString("ZipCode")  ;
                                      Address=jsonObject.getString("Address")  ;


                                      tvAadhar.setText(AadharNo);
                                      tvAddress.setText(Address);
                                      tvBankBranch.setText(BankBranch);
                                      tvBankIFSC.setText(BankIFSC);
                                      tvBankname.setText(BankName);
                                      tvBankAccount.setText(BankAccountNumber);
                                      tvDob.setText(DOBDay+"-"+DOBMonth+"-"+DOBYear);
                                      tvEmail.setText(Email);
                                      tvFathername.setText(FatherName);
                                      tvGender.setText(Gender);
                                      tvMobile.setText(MobileNo);
                                      tvUserName.setText(Name);
                                      tvNomineeAddress.setText(NomineeAddress);
                                      tvNomineeFathername.setText(NomineeFather);
                                      tvNomineeName.setText(NomineeName);
                                      tvNomineeRelation.setText(NomineeRelation);
                                      tvPanNumber.setText(PanNo);
                                      tvPayeeName.setText(PayeeName);
                                      tvSponserId.setText(SponsorId);
                                      tvSponserName.setText(SponsorName);


                                      pref.set(AppSettings.PayeeName,PayeeName);
                                      pref.set(AppSettings.Bankname,BankName);
                                      pref.set(AppSettings.BankIfsc,BankIFSC);
                                      pref.set(AppSettings.BankBranch,BankBranch);
                                      pref.set(AppSettings.BankAccountNumber,BankAccountNumber);
                                      pref.set(AppSettings.PanNumber,PanNo);
                                      pref.set(AppSettings.UserMobile,MobileNo);
                                      pref.set(AppSettings.UserName,Name);
                                      pref.commit();

                                    DashBoardActivity.tvUserName.setText(pref.get(AppSettings.UserName));
                                    DashBoardActivity.tvUserMobile.setText(pref.get(AppSettings.UserMobile));

                                }

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

    public class UploadFileAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024;
                File sourceFile = new File(String.valueOf(selectedImagePath));

                if (sourceFile.isFile()) {

                    try {
                        FileInputStream fileInputStream = new FileInputStream(
                                sourceFile);
                        URL url = new URL(BaseUrl+UpdateImage);

                        // Open a HTTP connection to the URL
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true); // Allow Inputs
                        conn.setDoOutput(true); // Allow Outputs
                        conn.setUseCaches(false); // Don't use a Cached Copy
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("ENCTYPE",
                                "multipart/form-data");
                        conn.setRequestProperty("Content-Type",
                                "multipart/form-data;boundary=" + boundary);
                        conn.setRequestProperty("bill", String.valueOf(selectedImagePath));

                        dos = new DataOutputStream(conn.getOutputStream());

                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"bill\";filename=\""
                                + selectedImagePath + "\"" + lineEnd);

                        dos.writeBytes(lineEnd);

                        // create a buffer of maximum size
                        bytesAvailable = fileInputStream.available();

                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        buffer = new byte[bufferSize];

                        // read file and write it into form...
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        while (bytesRead > 0) {

                            dos.write(buffer, 0, bufferSize);
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math
                                    .min(bytesAvailable, maxBufferSize);
                            bytesRead = fileInputStream.read(buffer, 0,
                                    bufferSize);

                        }

                        // send multipart form data necesssary after file
                        // data...
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens
                                + lineEnd);

                        // Responses from the server (code and message)
                        serverResponseCode = conn.getResponseCode();
                        String serverResponseMessage = conn
                                .getResponseMessage();

                        //Log.v("ff123333333", String.valueOf(serverResponseMessage));
                        if (serverResponseCode == 200) {


                            // messageText.setText(msg);
                            //Toast.makeText(ctx, "File Upload Complete.",
                            //      Toast.LENGTH_SHORT).show();

                            // recursiveDelete(mDirectory1);

                            loader.cancel();

                        }

                        // close the streams //
                        fileInputStream.close();
                        dos.flush();
                        dos.close();

                    } catch (Exception e) {

                        // dialog.dismiss();
                        e.printStackTrace();

                    }
                    // dialog.dismiss();

                } // End else block


            } catch (Exception ex) {
                // dialog.dismiss();
                loader.cancel();

                ex.printStackTrace();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            //Log.v("ff123333333",result);
            loader.cancel();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }



}