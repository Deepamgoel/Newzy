package com.example.deepamgoel.newsy.data.api;

import com.example.deepamgoel.newsy.BuildConfig;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {
    String API_KEY = BuildConfig.NewsApiKey;
    String BASE_URL = "https://newsapi.org/";

    @GET("v2/top-headlines")
    Call<ApiResponse> getTopHeadlines(
            @Query("country") String country,
            @Query("pageSize") String pageSize,
            @Query("apiKey") String apiKey);

    @GET("v2/top-headlines")
    Call<ApiResponse> getTopArticlesByCategory(
            @Query("category") String category,
            @Query("country") String country,
            @Query("pageSize") String pageSize,
            @Query("apiKey") String apiKey);
}