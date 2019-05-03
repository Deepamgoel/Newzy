package com.example.deepamgoel.newsy.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.deepamgoel.newsy.model.Article;

import java.util.List;

@Dao
public interface BookmarksDao {

    @Query("SELECT * FROM `bookmarks`")
    LiveData<List<Article>> getAllBookmarks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addBookmark(Article article);

    @Delete
    void deleteBookmark(Article article);
}
