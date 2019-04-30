package com.example.deepamgoel.newsy.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.deepamgoel.newsy.models.Article;
import com.example.deepamgoel.newsy.service.data.BookmarkedArticleRepository;

import java.util.List;

public class BookmarkedArticleViewModel extends ViewModel {
    private BookmarkedArticleRepository repository;

    BookmarkedArticleViewModel(@NonNull BookmarkedArticleRepository repository) {
        this.repository = repository;
    }

//    public LiveData<List<Article>> getAllBookmarks() {
//        return mRepository.getAllBookmarks();
//    }

//    public void insert(Article article) {
//        mRepository.addBookmark(article);
//    }
}
