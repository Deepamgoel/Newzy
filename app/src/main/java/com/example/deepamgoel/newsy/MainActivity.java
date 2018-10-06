package com.example.deepamgoel.newsy;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    private static final int NEWS_LOADER_ID = 1;
    private static final String REQUESTED_URL =
            "https://content.guardianapis.com/search?q=Technology&section=technology&format=json&show-fields=headline,thumbnail,short-url&show-tags=contributor,publication&api-key=751d026c-5315-4412-824f-90852ee18451";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.empty_view)
    RelativeLayout emptyView;
    @BindView(R.id.empty_view_text_view)
    TextView emptyStateTextView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private Adapter adapter;
    private boolean isList = false;
    private List<News> newsList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        adapter = new Adapter(this, new ArrayList<News>(), isList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (isConnected())
            getLoaderManager().initLoader(NEWS_LOADER_ID, null, this);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLoaderManager().destroyLoader(NEWS_LOADER_ID);
                if (isConnected())
                    getLoaderManager().restartLoader(NEWS_LOADER_ID, null, MainActivity.this);
            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        if (networkInfo == null || !networkInfo.isConnected()) {
            // Hiding progress bar
            progressBar.setVisibility(View.GONE);
            // Stopping refreshing
            refreshLayout.setRefreshing(false);

            //Showing empty view
            emptyView.setVisibility(View.VISIBLE);
            // Set empty state text to "No Internet Connection"
            emptyStateTextView.setText(R.string.no_internet);
            return false;
        } else {
            emptyView.setVisibility(View.GONE);
            emptyStateTextView.setText("");
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.layout_type:
                isList = !isList;
                item.setIcon(isList ? R.drawable.ic_view_list_black_24dp :
                        R.drawable.ic_view_stream_black_24dp);
                adapter = new Adapter(this, new ArrayList<News>(), isList);
                recyclerView.setAdapter(adapter);
                if (newsList != null) {
                    adapter.clear();
                    adapter.addAll(newsList);
                } else
                    getLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
                break;
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String pageSize = preferences.getString(getString(R.string.setting_page_size_key),
                getString(R.string.settings_max_page_default_value));

        String orderBy = preferences.getString(getString(R.string.setting_order_by_key),
                getString(R.string.settings_order_by_newest_value));

        Uri baseUri = Uri.parse(REQUESTED_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter(getString(R.string.query_order_by), orderBy);
        uriBuilder.appendQueryParameter(getString(R.string.query_page_size), pageSize);

        return new NewsAsyncTaskLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        // Hiding progress bar
        progressBar.setVisibility(View.GONE);
        // Stopping refreshing
        refreshLayout.setRefreshing(false);

        if (news.isEmpty()) {
            //Making Empty View Visible
            emptyView.setVisibility(View.VISIBLE);
            // Set empty state text to display "No news found."
            emptyStateTextView.setText(R.string.no_news);
        }
        // Clear the adapter of previous news data
        adapter.clear();

        if (!news.isEmpty()) {
            adapter.addAll(news);
            newsList = news;
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter.clear();
        newsList = null;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshLayout.setRefreshing(true);
        emptyView.setVisibility(View.GONE);
        getLoaderManager().destroyLoader(NEWS_LOADER_ID);
        if (isConnected())
            getLoaderManager().restartLoader(NEWS_LOADER_ID, null, MainActivity.this);
    }

    public void retry(View view) {
        refreshLayout.setRefreshing(true);
        emptyView.setVisibility(View.GONE);
        getLoaderManager().destroyLoader(NEWS_LOADER_ID);
        if (isConnected())
            getLoaderManager().restartLoader(NEWS_LOADER_ID, null, MainActivity.this);

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
