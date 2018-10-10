package com.example.deepamgoel.newsy;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.io.IOException;
import java.util.List;


class NewsAsyncTaskLoader extends AsyncTaskLoader<List<Model>> {

    private List<Model> list;
    private String mUrl;

    NewsAsyncTaskLoader(Context context, String requestedUrl) {
        super(context);
        this.mUrl = requestedUrl;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<Model> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        String jsonResponse;
        try {
            jsonResponse = HttpHandler.makeHttpRequest(QueryUtils.createUrl(mUrl));
            list = QueryUtils.extractNews(jsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
