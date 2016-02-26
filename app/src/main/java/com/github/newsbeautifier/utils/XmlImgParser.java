package com.github.newsbeautifier.utils;

import android.os.AsyncTask;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * * NewsBeautifier
 * Created by jerem_000 on 2/26/2016.
 */
public class XmlImgParser extends AsyncTask<String, Void, String> {

    private OnPostExecuteListener listener;

    public XmlImgParser(OnPostExecuteListener listener){
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... source) {
        InputStream in = getInputStreamFromSource(source[0]);
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

                if (name.equals("img")) {
                    return parser.getAttributeValue(null, "src");
                }
            }
        } catch (XmlPullParserException | NullPointerException | IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (listener != null){
            listener.onPostExecute(s);
        }
    }

    private InputStream getInputStreamFromSource(String xml) {
        try {
            return new ByteArrayInputStream(xml.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface OnPostExecuteListener{
        void onPostExecute(String url);
    }
}
