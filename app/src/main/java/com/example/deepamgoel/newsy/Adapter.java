package com.example.deepamgoel.newsy;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context context;
    private ArrayList<News> list;
    private boolean isList;

    Adapter(@NonNull Context context, ArrayList<News> list, boolean isList) {
        this.context = context;
        this.list = list;
        this.isList = isList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(
                isList ? R.layout.news_item_list : R.layout.news_item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final News news = list.get(position);
        holder.headline.setText(news.getHeadline());
        holder.author.setText(news.getAuthor());
        holder.date.setText(news.getPublishedDate());
        holder.image.setImageBitmap(news.getImageBitmap());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", news.getWebUrl().toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    void addAll(List<News> news) {
        list = (ArrayList<News>) news;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout parent;
        ImageView image;
        TextView headline;
        TextView author;
        TextView date;

        ViewHolder(View view) {
            super(view);
            parent = view.findViewById(R.id.parent);
            image = view.findViewById(R.id.thumbnail);
            headline = view.findViewById(R.id.headline);
            author = view.findViewById(R.id.author);
            date = view.findViewById(R.id.date);
        }
    }
}
