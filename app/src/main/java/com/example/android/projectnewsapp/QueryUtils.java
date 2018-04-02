package com.example.android.projectnewsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by arturoahernandez on 2/27/18.
 */

public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing a JSON response.
     */

    public static ArrayList<News> extractNews(String urlGuardian){

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(urlGuardian)) {
            return null;
        }

        //Create an empty array list to add the News items to
        ArrayList<News> news = new ArrayList<>();

        URL url = createURL(urlGuardian);

        String jsonResponse = "";

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            // TODO Handle the IOException
            Log.e(LOG_TAG, "Error making HTTP Request");
        }

        if (jsonResponse != null){
            try {

                JSONObject root = new JSONObject(jsonResponse);

                JSONObject response = root.getJSONObject("response");

                JSONArray results = response.getJSONArray("results");

                for (int i = 0; i < results.length(); i++){
                    JSONObject currentNews = results.getJSONObject(i);

                    String titleString = currentNews.getString("webTitle");
                    String sectionString = currentNews.getString("sectionName");

                    JSONArray tags = currentNews.optJSONArray("tags");
                    StringBuilder authorBuilder = new StringBuilder();
                    String authorString = "";
                    if(tags.length() != 0 && tags != null){
                        for(int j = 0; j < tags.length(); j++){
                            authorBuilder.append(tags.getJSONObject(j).getString("webTitle"));
                            if (tags.length() > 1 && j < tags.length()-1){
                                authorBuilder.append("; ");
                            }
                        }
                    }

                    authorString = authorBuilder.toString();

                    String publishedString = currentNews.optString("webPublicationDate");
                    String publishedDate = publishedString.split("T")[0];
                    String urlString = currentNews.getString("webUrl");

                    News news_item = new News(titleString, sectionString, authorString, publishedDate, urlString);
                    news.add(news_item);

                }


            }catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
            }
        } else {
            return null;
        }

        return news;
    }

    private static URL createURL(String urlString){
        URL url = null;

        try{
            url = new URL(urlString);
        } catch (MalformedURLException excpection){
            Log.e(LOG_TAG, "Error with creating URL", excpection);
            return null;
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            // TODO: Handle the exception
            Log.e(LOG_TAG, "Error response code " + urlConnection.getResponseCode());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }

        return output.toString();
    }

}
