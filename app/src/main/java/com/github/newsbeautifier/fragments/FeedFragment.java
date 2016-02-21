package com.github.newsbeautifier.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.newsbeautifier.R;
import com.github.newsbeautifier.models.RSSFeed;

/**
 * * NewsBeautifier
 * Created by jerem_000 on 2/21/2016.
 */
public class FeedFragment extends Fragment {

    public static final String FEED = "FEED";

    private RSSFeed mFeed;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        mFeed = getArguments().getParcelable(FEED);

        if (mFeed != null) {
            setTitle(mFeed.toString());
        }
        return rootView;
    }

    private void setTitle(String title) {
        Activity activity = getActivity();

        if (title != null && activity != null && activity instanceof AppCompatActivity){
            ActionBar supportActionBar = ((AppCompatActivity) activity).getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setTitle(title);
            }
        }
    }
}
