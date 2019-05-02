package com.example.deepamgoel.newsy.model;

import java.util.List;

public class ArticleList {

    private List<Article> articles;

    public ArticleList(List<Article> articles) {
        this.articles = articles;
    }

    public List<Article> getArticles() {
        return articles;
    }

}
