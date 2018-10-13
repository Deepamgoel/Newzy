package com.example.deepamgoel.newsy;

import android.content.Intent;
import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity /*implements LoaderManager.LoaderCallbacks<List<Model>>*/ {

    static final int NEWS_LOADER_ID = 1;
    static final String REQUESTED_URL =
            "https://content.guardianapis.com/search?q=&format=json&show-fields=headline,thumbnail,short-url&show-tags=contributor,publication&page-size=5&api-key=751d026c-5315-4412-824f-90852ee18451";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view_outer)
    RecyclerView recyclerView;
    @BindView(R.id.swipe)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.empty_view)
    RelativeLayout emptyView;
    @BindView(R.id.empty_view_text_view)
    TextView emptyStateTextView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private SectionAdapter sectionAdapter;
    private ArrayList<String> sectionList = new ArrayList<>();
//    private ArrayList<Model> newsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        loadSections();
        sectionAdapter = new SectionAdapter(this, sectionList, getLoaderManager());
        recyclerView.setAdapter(sectionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (QueryUtils.isConnected(this)) {
            setConnected(true);
        } else
            setConnected(false);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (QueryUtils.isConnected(MainActivity.this)) {
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
                break;

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
        } else
            setConnected(false);
    }

    public void retry(View view) {
        refreshLayout.setRefreshing(true);
        if (QueryUtils.isConnected(this)) {
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
