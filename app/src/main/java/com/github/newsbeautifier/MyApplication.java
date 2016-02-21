package com.github.newsbeautifier;

import android.app.Application;

import com.github.newsbeautifier.models.RSSFeed;
import com.github.newsbeautifier.models.User;
import com.github.newsbeautifier.utils.UpdateRSSFeedTask;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * * NewsBeautifier
 * Created by jerem_000 on 2/15/2016.
 */
public class MyApplication extends Application {

    public User mUser;

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);

        mUser = new User();

        if (mUser.getFeeds() == null){
            mUser.setFeeds(new ArrayList<RSSFeed>());
        }
        List<RSSFeed> feeds = new Select().from(RSSFeed.class).queryList();

        if (feeds.size() == 0){
            feeds = new ArrayList<>(Arrays.asList(UpdateRSSFeedTask.RSS_FEEDS));
        }
        for (RSSFeed feed : feeds){
            new MyFeedUpdate().execute(feed);
        }
    }

    private class MyFeedUpdate extends UpdateRSSFeedTask {
        @Override
        protected void onPostExecute(RSSFeed rssFeed) {
            super.onPostExecute(rssFeed);
        }
    }
}
