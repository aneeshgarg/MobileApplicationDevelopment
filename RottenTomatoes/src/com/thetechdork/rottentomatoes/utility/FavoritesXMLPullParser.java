/**
 * Midterm <br>
 * Filename: FavoritesXMLPullParser.java<br>
 * Students in Group: Aneesh Garg
 */
package com.thetechdork.rottentomatoes.utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.thetechdork.rottentomatoes.activity.BaseActivity;
import com.thetechdork.rottentomatoes.model.FavError;
import com.thetechdork.rottentomatoes.model.Favorite;
import com.thetechdork.rottentomatoes.model.FavoritesAPIResponse;

/**
 * 
 * @author Aneesh Garg
 * @since Oct 31, 2013
 */
public class FavoritesXMLPullParser {

    private static String RESPONSE_TAG    = "response";
    private static String ERROR_TAG       = "error";
    private static String ID_TAG          = "id";
    private static String MESSAGE_TAG     = "message";
    private static String FAVORITES_TAG   = "favorites";
    private static String FAVORITE_TAG    = "favorite";
    private static String IS_FAVORITE_TAG = "isFavorite";
    private static String COUNT_TAG       = "count";

    public static FavoritesAPIResponse parseXML(InputStream xmlIn) throws XmlPullParserException, IOException {

        FavoritesAPIResponse response = null;
        List<Favorite> favList = null;
        Favorite fav = null;
        FavError error = null;

        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setInput(xmlIn, "UTF-8");

        int event = parser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {

            switch (event) {

                case XmlPullParser.START_TAG:
                    if (RESPONSE_TAG.equals(parser.getName()))
                        response = new FavoritesAPIResponse();
                    else if (ERROR_TAG.equals(parser.getName())) 
                        error = new FavError();
                    else if (FAVORITES_TAG.equals(parser.getName())) 
                        favList = new ArrayList<Favorite>();
                    else if (FAVORITE_TAG.equals(parser.getName()))
                        fav = new Favorite();
                    else if (ID_TAG.equals(parser.getName())) {
                        if (error != null)
                            error.setId(Integer.valueOf(parser.nextText()));
                        else if (fav != null)
                            fav.setId(parser.nextText());                        
                    }
                    else if (MESSAGE_TAG.equals(parser.getName()) && error != null)
                        error.setErrorMessage(parser.nextText());
                    else if (IS_FAVORITE_TAG.equals(parser.getName()))
                        fav.setFavourite(Boolean.valueOf(parser.nextText()));
                    else if (COUNT_TAG.equals(parser.getName()))
                        fav.setCount(Integer.valueOf(parser.nextText()));
                    break;

                case XmlPullParser.END_TAG:
                    if (ERROR_TAG.equals(parser.getName())) { 
                        response.setError(error);
                        error = null;
                    }                    
                    else if (FAVORITES_TAG.equals(parser.getName())) {
                        response.setFavorites(favList);
                        favList = null;
                    }
                    else if (FAVORITE_TAG.equals(parser.getName())) {
                        favList.add(fav);
                        fav = null;
                    }
                    break;
            }

            event = parser.next();
        }
        
        Log.d(BaseActivity.LOG_KEY, response.toString());
        
        return response;
    }

}
