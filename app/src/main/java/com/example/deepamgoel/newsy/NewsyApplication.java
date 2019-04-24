package com.example.deepamgoel.newsy;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.squareup.leakcanary.LeakCanary;

public class NewsyApplication extends Application {

    private static SharedPreferences sPreferences;
    private static NewsyApplication instance;

    public static SharedPreferences getPreferences() {
        return sPreferences;
    }

    public static NewsyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        sPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }


}
