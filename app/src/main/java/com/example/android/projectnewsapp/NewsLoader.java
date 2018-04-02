package com.example.android.projectnewsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by arturoahernandez on 2/27/18.
 */

public class NewsLoader extends AsyncTaskLoader {

    /** Query URL */
    private String mUrl;

    public NewsLoader(Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<News> loadInBackground() {
        if (mUrl == null){
            return null;
        }

        ArrayList<News> news = QueryUtils.extractNews(mUrl);
        return news;
    }
}
