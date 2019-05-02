package com.example.deepamgoel.newsy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.deepamgoel.newsy.repository.ArticleListRepository;
import com.example.deepamgoel.newsy.model.ArticleList;
import com.example.deepamgoel.newsy.model.Resource;

public class ArticleListViewModel extends ViewModel {

    private ArticleListRepository repository;
    private LiveData<Resource<ArticleList>> articles;

    ArticleListViewModel(ArticleListRepository repository) {
        this.repository = repository;
    }

    public void init(String category) {
        if (articles != null) {
            return;
        }
        articles = repository.loadArticleList(category, false);
    }

    public LiveData<Resource<ArticleList>> getArticles() {
        return articles;
    }

    public LiveData<Resource<ArticleList>> refreshArticles(String category) {
        articles = repository.loadArticleList(category, true);
        return articles;
    }


}
