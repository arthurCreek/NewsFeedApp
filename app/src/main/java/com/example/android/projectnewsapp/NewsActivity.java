package com.example.android.projectnewsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<News>>{


    private NewsAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private String newsAPI = "http://content.guardianapis.com/search?show-tags=contributor&q=";
    private String apiKEY = "&api-key=test";
    private String mUrlFinal = "";
    private static final int LOADER_INT_ID = 1;
    private Boolean isRunning = false;
    private LoaderManager loaderManager;
    private View loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        loaderManager = getLoaderManager();

        //Give user clear instruction to enter search terms
        mEmptyStateTextView = findViewById(R.id.empty_view);
        mEmptyStateTextView.setText(R.string.enter_search);
        mEmptyStateTextView.setVisibility(View.VISIBLE);

        final EditText editText = findViewById(R.id.news_search);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                //Create a connectivity manager and store network info
                ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

                //Get the text from the edit text field
                String editorText = editText.getText().toString();

                //Create the final url
                mUrlFinal = newsAPI + editorText + apiKEY;
                boolean handled = false;

                //Continue only if action search is activated && editor text is !empty
                if (i == EditorInfo.IME_ACTION_SEARCH && !editorText.matches("")){

                    mEmptyStateTextView.setVisibility(View.GONE);

                    if (networkInfo != null && networkInfo.isConnected()){
                        loadingIndicator = findViewById(R.id.loading_spinner);
                        loadingIndicator.setVisibility(View.VISIBLE);
                        if(isRunning){
                            loaderManager.restartLoader(LOADER_INT_ID, null, NewsActivity.this);
                        }
                        loaderManager.initLoader(LOADER_INT_ID, null, NewsActivity.this);
                    } else {
                        //If no internet connection then state it here
                        loadingIndicator = findViewById(R.id.loading_spinner);
                        loadingIndicator.setVisibility(View.GONE);

                        mEmptyStateTextView = findViewById(R.id.empty_view);
                        mEmptyStateTextView.setText(R.string.no_internet);
                        mEmptyStateTextView.setVisibility(View.VISIBLE);
                        // Clear the adapter here
                        if (mAdapter != null) {
                            mAdapter.clear();
                        }


                    }
                    handled = true;
                } else {
                    //Just in case the user hits search and did not type anything show no books
                    mEmptyStateTextView = findViewById(R.id.empty_view);
                    if (networkInfo != null && networkInfo.isConnected()){
                        // Clear the adapter here
                        if (mAdapter != null) {
                            mAdapter.clear();
                        }
                        mEmptyStateTextView.setText(R.string.not_available);
                    } else {
                        // Clear the adapter here
                        if (mAdapter != null) {
                            mAdapter.clear();
                        }
                        mEmptyStateTextView.setText(R.string.no_internet);
                    }

                    mEmptyStateTextView.setVisibility(View.VISIBLE);
                }
                return handled;
            }
        });
    }

    @Override
    public Loader<ArrayList<News>> onCreateLoader(int i, Bundle bundle) {
        isRunning = true;
        return new NewsLoader(NewsActivity.this, mUrlFinal);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> news) {
        // Hide loading indicator because the data has been loaded
        loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);

        if (mAdapter != null) {
            mAdapter.clear();
        }

        //Make sure books is not empty or null
        if (news.isEmpty() || news == null){
            mEmptyStateTextView = findViewById(R.id.empty_view);
            mEmptyStateTextView.setText(R.string.not_available);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        } else {
            updateUi(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader) {
        if (mAdapter != null) {
            mAdapter.clear();
        }
    }

    public void updateUi(ArrayList<News> news){
        ListView newsListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new NewsAdapter(NewsActivity.this, news);

        newsListView.setAdapter(mAdapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Find the current earthquake that was clicked on
                News clickNews = mAdapter.getItem(i);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri uri = Uri.parse(clickNews.getmURL());

                // Create a new intent to view the earthquake URI
                Intent newsIntent = new Intent(Intent.ACTION_VIEW, uri);

                // Send the intent to launch a new activity
                startActivity(newsIntent);
            }
        });

    }
}
