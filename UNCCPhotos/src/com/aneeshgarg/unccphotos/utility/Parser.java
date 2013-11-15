/**
 * Assignment #: 5<br>
 * Filename: Parser.java<br>
 * Students in Group: Aneesh Garg
 */
package com.aneeshgarg.unccphotos.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.aneeshgarg.unccphotos.model.Photo;

public class Parser {

    public static final String PHOTOS_TAG = "photos";
    public static final String PHOTO_TAG  = "photo";
    public static final String TITLE_TAG  = "title";
    public static final String VIEWS_TAG  = "views";
    public static final String URL_TAG    = "url_m";
    public static final String ID_TAG    = "id";

    public static class JSONParsor {

        public static ArrayList<Photo> parseJSON(InputStream in) throws IOException, JSONException {
            String jsonString = getJSONString(in);
            Log.d("AneeshGarg", jsonString);
            ArrayList<Photo> photoList = new ArrayList<Photo>();

            JSONObject responseObject = new JSONObject(jsonString);
            JSONObject photosObject = responseObject.getJSONObject(Parser.PHOTOS_TAG);
            JSONArray photosArray = photosObject.getJSONArray(Parser.PHOTO_TAG);
            for (int i = 0; i < photosArray.length(); i++) {
                JSONObject photoObject = photosArray.getJSONObject(i);
                Photo photo = new Photo();
                photo.setTitle(photoObject.getString(Parser.TITLE_TAG));
                photo.setViews(Integer.valueOf(photoObject.getString(Parser.VIEWS_TAG)));
                photo.setUrl_m(photoObject.getString(Parser.URL_TAG));
                photo.setId(photoObject.getString(Parser.ID_TAG));
                photoList.add(photo);
            }

            return photoList;
        }

        private static String getJSONString(InputStream in) throws IOException {

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
            return sb.toString();
        }
    }

    public static class PhotoXmlPullParser {

        private static final String UTF_8 = "UTF-8";

        public static ArrayList<Photo> parseXML(InputStream in) throws XmlPullParserException, IOException {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(in, UTF_8);

            ArrayList<Photo> photoList = null;
            Photo photo = null;

            int event = parser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {

                if (event == XmlPullParser.START_TAG) {
                    if (Parser.PHOTOS_TAG.equals(parser.getName()))
                        photoList = new ArrayList<Photo>();
                    else if (Parser.PHOTO_TAG.equals(parser.getName())) {
                        photo = new Photo();
                        photo.setTitle(parser.getAttributeValue(null,TITLE_TAG));
                        photo.setUrl_m(parser.getAttributeValue(null, URL_TAG));
                        photo.setViews(Integer.valueOf(parser.getAttributeValue(null, VIEWS_TAG)));
                        photo.setId(parser.getAttributeValue(null, ID_TAG));
                        photoList.add(photo);
                        photo = null;
                    }
                }

                event = parser.next();
            }
            return photoList;
        }

    }

}
