package com.example.deepamgoel.newsy.data.db;

import androidx.room.TypeConverter;

import com.example.deepamgoel.newsy.model.ArticleList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;

public class AppConverters {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static String fromArticleList(ArticleList articles) {
        if (articles == null) return null;

        return new Gson().toJson(articles);
    }

    @TypeConverter
    public static ArticleList toArticleList(String json) {
        if (json == null) return null;

        Type type = new TypeToken<ArticleList>() {
        }.getType();

        return new Gson().fromJson(json, type);
    }

}
