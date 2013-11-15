package com.thetechdork.rottentomatoes.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thetechdork.rottentomatoes.model.FavoritesAPIResponse;
import com.thetechdork.rottentomatoes.model.Movie;
import com.thetechdork.rottentomatoes.tasks.CallFavoriteAPITask;
import com.thetechdork.rottentomatoes.tasks.GetMovieInfoTask;
import com.thetechdork.rottentomatoes.tasks.LoadImageTask;
import com.thetechdork.rottentomatoes.utility.Utility;

public class MovieActivity extends BaseActivity {

    public static final String GENRE_MODE           = "Get Genre";
    public static final String MOVIE_TITLE_MODE     = "Get Title";
    public static final String MOVIE_INFO_MODE      = "Get Movie Info";
    public static final String MOVIE_ALTERNATE_LINK = "Movie Alternate Link";
    private Movie              movie;
    private ImageView          favImageView;
    private boolean            isSourceFavorites    = false;
    private boolean            removedFromFavorites = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        if (getIntent().getExtras() != null) {
            movie = (Movie) getIntent().getExtras().getSerializable(MoviesActivity.MOVIE_OBJECT);
            isSourceFavorites = getIntent().getExtras().getBoolean(MoviesActivity.START_FROM_FAVOURITE, false);
        }

        if (movie == null)
            finish();
        else {
            ((TextView) findViewById(R.id.movie_activity_title)).setText(movie.getTitle());
            ((TextView) findViewById(R.id.movie_release_date)).setText(Utility.getMovieReleaseDateString(movie.getReleaseDate()));
            ((TextView) findViewById(R.id.movie_mpaa_rating)).setText(movie.getMpaaRating());
            ((TextView) findViewById(R.id.movie_duration)).setText(Utility.getMovieDurationString(movie.getRuntime()));
            ((TextView) findViewById(R.id.movie_critics_rating)).setText(movie.getCriticsScore() != null ? movie.getCriticsScore().toString() + "%" : "");
            ((TextView) findViewById(R.id.movie_audience_rating)).setText(movie.getAudienceScore() != null ? movie.getAudienceScore().toString() + "%" : "");

            ((ImageView) findViewById(R.id.movie_critics_rating_image)).setImageResource(Utility.getRatingImageResource(movie.getCriticsRating()));
            ((ImageView) findViewById(R.id.movie_audience_rating_image)).setImageResource(Utility.getRatingImageResource(movie.getAudienceRating()));

            String posterUrl = movie.getDetailedURL();
            if (posterUrl.length() > 0)
                new LoadImageTask(this).execute(movie.getId(), posterUrl);

            ((ImageView) findViewById(R.id.movie_back_image)).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (isSourceFavorites) {
                        Intent returnIntent = new Intent();
                        if (removedFromFavorites) {
                            returnIntent.putExtra("RemovedMovie", movie);
                            setResult(RESULT_OK,returnIntent);     
                            finish();
                        }
                        else {
                            setResult(RESULT_CANCELED, returnIntent);        
                            finish();                
                        }
                    } else
                        finish();
                }
            });

            ((ImageView) findViewById(R.id.movie_web_image)).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MovieActivity.this, MovieWebActivity.class);
                    intent.putExtra(MOVIE_ALTERNATE_LINK, movie.getAlternateURL());
                    startActivity(intent);

                }
            });

            if (movie.getGenre() != null)
                ((TextView) findViewById(R.id.movie_genre)).setText(movie.getGenre());
            else
                new GetMovieInfoTask(this, movie).execute(GENRE_MODE, null);
            favImageView = ((ImageView) findViewById(R.id.movie_favorite_image));
            new CallFavoriteAPITask(this).execute(IS_FAVORITE_MODE, getSharedPreferences(SHARED_PREF_NAME, 0).getString(USERNAME_KEY, ""), movie.getId());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public void setImage(String id, Bitmap image) {
        if (image != null) {
            ((ImageView) findViewById(R.id.movie_poster_image)).setImageBitmap(image);
        }

    }

    @Override
    public void updateGenreToMovie(Movie movie) {
        if (movie.getGenre() != null)
            ((TextView) findViewById(R.id.movie_genre)).setText(movie.getGenre());
        else
            Log.d(LOG_KEY, "Genre is null");

    }

    @Override
    public void addedToFavorite(FavoritesAPIResponse response) {
        if (response != null) {
            int errorId = response.getError().getId();
            if (errorId == 0) {
                Toast.makeText(this, response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                showFavoriteIcon();
                if (isSourceFavorites)
                    removedFromFavorites = false;
            } else {
                showNonFavoriteIcon();
                Toast.makeText(this, response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void deletedFromFavorite(FavoritesAPIResponse response) {
        if (response != null) {
            int errorId = response.getError().getId();
            if (errorId == 0) {
                Toast.makeText(this, response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                showNonFavoriteIcon();
                if (isSourceFavorites)
                    removedFromFavorites = true;
            } else {
                showFavoriteIcon();
                Toast.makeText(this, response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void isFavorite(FavoritesAPIResponse response) {
        if (response != null) {
            int errorId = response.getError().getId();
            if (errorId == 0)
                if (response.getFavorites().get(0).isFavourite())
                    showFavoriteIcon();
                else
                    showNonFavoriteIcon();
            else
                Toast.makeText(this, response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void showFavoriteIcon() {
        favImageView.setImageResource(R.drawable.rating_important);
        favImageView.setOnClickListener(null);
        favImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CallFavoriteAPITask(MovieActivity.this).execute(DELETE_FAVORITE_MODE, getSharedPreferences(SHARED_PREF_NAME, 0).getString(USERNAME_KEY, ""), movie.getId());
            }
        });
    }

    private void showNonFavoriteIcon() {
        favImageView.setImageResource(R.drawable.rating_not_important);
        favImageView.setOnClickListener(null);
        favImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CallFavoriteAPITask(MovieActivity.this).execute(ADD_FAVORITE_MODE, getSharedPreferences(SHARED_PREF_NAME, 0).getString(USERNAME_KEY, ""), movie.getId());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isSourceFavorites) {
            Intent returnIntent = new Intent();
            if (removedFromFavorites) {
                returnIntent.putExtra("RemovedMovie", movie);
                setResult(RESULT_OK,returnIntent);     
                finish();
            }
            else {
                setResult(RESULT_CANCELED, returnIntent);        
                finish();                
            }
        } else
            super.onBackPressed();
    }
}
