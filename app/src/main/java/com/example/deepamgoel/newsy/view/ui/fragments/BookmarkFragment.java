package com.example.deepamgoel.newsy.view.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.deepamgoel.newsy.R;
import com.example.deepamgoel.newsy.models.Article;
import com.example.deepamgoel.newsy.view.adapters.ArticleListRecyclerViewAdapter;
import com.example.deepamgoel.newsy.viewmodels.BookmarkedArticleViewModel;
import com.example.deepamgoel.newsy.viewmodels.BookmarkedArticleViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookmarkFragment extends Fragment {

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    ArticleListRecyclerViewAdapter adapter;
    private List<Article> mArticles = new ArrayList<>();

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

        refreshLayout.setEnabled(false);

        this.configureRecyclerView(view.getContext());
        this.configureViewModel();
    }

    private void configureRecyclerView(Context context) {
        adapter = new ArticleListRecyclerViewAdapter(context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration divider =
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(divider);
    }

    private void configureViewModel() {
        BookmarkedArticleViewModelFactory viewModelFactory =
                new BookmarkedArticleViewModelFactory();
        BookmarkedArticleViewModel viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(BookmarkedArticleViewModel.class);
//        viewModel.init();
//        viewModel.getAllBookmarks().observe(this, list -> {
//
//        });
    }

}
