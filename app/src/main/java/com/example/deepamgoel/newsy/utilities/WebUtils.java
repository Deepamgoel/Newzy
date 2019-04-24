package com.example.deepamgoel.newsy.utilities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsIntent;

import com.example.deepamgoel.newsy.R;

import static com.example.deepamgoel.newsy.NewsyApplication.getPreferences;

public abstract class WebUtils {

    public static void loadUrl(Context context, Uri uri) {
        if (getPreferences().getBoolean(context.getString(R.string.settings_default_browser_key), true)) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent intent = builder.build();
            builder.addDefaultShareMenuItem();
            intent.launchUrl(context, uri);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            context.startActivity(intent);
        }
    }
}