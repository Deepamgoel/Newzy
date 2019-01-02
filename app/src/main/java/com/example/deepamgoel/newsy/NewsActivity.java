package com.example.deepamgoel.newsy;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
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

import static com.example.deepamgoel.newsy.MainActivity.NEWS_LOADER_ID;
import static com.example.deepamgoel.newsy.MainActivity.REQUESTED_URL;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Model>> {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view_news)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_news)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.empty_view_news)
    RelativeLayout emptyView;
    @BindView(R.id.empty_text_view_news)
    TextView emptyStateTextView;
    @BindView(R.id.progress_bar_news)
    ProgressBar progressBar;

    private NewsAdapter newsAdapter;
    private DividerItemDecoration divider;
    private boolean isList = false;
    private ArrayList<Model> newsList = new ArrayList<>();

    private String SECTION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);

        SECTION = getIntent().getStringExtra(getString(R.string.section));

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(String.format("%s%s",
                SECTION.substring(0, 1).toUpperCase(), SECTION.substring(1)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newsAdapter = new NewsAdapter(this, newsList, isList);
        recyclerView.setAdapter(newsAdapter);
        divider = new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (QueryUtils.isConnected(this)) {
            setConnected(true);
            getLoaderManager().initLoader(NEWS_LOADER_ID, null, NewsActivity.this);
        } else
            setConnected(false);


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLoaderManager().destroyLoader(NEWS_LOADER_ID);
                if (QueryUtils.isConnected(NewsActivity.this)) {
                    getLoaderManager().restartLoader(NEWS_LOADER_ID, null, NewsActivity.this);
                    setConnected(true);
                } else
                    setConnected(false);

            }
        });

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
                newsAdapter = new NewsAdapter(this, newsList, isList);
                recyclerView.setAdapter(newsAdapter);

                if (isList)
                    recyclerView.addItemDecoration(divider);
                else
                    recyclerView.removeItemDecoration(divider);

                if (newsList == null) {
                    getLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
                }
                return true;

            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Model>> onCreateLoader(int id, Bundle args) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String pageSize = preferences.getString(getString(R.string.setting_page_size_key),
                getString(R.string.settings_max_page_default_value));

        String orderBy = preferences.getString(getString(R.string.setting_order_by_key),
                getString(R.string.settings_order_by_newest_value));

        Uri baseUri = Uri.parse(REQUESTED_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter(getString(R.string.section), SECTION);
        uriBuilder.appendQueryParameter(getString(R.string.query_order_by), orderBy);
        uriBuilder.appendQueryParameter(getString(R.string.query_page_size), pageSize);

        return new NewsAsyncTaskLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Model>> loader, List<Model> list) {
        // Hiding progress bar
        progressBar.setVisibility(View.GONE);
        // Stopping refreshing
        refreshLayout.setRefreshing(false);

        if (list.isEmpty()) {
            //Making Empty View Visible
            emptyView.setVisibility(View.VISIBLE);
            // Set empty state text to display "No news found."
            emptyStateTextView.setText(R.string.no_news);
        }

        if (!list.isEmpty()) {
            newsList.clear();
            newsList.addAll(list);
            newsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Model>> loader) {
        newsList.clear();
        newsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshLayout.setRefreshing(true);
        emptyView.setVisibility(View.GONE);
        getLoaderManager().destroyLoader(NEWS_LOADER_ID);
        if (QueryUtils.isConnected(this)) {
            getLoaderManager().restartLoader(NEWS_LOADER_ID, null, NewsActivity.this);
            setConnected(true);
        } else
            setConnected(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void retry(View view) {
        refreshLayout.setRefreshing(true);
        emptyView.setVisibility(View.GONE);
        getLoaderManager().destroyLoader(NEWS_LOADER_ID);
        if (QueryUtils.isConnected(this)) {
            getLoaderManager().restartLoader(NEWS_LOADER_ID, null, NewsActivity.this);
            setConnected(true);
        } else
            setConnected(false);
    }

    private void setConnected(boolean isConnected) {
        // Hide progress bar
        progressBar.setVisibility(View.GONE);
        // Stop refreshing
        refreshLayout.setRefreshing(false);
        if (!isConnected) {
            // Hide RecyclerView
            recyclerView.setVisibility(View.GONE);
            // Show empty view
            emptyView.setVisibility(View.VISIBLE);
            // Set empty state text to "No Internet Connection"
            emptyStateTextView.setText(R.string.no_internet);
        } else {
            // Hide RecyclerView
            recyclerView.setVisibility(View.VISIBLE);
            // Hide empty view
            emptyView.setVisibility(View.GONE);
            emptyStateTextView.setText("");
        }
    }
}
