package com.github.newsbeautifier.models;

import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

/**
 * * NewsBeautifier
 * Created by jerem_000 on 2/19/2016.
 */

public class User {

    private Long id = (long)1;

    private List<RSSFeed> feeds = null;

    public User(){}

    public List<RSSFeed> getFeeds() {
        if (feeds == null){
            feeds = new Select()
                    .from(RSSFeed.class)
                    .where(RSSFeed_Table.userId.eq(getId()))
                    .queryList();
        }
        return feeds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFeeds(List<RSSFeed> pFeeds) {
        feeds = pFeeds;
    }
}
