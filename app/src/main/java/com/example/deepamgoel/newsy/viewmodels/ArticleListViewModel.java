package com.example.deepamgoel.newsy.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.deepamgoel.newsy.models.Article;
import com.example.deepamgoel.newsy.service.data.ArticleListRepository;

import java.util.List;

public class ArticleListViewModel extends ViewModel {

    private LiveData<List<Article>> articleList;
    private ArticleListRepository repository;

    ArticleListViewModel(ArticleListRepository repository) {
        this.repository = repository;
    }

    public void init(String category) {
        if (articleList != null) {
            return;
        }
        articleList = repository.getArticleList(category);
    }

    public LiveData<List<Article>> getLatestArticle() {
        return articleList;
    }
}
