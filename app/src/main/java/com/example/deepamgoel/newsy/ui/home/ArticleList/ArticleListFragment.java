package com.example.deepamgoel.newsy.ui.home.ArticleList;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.deepamgoel.newsy.model.ArticleList;
import com.example.deepamgoel.newsy.model.Resource;
import com.example.deepamgoel.newsy.ui.RecyclerViewAdapter;
import com.example.deepamgoel.newsy.util.QueryUtils;
import com.example.deepamgoel.newsy.viewmodel.ArticleListViewModel;
import com.example.deepamgoel.newsy.viewmodel.ArticleListViewModelFactory;
import com.example.deepamgoel.newsy.viewmodel.BookmarksViewModel;
import com.example.deepamgoel.newsy.viewmodel.BookmarksViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.deepamgoel.newsy.NewsyApplication.getAppContext;
import static com.example.deepamgoel.newsy.NewsyApplication.getPreferences;

public class ArticleListFragment extends Fragment {
    private static final String ARG_SECTION = "section";
    private static final String TAG = ArticleListFragment.class.getName();

    @BindView(R.id.emptyView_text)
    TextView emptyViewTextView;
    @BindView(R.id.emptyView_button)
    Button emptyViewButton;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private String mCategory;
    private RecyclerViewAdapter mAdapter;
    private ArticleListViewModel mArticleListViewModel;
    private BookmarksViewModel mBookmarksViewModel;

    public ArticleListFragment() {
    }

    public static ArticleListFragment newInstance(String section) {
        ArticleListFragment fragment = new ArticleListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION, section);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategory = getArguments().getString(ARG_SECTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.configureRecyclerView(view.getContext());
        this.configureViewModel();

        refreshLayout.setOnRefreshListener(this::refreshData);
        emptyViewButton.setOnClickListener(v -> refreshData());
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO: 03-05-2019 not idle solution to update preference changes
        if (mArticleListViewModel != null)
            refreshData();
    }

    private void configureRecyclerView(Context context) {
        mAdapter = new RecyclerViewAdapter(context, null, ArticleListFragment.class.getSimpleName());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration divider =
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(divider);
    }

    private void configureViewModel() {
        ArticleListViewModelFactory articleListViewModelFactory =
                new ArticleListViewModelFactory(getAppContext());
        mArticleListViewModel = ViewModelProviders.of(this, articleListViewModelFactory)
                .get(ArticleListViewModel.class);
        BookmarksViewModelFactory bookmarkViewModelFactory =
                new BookmarksViewModelFactory(getAppContext());
        mBookmarksViewModel = ViewModelProviders.of(this, bookmarkViewModelFactory)
                .get(BookmarksViewModel.class);
        mArticleListViewModel.init(mCategory);
        mArticleListViewModel.getArticles().observe(this, this::observe);
    }

    private void refreshData() {
        if (mArticleListViewModel != null) {
            if (QueryUtils.isConnected(requireContext())) {
                mArticleListViewModel.refreshArticles(mCategory).removeObservers(this);
                mArticleListViewModel.refreshArticles(mCategory).observe(this, this::observe);
            } else {
                refreshLayout.setRefreshing(false);
                View view = requireActivity().findViewById(android.R.id.content);
                Snackbar.make(view, getString(R.string.msg_no_internet), Snackbar.LENGTH_SHORT).show();
                if (mAdapter.getItemCount() == 0)
                    toggleEmptyState();
            }
        }
    }

    private void observe(Resource<ArticleList> listResource) {
        ArticleList data = listResource.data;
        switch (listResource.status) {
            case LOADING:
                refreshLayout.setRefreshing(true);
                break;
            case SUCCESS:
                refreshLayout.setRefreshing(false);
                updateList(data);
                break;
            case EMPTY:
                refreshLayout.setRefreshing(false);
                toggleEmptyState();
                break;
            case ERROR:
                refreshLayout.setRefreshing(false);
                String errorMsg = listResource.msg;
                if (errorMsg != null) {
                    Toast.makeText(getContext(), getString(R.string.msg_error), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "configureViewModel: " + errorMsg);
                }
                updateList(data);
                break;
        }
    }

    private void updateList(ArticleList data) {
        if (data != null) {
            List<Article> list = data.getArticles();
            mAdapter.updateAdapter(list, mBookmarksViewModel);
            recyclerView.setVisibility(View.VISIBLE);
            emptyViewButton.setVisibility(View.INVISIBLE);
            emptyViewTextView.setVisibility(View.INVISIBLE);
        } else toggleEmptyState();
    }

    private void toggleEmptyState() {
        recyclerView.setVisibility(View.INVISIBLE);
        emptyViewButton.setVisibility(View.VISIBLE);
        emptyViewTextView.setVisibility(View.VISIBLE);
        emptyViewTextView.setText(getString(R.string.msg_no_news));
    }
}

