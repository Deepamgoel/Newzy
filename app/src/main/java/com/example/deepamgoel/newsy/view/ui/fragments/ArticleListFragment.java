package com.example.deepamgoel.newsy.view.ui.fragments;

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
import com.example.deepamgoel.newsy.models.Article;
import com.example.deepamgoel.newsy.view.adapters.ArticleListRecyclerViewAdapter;
import com.example.deepamgoel.newsy.viewmodels.ArticleListViewModel;
import com.example.deepamgoel.newsy.viewmodels.ArticleListViewModelFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleListFragment extends Fragment /*implements Callback<ApiResponse>*/ {
    private static final String ARG_SECTION = "section";

    @BindView(R.id.emptyView_text)
    TextView emptyViewTextView;
    @BindView(R.id.emptyView_button)
    Button emptyViewButton;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private ArticleListRecyclerViewAdapter adapter;

    public ArticleListFragment() {
    }

    static ArticleListFragment newInstance(String section) {
        ArticleListFragment fragment = new ArticleListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION, section);
        fragment.setArguments(args);
        return fragment;
    }

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

        // TODO: 30-04-2019  
//        refreshLayout.setOnRefreshListener(this::refresh);
//        emptyViewButton.setOnClickListener(v -> refresh());
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
        String category = getArguments().getString(ARG_SECTION);
        ArticleListViewModelFactory viewModelFactory = new ArticleListViewModelFactory();
        ArticleListViewModel viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ArticleListViewModel.class);
        viewModel.init(category);
        viewModel.getLatestArticle().observe(this, this::updateList);
    }

    private void updateList(List<Article> list) {
        refreshLayout.setRefreshing(false);
        if (list != null && !list.isEmpty()) {
            adapter.addArticleList(list);
            recyclerView.setVisibility(View.VISIBLE);
            emptyViewButton.setVisibility(View.INVISIBLE);
            emptyViewTextView.setVisibility(View.INVISIBLE);
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
            emptyViewButton.setVisibility(View.VISIBLE);
            emptyViewTextView.setVisibility(View.VISIBLE);
            emptyViewTextView.setText(R.string.msg_no_news);
        }
    }
}
