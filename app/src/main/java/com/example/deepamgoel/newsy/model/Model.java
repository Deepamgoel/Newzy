package com.example.deepamgoel.newsy.model;

public class Model {
    private String mTitle;
    private String mUrl;
    private String mUrlToImage;
    private String mAuthor;
    private String mPublishedDate;
    private String source;

    public Model(String mTitle, String mUrlToImage, String mUrl, String mAuthor, String mPublishedDate, String source) {
        this.mTitle = mTitle;
        this.mUrlToImage = mUrlToImage;
        this.mUrl = mUrl;
        this.mAuthor = mAuthor;
        this.mPublishedDate = mPublishedDate;
        this.source = source;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getUrlToImage() {
        return mUrlToImage;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }

    public String getSource() {
        return source;
    }
}
