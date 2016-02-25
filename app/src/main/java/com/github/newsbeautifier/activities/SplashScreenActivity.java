package com.github.newsbeautifier.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.newsbeautifier.R;
import com.github.newsbeautifier.models.RSSFeed;
import com.github.newsbeautifier.utils.MyRequestQueue;
import com.github.newsbeautifier.utils.RSSParser;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

public class SplashScreenActivity extends AppCompatActivity {

    private List<RSSFeed> feeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        feeds = new Select().from(RSSFeed.class).queryList();
        addNewFeeds(feeds, Arrays.asList(RSSParser.RSS_FEEDS));

        for (RSSFeed feed : feeds){
            MyRequestQueue.getInstance(this).addToRequestQueue(getFeedStringRequest(feed));
        }
    }

    private StringRequest getFeedStringRequest(final RSSFeed feed){
        return new StringRequest(Request.Method.GET, feed.getUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            InputStream stream = new ByteArrayInputStream(response.getBytes("UTF-8"));
                            RSSParser.getInstance().readRssFeed(stream, feed);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        if (feeds.lastIndexOf(feed) == feeds.size() - 1){
                            startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (feeds.lastIndexOf(feed) == feeds.size() - 1){
                    startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
                }
            }
        });
    }

    private void addNewFeeds(List<RSSFeed> feeds, List<RSSFeed> defRssFeeds) {
        for (RSSFeed tmp : defRssFeeds){
            boolean added = true;
            for (RSSFeed tmp2 : feeds){
                if (tmp2.getUrl().equals(tmp.getUrl())){
                    added = false;
                    break;
                }
            }
            if (added){
                feeds.add(tmp);
            }
        }
    }
}
