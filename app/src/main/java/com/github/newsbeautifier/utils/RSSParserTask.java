package com.github.newsbeautifier.utils;

import android.os.AsyncTask;
import android.util.Xml;

import com.github.newsbeautifier.models.RSSFeed;
import com.github.newsbeautifier.models.RSSItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * * NewsBeautifier
 * Created by jerem_000 on 2/18/2016.
 */
public abstract class RSSParserTask extends AsyncTask<String, Boolean, RSSFeed> {
    public static final String[] RSS_FEEDS = {
            "http://www.theverge.com/rss/frontpage",
            "http://www.jeuxvideo.com/rss/rss-ps4.xml",
            "https://news.google.fr/news?cf=all&hl=fr&pz=1&ned=fr&output=rss",
            "http://feeds.feedburner.com/Mobilecrunch"
    };

    private static final String ns = null;
    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;

    @Override
    protected RSSFeed doInBackground(String... params) {
        if (params.length == 0){
            return new RSSFeed();
        }
        InputStream in = readFromUrl(params[0]);
        if (in == null){
            return new RSSFeed();
        }

        return readRssFeed(in);
    }

    private RSSFeed readRssFeed(InputStream in) {
        RSSFeed feed = new RSSFeed();
        XmlPullParser parser = Xml.newPullParser();

        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();

                switch (name) {
                    case RSSFeed.FEED_TAG:
                        feed.setLanguage(readLanguage(parser));
                        parser.require(XmlPullParser.START_TAG, ns, RSSFeed.ENTRY_TAG);
                        break;
                    case RSSFeed.CHANNEL_TAG:
                        parser.require(XmlPullParser.START_TAG, ns, RSSFeed.CHANNEL_TAG);
                        break;
                    case RSSFeed.TITLE_TAG:
                        feed.setTitle(readText(parser));
                        break;
                    case RSSFeed.DESCRIPTION_TAG:
                        feed.setDescription(readText(parser));
                        break;
                    case RSSFeed.ICON_TAG:
                        feed.setIcon(readText(parser));
                        break;
                    case RSSFeed.CATEGORY_TAG:
                        feed.setCategory(readText(parser));
                        break;
                    case RSSFeed.UPDATE_TAG:
                        feed.setUpdatedDate(readText(parser));
                        break;
                    case RSSFeed.LINK_TAG:
                        feed.setLink(readLink(parser));
                        break;
                    case RSSFeed.LANGUAGE_TAG:
                        feed.setLanguage(readText(parser));
                        break;
                    case RSSFeed.ENTRY_TAG:
                        feed.addEntry(readEntry(parser, RSSFeed.ENTRY_TAG));
                        break;
                    case RSSFeed.ITEM_TAG:
                        feed.addEntry(readEntry(parser, RSSFeed.ITEM_TAG));
                        break;
                    default:
                        skip(parser);
                        break;
                }
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return feed;
    }

    private String readLanguage(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, RSSFeed.FEED_TAG);

        return parser.getAttributeValue(null, RSSFeed.LANGUAGE_ATTRIBUTE);
    }

    private RSSItem readEntry(XmlPullParser parser, String itemTag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, itemTag);
        RSSItem item = new RSSItem();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            switch (name) {
                case RSSItem.TITLE_TAG:
                    item.setTitle(readTitle(parser));
                    break;
                case RSSItem.CONTENT_TAG:
                    item.setContent(readText(parser));
                    break;
                case RSSItem.DESCRIPTION_TAG:
                    item.setDescription(readText(parser));
                    break;
                case RSSItem.LANGUAGE_TAG:
                    item.setLanguage(readText(parser));
                    break;
                case RSSItem.CATEGORY_TAG:
                    item.setCategory(readText(parser));
                    break;
                case RSSItem.LINK_TAG:
                    item.setLink(readLink(parser));
                    break;
                case RSSItem.AUTHOR_TAG:
                    item.setAuthor(readAuthor(parser));
                    break;
                case RSSItem.PUBDATE_TAG:
                case RSSItem.PUBDATE_TAG2:
                    item.setPubDate(readText(parser));
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return item;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = "";
        parser.require(XmlPullParser.START_TAG, ns, RSSItem.LINK_TAG);
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "rel");
        if (tag.equals(RSSItem.LINK_TAG)) {
            if (relType != null && relType.equals("alternate")){
                link = parser.getAttributeValue(null, "href");
                parser.nextTag();
            } else {
                link = readText(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, RSSItem.LINK_TAG);

        return link;
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, RSSItem.TITLE_TAG);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, RSSItem.TITLE_TAG);

        return title;
    }

    private String readAuthor(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, RSSItem.AUTHOR_TAG);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("name")) {
                String author = readText(parser);
                parser.nextTag();
                parser.require(XmlPullParser.END_TAG, ns, RSSItem.AUTHOR_TAG);
                return author;
            }
        }
        return "";
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private InputStream readFromUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            return conn.getInputStream();
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
