package com.github.newsbeautifier.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.github.newsbeautifier.MyApplication;
import com.github.newsbeautifier.R;
import com.github.newsbeautifier.adapters.RssGridAdapter;
import com.github.newsbeautifier.models.RSSFeed;
import com.github.newsbeautifier.models.User;
import com.github.newsbeautifier.utils.RSSParser;
import com.github.newsbeautifier.utils.UpdateRSSFeedTask;

import java.util.ArrayList;

public class FeedSelectFragment extends Fragment {

    private ArrayList<RSSFeed> mRssList = new ArrayList<>();
    private GridView mRssGrid;
    private RssGridAdapter mRssGridAdapter;

    public FeedSelectFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_feed_select, container, false);

        mRssGrid = (GridView)inflatedView.findViewById(R.id.rssGridView);

        User user = ((MyApplication)getActivity().getApplication()).mUser;

        mRssList.addAll(user.getFeeds());

        ArrayList<RSSFeed> tmpList = new ArrayList<>();
        tmpList.add(RSSParser.RSS_FEEDS[0]);
        tmpList.add(RSSParser.RSS_FEEDS[1]);
        tmpList.add(RSSParser.RSS_FEEDS[2]);
        tmpList.add(RSSParser.RSS_FEEDS[3]);
        boolean contained = false;
        for (RSSFeed feed : tmpList) {
            for (RSSFeed feed2 : mRssList)
                if (feed2.getUrl().equals(feed.getUrl())) {
                    contained = true;
                    break;
                }
            if (!contained) {
                mRssList.add(feed);
                new MyRSSParserTask().execute(feed);
            }
            contained = false;
        }

        mRssGridAdapter = new RssGridAdapter(getActivity(), R.layout.grid_view_rss_tile, mRssList, user);
        mRssGrid.setAdapter(mRssGridAdapter);

        return inflatedView;
    }

    private class MyRSSParserTask extends UpdateRSSFeedTask {
        @Override
        protected void onPostExecute(RSSFeed rssFeed) {
            ((RssGridAdapter)mRssGrid.getAdapter()).notifyDataSetChanged();
            super.onPostExecute(rssFeed);
        }
    }
}

