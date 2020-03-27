package com.tisanehealth.recharge_pay_bill.mobile_recharge;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.medialablk.easytoast.EasyToast;
import com.tisanehealth.Adapter.recharge.Adapter_contacts;
import com.tisanehealth.Adapter.recharge.Adapter_contacts1;
import com.tisanehealth.Helper.MyKeyboard;
import com.tisanehealth.Model.recharge.ContactModel;
import com.tisanehealth.R;


import java.util.ArrayList;

import io.paperdb.Paper;


public class ContactActivity extends AppCompatActivity implements View.OnClickListener{

    EditText etSearch;
    RecyclerView recyclerView;
    Adapter_contacts adapter_contacts;
    Adapter_contacts1 adapter_contacts1;
    RelativeLayout rlKeypad,rlBack;
    ImageView ivBack;
    static final int PICK_CONTACT=1;
    Cursor cursor ;
    String name, phonenumber ,image;
    ArrayList<ContactModel> StoreContacts=new ArrayList<>() ;
    ArrayList<ContactModel> StoreContacts1=new ArrayList<>() ;
    ContactModel contactModel;
    private FilterType filterType;
    /*  boolean variable for Filtering */
    private boolean isSearchWithPrefix = false;

    String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.PHOTO_URI};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_activtiy);

        filterType    = FilterType.NAME;
        rlKeypad      =findViewById(R.id.rlKeypad);
        rlBack        =findViewById(R.id.rlBack);
        recyclerView  =findViewById(R.id.recyclerview);
        etSearch      =findViewById(R.id.etSearch);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        TelephonyManager manager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String carrierName = manager.getNetworkOperatorName();

        //Log.v("carriername",carrierName);

        GetContactsIntoArrayList();


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int arg1, int arg2, int arg3) {
                adapter_contacts.filter(filterType, charSequence.toString(), isSearchWithPrefix);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {

            }


        });


        rlKeypad.setOnClickListener(this);
        rlBack.setOnClickListener(this);
    }


    public void GetContactsIntoArrayList() {
        StoreContacts.clear();
     /*   cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null , ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1", null, "UPPER(" + ContactsContract.Contacts.DISPLAY_NAME + ") ASC" );

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
                StoreContacts1.add(contactModel);
            }
            else
            {

            }



}
*/
        StoreContacts= Paper.book().read("contactlist");
        StoreContacts1= Paper.book().read("contactlist");
            adapter_contacts = new Adapter_contacts(ContactActivity.this, StoreContacts);
            adapter_contacts1 = new Adapter_contacts1(this, StoreContacts1);
            recyclerView.setAdapter(adapter_contacts);



        }


    //======================================Dialog==============================================//
    public void keyboardDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dial_keypad);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        final LinearLayout llNumber=dialog.findViewById(R.id.llNumber);
        final TextView tvNumber=dialog.findViewById(R.id.tvNumber);

        final EditText etNumber=dialog.findViewById(R.id.etNumber);
        ImageView ivBack=dialog.findViewById(R.id.ivBack);
        final RecyclerView recyclerviewContact=dialog.findViewById(R.id.recyclerviewContact);
        recyclerviewContact.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        MyKeyboard keyboard = dialog.findViewById(R.id.keyboardview);

        etNumber.setRawInputType(InputType.TYPE_CLASS_TEXT);
        etNumber.setTextIsSelectable(true);

        // pass the InputConnection from the EditText to the keyboard
        InputConnection ic = etNumber.onCreateInputConnection(new EditorInfo());
        keyboard.setInputConnection(ic);



        etNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence charSequence, int arg1, int arg2, int arg3) {
                adapter_contacts1.filter(FilterType.NUMBER,charSequence.toString(), isSearchWithPrefix);
                if (StoreContacts1.isEmpty())
                {

                    if (etNumber.getText().toString().isEmpty())
                    {
                        llNumber.setVisibility(View.GONE);
                        recyclerviewContact.setVisibility(View.GONE);
                        tvNumber.setText(charSequence.toString());
                    }
                    else
                    {
                        llNumber.setVisibility(View.VISIBLE);
                        recyclerviewContact.setVisibility(View.GONE);
                        tvNumber.setText(charSequence.toString());

                    }

                }
                else
                {
                    llNumber.setVisibility(View.GONE);
                    recyclerviewContact.setVisibility(View.VISIBLE);
                    recyclerviewContact.setAdapter(adapter_contacts1);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }


        });

        llNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvNumber.getText().toString().length()==10)
                {
                    Intent i=new Intent(ContactActivity.this, RechargeActivity.class);
                    i.putExtra("name","New Number");
                    i.putExtra("number",tvNumber.getText().toString());
                    startActivity(i);
                }
                else
                {
                    EasyToast.warning(ContactActivity.this,"Please select 10 digit Mobile Number.");
                }

            }
        });


        dialog.show();

    }


    private KeyboardView.OnKeyboardActionListener mOnKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {
        @Override public void onKey(int primaryCode, int[] keyCodes)
        {
            //Here check the primaryCode to see which key is pressed
            //based on the android:codes property
            if(primaryCode==1)
            {
                Log.i("Key","You just pressed 1 button");
            }
        }

        @Override public void onPress(int arg0) {
        }

        @Override public void onRelease(int primaryCode) {
        }

        @Override public void onText(CharSequence text) {
        }

        @Override public void swipeDown() {
        }

        @Override public void swipeLeft() {
        }

        @Override public void swipeRight() {
        }

        @Override public void swipeUp() {
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.rlBack:
              //  startActivity(new Intent(this,RechargeActivity.class));

                finish();
                break;
            case R.id.rlKeypad:
                keyboardDialog();
                break;
        }
    }
}
