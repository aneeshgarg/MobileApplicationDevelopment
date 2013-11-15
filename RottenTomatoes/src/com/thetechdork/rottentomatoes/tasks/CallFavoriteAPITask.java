/**
 * Midterm <br>
 * Filename: CallFavoriteAPPITask.java<br>
 * Students in Group: Aneesh Garg
 */
package com.thetechdork.rottentomatoes.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParserException;

import com.thetechdork.rottentomatoes.activity.BaseActivity;
import com.thetechdork.rottentomatoes.interfaces.FavoriteInterface;
import com.thetechdork.rottentomatoes.model.FavoritesAPIResponse;
import com.thetechdork.rottentomatoes.utility.Config;
import com.thetechdork.rottentomatoes.utility.FavoritesXMLPullParser;
import com.thetechdork.rottentomatoes.utility.Utility;

import android.os.AsyncTask;
import android.util.Log;

/**
 *
 * @author Aneesh Garg
 * @since Nov 3, 2013
 */
public class CallFavoriteAPITask extends AsyncTask<String, Void, FavoritesAPIResponse> {
    private static HttpClient client = new DefaultHttpClient();
    
    private String mode;
    private String username;
    private FavoriteInterface favoriteInterface;
    
    public CallFavoriteAPITask(FavoriteInterface favInterface) {
        this.favoriteInterface = favInterface;
    }

    @Override
    protected FavoritesAPIResponse doInBackground(String... params) {
        FavoritesAPIResponse favApiResponse = null;
        this.mode = params[0];
        this.username = params[1];
        
        String urlString = Utility.FAVORITE_BASE_URL;
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("uid", Config.getUid(this.username)));
        Log.d(BaseActivity.LOG_KEY,"UserId::: "+Config.getUid(username));
        
        if (FavoriteInterface.IS_FAVORITE_MODE.equals(mode)) {
            String mId = params[2];
            postParams.add(new BasicNameValuePair("mid", mId));
            Log.d(BaseActivity.LOG_KEY,"MovieId::: " + mId);
            urlString += "/isFavorite.php";
        }
        else if (FavoriteInterface.ADD_FAVORITE_MODE.equals(mode)) {
            String mId = params[2];
            postParams.add(new BasicNameValuePair("mid", mId));
            urlString += "/addToFavorites.php";            
        }
        else if (FavoriteInterface.DELETE_FAVORITE_MODE.equals(mode)) {
            String mId = params[2];
            postParams.add(new BasicNameValuePair("mid", mId));
            urlString += "/deleteFavorite.php";            
        }
        else if (FavoriteInterface.DELETE_ALL_FAVORITES_MODE.equals(mode))
            urlString += "/deleteAllFavorites.php";
        else if (FavoriteInterface.GET_ALL_FAVORITES_MODE.equals(mode))
            urlString += "/getAllFavorites.php";
        else if (FavoriteInterface.GET_FAVORITE_STATS_MODE.equals(mode))
            urlString += "/getFavoriteStats.php";
        
        HttpPost request = new HttpPost(urlString);
        try {
            UrlEncodedFormEntity formParams = new UrlEncodedFormEntity(postParams);
            request.setEntity(formParams);            
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                InputStream in = response.getEntity().getContent();
                favApiResponse = FavoritesXMLPullParser.parseXML(in);
                in.close();
            }
        } catch (UnsupportedEncodingException e) {
            Log.e(BaseActivity.LOG_KEY,e.getMessage());
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            Log.e(BaseActivity.LOG_KEY,e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(BaseActivity.LOG_KEY,e.getMessage());
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            Log.e(BaseActivity.LOG_KEY,e.getMessage());
            e.printStackTrace();
        }
        
        
        return favApiResponse;
    }

    @Override
    protected void onPostExecute(FavoritesAPIResponse result) {
        if (FavoriteInterface.IS_FAVORITE_MODE.equals(mode))
            favoriteInterface.isFavorite(result);
        else if (FavoriteInterface.ADD_FAVORITE_MODE.equals(mode))
            favoriteInterface.addedToFavorite(result);
        else if (FavoriteInterface.DELETE_FAVORITE_MODE.equals(mode))
            favoriteInterface.deletedFromFavorite(result);
        else if (FavoriteInterface.DELETE_ALL_FAVORITES_MODE.equals(mode))
            favoriteInterface.deletedAllFavorites(result);
        else if (FavoriteInterface.GET_ALL_FAVORITES_MODE.equals(mode))
            favoriteInterface.setAllFavorites(result);
        else if (FavoriteInterface.GET_FAVORITE_STATS_MODE.equals(mode))
            favoriteInterface.setFavoriteStats(result);
    }

}
