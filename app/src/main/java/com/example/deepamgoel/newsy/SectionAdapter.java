package com.example.deepamgoel.newsy;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.deepamgoel.newsy.MainActivity.REQUESTED_URL;


class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.ViewHolder>
        implements LoaderManager.LoaderCallbacks<List<Model>> {

    private Context context;
    private ViewHolder viewHolder;
    private ArrayList<String> sectionList;
    private LoaderManager loaderManager;
    private ArrayList<NewsAdapter> newsAdapters = new ArrayList<>();
    private ArrayList<ArrayList<Model>> newsList = new ArrayList<>();

    SectionAdapter(Context context, ArrayList<String> SectionList, LoaderManager loaderManager) {
        this.context = context;
        this.sectionList = SectionList;
        this.loaderManager = loaderManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        viewHolder = holder;
        String section = sectionList.get(position);
        holder.sectionName.setText(String.format("%s%s",
                section.substring(0, 1).toUpperCase(), section.substring(1)));

        newsList.add(position, new ArrayList<Model>());
        newsAdapters.add(position, new NewsAdapter(context, newsList.get(position), false));
        holder.recyclerView.setAdapter(newsAdapters.get(position));
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        if (QueryUtils.isConnected(context)) {
            loaderManager.initLoader(position, null, this);
        }
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    @Override
    public Loader<List<Model>> onCreateLoader(int id, Bundle args) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String pageSize = preferences.getString(context.getString(R.string.setting_page_size_key),
                context.getString(R.string.settings_max_page_default_value));

        String orderBy = preferences.getString(context.getString(R.string.setting_order_by_key),
                context.getString(R.string.settings_order_by_newest_value));

        Uri baseUri = Uri.parse(REQUESTED_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter(context.getString(R.string.section), sectionList.get(id));
        uriBuilder.appendQueryParameter(context.getString(R.string.query_order_by), orderBy);
        uriBuilder.appendQueryParameter(context.getString(R.string.query_page_size), pageSize);
        return new NewsAsyncTaskLoader(context, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Model>> loader, List<Model> list) {

        // Hiding progress bar
        viewHolder.progressBar.setVisibility(View.GONE);

        if (list.isEmpty()) {
            //Making Empty View Visible
            viewHolder.emptyStateTextView.setVisibility(View.VISIBLE);
            // Set empty state text to display "No news found."
            viewHolder.emptyStateTextView.setText(R.string.no_news);
        }
        if (!list.isEmpty()) {
            newsList.get(loader.getId()).addAll(list);
            newsAdapters.get(loader.getId()).notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Model>> loader) {
        newsList.clear();
        newsAdapters.get(loader.getId()).notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.section_name)
        TextView sectionName;
        @BindView(R.id.more_button)
        Button moreButton;
        @BindView(R.id.recycler_view_inner)
        RecyclerView recyclerView;
        @BindView(R.id.empty_view_text_view)
        TextView emptyStateTextView;
        @BindView(R.id.progress_bar)
        ProgressBar progressBar;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NewsActivity.class);
                    intent.putExtra(context.getString(R.string.section), sectionList.get(getAdapterPosition()).toLowerCase());
                    context.startActivity(intent);
                }
            });
        }
    }
}
