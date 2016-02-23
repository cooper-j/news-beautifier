package com.github.newsbeautifier.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.newsbeautifier.MyApplication;
import com.github.newsbeautifier.R;
import com.github.newsbeautifier.models.RSSFeed;
import com.github.newsbeautifier.models.RSSItem;
import com.github.newsbeautifier.models.User;
import com.github.newsbeautifier.utils.UpdateRSSFeedTask;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class FeedListFragment extends Fragment implements SearchView.OnQueryTextListener {

    private RSSAdapter mAdapter;
    private ListView mListView;
    private ListView mResultListView;
    private RSSAdapter mResultAdapter;

    public FeedListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rss, container, false);

        mResultListView = (ListView) view.findViewById(R.id.result_list_view);
        mListView = (ListView) view.findViewById(R.id.list_view);
        mAdapter = new RSSAdapter();
        mAdapter.fetchAvailableFeeds();

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new MyUpdateRSSFeedTask().execute(mAdapter.getItem(position));
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                addToMyFeed(mAdapter.getItem(position));
                return true;
            }
        });

        mResultAdapter = new RSSAdapter();
        mResultListView.setAdapter(mResultAdapter);
        setHasOptionsMenu(true);

        return view;
    }

    private void addToMyFeed(RSSFeed feed) {
        User user = ((MyApplication)getActivity().getApplication()).mUser;

        if (user != null){
            if (user.getFeeds() == null){
                user.setFeeds(new ArrayList<RSSFeed>());
            }
            List<RSSFeed> feeds = user.getFeeds();
            boolean contained = false;
            for (RSSFeed tmp : feeds){
                if (tmp.getLink().equals(feed.getLink())) {
                    contained = true;
                }
            }

            if (!contained){
                feed.setUserId(user.getId());
                feed.update();
                user.getFeeds().add(feed);

                try {
                    ((OnMyFeedsChanged)getActivity()).onMyFeedsChanged();
                } catch (ClassCastException e){
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(), "This feed has been added to your feeds", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "You already follow this feed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mResultAdapter.clear();
        String query = Normalizer.normalize(newText, Normalizer.Form.NFD);
        query = query.replaceAll("[^\\p{ASCII}]", "");

        if (!query.isEmpty()) {
            for (int i = 0; i < mAdapter.getCount(); i++) {
                RSSFeed item = mAdapter.getItem(i);
                String c = Normalizer.normalize(item.toString(), Normalizer.Form.NFD);
                c = c.replaceAll("[^\\p{ASCII}]", "");

                if (c.toLowerCase().contains(query.toLowerCase())) {
                    mResultAdapter.add(item);
                }
            }
        }
        mResultAdapter.notifyDataSetChanged();
        return true;
    }

    private class MyUpdateRSSFeedTask extends UpdateRSSFeedTask {
        @Override
        protected void onPostExecute(RSSFeed rssFeed) {
            super.onPostExecute(rssFeed);

            if (rssFeed != null && !rssFeed.getLink().isEmpty()) {
                rssFeed.save();
                for (RSSItem item : rssFeed.getItems()) {
                    item.save();
                }
            }
        }
    }

    private class RSSAdapter extends ArrayAdapter<RSSFeed>{
        public RSSAdapter() {
            super(getActivity(), android.R.layout.simple_list_item_1);
        }

        public void fetchAvailableFeeds(){
            List<RSSFeed> all = new Select()
                    .from(RSSFeed.class).queryList();

            addAll(all);
            notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.rss_fragment_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                mResultListView.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mListView.setVisibility(View.VISIBLE);
                mResultListView.setVisibility(View.GONE);

                return true;
            }
        });
    }

    public interface OnMyFeedsChanged{
        void onMyFeedsChanged();
    }
}
