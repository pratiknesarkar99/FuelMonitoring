package com;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.example.fuelmonitoring.RunAppBgService;

public class MyApp extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        super.onCreate();

        startService(new Intent(this, RunAppBgService.class));
    }

    public static Context getContext() {
        return mContext;
    }
}
