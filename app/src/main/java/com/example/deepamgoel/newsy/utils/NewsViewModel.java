package com.example.deepamgoel.newsy.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.deepamgoel.newsy.model.Model;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class NewsViewModel extends AndroidViewModel {

    private final NewsLiveData data;
    private String mUrl;

    NewsViewModel(@NonNull Application application, String mUrl) {
        super(application);
        this.mUrl = mUrl;
        data = new NewsLiveData(application);
    }

    public LiveData<List<Model>> getData() {
        return data;
    }

    public class NewsLiveData extends LiveData<List<Model>> {

        NewsLiveData(Context context) {
            loadData();
        }

        @SuppressLint("StaticFieldLeak")
        private void loadData() {
            new AsyncTask<URL, Void, List<Model>>() {
                @Override
                protected List<Model> doInBackground(URL... urls) {
                    if (urls == null)
                        return null;

                    List<Model> data = new ArrayList<>();
                    for (URL url : urls) {
                        try {
                            String jsonResponse = HttpHandler.makeHttpRequest(url);
                            data = QueryUtils.extractNews(jsonResponse);
                            return data;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return data;
                }

                @Override
                protected void onPostExecute(List<Model> list) {
                    super.onPostExecute(list);
                    data.setValue(list);
                }
            }.execute(QueryUtils.createUrl(mUrl));
        }
    }

}

