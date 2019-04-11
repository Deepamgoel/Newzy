package com.example.deepamgoel.newsy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.deepamgoel.newsy.R;
import com.example.deepamgoel.newsy.activity.WebViewActivity;
import com.example.deepamgoel.newsy.model.Model;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Model> list;

    public RecyclerViewAdapter(Context context, List<Model> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.news_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Model news = list.get(position);
        holder.headline.setText(news.getHeadline());
        holder.author.setText(news.getAuthor());
        holder.date.setText(news.getPublishedDate());
        Glide.with(context).load(news.getImageUrl()).into(holder.image);
        holder.parent.setOnClickListener(v -> {
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra("url", news.getArticleUrl());
            context.startActivity(intent);
        });
        holder.more.setOnClickListener(v -> {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.bottom_sheet_dialog, null);

            final BottomSheetDialog dialog = new BottomSheetDialog(context);
            dialog.setContentView(view);
            dialog.show();

            TextView share = view.findViewById(R.id.share);
            share.setOnClickListener(v1 -> {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, news.getArticleUrl());
                intent.setType("text/plain");
                context.startActivity(intent);
                dialog.dismiss();
            });

            TextView preview = view.findViewById(R.id.preview);
            preview.setOnClickListener(v2 -> {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", news.getArticleUrl());
                context.startActivity(intent);
                dialog.dismiss();
            });

            TextView bookmark = view.findViewById(R.id.bookmark);
            bookmark.setOnClickListener(v3 -> {
                Toast.makeText(context, R.string.news_bookmarked, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        });
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

        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
