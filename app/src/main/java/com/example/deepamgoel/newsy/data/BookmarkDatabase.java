package com.example.deepamgoel.newsy.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.deepamgoel.newsy.utilities.Constants;

@Database(entities = {Bookmark.class}, version = 1, exportSchema = false)
public abstract class BookmarkDatabase extends RoomDatabase {

    private static volatile BookmarkDatabase INSTANCE;

    static BookmarkDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BookmarkDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BookmarkDatabase.class, Constants.DATABASE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract BookmarkDao bookmarkDao();
}