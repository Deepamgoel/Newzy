package com.example.deepamgoel.newsy;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> sectionList;
    private RecyclerView recyclerView;

    SectionAdapter(Context context, ArrayList<String> SectionList) {
        this.context = context;
        this.sectionList = SectionList;
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
        String section = sectionList.get(position);
        holder.sectionName.setText(String.format("%s%s",
                section.substring(0, 1).toUpperCase(), section.substring(1)));

        holder.recyclerView.setAdapter(new NewsAdapter(context, holder.newsList, true));
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    void update(int position, ArrayList<Model> list) {
        SectionAdapter.ViewHolder viewHolder = (SectionAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder != null) {
            viewHolder.newsList.addAll(list);
            viewHolder.recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.section_name)
        TextView sectionName;
        @BindView(R.id.more_button)
        Button moreButton;
        @BindView(R.id.recycler_view_inner)
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
