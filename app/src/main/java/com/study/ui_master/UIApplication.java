package com.study.ui_master;

import android.app.Application;

public class UIApplication extends Application {
    private static UIApplication uiApplication;

    public static UIApplication getInstance() {
        return uiApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        uiApplication = this;
    }
}
