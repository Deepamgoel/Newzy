package com.example.deepamgoel.newsy;

import android.graphics.Bitmap;

import java.net.URL;

class News {
    private String headline;
    private URL webUrl;
    private Bitmap imageBitmap;
    private String author;
    private String publishedDate;

    News(String headline, Bitmap imageBitmap, URL webUrl, String author, String publishedDate) {
        this.headline = headline;
        this.imageBitmap = imageBitmap;
        this.webUrl = webUrl;
        this.author = author;
        this.publishedDate = publishedDate;
    }

    String getHeadline() {
        return headline;
    }

    Bitmap getImageBitmap() {
        return imageBitmap;
    }

    URL getWebUrl() {
        return webUrl;
    }

    String getAuthor() {
        return author;
    }

    String getPublishedDate() {
        return publishedDate;
    }
}
