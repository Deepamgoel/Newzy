package com.example.deepamgoel.newsy;

import java.net.URL;

class News {
    private String headline;
    private URL webUrl;
    private URL imageUrl;
    private String author;
    private String publishedDate;

    News(String headline, URL imageUrl, URL webUrl, String author, String publishedDate) {
        this.headline = headline;
        this.imageUrl = imageUrl;
        this.webUrl = webUrl;
        this.author = author;
        this.publishedDate = publishedDate;
    }

    String getHeadline() {
        return headline;
    }

    URL getImageUrl() {
        return imageUrl;
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
