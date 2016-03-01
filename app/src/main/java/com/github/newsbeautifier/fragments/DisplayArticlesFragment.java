package com.github.newsbeautifier.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.newsbeautifier.MyApplication;
import com.github.newsbeautifier.R;
import com.github.newsbeautifier.adapters.StaggeredRecyclerViewAdapter;
import com.github.newsbeautifier.models.RSSFeed;
import com.github.newsbeautifier.models.RSSItem;
import com.github.newsbeautifier.utils.MyRequestQueue;
import com.github.newsbeautifier.utils.RSSParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class DisplayArticlesFragment extends Fragment implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String FEED_URL = "FEED_URL";
    public static final String FEED_USER_ID = "FEED_USER_ID";
    public static final String ARTICLES = "ARTICLES";

    private RecyclerView mRecyclerView;
    private StaggeredRecyclerViewAdapter mAdapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private List<RSSItem> mArticleList;
    private String mFeedUrl;
    private Long mFeedUserId;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public DisplayArticlesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_display_articles, container, false);

        mArticleList = getArguments().getParcelableArrayList(ARTICLES);
        mFeedUrl = getArguments().getString(FEED_URL, null);
        mFeedUserId = getArguments().getLong(FEED_USER_ID, -1);

        mRecyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);

        mAdapter = new StaggeredRecyclerViewAdapter(getActivity(), mArticleList);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        setHasOptionsMenu(true);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<RSSItem> filteredModelList = filter(mArticleList, newText);
        mAdapter.animateTo(filteredModelList);
        mRecyclerView.scrollToPosition(0);
        return true;
    }

    private List<RSSItem> filter(List<RSSItem> models, String query) {
        query = query.toLowerCase();

        final List<RSSItem> filteredModelList = new ArrayList<>();
        for (RSSItem model : models) {
            final String text = model.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        if (mFeedUrl == null){
            refreshUserFeeds();
        } else {
            refreshThisFeed();
        }
    }

    private void refreshDone() {
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.notifyDataSetChanged();
    }

    private void refreshThisFeed() {
        final RSSFeed feed = new RSSFeed(mFeedUrl);
        feed.setUserId(mFeedUserId == -1 ? null : mFeedUserId);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, feed.getUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            InputStream stream = new ByteArrayInputStream(response.getBytes("UTF-8"));
                            RSSParser.getInstance().readRssFeed(stream, feed);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        mArticleList = feed.getItems();
                        refreshDone();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mArticleList = feed.getItems();
                refreshDone();
            }
        });
        MyRequestQueue.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void refreshUserFeeds() {
        final List<RSSFeed> feeds = ((MyApplication) getActivity().getApplication()).mUser.getFeeds();
        final List<RSSItem> newItems = new ArrayList<>();

        for (final RSSFeed feed : feeds){
            StringRequest stringRequest = new StringRequest(Request.Method.GET, feed.getUrl(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                InputStream stream = new ByteArrayInputStream(response.getBytes("UTF-8"));
                                RSSParser.getInstance().readRssFeed(stream, feed);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            newItems.addAll(feed.getItems());
                            if (feeds.lastIndexOf(feed) == feeds.size() - 1) {
                                mArticleList = newItems;
                                refreshDone();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (feeds.lastIndexOf(feed) == feeds.size() - 1) {
                        mArticleList = newItems;
                        refreshDone();
                    }
                }
            });
            MyRequestQueue.getInstance(getActivity()).addToRequestQueue(stringRequest);
        }
    }
}
