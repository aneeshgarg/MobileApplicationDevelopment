/**
 * Midterm <br>
 * Filename: FavoritesAPIResponse.java<br>
 * Students in Group: Aneesh Garg
 */
package com.thetechdork.rottentomatoes.model;

import java.util.List;

/**
 * 
 * @author Aneesh Garg
 * @since Oct 31, 2013
 */
public class FavoritesAPIResponse {
    private FavError          error;
    private List<Favorite> favorites;

    public FavError getError() {
        return error;
    }

    public void setError(FavError error) {
        this.error = error;
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    @Override
    public String toString() {
        return "FavoritesAPIResponse [error=" + error + ", favorites=" + favorites + "]";
    }
}
