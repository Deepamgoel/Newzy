package com.example.deepamgoel.newsy.model;

public class Model {
    private String mHeadline;
    private String mArticleUrl;
    private String mImageUrl;
    private String mAuthor;
    private String mPublishedDate;
    private String section;

    public Model(String mHeadline, String mImageUrl, String mArticleUrl, String mAuthor, String mPublishedDate, String section) {
        this.mHeadline = mHeadline;
        this.mImageUrl = mImageUrl;
        this.mArticleUrl = mArticleUrl;
        this.mAuthor = mAuthor;
        this.mPublishedDate = mPublishedDate;
        this.section = section;
    }

    public String getSection() {
        return section;
    }

    public String getHeadline() {
        return mHeadline;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getArticleUrl() {
        return mArticleUrl;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }
}
