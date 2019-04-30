package com.example.deepamgoel.newsy.service.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.deepamgoel.newsy.models.Article;

import java.util.List;

@Dao
public interface BookmarkedArticleDao {

    @Query("SELECT * FROM articles")
    LiveData<List<Article>> getAllBookmarks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addBookmark(Article article);

    @Delete
    void deleteBookmark(Article article);
}
