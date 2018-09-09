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

    public String getHeadline() {
        return headline;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public URL getWebUrl() {
        return webUrl;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublishedDate() {
        return publishedDate;
    }
}
