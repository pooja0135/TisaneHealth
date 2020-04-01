package com.tisanehealth.Activity;

import android.app.Application;

import io.paperdb.Paper;

public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(this);
    }
}
