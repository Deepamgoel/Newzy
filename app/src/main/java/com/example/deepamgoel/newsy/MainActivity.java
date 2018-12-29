package com.example.deepamgoel.newsy;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Model>> {


    static final int NEWS_LOADER_ID = 1;
    static final String REQUESTED_URL =
            "https://content.guardianapis.com/search?q=&format=json&show-fields=headline,thumbnail,short-url&show-tags=contributor,publication&page-size=5&api-key=751d026c-5315-4412-824f-90852ee18451";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view_main)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_main)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.empty_view_main)
    RelativeLayout emptyView;
    @BindView(R.id.empty_text_view_main)
    TextView emptyStateTextView;
    @BindView(R.id.progress_bar_main)
    ProgressBar progressBar;

    private SectionAdapter sectionAdapter;
    private List<String> sectionList = new ArrayList<>();
    private Map<String, List<Model>> sectionNewsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        loadSections();
        sectionAdapter = new SectionAdapter(this, sectionList, sectionNewsMap);
        sectionAdapter.setHasStableIds(true);
        recyclerView.setAdapter(sectionAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);

        if (QueryUtils.isConnected(this)) {
            setConnected(true);
            for (int i = 0; i < sectionList.size(); i++) {
                getLoaderManager().initLoader(i, null, this);
            }
        } else
            setConnected(false);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                if (QueryUtils.isConnected(MainActivity.this)) {
                    setConnected(true);
                    loadSectionNews();
                } else
                    setConnected(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.layout_type);
        item.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshLayout.setRefreshing(true);
        emptyView.setVisibility(View.GONE);
        if (QueryUtils.isConnected(this)) {
            setConnected(true);
            loadSectionNews();
        } else
            setConnected(false);
    }

    @Override
    public Loader<List<Model>> onCreateLoader(int id, Bundle args) {
        Log.d("TAG", "onCreateLoader: Section " + sectionList.get(id));

        String pageSize = "5";
        String orderBy = getString(R.string.settings_order_by_newest_value);
        String section = sectionList.get(id).toLowerCase();

        Uri baseUri = Uri.parse(REQUESTED_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter(getString(R.string.section), section);
        uriBuilder.appendQueryParameter(getString(R.string.query_order_by), orderBy);
        uriBuilder.appendQueryParameter(getString(R.string.query_page_size), pageSize);

        return new NewsAsyncTaskLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Model>> loader, List<Model> list) {

        if (!list.isEmpty()) {
            sectionNewsMap.put(sectionList.get(loader.getId()), list);
            sectionAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Model>> loader) {
        sectionNewsMap.get(sectionList.get(loader.getId())).clear();
        sectionAdapter.notifyDataSetChanged();
    }

    public void retry(View view) {
        refreshLayout.setRefreshing(true);
        if (QueryUtils.isConnected(this)) {
            setConnected(true);
            loadSectionNews();
        } else
            setConnected(false);
    }

    private void loadSections() {
        sectionList.add("World");
        sectionList.add("Politics");
        sectionList.add("Sport");
        sectionList.add("Film");
        sectionList.add("Technology");
    }

    private void loadSectionNews() {
        for (int i = 0; i < sectionList.size(); i++) {
            getLoaderManager().initLoader(i, null, this);
        }
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
            // Hiding empty view
            emptyView.setVisibility(View.GONE);
            emptyStateTextView.setText("");
        }
    }
}
