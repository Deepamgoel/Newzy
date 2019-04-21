package com.example.deepamgoel.newsy.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookmarkDao {

    @Query("SELECT * FROM bookmarks")
    List<Bookmark> getBookmarks();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertBookmark(Bookmark bookmark);

    @Delete
    void deleteBookmark(Bookmark bookmark);
}
