package com.example.deepamgoel.newsy;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Model> list;
    private boolean isList;

    NewsAdapter(@NonNull Context context, ArrayList<Model> list, boolean isList) {
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Model news = list.get(position);
        holder.headline.setText(news.getHeadline());
        holder.author.setText(news.getAuthor());
        holder.date.setText(news.getPublishedDate());
        Glide.with(context).load(news.getImageUrl()).into(holder.image);
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", news.getArticleUrl());
                context.startActivity(intent);
            }
        });

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = null;
                if (inflater != null) {
                    view = inflater.inflate(R.layout.bottom_sheet_dialog, null);
                }
                final BottomSheetDialog dialog = new BottomSheetDialog(context);
                dialog.setContentView(view);
                dialog.show();

                TextView share = view.findViewById(R.id.share);
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT, news.getArticleUrl());
                        intent.setType("text/plain");
                        context.startActivity(intent);
                        dialog.dismiss();
                    }
                });

                TextView preview = view.findViewById(R.id.preview);
                preview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("url", news.getArticleUrl());
                        context.startActivity(intent);
                        dialog.dismiss();
                    }
                });

                TextView bookmark = view.findViewById(R.id.bookmark);
                bookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Model Bookmarked", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                bookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, R.string.news_bookmarked, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.parent)
        LinearLayout parent;
        @BindView(R.id.thumbnail)
        ImageView image;
        @BindView(R.id.headline)
        TextView headline;
        @BindView(R.id.author)
        TextView author;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.more)
        ImageButton more;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
