/**
 * Midterm <br>
 * Filename: FavoriteInterface.java<br>
 * Students in Group: Aneesh Garg
 */
package com.thetechdork.rottentomatoes.interfaces;

import com.thetechdork.rottentomatoes.model.FavoritesAPIResponse;

/**
 *
 * @author Aneesh Garg
 * @since Nov 3, 2013
 */
public interface FavoriteInterface {
    public static final String ADD_FAVORITE_MODE = "Add Id To Favorite";
    public static final String GET_ALL_FAVORITES_MODE = "Get All Favorite Movies";
    public static final String DELETE_FAVORITE_MODE = "Delete Id From Favorites";
    public static final String DELETE_ALL_FAVORITES_MODE = "Delete All Movies from Favorites";
    public static final String IS_FAVORITE_MODE = "Is Id in Favorites";
    public static final String GET_FAVORITE_STATS_MODE = "Get Statistics of Favorite Movies";

    public void addedToFavorite(FavoritesAPIResponse response);
    public void setAllFavorites(FavoritesAPIResponse response);
    public void deletedFromFavorite(FavoritesAPIResponse response);
    public void deletedAllFavorites(FavoritesAPIResponse response);
    public void isFavorite(FavoritesAPIResponse response);
    public void setFavoriteStats(FavoritesAPIResponse response);

}
