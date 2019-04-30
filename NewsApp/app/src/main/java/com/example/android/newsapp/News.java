package com.example.android.newsapp;

public class News {
    private String mTime;
    private String mDate;
    private String mNewsCategory;
    private String mHeadline;
    private String mThumbnail;
    private String mAuthor;

    public News(String time, String date, String newsCategory, String headline, String thumbnail, String author) {
        mTime = time;
        mDate = date;
        mNewsCategory = newsCategory;
        mHeadline = headline;
        mThumbnail = thumbnail;
        mAuthor = author;
    }

    public String getTime() {
        return mTime;
    }

    public String getDate() {
        return mDate;
    }

    public String getNewsCategory() {
        return mNewsCategory;
    }

    public String getHeadline() {
        return mHeadline;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getThumbnail() {
        return mThumbnail;
    }
}
