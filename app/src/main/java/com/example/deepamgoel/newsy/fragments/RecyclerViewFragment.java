package com.example.deepamgoel.newsy.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.deepamgoel.newsy.R;
import com.example.deepamgoel.newsy.adapters.RecyclerViewAdapter;
import com.example.deepamgoel.newsy.interfaces.NewsApiService;
import com.example.deepamgoel.newsy.models.ApiResponse;
import com.example.deepamgoel.newsy.models.Article;
import com.example.deepamgoel.newsy.utils.QueryUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

import static android.content.ContentValues.TAG;

public class RecyclerViewFragment extends Fragment implements Callback<ApiResponse> {
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

    private int mPageIndex;
    private String mCategory;
    private NewsApiService mApiService;
    private SharedPreferences mPreferences;
    private List<Article> mArticles = new ArrayList<>();

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
//        refresh();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPageIndex = getArguments().getInt(ARG_INDEX);
            mCategory = getArguments().getString(ARG_SECTION);
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
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        recyclerView.setAdapter(new RecyclerViewAdapter(getContext(), mArticles));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(
                Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL));

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewsApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mApiService = retrofit.create(NewsApiService.class);

        mPreferences.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> refresh());

        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(true);
            refresh();
        });

        refreshLayout.setRefreshing(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mApiService != null)
            refresh();
    }

    private void refresh() {
        if (QueryUtils.isConnected(Objects.requireNonNull(getContext()))) {
            request(mApiService);
        } else {
            refreshLayout.setRefreshing(false);
            if (mArticles.size() != 0) {
                // TODO: 17-04-2019 implement caching
                View view = Objects.requireNonNull(getActivity()).
                        findViewById(android.R.id.content);
                Snackbar snackbar = Snackbar.make(view
                        , getString(R.string.msg_no_internet)
                        , Snackbar.LENGTH_LONG);
                snackbar.setAction(getString(R.string.label_retry), v -> {
                    refreshLayout.setRefreshing(true);
                    refresh();
                    snackbar.dismiss();
                })
                        .setActionTextColor(getResources().getColor(R.color.colorPrimary));
                snackbar.show();
            } else {
                recyclerView.setVisibility(View.INVISIBLE);
                emptyViewRelativeLayout.setVisibility(View.VISIBLE);
                emptyViewTextView.setText(R.string.msg_no_internet);
            }
        }
    }

    private void request(NewsApiService apiService) {
        String country = mPreferences.getString(getString(R.string.settings_country_key), getString(R.string.settings_country_india_value));
        String pageSize = mPreferences.getString(getString(R.string.setting_page_size_key), getString(R.string.settings_max_page_default_value));

        Call<ApiResponse> responseCall;
        if (mPageIndex == 0)
            responseCall = apiService.getTopHeadlinesByCountry(
                    country,
                    pageSize,
                    NewsApiService.API_KEY
            );
        else
            responseCall = apiService.getTopHeadlinesByCategory(
                    mCategory.toLowerCase(),
                    country,
                    pageSize,
                    NewsApiService.API_KEY
            );

        responseCall.enqueue(this);
    }

    private void updateData(List<Article> data) {
        refreshLayout.setRefreshing(false);
        if (data != null && !data.isEmpty()) {
            mArticles.clear();
            mArticles.addAll(data);
            Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
            emptyViewRelativeLayout.setVisibility(View.INVISIBLE);
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
            emptyViewRelativeLayout.setVisibility(View.VISIBLE);
            emptyViewTextView.setText(R.string.msg_no_news);
        }
    }

    @Override
    @EverythingIsNonNull
    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
        if (response.isSuccessful()) {
            ApiResponse apiResponse = response.body();
            if (apiResponse != null)
                updateData(apiResponse.getArticles());
        } else
            Log.d(TAG, "onResponse: " + response.errorBody());
    }

    @Override
    @EverythingIsNonNull
    public void onFailure(Call<ApiResponse> call, Throwable t) {
        t.printStackTrace();
    }

}
