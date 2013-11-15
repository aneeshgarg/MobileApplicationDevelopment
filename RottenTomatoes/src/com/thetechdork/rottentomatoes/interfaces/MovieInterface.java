package com.thetechdork.rottentomatoes.interfaces;

import java.util.List;

import com.thetechdork.rottentomatoes.model.Movie;

public interface MovieInterface {
    
    public void setMovieList(List<Movie> movieList);
    public void addMovieToList(Movie movie);
    public void updateGenreToMovie(Movie movie);
    public void setMovieTile(Movie movie);

}
