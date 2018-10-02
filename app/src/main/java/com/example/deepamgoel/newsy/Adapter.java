package com.example.deepamgoel.newsy;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context context;
    private ArrayList<News> list;

    Adapter(@NonNull Context context, ArrayList<News> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final News news = list.get(position);
        holder.headline.setText(news.getHeadline());
        holder.author.setText(news.getAuthor());
        holder.date.setText(news.getPublishedDate());
        holder.image.setImageBitmap(news.getImageBitmap());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(news.getWebUrl().toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    void clear() {
//        list.clear();
//        notifyItemRangeRemoved(0,list.size());
    }

    void addAll(List<News> news) {
        list.clear();
        list = (ArrayList<News>) news;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView image;
        TextView headline;
        TextView author;
        TextView date;

        ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.card);
            image = view.findViewById(R.id.thumbnail);
            headline = view.findViewById(R.id.headline);
            author = view.findViewById(R.id.author);
            date = view.findViewById(R.id.date);
        }
    }
}
