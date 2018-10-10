package com.example.deepamgoel.newsy;

class Model {
    private String mHeadline;
    private String mArticleUrl;
    private String mImageUrl;
    private String mAuthor;
    private String mPublishedDate;

    Model(String mHeadline, String mImageUrl, String mArticleUrl, String mAuthor, String mPublishedDate) {
        this.mHeadline = mHeadline;
        this.mImageUrl = mImageUrl;
        this.mArticleUrl = mArticleUrl;
        this.mAuthor = mAuthor;
        this.mPublishedDate = mPublishedDate;
    }

    String getHeadline() {
        return mHeadline;
    }

    String getImageUrl() {
        return mImageUrl;
    }

    String getArticleUrl() {
        return mArticleUrl;
    }

    String getAuthor() {
        return mAuthor;
    }

    String getPublishedDate() {
        return mPublishedDate;
    }
}
