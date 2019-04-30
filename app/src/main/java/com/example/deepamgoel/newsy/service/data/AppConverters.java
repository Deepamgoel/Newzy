package com.example.deepamgoel.newsy.service.data;

import androidx.room.TypeConverter;

import com.example.deepamgoel.newsy.models.Article;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;

public class AppConverters {
    private static Gson gson = new Gson();

    @TypeConverter
    public static String fromArticle(Article article) {
        return article == null ? null : gson.toJson(article);
    }

    @TypeConverter
    public static Article toArticle(String string) {
        Type type = new TypeToken<Article>() {
        }.getType();
        return string == null ? null : gson.fromJson(string, type);
    }

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long fromDate(Date date) {
        return date == null ? null : date.getTime();
    }
}
