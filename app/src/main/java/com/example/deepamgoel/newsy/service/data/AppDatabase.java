package com.example.deepamgoel.newsy.service.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.deepamgoel.newsy.models.Article;
import com.example.deepamgoel.newsy.utilities.Constants;

@Database(entities = {Article.class}, version = 1, exportSchema = false)
@TypeConverters(AppConverters.class)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, Constants.DATABASE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract BookmarkedArticleDao mBookmarkedArticleDao();
}