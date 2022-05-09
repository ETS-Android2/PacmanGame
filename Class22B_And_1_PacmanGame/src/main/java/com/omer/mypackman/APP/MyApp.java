package com.omer.mypackman.APP;

import android.app.Application;

import com.omer.mypackman.DB.MSP;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MSP.initHelper(this);
    }
}
