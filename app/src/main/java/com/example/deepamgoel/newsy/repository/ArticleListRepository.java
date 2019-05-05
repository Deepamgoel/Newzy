package com.example.deepamgoel.newsy.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.deepamgoel.newsy.R;
import com.example.deepamgoel.newsy.data.api.ApiResponse;
import com.example.deepamgoel.newsy.data.api.NewsService;
import com.example.deepamgoel.newsy.data.db.ArticlesCacheDao;
import com.example.deepamgoel.newsy.model.ArticleList;
import com.example.deepamgoel.newsy.model.ArticlesCache;
import com.example.deepamgoel.newsy.model.Resource;
import com.example.deepamgoel.newsy.util.AppExecutor;

import retrofit2.Call;

import static com.example.deepamgoel.newsy.NewsyApplication.getAppContext;
import static com.example.deepamgoel.newsy.NewsyApplication.getPreferences;

public class ArticleListRepository {
    private static ArticleListRepository repository;

    private final NewsService apiService;
    private final ArticlesCacheDao articlesCacheDao;
    private final AppExecutor executor;

    private ArticleListRepository(NewsService apiService, ArticlesCacheDao articlesCacheDao,
                                  AppExecutor executor) {
        this.apiService = apiService;
        this.articlesCacheDao = articlesCacheDao;
        this.executor = executor;
    }

    public synchronized static ArticleListRepository getInstance(NewsService apiService,
                                                                 ArticlesCacheDao articlesCacheDao,
                                                                 AppExecutor executor) {
        if (repository == null) {
            repository = new ArticleListRepository(apiService, articlesCacheDao, executor);
        }
        return repository;
    }

    public LiveData<Resource<ArticleList>> loadArticleList(@NonNull String category,
                                                           boolean forceRefresh) {
        return new NetworkBoundResources<ArticleList, ApiResponse>(executor) {
            @Override
            protected void saveCallResults(@NonNull ApiResponse item) {
                ArticlesCache cache = new ArticlesCache();
                cache.setCategory(category);
                cache.setArticles(new ArticleList(item.getArticles()));
                articlesCacheDao.addToCache(cache);
            }

            @Override
            protected boolean shouldFetch(@Nullable ArticleList data) {
                return forceRefresh || data == null || data.getArticles().isEmpty();
            }

            @NonNull
            @Override
            protected LiveData<ArticleList> loadFromDb() {
                return articlesCacheDao.getFromCache(category);
            }

            @NonNull
            @Override
            protected Call<ApiResponse> createCall() {
                String country = getPreferences().getString(getAppContext().getString(R.string.settings_country_key), getAppContext().getString(R.string.settings_country_india_value));
                String pageSize = getPreferences().getString(getAppContext().getString(R.string.setting_page_size_key), getAppContext().getString(R.string.settings_max_page_default_value));

                return apiService.getTopArticlesByCategory(
                        category,
                        country,
                        pageSize,
                        NewsService.API_KEY
                );
            }
        }.getAsLiveData();
    }

}