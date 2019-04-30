package com.example.deepamgoel.newsy.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.deepamgoel.newsy.service.data.ArticleListRepository;

public class ArticleListViewModelFactory implements ViewModelProvider.Factory {

    private ArticleListRepository mRepository;

    public ArticleListViewModelFactory() {
        mRepository = ArticleListRepository.getInstance();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ArticleListViewModel(mRepository);
    }
}
