package com.example.deepamgoel.newsy.ui.bookmark;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.deepamgoel.newsy.R;
import com.example.deepamgoel.newsy.model.Article;
import com.example.deepamgoel.newsy.ui.RecyclerViewAdapter;
import com.example.deepamgoel.newsy.viewmodel.BookmarksViewModel;
import com.example.deepamgoel.newsy.viewmodel.BookmarksViewModelFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.deepamgoel.newsy.NewsyApplication.getAppContext;

public class BookmarksFragment extends Fragment {

    @BindView(R.id.emptyView_text)
    TextView emptyViewTextView;
    @BindView(R.id.emptyView_button)
    Button emptyViewButton;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private RecyclerViewAdapter mAdapter;
    private BookmarksViewModel mBookmarksViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.configureRecyclerView(view.getContext());
        this.configureViewModel();

        refreshLayout.setEnabled(false);
        emptyViewButton.setEnabled(false);
        emptyViewButton.setVisibility(View.GONE);
    }

    private void configureRecyclerView(Context context) {
        mAdapter = new RecyclerViewAdapter(context, null);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration divider =
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(divider);
    }

    private void configureViewModel() {
        BookmarksViewModelFactory viewModelFactory =
                new BookmarksViewModelFactory(getAppContext());
        mBookmarksViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(BookmarksViewModel.class);
        mBookmarksViewModel.init();
        mBookmarksViewModel.getBookmarks().observe(this, this::updateList);
    }

    private void updateList(List<Article> list) {
        if (list != null && list.size() > 0) {
            mAdapter.updateAdapter(list, mBookmarksViewModel);
            recyclerView.setVisibility(View.VISIBLE);
            emptyViewTextView.setVisibility(View.INVISIBLE);
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
            emptyViewTextView.setVisibility(View.VISIBLE);
            emptyViewTextView.setText(getString(R.string.msg_no_news));
        }
    }
}
