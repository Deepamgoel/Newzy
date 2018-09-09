package com.example.deepamgoel.newsy;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    private static final int NEWS_LOADER_ID = 1;

    private static final String REQUESTED_URL =
            "https://content.guardianapis.com/search?q=Technology&format=json&order-by=newest&page-size=20&from-date=2018-09-01&show-fields=headline,thumbnail,short-url&show-tags=contributor,publication&api-key=751d026c-5315-4412-824f-90852ee18451";

    private Adapter adapter;
    private ProgressBar progressBar;
    private TextView emptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView newsListView = findViewById(R.id.list);
        emptyStateTextView = findViewById(R.id.empty_view);
        progressBar = findViewById(R.id.progress_bar);

        newsListView.setEmptyView(emptyStateTextView);

        adapter = new Adapter(this, new ArrayList<News>());
        newsListView.setAdapter(adapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news = adapter.getItem(position);
                Uri uri = Uri.parse(news.getWebUrl().toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            // Hiding progress bar
            progressBar.setVisibility(View.GONE);

            // Set empty state text to "No Internet Connection"
            emptyStateTextView.setText(R.string.no_internet);
        } else
            getLoaderManager().initLoader(NEWS_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsAsyncTaskLoader(this, REQUESTED_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        // Hiding progress bar
        progressBar.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
        emptyStateTextView.setText(R.string.no_news);

        // Clear the adapter of previous earthquake data
        adapter.clear();

        if (news != null && !news.isEmpty()) {
            adapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter.clear();
    }

    private static class NewsAsyncTaskLoader extends AsyncTaskLoader<List<News>> {

        String url;
        List<News> cache;

        NewsAsyncTaskLoader(Context context, String requestedUrl) {
            super(context);
            this.url = requestedUrl;
        }

        @Override
        public List<News> loadInBackground() {
            if (url == null) {
                return null;
            }

            return QueryUtils.fetchNewsData(url);
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (cache != null)
                deliverResult(cache);
            else
                forceLoad();
        }

        @Override
        public void deliverResult(List<News> data) {
            super.deliverResult(data);
            cache = data;
        }
    }
}
