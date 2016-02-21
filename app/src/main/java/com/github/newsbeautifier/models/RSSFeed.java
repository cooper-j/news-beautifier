package com.github.newsbeautifier.models;

import com.github.newsbeautifier.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * * NewsBeautifier
 * Created by jerem_000 on 2/18/2016.
 */

@ModelContainer
@Table(database = MyDatabase.class)
public class RSSFeed extends BaseModel{
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
    public static final String IMAGE_TAG = "image";

    @PrimaryKey
    @Column
    private String link = "";

    @Column
    private String title = "";

    @Column
    private String category = "";

    @Column
    private String icon = "";

    @Column
    private String description = "";

    @Column
    private String language = "";

    @Column
    private String updatedDate = "";

    List<RSSItem> items = new ArrayList<>();

    @Column
    private String image;

    @Column
    Long userId;

    public RSSFeed(){

    }

    public RSSFeed(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String pTitle) {
        title = pTitle;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String pIcon) {
        icon = pIcon;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String pLink) {
        link = pLink;
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

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String pUpdateddate) {
        updatedDate = pUpdateddate;
    }

    public List<RSSItem> getItems() {
        if (items == null || items.size() == 0){
            items = SQLite.select()
                    .from(RSSItem.class)
                    .where(RSSItem_Table.feedLink.eq(link))
                    .queryList();
        }
        return items;
    }

    public void setItems(List<RSSItem> pItems) {
        items = pItems;
    }

    public void addEntry(RSSItem item) {
        this.items.add(item);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String pCategory) {
        category = pCategory;
    }

    @Override
    public String toString() {
        return title == null || title.isEmpty() ? link : title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userid) {
        userId = userid;
    }


}
