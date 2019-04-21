package com.example.deepamgoel.newsy.interfaces;

import com.example.deepamgoel.newsy.BuildConfig;
import com.example.deepamgoel.newsy.models.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiService {
    String API_KEY = BuildConfig.NewsApiKey;
    String BASE_URL = "https://newsapi.org/";

    @GET("v2/top-headlines")
    Call<ApiResponse> getTopHeadlinesByCountry(
            @Query("country") String country,
            @Query("pageSize") String pageSize,
            @Query("apiKey") String apiKey);

    @GET("v2/top-headlines")
    Call<ApiResponse> getTopHeadlinesByCategory(
            @Query("category") String category,
            @Query("country") String country,
            @Query("pageSize") String pageSize,
            @Query("apiKey") String apiKey);
}