/**
 * Midterm <br>
 * Filename: MovieListAdapter.java<br>
 * Students in Group: Aneesh Garg
 */
package com.thetechdork.rottentomatoes.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.thetechdork.rottentomatoes.activity.R;
import com.thetechdork.rottentomatoes.model.Movie;
import com.thetechdork.rottentomatoes.tasks.LoadImageTask;
import com.thetechdork.rottentomatoes.utility.Utility;

/**
 *
 * @author Aneesh Garg
 * @since Nov 2, 2013
 */
public class MovieListAdapter extends ArrayAdapter<Movie>{
    private Context context;
    private List<Movie> movies;

    public MovieListAdapter(Context context, List<Movie> movies) {
        super(context, R.layout.movies_row_layout, movies);
        this.context = context;
        this.movies = movies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.movies_row_layout,parent,false);
        }
        
        Movie movie = movies.get(position);
        //Log.d(BaseActivity.LOG_KEY, movie.toString());
        ((ImageView)convertView.findViewById(R.id.row_movie_thumbnail)).setImageResource(R.drawable.poster_not_found);        
        convertView.setTag(movie);
        new LoadImageTask(convertView, LoadImageTask.SYNC_IMAGE).execute(movie.getId(),movie.getThumbnailURL());
        ((TextView)convertView.findViewById(R.id.row_movie_title)).setText(movie.getTitle());
        ((TextView)convertView.findViewById(R.id.row_movie_year)).setText(movie.getYear().toString());
        ((TextView)convertView.findViewById(R.id.row_movie_mpaa)).setText(movie.getMpaaRating());

        ((ImageView)convertView.findViewById(R.id.row_movie_critic_image)).setImageResource(Utility.getRatingImageResource(movie.getCriticsRating()));
        ((ImageView)convertView.findViewById(R.id.row_movie_audience_image)).setImageResource(Utility.getRatingImageResource(movie.getAudienceRating()));
        
        return convertView;
    }

}
