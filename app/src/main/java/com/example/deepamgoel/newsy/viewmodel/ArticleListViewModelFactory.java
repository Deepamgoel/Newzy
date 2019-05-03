package com.example.deepamgoel.newsy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.deepamgoel.newsy.data.api.NewsService;
import com.example.deepamgoel.newsy.data.db.AppDatabase;
import com.example.deepamgoel.newsy.data.db.ArticlesCacheDao;
import com.example.deepamgoel.newsy.repository.ArticleListRepository;
import com.example.deepamgoel.newsy.util.AppExecutor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleListViewModelFactory implements ViewModelProvider.Factory {

    private ArticleListRepository mRepository;

    public ArticleListViewModelFactory(Application application) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        NewsService apiService = retrofit.create(NewsService.class);
        ArticlesCacheDao articlesCacheDao = AppDatabase.getDatabase(application).articlesCacheDao();
        AppExecutor executor = new AppExecutor();


        mRepository = ArticleListRepository.getInstance(apiService, articlesCacheDao, executor);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ArticleListViewModel(mRepository);
    }
}
