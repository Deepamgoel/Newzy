package com.example.deepamgoel.newsy;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> list;

    SectionAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String section = list.get(position);
        holder.sectionName.setText(String.format("%s%s",
                section.substring(0, 1).toUpperCase(), section.substring(1)));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.section_name)
        TextView sectionName;
        @BindView(R.id.more_button)
        Button moreButton;
        @BindView(R.id.recycler_view_inner)
        RecyclerView recyclerView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NewsActivity.class);
                    intent.putExtra(context.getString(R.string.section), list.get(getAdapterPosition()).toLowerCase());
                    context.startActivity(intent);
                }
            });
        }
    }
}
