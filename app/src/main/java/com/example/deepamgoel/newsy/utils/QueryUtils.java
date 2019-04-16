package com.example.deepamgoel.newsy.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.example.deepamgoel.newsy.models.Article;
import com.example.deepamgoel.newsy.models.Source;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.content.Context.CONNECTIVITY_SERVICE;

public abstract class QueryUtils {

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }

    public static String dateFormatter(String publishedDate) {
        SimpleDateFormat defaultFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        SimpleDateFormat requiredFormat = new SimpleDateFormat("MMM dd, yyyy",
                Locale.getDefault());

        String formattedDate = "";
        try {
            Date date = defaultFormat.parse(publishedDate);
            formattedDate = requiredFormat.format(date);
            long dateInMillis = defaultFormat.parse(publishedDate).getTime();
            long systemDateInMillis = Calendar.getInstance().getTimeInMillis();
            long differenceInMillis = systemDateInMillis - dateInMillis;
            long differenceInMinute = TimeUnit.MILLISECONDS.toMinutes(differenceInMillis);
            long differenceInHours = TimeUnit.MILLISECONDS.toHours(differenceInMillis);

            if (differenceInMinute < 60)
                return differenceInMinute + " minutes ago";
            if (differenceInHours < 24)
                return differenceInHours + " hours ago";
            if (differenceInHours > 24 && differenceInHours < 48)
                return "yesterday";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }
}
