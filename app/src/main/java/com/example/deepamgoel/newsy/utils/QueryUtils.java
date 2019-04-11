package com.example.deepamgoel.newsy.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.example.deepamgoel.newsy.model.Model;

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

public class QueryUtils {

    private QueryUtils() {
    }

    static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    static List<Model> extractNews(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        List<Model> newsList = new ArrayList<>();

        try {
            JSONObject baseJsonObject = new JSONObject(jsonResponse);
            JSONObject response = baseJsonObject.getJSONObject("response");
            String status = response.getString("status");

            if (status.equals("ok")) {
                JSONArray results = response.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject news = results.getJSONObject(i);

                    JSONObject fields = news.getJSONObject("fields");
                    String headline = fields.getString("headline");
                    String url = news.getString("webUrl");
                    String imageUrl = fields.getString("thumbnail");
                    String dateTime = news.getString("webPublicationDate");
                    String section = news.getString("sectionName");
                    JSONArray tags = news.getJSONArray("tags");

                    // Combining author and publication
                    String author = "";
                    String SEPARATOR = " | ";
                    if (!tags.isNull(0)) {
                        JSONObject contributor = tags.getJSONObject(0);
                        author = contributor.getString("webTitle");
                    }
                    if (!tags.isNull(1)) {
                        JSONObject publication = tags.getJSONObject(1);
                        author = author + SEPARATOR + publication.getString("webTitle");
                    }

                    //Formatting date
                    String formattedDate = dateFormatter(dateTime);
                    Model newsObject = new Model(headline, imageUrl, url, author, formattedDate, section);
                    newsList.add(newsObject);
                }
            } else
                return null;
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }
        return newsList;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }

    private static String dateFormatter(String dateTime) {
        SimpleDateFormat defaultFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        SimpleDateFormat requiredFormat = new SimpleDateFormat("MMM dd, yyyy",
                Locale.getDefault());

        String formattedDate = "";
        try {
            Date date = defaultFormat.parse(dateTime);
            formattedDate = requiredFormat.format(date);
            long dateInMillis = defaultFormat.parse(dateTime).getTime();
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