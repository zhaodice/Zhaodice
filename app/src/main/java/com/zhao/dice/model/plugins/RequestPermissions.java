package com.zhao.dice.model.plugins;

import android.Manifest;

public class RequestPermissions {
    public static final String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
}
