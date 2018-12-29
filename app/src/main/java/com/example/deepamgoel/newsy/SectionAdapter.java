package com.example.deepamgoel.newsy;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.deepamgoel.newsy.MainActivity.REQUESTED_URL;


class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.ViewHolder>
        implements LoaderManager.LoaderCallbacks<List<Model>> {


    private Context context;
    private ArrayList<String> sectionList;
    private LoaderManager loaderManager;
    private RecyclerView recyclerView;

    SectionAdapter(Context context, ArrayList<String> SectionList, LoaderManager loaderManager) {
        this.context = context;
        this.sectionList = SectionList;
        this.loaderManager = loaderManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        recyclerView = (RecyclerView) parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        String section = sectionList.get(holder.getLayoutPosition());
        holder.sectionName.setText(String.format("%s%s",
                section.substring(0, 1).toUpperCase(), section.substring(1)));

        NewsAdapter innerAdapter = new NewsAdapter(context, holder.newsList, true);
        holder.recyclerView.setAdapter(innerAdapter);
        holder.recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerView.setLayoutManager(layoutManager);

        if (QueryUtils.isConnected(context)) {
            Log.d("TAG", "onBindView: " + section);
            loaderManager.initLoader(holder.getLayoutPosition(), null, this);
        }
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    void refresh() {
    }

    @Override
    public Loader<List<Model>> onCreateLoader(int id, Bundle args) {
        Log.d("TAG", "onCreateLoader: Section " + sectionList.get(id));
        Log.d("TAG", "onCreateLoader: ViewHolder " + recyclerView.findViewHolderForLayoutPosition(id));

        String pageSize = "5";
        String orderBy = context.getString(R.string.settings_order_by_newest_value);
        String section = sectionList.get(id).toLowerCase();

        Uri baseUri = Uri.parse(REQUESTED_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter(context.getString(R.string.section), section);
        uriBuilder.appendQueryParameter(context.getString(R.string.query_order_by), orderBy);
        uriBuilder.appendQueryParameter(context.getString(R.string.query_page_size), pageSize);

        return new NewsAsyncTaskLoader(context, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Model>> loader, List<Model> list) {
        Log.d("TAG", "onLoadFinished: Section " + sectionList.get(loader.getId()));
        Log.d("TAG", "onLoadFinished: ViewHolder " + recyclerView.findViewHolderForLayoutPosition(loader.getId()));

        ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForLayoutPosition(loader.getId());

        if (!list.isEmpty() && holder != null) {
            holder.newsList.addAll(list);
            holder.recyclerView.getAdapter().notifyDataSetChanged();
        }
        if (holder == null)
            Log.d("TAG", "onLoadFinished: Null Holder");
    }

    @Override
    public void onLoaderReset(Loader<List<Model>> loader) {
        ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForLayoutPosition(loader.getId());
        if (holder != null) {
            holder.newsList.clear();
            holder.recyclerView.getAdapter().notifyDataSetChanged();
        } else
            Log.d("TAG", "onLoaderReset: Null Holder");
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name_text_view_section)
        TextView sectionName;
        @BindView(R.id.more_button_section)
        Button moreButton;
        @BindView(R.id.recycler_view_section)
        RecyclerView recyclerView;
        ArrayList<Model> newsList = new ArrayList<>();

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
