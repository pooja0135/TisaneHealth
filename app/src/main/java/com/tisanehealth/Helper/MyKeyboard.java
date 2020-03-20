package com.tisanehealth.Helper;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tisanehealth.R;

public class MyKeyboard extends LinearLayout implements View.OnClickListener {

    // constructors
    public MyKeyboard(Context context) {
        this(context, null, 0);
    }

    public MyKeyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    // keyboard keys (TextViews)
    private TextView mTextView1;
    private TextView mTextView2;
    private TextView mTextView3;
    private TextView mTextView4;
    private TextView mTextView5;
    private TextView mTextView6;
    private TextView mTextView7;
    private TextView mTextView8;
    private TextView mTextView9;
    private TextView mTextView0;

    ImageView ivCross;


    // This will map the TextView resource id to the String value that we want to
    // input when that TextView is clicked.
    SparseArray<String> keyValues = new SparseArray<>();

    // Our communication link to the EditText
    InputConnection inputConnection;

    private void init(Context context, AttributeSet attrs) {

        // initialize TextViews
        LayoutInflater.from(context).inflate(R.layout.keyboard, this, true);
        mTextView1 = (TextView) findViewById(R.id.TextView_1);
        mTextView2 = (TextView) findViewById(R.id.TextView_2);
        mTextView3 = (TextView) findViewById(R.id.TextView_3);
        mTextView4 = (TextView) findViewById(R.id.TextView_4);
        mTextView5 = (TextView) findViewById(R.id.TextView_5);
        mTextView6 = (TextView) findViewById(R.id.TextView_6);
        mTextView7 = (TextView) findViewById(R.id.TextView_7);
        mTextView8 = (TextView) findViewById(R.id.TextView_8);
        mTextView9 = (TextView) findViewById(R.id.TextView_9);
        mTextView0 = (TextView) findViewById(R.id.TextView_0);
        ivCross     =  findViewById(R.id.ivCross);

        // set TextView click listeners
        mTextView1.setOnClickListener(this);
        mTextView2.setOnClickListener(this);
        mTextView3.setOnClickListener(this);
        mTextView4.setOnClickListener(this);
        mTextView5.setOnClickListener(this);
        mTextView6.setOnClickListener(this);
        mTextView7.setOnClickListener(this);
        mTextView8.setOnClickListener(this);
        mTextView9.setOnClickListener(this);
        mTextView0.setOnClickListener(this);
        ivCross.setOnClickListener(this);

        // map TextViews IDs to input strings
        keyValues.put(R.id.TextView_1, "1");
        keyValues.put(R.id.TextView_2, "2");
        keyValues.put(R.id.TextView_3, "3");
        keyValues.put(R.id.TextView_4, "4");
        keyValues.put(R.id.TextView_5, "5");
        keyValues.put(R.id.TextView_6, "6");
        keyValues.put(R.id.TextView_7, "7");
        keyValues.put(R.id.TextView_8, "8");
        keyValues.put(R.id.TextView_9, "9");
        keyValues.put(R.id.TextView_0, "0");

    }

    @Override
    public void onClick(View v) {

        // do nothing if the InputConnection has not been set yet
        if (inputConnection == null) return;

        // Delete text or input key value
        // All communication goes through the InputConnection
        if (v.getId() == R.id.ivCross) {
            CharSequence selectedText = inputConnection.getSelectedText(0);
            if (TextUtils.isEmpty(selectedText)) {
                // no selection, so delete previous character
                inputConnection.deleteSurroundingText(1, 0);
            } else {
                // delete the selection
                inputConnection.commitText("", 1);
            }
        } else {
            String value = keyValues.get(v.getId());
            inputConnection.commitText(value, 1);
        }
    }

    // The activity (or some parent or controller) must give us
    // a reference to the current EditText's InputConnection
    public void setInputConnection(InputConnection ic) {
        this.inputConnection = ic;
    }
}
