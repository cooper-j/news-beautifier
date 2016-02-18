package com.github.newsbeautifier.models;

/**
 * * NewsBeautifier
 * Created by jerem_000 on 2/18/2016.
 */
public class RSSItem {
    public static final String TITLE_TAG = "title";
    public static final String CATEGORY_TAG = "category";
    public static final String LANGUAGE_TAG = "language";
    public static final String DESCRIPTION_TAG = "description";
    public static final String CONTENT_TAG = "content";
    public static final String LINK_TAG = "link";
    public static final String AUTHOR_TAG = "author";
    public static final String PUBDATE_TAG = "published";
    public static final String PUBDATE_TAG2 = "pubDate";

    private String mTitle = "";
    private String mCategory = "";
    private String mContent = "";
    private String mLanguage = "";
    private String mDescription = "";
    private String mLink = "";
    private String mAuthor = "";
    private String mPubDate = "";

    public RSSItem(String title, String content, String link) {
        this.mTitle = title;
        this.mContent = content;
        this.mLink = link;
    }

    public RSSItem() {

    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String pTitle) {
        mTitle = pTitle;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String pContent) {
        mContent = pContent;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String pLink) {
        mLink = pLink;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String pAuthor) {
        mAuthor = pAuthor;
    }

    public String getPubDate() {
        return mPubDate;
    }

    public void setPubDate(String pPubdate) {
        mPubDate = pPubdate;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String pDescription) {
        mDescription = pDescription;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String pLanguage) {
        mLanguage = pLanguage;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String pCategory) {
        mCategory = pCategory;
    }
}
