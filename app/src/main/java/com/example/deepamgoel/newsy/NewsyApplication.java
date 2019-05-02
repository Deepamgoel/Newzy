package com.example.deepamgoel.newsy;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class NewsyApplication extends Application {

    private static SharedPreferences sPreferences;
    private static NewsyApplication instance;
    private RefWatcher refWatcher;

    public static SharedPreferences getPreferences() {
        return sPreferences;
    }

    public static NewsyApplication getAppContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to Perflib for heap analysis.
            // You should not init your app in this process.
            return;
        }
        super.onCreate();
        refWatcher = LeakCanary.install(this);

        instance = this;
        sPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    }

    public void mustDie(Object object) {
        if (refWatcher != null) {
            refWatcher.watch(object);
        }
    }

}
