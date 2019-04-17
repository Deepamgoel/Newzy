package com.example.deepamgoel.newsy.utils;

import android.content.Context;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsIntent;

public abstract class WebUtils {

    public static void loadUrl(Context context, Uri uri) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent intent = builder.build();
        builder.addDefaultShareMenuItem();
        intent.launchUrl(context, uri);
    }

}
