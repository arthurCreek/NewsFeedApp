package com.example.android.projectnewsapp;

/**
 * Created by arturoahernandez on 2/27/18.
 */

public class News {

    //
    private String mTitle;
    private String mSection;
    private String mAuthor;
    private String mDate;
    private String mURL;

    public News(String mTitle, String mSection, String mAuthor, String mDate, String mURL) {
        this.mTitle = mTitle;
        this.mSection = mSection;
        this.mAuthor = mAuthor;
        this.mDate = mDate;
        this.mURL = mURL;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmSection() {
        return mSection;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmURL() {
        return mURL;
    }
}
