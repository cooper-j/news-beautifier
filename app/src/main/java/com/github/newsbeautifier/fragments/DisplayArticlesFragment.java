package com.github.newsbeautifier.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.newsbeautifier.R;
import com.github.newsbeautifier.adapters.StaggeredRecyclerViewAdapter;
import com.github.newsbeautifier.models.RSSItem;

import java.util.ArrayList;
import java.util.List;

public class DisplayArticlesFragment extends Fragment implements SearchView.OnQueryTextListener {

    public static final String ARTICLES = "ARTICLES";

    private RecyclerView mRecyclerView;
    private StaggeredRecyclerViewAdapter mAdapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private List<RSSItem> mArticleList;

    public DisplayArticlesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mArticleList = getArguments().getParcelableArrayList(ARTICLES);

        mRecyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);

        mAdapter = new StaggeredRecyclerViewAdapter(getActivity(), mArticleList);

        mRecyclerView.setAdapter(mAdapter);

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
}
