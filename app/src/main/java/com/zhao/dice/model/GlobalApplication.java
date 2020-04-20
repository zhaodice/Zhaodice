package com.zhao.dice.model;

import android.app.Application;
import android.os.Environment;

public class GlobalApplication extends Application {
    public static final String SDCARD=Environment.getExternalStorageDirectory().getPath();
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
