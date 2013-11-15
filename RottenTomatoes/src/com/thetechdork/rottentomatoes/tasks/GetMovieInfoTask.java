/**
 * Midterm <br>
 * Filename: GetMovieInfoTask.java<br>
 * Students in Group: Aneesh Garg
 */
package com.thetechdork.rottentomatoes.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import com.thetechdork.rottentomatoes.activity.BaseActivity;
import com.thetechdork.rottentomatoes.activity.MovieActivity;
import com.thetechdork.rottentomatoes.interfaces.MovieInterface;
import com.thetechdork.rottentomatoes.model.Movie;
import com.thetechdork.rottentomatoes.utility.RottenTomatoesJSONParser;
import com.thetechdork.rottentomatoes.utility.Utility;

import android.os.AsyncTask;
import android.util.Log;

/**
 * 
 * @author Aneesh Garg
 * @since Nov 3, 2013
 */
public class GetMovieInfoTask extends AsyncTask<String, Void, Movie> {
    private static HttpClient client = new DefaultHttpClient();

    MovieInterface            movieInterface;
    String                    mode;
    Movie                     movie;

    public GetMovieInfoTask(MovieInterface movieInterface, Movie movie) {
        this.movieInterface = movieInterface;
        this.movie = movie;
    }

    @Override
    protected Movie doInBackground(String... params) {
        this.mode = params[0];
        String id = "";
        if (MovieActivity.GENRE_MODE.equals(this.mode))
            id = this.movie.getId();
        else if (MovieActivity.MOVIE_INFO_MODE.equals(this.mode))
            id = params[1];
        else if (MovieActivity.MOVIE_TITLE_MODE.equals(this.mode))
            id = params[1];

        String urlString = Utility.RT_BASE_URL;
        urlString += "/movies/" + id + ".json?apikey=" + Utility.API_KEY;

        HttpGet request = new HttpGet(urlString);
        HttpResponse response;
        try {
            response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                InputStream in = response.getEntity().getContent();
                if (MovieActivity.GENRE_MODE.equals(this.mode)) {
                    List<String> genreList = RottenTomatoesJSONParser.parseGenreFromMovieJSON(in);
                    this.movie.setGenre(genreList);
                }
                else if (MovieActivity.MOVIE_INFO_MODE.equals(this.mode))
                    this.movie = RottenTomatoesJSONParser.parseMovieJSON(in);
                else if (MovieActivity.MOVIE_TITLE_MODE.equals(this.mode))
                    this.movie = RottenTomatoesJSONParser.parseMovieJSONForTitle(in);
                
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

        return this.movie;
    }

    @Override
    protected void onPostExecute(Movie result) {
        
        super.onPostExecute(result);
        if (MovieActivity.GENRE_MODE.equals(this.mode))
            movieInterface.updateGenreToMovie(result);
        else if (MovieActivity.MOVIE_INFO_MODE.equals(this.mode))
            movieInterface.addMovieToList(result);
        else if (MovieActivity.MOVIE_TITLE_MODE.equals(this.mode))
            movieInterface.setMovieTile(movie);
                
    }

}
