package com.example.deepamgoel.newsy.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "articles_cache")
public class ArticlesCache {

    @PrimaryKey
    @NonNull
    private String category;
    @ColumnInfo(name = "articles")
    private ArticleList articles;
    // TODO: 03-05-2019 add timestamp for cache refresh

    @NonNull
    public String getCategory() {
        return category;
    }

    public void setCategory(@NonNull String category) {
        this.category = category;
    }

    public ArticleList getArticles() {
        return articles;
    }

    public void setArticles(ArticleList articles) {
        this.articles = articles;
    }
}
