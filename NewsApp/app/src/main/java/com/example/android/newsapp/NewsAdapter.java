package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * Constructs a new {@link NewsAdapter}.
     *
     * @param context of the app
     * @param news is the list of news which is the data source of the adapter
     */
    public NewsAdapter(Context context, List<News> news) {
        super(context,0, news);
    }

    /**
     * Returns a list item view that displays information about the news at the given position
     * in the list of news.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_list, parent, false);
        }
        // Find the news at the given position in the list
        News currentNews = getItem( position );

        // Find the TextView with view ID time
        TextView timeView = listItemView.findViewById(R.id.time);
        timeView.setText( currentNews.getTime() );

        //find the TextView with view ID date
        TextView dateView = listItemView.findViewById( R.id.date );
        dateView.setText( currentNews.getDate() );

        //find the TextView with view ID category
        TextView categoryView = listItemView.findViewById( R.id.category );
        categoryView.setText( currentNews.getNewsCategory() );

        //find the TextView with view ID headline
        TextView headlineView = listItemView.findViewById( R.id.headline );
        headlineView.setText( currentNews.getHeadline() );

        TextView authorView = listItemView.findViewById( R.id.author );
        authorView.setText( currentNews.getAuthor() );

        return listItemView;
    }
}
