/**
 * Midterm <br>
 * Filename: RottenTomatoesJSONParser.java<br>
 * Students in Group: Aneesh Garg
 */
package com.thetechdork.rottentomatoes.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.thetechdork.rottentomatoes.activity.BaseActivity;
import com.thetechdork.rottentomatoes.model.Movie;

/**
 * 
 * @author Aneesh Garg
 * @since Oct 31, 2013
 */
public class RottenTomatoesJSONParser {

    private static final String YYYY_MM_DD      = "yyyy-MM-dd";

    private static final String ALTERNATE       = "alternate";
    private static final String LINKS           = "links";
    private static final String RUNTIME         = "runtime";
    private static final String GENRES          = "genres";
    private static final String DETAILED        = "detailed";
    private static final String THUMBNAIL       = "thumbnail";
    private static final String POSTERS         = "posters";
    private static final String AUDIENCE_SCORE  = "audience_score";
    private static final String AUDIENCE_RATING = "audience_rating";
    private static final String CRITICS_SCORE   = "critics_score";
    private static final String CRITICS_RATING  = "critics_rating";
    private static final String RATINGS         = "ratings";
    private static final String THEATER         = "theater";
    private static final String RELEASE_DATES   = "release_dates";
    private static final String MPAA_RATING     = "mpaa_rating";
    private static final String YEAR            = "year";
    private static final String TITLE           = "title";
    private static final String ID_TAG          = "id";
    private static final String MOVIES_TAG      = "movies";

    public static List<Movie> parseMoviesJSON(InputStream in) throws IOException, ParseException, JSONException {
        List<Movie> movies = new ArrayList<Movie>();
        String json = getJSONString(in);

        JSONObject responseObject = new JSONObject(json);
        JSONArray moviesList = responseObject.getJSONArray(MOVIES_TAG);

        for (int i = 0; i < moviesList.length(); i++) {
            Movie movie = new Movie();
            JSONObject movieObject = moviesList.getJSONObject(i);
            try {
                movie.setId(movieObject.getString(ID_TAG));
                movie.setTitle(movieObject.getString(TITLE));
                movie.setYear(Integer.valueOf(movieObject.getString(YEAR)));
                movie.setMpaaRating(movieObject.getString(MPAA_RATING));
                String runtime = movieObject.getString(RUNTIME);
                if (runtime.length() > 0)
                    movie.setRuntime(Integer.valueOf(runtime));

                JSONObject releaseObject = movieObject.getJSONObject(RELEASE_DATES);
                movie.setReleaseDate(new SimpleDateFormat(YYYY_MM_DD, Locale.US).parse(releaseObject.getString(THEATER)));

                JSONObject ratingsObject = movieObject.getJSONObject(RATINGS);
                try {
                    movie.setCriticsRating(ratingsObject.getString(CRITICS_RATING));
                } catch (JSONException e) {
                    Log.e(BaseActivity.LOG_KEY, e.getMessage());
                    movie.setCriticsRating(null);
                }
                movie.setCriticsScore(Integer.valueOf(ratingsObject.getString(CRITICS_SCORE)));
                try {
                    movie.setAudienceRating(ratingsObject.getString(AUDIENCE_RATING));
                } catch (JSONException e) {
                    Log.e(BaseActivity.LOG_KEY, e.getMessage());
                    movie.setAudienceRating(null);
                }
                movie.setAudienceScore(Integer.valueOf(ratingsObject.getString(AUDIENCE_SCORE)));

                JSONObject postersObject = movieObject.getJSONObject(POSTERS);
                movie.setThumbnailURL(postersObject.getString(THUMBNAIL));
                movie.setDetailedURL(postersObject.getString(DETAILED));

                JSONObject linksObject = movieObject.getJSONObject(LINKS);
                movie.setAlternateURL(linksObject.getString(ALTERNATE));

                movies.add(movie);

            } catch (JSONException e) {
                Log.e(BaseActivity.LOG_KEY, e.getMessage());
            }

        }
        Log.d(BaseActivity.LOG_KEY, movies.toString());
        return movies;
    }

    public static Movie parseMovieJSON(InputStream in) throws IOException, JSONException, ParseException {
        String json = getJSONString(in);

        JSONObject movieObject = new JSONObject(json);
        Movie movie = new Movie();

        movie.setId(movieObject.getString(ID_TAG));
        movie.setTitle(movieObject.getString(TITLE));
        movie.setYear(Integer.valueOf(movieObject.getString(YEAR)));
        movie.setMpaaRating(movieObject.getString(MPAA_RATING));
        String runtime = movieObject.getString(RUNTIME);
        if (runtime.length() > 0)
            movie.setRuntime(Integer.valueOf(runtime));

        JSONObject releaseObject = movieObject.getJSONObject(RELEASE_DATES);
        movie.setReleaseDate(new SimpleDateFormat(YYYY_MM_DD, Locale.US).parse(releaseObject.getString(THEATER)));

        JSONObject ratingsObject = movieObject.getJSONObject(RATINGS);
        try {
            movie.setCriticsRating(ratingsObject.getString(CRITICS_RATING));
        } catch (JSONException e) {
            Log.e(BaseActivity.LOG_KEY, e.getMessage());
            movie.setCriticsRating(null);
        }
        movie.setCriticsScore(Integer.valueOf(ratingsObject.getString(CRITICS_SCORE)));
        try {
            movie.setAudienceRating(ratingsObject.getString(AUDIENCE_RATING));
        } catch (JSONException e) {
            Log.e(BaseActivity.LOG_KEY, e.getMessage());
            movie.setAudienceRating(null);
        }
        movie.setAudienceScore(Integer.valueOf(ratingsObject.getString(AUDIENCE_SCORE)));

        JSONObject postersObject = movieObject.getJSONObject(POSTERS);
        movie.setThumbnailURL(postersObject.getString(THUMBNAIL));
        movie.setDetailedURL(postersObject.getString(DETAILED));

        List<String> genreList = new ArrayList<String>();
        JSONArray genreArray = movieObject.getJSONArray(GENRES);
        for (int i = 0; i < genreArray.length(); i++)
            genreList.add(genreArray.getString(i));

        JSONObject linksObject = movieObject.getJSONObject(LINKS);
        movie.setAlternateURL(linksObject.getString(ALTERNATE));

        // Log.d(BaseActivity.LOG_KEY, movie.toString());
        return movie;
    }

    public static List<String> parseGenreFromMovieJSON(InputStream in) throws IOException, JSONException {
        List<String> genreList = new ArrayList<String>();
        String json = getJSONString(in);

        JSONObject movieObject = new JSONObject(json);
        JSONArray genreArray = movieObject.getJSONArray(GENRES);
        for (int i = 0; i < genreArray.length(); i++)
            genreList.add(genreArray.getString(i));

        return genreList;
    }

    public static Movie parseMovieJSONForTitle(InputStream in) throws IOException, JSONException {
        String json = getJSONString(in);

        JSONObject movieObject = new JSONObject(json);
        Movie movie = new Movie();

        movie.setId(movieObject.getString(ID_TAG));
        movie.setTitle(movieObject.getString(TITLE));

        // Log.d(BaseActivity.LOG_KEY, movie.toString());
        return movie;
    }

    public static String getJSONString(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
            sb.append(line);
            line = reader.readLine();
        }
        return sb.toString().trim();
    }
}
