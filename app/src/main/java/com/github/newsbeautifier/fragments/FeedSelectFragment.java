package com.github.newsbeautifier.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.github.newsbeautifier.R;
import com.github.newsbeautifier.adapters.RssGridAdapter;
import com.github.newsbeautifier.models.RSSFeed;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.List;

public class FeedSelectFragment extends Fragment {

    private List<RSSFeed> mRssList = new ArrayList<>();
    private GridView mRssGrid;
    private RssGridAdapter mRssGridAdapter;

    public FeedSelectFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_feed_select, container, false);

        mRssGrid = (GridView)inflatedView.findViewById(R.id.rssGridView);

        mRssList = new Select()
                .from(RSSFeed.class).queryList();

        mRssGridAdapter = new RssGridAdapter(getActivity(), R.layout.grid_view_rss_tile, mRssList);
        mRssGrid.setAdapter(mRssGridAdapter);

        return inflatedView;
    }
}

