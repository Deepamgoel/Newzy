package com.example.deepamgoel.newsy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.deepamgoel.newsy.model.ArticleList;
import com.example.deepamgoel.newsy.model.Resource;
import com.example.deepamgoel.newsy.repository.ArticleListRepository;

public class ArticleListViewModel extends ViewModel {

    private String category;
    private ArticleListRepository repository;
    private LiveData<Resource<ArticleList>> articles;

    ArticleListViewModel(ArticleListRepository repository) {
        this.repository = repository;
    }

    public void init(String category) {
        if (articles != null) {
            return;
        }
        this.category = category;
        articles = repository.loadArticleList(category, false);
    }

    public LiveData<Resource<ArticleList>> getArticles() {
        return articles;
    }

    public LiveData<Resource<ArticleList>> refreshArticles() {
        articles = repository.loadArticleList(category, true);
        return articles;
    }

    public LiveData<Resource<ArticleList>> searchQuery(String query) {
        return repository.searchQuery(query);
    }

}
