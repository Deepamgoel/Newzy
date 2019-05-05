package com.example.deepamgoel.newsy.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepamgoel.newsy.R;
import com.example.deepamgoel.newsy.model.Article;
import com.example.deepamgoel.newsy.model.ArticleList;
import com.example.deepamgoel.newsy.model.Resource;
import com.example.deepamgoel.newsy.ui.RecyclerViewAdapter;
import com.example.deepamgoel.newsy.ui.home.ArticleList.ArticleListFragment;
import com.example.deepamgoel.newsy.viewmodel.ArticleListViewModel;
import com.example.deepamgoel.newsy.viewmodel.ArticleListViewModelFactory;
import com.example.deepamgoel.newsy.viewmodel.BookmarksViewModel;
import com.example.deepamgoel.newsy.viewmodel.BookmarksViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.deepamgoel.newsy.NewsyApplication.getAppContext;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    private static final String TAG = SearchFragment.class.getSimpleName();
    @BindView(R.id.search_view)
    SearchView mSearchView;
    @BindView(R.id.result_list)
    RecyclerView mRecyclerView;


    private RecyclerViewAdapter mAdapter;
    private ArticleListViewModel mArticleListViewModel;
    private BookmarksViewModel mBookmarksViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSearchView.setOnQueryTextListener(this);


        this.configureRecyclerView(requireContext());
        this.configureViewModel();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (mArticleListViewModel != null) {
            mArticleListViewModel.searchQyery(query).removeObservers(this);
            mArticleListViewModel.searchQyery(query).observe(this, this::observe);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void configureRecyclerView(Context context) {
        mAdapter = new RecyclerViewAdapter(context, null, ArticleListFragment.class.getSimpleName());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration divider =
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.divider));
        mRecyclerView.addItemDecoration(divider);
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
    }

    private void observe(Resource<ArticleList> listResource) {
        ArticleList data = listResource.data;
        switch (listResource.status) {
            case LOADING:
                break;
            case SUCCESS:
                updateList(data);
                break;
            case EMPTY:
                Toast.makeText(getContext(), getString(R.string.msg_no_result), Toast.LENGTH_SHORT).show();
                break;
            case ERROR:
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
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mSearchView.setQuery("", false);
        mSearchView.clearFocus();
        mAdapter.updateAdapter(new ArrayList<>(), null);
        mArticleListViewModel = null;
        mBookmarksViewModel = null;

    }
}