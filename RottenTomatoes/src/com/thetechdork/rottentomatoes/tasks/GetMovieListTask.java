/**
 * Midterm <br>
 * Filename: GetMovieListTask.java<br>
 * Students in Group: Aneesh Garg
 */
package com.thetechdork.rottentomatoes.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import android.os.AsyncTask;
import android.util.Log;

import com.thetechdork.rottentomatoes.activity.BaseActivity;
import com.thetechdork.rottentomatoes.activity.MoviesActivity;
import com.thetechdork.rottentomatoes.interfaces.MovieInterface;
import com.thetechdork.rottentomatoes.model.Movie;
import com.thetechdork.rottentomatoes.utility.RottenTomatoesJSONParser;
import com.thetechdork.rottentomatoes.utility.Utility;

/**
 *
 * @author Aneesh Garg
 * @since Nov 2, 2013
 */
public class GetMovieListTask extends AsyncTask<String,Void,List<Movie>> {
    
    private static HttpClient client = new DefaultHttpClient();
    
    private MovieInterface activityInterface;
    
    public GetMovieListTask(MovieInterface activityInterface) {
        this.activityInterface = activityInterface;
    }

    @Override
    protected List<Movie> doInBackground(String... params) {
        List<Movie> movieList = new ArrayList<Movie>();
        String urlString = Utility.RT_BASE_URL;
        if (MoviesActivity.BOX_OFFICE_MOVIES.equals(params[0])) 
            urlString += "/lists/movies/box_office.json?limit=50";
         else if (MoviesActivity.IN_THEATERS_MOVIES.equals(params[0])) 
             urlString += "/lists/movies/in_theaters.json?page_limit=50";
         else if (MoviesActivity.OPENING_MOVIES.equals(params[0])) 
             urlString += "/lists/movies/opening.json?limit=50";
         else if (MoviesActivity.UPCOMING_MOVIES.equals(params[0]))
             urlString += "/lists/movies/upcoming.json?page_limit=50";  
        urlString += "&apikey="+Utility.API_KEY;
        
        HttpGet request = new HttpGet(urlString);
        
        try {
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                InputStream in = response.getEntity().getContent();
                movieList = RottenTomatoesJSONParser.parseMoviesJSON(in);
                in.close();
            }
        } catch (ClientProtocolException e) {
            Log.e(BaseActivity.LOG_KEY, e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(BaseActivity.LOG_KEY, e.getMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e(BaseActivity.LOG_KEY, e.getMessage());
            e.printStackTrace();
        } catch (ParseException e) {
            Log.e(BaseActivity.LOG_KEY, e.getMessage());
            e.printStackTrace();
        }
        
        return movieList;
    }

    @Override
    protected void onPostExecute(List<Movie> result) {
        super.onPostExecute(result);
        activityInterface.setMovieList(result);
    }

}
