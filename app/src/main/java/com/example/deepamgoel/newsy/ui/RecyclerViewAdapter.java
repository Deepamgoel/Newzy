package com.example.deepamgoel.newsy.ui;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.HapticFeedbackConstants;
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
import com.example.deepamgoel.newsy.model.Article;
import com.example.deepamgoel.newsy.ui.bookmark.BookmarksFragment;
import com.example.deepamgoel.newsy.util.QueryUtils;
import com.example.deepamgoel.newsy.util.WebUtils;
import com.example.deepamgoel.newsy.viewmodel.BookmarksViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.deepamgoel.newsy.NewsyApplication.getPreferences;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private BookmarksViewModel mViewModel;
    private String mParent;

    private List<Article> mArticles;

    public RecyclerViewAdapter(Context context, BookmarksViewModel viewModel, String parent) {
        this.mContext = context;
        this.mViewModel = viewModel;
        this.mParent = parent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (getPreferences().getString(mContext.getString(R.string.settings_default_view_key), mContext.getString(R.string.settings_default_view_list)).equals(mContext.getString(R.string.settings_default_view_list)))
            return R.layout.news_item_list;
        else
            return R.layout.news_item_card;
    }

    @Override
    public int getItemCount() {
        if (mArticles != null)
            return mArticles.size();
        else
            return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mArticles != null && mArticles.size() > 0) {
            final Article article = mArticles.get(position);

            holder.source.setText(article.getSource().getName());
            holder.title.setText(article.getTitle());
            holder.date.setText(QueryUtils.dateFormatter(article.getPublishedDate()));

            String url = article.getUrlToImage();

            if (url != null && !url.isEmpty())
                Picasso.get()
                        .load(url)
                        .error(android.R.color.transparent)
                        .placeholder(android.R.color.transparent)
                        .into(holder.image);

            Uri uri = Uri.parse(article.getUrl());

            holder.parent.setOnClickListener(v -> WebUtils.loadUrl(mContext, uri));
            holder.more.setOnClickListener(v -> onClickMore(uri, article));
            holder.parent.setOnLongClickListener(v -> {
                v.setHapticFeedbackEnabled(true);
                v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                onClickMore(uri, article);
                return false;
            });
        }
    }

    private void onClickMore(Uri uri, Article article) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.bottom_sheet_dialog, null);

        final BottomSheetDialog dialog = new BottomSheetDialog(mContext);
        dialog.setContentView(view);
        dialog.show();

        TextView previewTextView = view.findViewById(R.id.preview);
        previewTextView.setOnClickListener(v -> {
            WebUtils.loadUrl(mContext, uri);
            dialog.dismiss();
        });

        TextView shareTextView = view.findViewById(R.id.share);
        shareTextView.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, uri.toString());
            intent.setType("text/plain");
            mContext.startActivity(intent);
            dialog.dismiss();
        });

        TextView linkTextView = view.findViewById(R.id.get_link);
        linkTextView.setOnClickListener(v -> {
            ClipboardManager clipboardManager =
                    (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(mContext.getString(R.string.label_copy_link), uri.toString());
            clipboardManager.setPrimaryClip(clip);
            Toast.makeText(mContext, R.string.msg_link_copied, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        TextView bookmarkTextView = view.findViewById(R.id.bookmark);
        bookmarkTextView.setOnClickListener(v -> {
            if (mViewModel != null) {
                mViewModel.insert(article);
                Toast.makeText(mContext, R.string.msg_news_bookmarked, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        TextView removeBookmarkTextView = view.findViewById(R.id.remove_bookmark);
        removeBookmarkTextView.setOnClickListener(v -> {
            if (mViewModel != null) {
                mViewModel.delete(article);
                Toast.makeText(mContext, R.string.msg_bookmarked_removed, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        Log.d("TAG", "onClickMore: " + mParent);
        if (mParent.equals(BookmarksFragment.class.getSimpleName())) {
            bookmarkTextView.setEnabled(false);
            bookmarkTextView.setVisibility(View.GONE);
            removeBookmarkTextView.setEnabled(true);
            removeBookmarkTextView.setVisibility(View.VISIBLE);
        } else {
            removeBookmarkTextView.setEnabled(false);
            removeBookmarkTextView.setVisibility(View.GONE);
            bookmarkTextView.setEnabled(true);
            bookmarkTextView.setVisibility(View.VISIBLE);
        }
    }

    public void updateAdapter(List<Article> list, BookmarksViewModel viewModel) {
        mArticles = list;
        this.mViewModel = viewModel;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.parent)
        LinearLayout parent;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.source)
        TextView source;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.more)
        ImageButton more;

        private ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
