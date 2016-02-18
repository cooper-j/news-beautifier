package com.github.newsbeautifier.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.newsbeautifier.R;
import com.github.newsbeautifier.models.RSSFeed;
import com.github.newsbeautifier.utils.RSSParserTask;

public class RSSFragment extends Fragment {

    public RSSFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rss, container, false);

        ListView listView = (ListView) view.findViewById(R.id.list_view);

        listView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, RSSParserTask.RSS_FEEDS));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new MyRSSParserTask().execute(RSSParserTask.RSS_FEEDS[position]);
            }
        });
        return view;
    }

    private class MyRSSParserTask extends RSSParserTask {
        @Override
        protected void onPostExecute(RSSFeed rssFeed) {
            super.onPostExecute(rssFeed);
        }
    }
}
