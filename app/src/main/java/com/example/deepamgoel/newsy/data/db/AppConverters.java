package com.example.deepamgoel.newsy.data.db;

import androidx.room.TypeConverter;

import com.example.deepamgoel.newsy.model.ArticleList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class AppConverters {

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
