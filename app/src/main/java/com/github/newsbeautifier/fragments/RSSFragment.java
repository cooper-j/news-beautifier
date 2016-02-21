package com.github.newsbeautifier.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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

import java.util.ArrayList;
import java.util.List;

public class RSSFragment extends Fragment {

    private RSSAdapter adapter;

    public RSSFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rss, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new RSSAdapter();

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new MyUpdateRSSFeedTask().execute(adapter.getItem(position));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                addToMyFeed(adapter.getItem(position));
                return false;
            }
        });
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
            List<RSSFeed> all = new Select()
                    .from(RSSFeed.class).queryList();

            addAll(all);
            notifyDataSetChanged();
        }
    }

    public interface OnMyFeedsChanged{
        void onMyFeedsChanged();
    }
}
