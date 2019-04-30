package com.example.deepamgoel.newsy.service.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.deepamgoel.newsy.R;
import com.example.deepamgoel.newsy.models.ApiResponse;
import com.example.deepamgoel.newsy.models.Article;
import com.example.deepamgoel.newsy.service.network.NewsService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

import static com.example.deepamgoel.newsy.NewsyApplication.getAppContext;
import static com.example.deepamgoel.newsy.NewsyApplication.getPreferences;

public class ArticleListRepository {
    private static final String TAG = "ArticleListRepository";

    private static ArticleListRepository sArticleListRepository;
    private final NewsService mApiService;

    private ArticleListRepository() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mApiService = retrofit.create(NewsService.class);
    }

    public synchronized static ArticleListRepository getInstance() {
        if (sArticleListRepository == null) {
            sArticleListRepository = new ArticleListRepository();
        }
        return sArticleListRepository;
    }

    public LiveData<List<Article>> getArticleList(String category) {

        final MutableLiveData<List<Article>> data = new MutableLiveData<>();

        String country = getPreferences().getString(getAppContext().getString(R.string.settings_country_key), getAppContext().getString(R.string.settings_country_india_value));
        String pageSize = getPreferences().getString(getAppContext().getString(R.string.setting_page_size_key), getAppContext().getString(R.string.settings_max_page_default_value));

        Call<ApiResponse> responseCall;
        if (category.equals(getAppContext().getString(R.string.category_headline))) {
            responseCall = mApiService.getTopHeadlines(
                    country,
                    pageSize,
                    NewsService.API_KEY
            );
        } else {
            responseCall = mApiService.getTopArticlesByCategory(
                    category,
                    country,
                    pageSize,
                    NewsService.API_KEY
            );
        }

        responseCall.enqueue(new Callback<ApiResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null) {
                        data.setValue(apiResponse.getArticles());
                    }
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return data;
    }

}