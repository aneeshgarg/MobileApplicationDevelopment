package com.thetechdork.rottentomatoes.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.thetechdork.rottentomatoes.adapter.MovieListAdapter;
import com.thetechdork.rottentomatoes.model.Favorite;
import com.thetechdork.rottentomatoes.model.FavoritesAPIResponse;
import com.thetechdork.rottentomatoes.model.Movie;
import com.thetechdork.rottentomatoes.tasks.CallFavoriteAPITask;
import com.thetechdork.rottentomatoes.tasks.GetMovieInfoTask;
import com.thetechdork.rottentomatoes.tasks.GetMovieListTask;

public class MoviesActivity extends BaseActivity {
    public static final String START_FROM_FAVOURITE = "Start from Favourite";
    public static final String MOVIE_OBJECT = "Movie_Object"; 
    private List<Movie> movieList;
    private ProgressDialog dialog;
    private ListView movieListView;
    private String mode;
    private MovieListAdapter adapter;
    private int removedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            mode = intent.getStringExtra(MODE_KEY);  
            dialog = new ProgressDialog(this);
            dialog.setMessage("Loading Movies");
            dialog.setCancelable(false);
            dialog.show();
            if (MY_FAVORITE_MOVIES.equals(mode))
                new CallFavoriteAPITask(this).execute(GET_ALL_FAVORITES_MODE, getSharedPreferences(SHARED_PREF_NAME, 0).getString(USERNAME_KEY, ""));
            else
                new GetMovieListTask(this).execute(mode);            
        }
        
        movieListView = (ListView)findViewById(R.id.movies_list_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
        
        adapter = new MovieListAdapter(this, this.movieList);
        movieListView.setAdapter(adapter);
        movieListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MoviesActivity.this, MovieActivity.class);
                intent.putExtra(MOVIE_OBJECT, MoviesActivity.this.movieList.get(position));
                startActivity(intent);
                
            }
        });
        
        
        dialog.dismiss();
    }

    @Override
    public void setAllFavorites(FavoritesAPIResponse response) {
        if (response != null) {
            if (response.getError().getId() == 0) {
                List<Favorite> favorites = response.getFavorites();
                for (Favorite fav : favorites)
                    new GetMovieInfoTask(this, null).execute(MovieActivity.MOVIE_INFO_MODE, fav.getId());
                if (favorites.size() <= 0) {
                    Toast.makeText(this, "No Movie is marked as favorite.", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    finish();
                }
            }
            else
                Toast.makeText(this, response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
        }
        
    }

    @Override
    public void addMovieToList(Movie movie) {
        if (adapter != null)
            adapter.add(movie);
        else {
            this.movieList = new ArrayList<Movie>();
            this.movieList.add(movie);
            
            adapter = new MovieListAdapter(this, this.movieList);
            movieListView.setAdapter(adapter);
            movieListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MoviesActivity.this, MovieActivity.class);
                    intent.putExtra(MOVIE_OBJECT, MoviesActivity.this.movieList.get(position));
                    intent.putExtra(START_FROM_FAVOURITE, true);
                    startActivityForResult(intent, 1);
                    
                }
            });
            
            movieListView.setOnItemLongClickListener(new OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    new CallFavoriteAPITask(MoviesActivity.this).execute(DELETE_FAVORITE_MODE, getSharedPreferences(SHARED_PREF_NAME, 0).getString(USERNAME_KEY, ""), movieList.get(position).getId());
                    removedPosition = position;
                    return true;
                }
            });
        }        
        
        dialog.dismiss();
    }

    @Override
    public void deletedFromFavorite(FavoritesAPIResponse response) {
        if (response != null) {
            int errorId = response.getError().getId();
            if (errorId == 0) {
                Toast.makeText(this, response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                if (removedPosition != -1)
                    adapter.remove(movieList.get(removedPosition));
            } else {
                Toast.makeText(this, response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
            }
        }
        removedPosition = -1;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

           if(resultCode == RESULT_OK){      
               Movie movie=(Movie) data.getSerializableExtra("RemovedMovie");
               if (movie != null)
                   adapter.remove(movie);
           }
        }
      }
    

}
