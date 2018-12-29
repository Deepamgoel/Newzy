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
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.ViewHolder> {


    private Context context;
    private List<String> sectionList;
    private Map<String, ArrayList<Model>> sectionListMap;

    SectionAdapter(Context context, List<String> SectionList, Map<String, ArrayList<Model>> sectionListMap) {
        this.context = context;
        this.sectionList = SectionList;
        this.sectionListMap = sectionListMap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        String section = sectionList.get(holder.getLayoutPosition());
        holder.sectionName.setText(String.format("%s%s",
                section.substring(0, 1).toUpperCase(), section.substring(1)));

        if (sectionListMap.size() != 0 && sectionListMap.containsKey(sectionList.get(position))) {
            holder.newsList = sectionListMap.get(sectionList.get(position));
            NewsAdapter innerAdapter = new NewsAdapter(context, holder.newsList, true);
            holder.recyclerView.setAdapter(innerAdapter);
            holder.recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    context, LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
        }
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
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
