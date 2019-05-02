package com.example.deepamgoel.newsy.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.deepamgoel.newsy.model.ArticleList;
import com.example.deepamgoel.newsy.model.ArticlesCache;

@Dao
public interface ArticlesCacheDao {
    @Query("SELECT articles FROM articles_cache WHERE category = :category")
    LiveData<ArticleList> getFromCache(String category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addToCache(ArticlesCache cache);

// TODO: 02-05-2019
//@Query("")
//    int isExist();
}