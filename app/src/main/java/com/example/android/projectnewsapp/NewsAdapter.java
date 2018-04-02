package com.example.android.projectnewsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by arturoahernandez on 2/27/18.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> objects) {
        super(context, 0, objects);
    }

    /**
     * Returns a list item view that displays information about the News at the given position
     * in the list of News.
     */

    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView titleView = (TextView) listItemView.findViewById(R.id.title_name);
        String titleName = currentNews.getmTitle();
        titleView.setText(titleName);

        TextView sectionView = (TextView) listItemView.findViewById(R.id.section);
        String sectionName = currentNews.getmSection();
        sectionView.setText(sectionName);

        TextView authorView = (TextView) listItemView.findViewById(R.id.author);
        String authorName = currentNews.getmAuthor();
        if (!authorName.matches("")){
            authorView.setText(getContext().getString(R.string.author_name) + ":\n" + authorName);
        } else {
            authorView.setText(R.string.author_unavailable);
        }


        TextView published = (TextView) listItemView.findViewById(R.id.published);
        String publishedDate = currentNews.getmDate();
        if (!publishedDate.matches("")){
            published.setText(getContext().getString(R.string.publish_date) + ": " + publishedDate);
        } else {
            published.setText(R.string.publish_unavailable);
        }


        return listItemView;
    }
}
