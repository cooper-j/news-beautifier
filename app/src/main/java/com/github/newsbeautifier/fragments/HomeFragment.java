package com.github.newsbeautifier.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.newsbeautifier.MyApplication;
import com.github.newsbeautifier.R;
import com.github.newsbeautifier.adapters.StaggeredRecyclerViewAdapter;
import com.github.newsbeautifier.models.RSSFeed;
import com.github.newsbeautifier.models.RSSItem;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        List<RSSFeed> feedList = ((MyApplication)getActivity().getApplication()).mUser.getFeeds();
        List<RSSItem> articleList = new ArrayList<>();

        for (int i = 0; i < feedList.size(); ++i)
            articleList.addAll(feedList.get(i).getItems());


        StaggeredRecyclerViewAdapter rcAdapter = new StaggeredRecyclerViewAdapter(getActivity(), articleList);
        recyclerView.setAdapter(rcAdapter);

        return v;
    }
}
