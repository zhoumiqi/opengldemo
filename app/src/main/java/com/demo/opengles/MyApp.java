package com.demo.opengles;

import android.app.Application;

public class MyApp extends Application {
    private static MyApp sInstance;

    public static MyApp getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}
