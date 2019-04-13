package com.example.deepamgoel.newsy.fragment;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.deepamgoel.newsy.R;
import com.example.deepamgoel.newsy.adapter.RecyclerViewAdapter;
import com.example.deepamgoel.newsy.model.Model;
import com.example.deepamgoel.newsy.utils.NewsVewModelFactory;
import com.example.deepamgoel.newsy.utils.NewsViewModel;
import com.example.deepamgoel.newsy.utils.QueryUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.deepamgoel.newsy.activity.MainActivity.REQUESTED_URL_V2;
import static com.example.deepamgoel.newsy.activity.MainActivity.newsApiKey;

public class RecyclerViewFragment extends Fragment {
    private static final String ARG_SECTION = "section";
    private static final String ARG_INDEX = "index";

    @BindView(R.id.emptyView)
    RelativeLayout emptyViewRelativeLayout;
    @BindView(R.id.emptyView_text)
    TextView emptyViewTextView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private int index;
    private String category;
    private NewsViewModel viewModel;
    private List<Model> newsList = new ArrayList<>();

    private RecyclerViewFragment() {
    }

    static RecyclerViewFragment newInstance(int index, String section) {
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        args.putString(ARG_SECTION, section);
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick(R.id.emptyView_button)
    void onRetry() {
        // TODO: 11-04-2019 Not working
        refresh();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_INDEX);
            category = getArguments().getString(ARG_SECTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new RecyclerViewAdapter(getContext(), newsList));
        recyclerView.addItemDecoration(new DividerItemDecoration(
                Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL));

        ViewModelProvider viewModelProvider =
                ViewModelProviders.of(this,
                        new NewsVewModelFactory(Objects.requireNonNull(
                                getActivity()).getApplication(), getUri()));
        viewModel = viewModelProvider.get(NewsViewModel.class);

        if (QueryUtils.isConnected(getContext())) {
            refreshLayout.setRefreshing(true);
            viewModel.getData().observe(this, this::updateData);
            setConnected(true);
        } else
            setConnected(false);


        refreshLayout.setOnRefreshListener(this::refresh);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private String getUri() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String country = preferences.getString(getString(R.string.settings_country_key), getString(R.string.settings_country_india_value));
        String pageSize = preferences.getString(getString(R.string.setting_page_size_key), getString(R.string.settings_max_page_default_value));
        String category = this.category.toLowerCase();

        Uri baseUri = Uri.parse(REQUESTED_URL_V2);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter(getString(R.string.query_country), country);
        if (index != 0)
            uriBuilder.appendQueryParameter(getString(R.string.query_category), category);
        uriBuilder.appendQueryParameter(getString(R.string.query_page_size), pageSize);
        uriBuilder.appendQueryParameter(getString(R.string.query_api_key), newsApiKey);
        return uriBuilder.toString();
    }

    private void updateData(List<Model> data) {
        refreshLayout.setRefreshing(false);
        if (!data.isEmpty()) {
            emptyViewRelativeLayout.setVisibility(View.INVISIBLE);
            newsList.clear();
            newsList.addAll(data);
            Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        } else {
            emptyViewRelativeLayout.setVisibility(View.VISIBLE);
            emptyViewTextView.setText(R.string.msg_no_news);
        }

    }

    private void refresh() {
        refreshLayout.setRefreshing(true);
        viewModel.getData().removeObservers(Objects.requireNonNull(getActivity()));
        if (QueryUtils.isConnected(Objects.requireNonNull(getContext()))) {
            setConnected(true);
            viewModel.getData().observe(this, this::updateData);
        } else
            setConnected(false);
    }

    private void setConnected(boolean isConnected) {
        if (isConnected) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyViewRelativeLayout.setVisibility(View.INVISIBLE);
        } else {
            refreshLayout.setRefreshing(false);
            recyclerView.setVisibility(View.INVISIBLE);
            emptyViewRelativeLayout.setVisibility(View.VISIBLE);
            emptyViewTextView.setText(R.string.msg_no_internet);
        }
    }
}
