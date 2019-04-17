package com.example.deepamgoel.newsy.adapters;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
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

import com.example.deepamgoel.newsy.R;
import com.example.deepamgoel.newsy.models.Article;
import com.example.deepamgoel.newsy.utils.QueryUtils;
import com.example.deepamgoel.newsy.utils.WebUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final int LAYOUT_TYPE_LIST = R.layout.news_item_list;
    private static final int LAYOUT_TYPE_CARD = R.layout.news_item_card;

    private Context context;
    private List<Article> articleList;

    public RecyclerViewAdapter(Context context, List<Article> articleList) {
        this.context = context;
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String defaultView = preferences.getString(context.getString(R.string.settings_default_view_key), context.getString(R.string.settings_default_view_list_value));
        if (Objects.requireNonNull(defaultView).equals(context.getString(R.string.settings_default_view_list_value)))
            return LAYOUT_TYPE_LIST;
        else
            return LAYOUT_TYPE_CARD;
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Article article = articleList.get(position);

        holder.source.setText(article.getSource().getName());
        holder.title.setText(article.getTitle());
        holder.date.setText(QueryUtils.dateFormatter(article.getPublishedDate()));

        Uri uri = Uri.parse(article.getUrl());

        Picasso.get()
                .load(article.getUrlToImage())
                .error(android.R.color.transparent)
                .placeholder(android.R.color.transparent)
                .into(holder.image);

        holder.parent.setOnClickListener(v -> WebUtils.loadUrl(context, uri));
        holder.more.setOnClickListener(v -> onClickMenu(uri));
        holder.parent.setOnLongClickListener(v -> {
            onClickMenu(uri);
            return false;
        });

    }

    private void onClickMenu(Uri uri) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context)
                .inflate(R.layout.bottom_sheet_dialog, null);

        final BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(view);
        dialog.show();

        TextView preview = view.findViewById(R.id.preview);
        preview.setOnClickListener(v -> {
            WebUtils.loadUrl(context, uri);
            dialog.dismiss();
        });

        TextView share = view.findViewById(R.id.share);
        share.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, uri.toString());
            intent.setType("text/plain");
            context.startActivity(intent);
            dialog.dismiss();
        });

        TextView link = view.findViewById(R.id.get_link);
        link.setOnClickListener(v -> {
            ClipboardManager clipboardManager =
                    (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(context.getString(R.string.label_copy_link), uri.toString());
            clipboardManager.setPrimaryClip(clip);
            Toast.makeText(context, R.string.msg_link_copied, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        TextView bookmark = view.findViewById(R.id.bookmark);
        bookmark.setOnClickListener(v -> {
            // TODO: 17-04-2019 implement bookmark
            Toast.makeText(context, R.string.msg_news_bookmarked, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.parent)
        LinearLayout parent;
        @BindView(R.id.source)
        TextView source;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.image)
        ImageView image;
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
