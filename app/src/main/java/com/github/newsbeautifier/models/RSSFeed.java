package com.github.newsbeautifier.models;

import java.util.ArrayList;
import java.util.List;

/**
 * * NewsBeautifier
 * Created by jerem_000 on 2/18/2016.
 */
public class RSSFeed {
    public static final String FEED_TAG = "feed";
    public static final String ENTRY_TAG = "entry";
    public static final String ITEM_TAG = "item";

    public static final String TITLE_TAG = "title";
    public static final String CATEGORY_TAG = "category";
    public static final String ICON_TAG = "icon";
    public static final String DESCRIPTION_TAG = "description";
    public static final String LINK_TAG = "link";
    public static final String UPDATE_TAG = "updated";

    public static final String LANGUAGE_ATTRIBUTE = "xml:lang";
    public static final String CHANNEL_TAG = "channel";
    public static final String LANGUAGE_TAG = "language";

    private String mTitle = "";
    private String mCategory = "";
    private String mIcon = "";
    private String mLink = "";
    private String mDescription = "";
    private String mLanguage = "";
    private String mUpdatedDate = "";
    private List<RSSItem> mItems = new ArrayList<>();

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String pTitle) {
        mTitle = pTitle;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String pIcon) {
        mIcon = pIcon;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String pLink) {
        mLink = pLink;
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

    public String getUpdatedDate() {
        return mUpdatedDate;
    }

    public void setUpdatedDate(String pUpdateddate) {
        mUpdatedDate = pUpdateddate;
    }

    public List<RSSItem> getItems() {
        return mItems;
    }

    public void setItems(List<RSSItem> pItems) {
        mItems = pItems;
    }

    public void addEntry(RSSItem item) {
        this.mItems.add(item);
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String pCategory) {
        mCategory = pCategory;
    }
}
