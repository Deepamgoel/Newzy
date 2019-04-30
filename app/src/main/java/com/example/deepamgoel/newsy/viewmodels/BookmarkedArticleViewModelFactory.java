package com.example.deepamgoel.newsy.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.deepamgoel.newsy.service.data.BookmarkedArticleRepository;

public class BookmarkedArticleViewModelFactory implements ViewModelProvider.Factory {
    private BookmarkedArticleRepository mRepository;

    public BookmarkedArticleViewModelFactory() {
        mRepository = BookmarkedArticleRepository.getInstance();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new BookmarkedArticleViewModel(mRepository);
    }
}
