package com.thetechdork.rottentomatoes.activity;

import java.util.List;

import com.thetechdork.rottentomatoes.interfaces.FavoriteInterface;
import com.thetechdork.rottentomatoes.interfaces.ImageInterface;
import com.thetechdork.rottentomatoes.interfaces.MovieInterface;
import com.thetechdork.rottentomatoes.interfaces.ProgressDialogInterface;
import com.thetechdork.rottentomatoes.model.FavoritesAPIResponse;
import com.thetechdork.rottentomatoes.model.Movie;
import com.thetechdork.rottentomatoes.tasks.CallFavoriteAPITask;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BaseActivity extends Activity implements FavoriteInterface,ImageInterface,MovieInterface,ProgressDialogInterface {

    public static final String SHARED_PREF_NAME = "RottenTomatoesPref";
    public static final String USERNAME_KEY     = "username";

    public static final String LOG_KEY          = "TheTechDork";
    
    public static final String MODE_KEY = "Mode";
    public static final String FAVORITE_STATISTICS = "Favorite Statistics";
    public static final String UPCOMING_MOVIES = "Upcoming Movies";
    public static final String OPENING_MOVIES = "Opening Movies";
    public static final String IN_THEATERS_MOVIES = "In Theaters Movies";
    public static final String BOX_OFFICE_MOVIES = "Box Office Movies";
    public static final String MY_FAVORITE_MOVIES = "My Favorite Movies";
    
    private ProgressDialog dialog;

    public static enum MoviesType {
        FAVORITE, BOX_OFFICE, IN_THEATERS, OPENING, UPCOMING
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.clear_favorites:
                dialog = new ProgressDialog(this);
                dialog.setCancelable(false);
                dialog.setMessage("Clearing all Favorites");
                dialog.show();
                new CallFavoriteAPITask(this).execute(DELETE_ALL_FAVORITES_MODE, getSharedPreferences(SHARED_PREF_NAME, 0).getString(USERNAME_KEY, ""));
                break;

            case R.id.setup_username:
                Intent intent = new Intent(this, UsernameActivity.class);
                startActivity(intent);
                break;

            case R.id.exit:
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void startProgressDialog(String message, String title) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void dismissProgressDialog() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setMovieList(List<Movie> movieList) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addMovieToList(Movie movie) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateGenreToMovie(Movie movie) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setImage(String id, Bitmap image) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addedToFavorite(FavoritesAPIResponse response) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setAllFavorites(FavoritesAPIResponse response) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deletedFromFavorite(FavoritesAPIResponse response) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deletedAllFavorites(FavoritesAPIResponse response) {
        if (response != null)
            Toast.makeText(this, response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Cannot clear all favorites.", Toast.LENGTH_LONG).show();
        dialog.dismiss();
        
    }

    @Override
    public void isFavorite(FavoritesAPIResponse response) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setFavoriteStats(FavoritesAPIResponse response) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setMovieTile(Movie movie) {
        // TODO Auto-generated method stub
        
    }

}
