package com.example.deepamgoel.newsy.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "articles_cache")
public class ArticlesCache {

    @PrimaryKey
    @NonNull
    private String category;
    @ColumnInfo(name = "articles")
    private ArticleList articles;
    @ColumnInfo(name = "last_fetch")
    private long lastFetch;

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

    public long getLastFetch() {
        return lastFetch;
    }

    public void setLastFetch(long lastFetch) {
        this.lastFetch = lastFetch;
    }
}
