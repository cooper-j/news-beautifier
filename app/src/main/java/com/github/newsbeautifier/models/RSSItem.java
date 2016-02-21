package com.github.newsbeautifier.models;

import com.github.newsbeautifier.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * * NewsBeautifier
 * Created by jerem_000 on 2/18/2016.
 */

@Table(database = MyDatabase.class)
public class RSSItem extends BaseModel {
    public static final String TITLE_TAG = "title";
    public static final String CATEGORY_TAG = "category";
    public static final String LANGUAGE_TAG = "language";
    public static final String DESCRIPTION_TAG = "description";
    public static final String CONTENT_TAG = "content";
    public static final String LINK_TAG = "link";
    public static final String AUTHOR_TAG = "author";
    public static final String PUBDATE_TAG = "published";
    public static final String PUBDATE_TAG2 = "pubDate";


    @Column
    private String feedLink;

    @PrimaryKey
    @Column
    private String title = "";

    @Column
    private String category = "";

    @Column
    private String content = "";

    @Column
    private String language = "";

    @Column
    private String description = "";

    @Column
    private String link = "";

    @Column
    private String author = "";

    @Column
    private String pubDate = "";

    public RSSItem() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String pTitle) {
        title = pTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String pContent) {
        content = pContent;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String pLink) {
        link = pLink;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String pAuthor) {
        author = pAuthor;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pPubdate) {
        pubDate = pPubdate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String pDescription) {
        description = pDescription;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String pLanguage) {
        language = pLanguage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String pCategory) {
        category = pCategory;
    }

    public String getFeedLink() {
        return feedLink;
    }

    public void setFeedLink(String feedlink) {
        feedLink = feedlink;
    }
}
