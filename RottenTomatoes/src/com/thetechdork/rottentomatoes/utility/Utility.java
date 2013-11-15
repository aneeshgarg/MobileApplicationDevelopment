/**
 * Midterm <br>
 * Filename: Utility.java<br>
 * Students in Group: Aneesh Garg
 */
package com.thetechdork.rottentomatoes.utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.thetechdork.rottentomatoes.activity.R;

/**
 * 
 * @author Aneesh Garg
 * @since Nov 2, 2013
 */
public class Utility {

    public static final String  API_KEY         = "52ynatppgz2t7v6hk6m7s6k5";
    public static final String  RT_BASE_URL     = "http://api.rottentomatoes.com/api/public/v1.0";
    public static final String FAVORITE_BASE_URL = "http://cci-webdev.uncc.edu/~mshehab/api-rest/favorites";

    private static final String UPRIGHT         = "Upright";
    private static final String SPILLED         = "Spilled";
    private static final String ROTTEN          = "Rotten";
    private static final String FRESH           = "Fresh";
    private static final String CERTIFIED_FRESH = "Certified Fresh";

    public static int getRatingImageResource(String type) {

        if (CERTIFIED_FRESH.equals(type))
            return R.drawable.certified_fresh;
        else if (FRESH.equals(type))
            return R.drawable.fresh;
        else if (ROTTEN.equals(type))
            return R.drawable.rotten;
        else if (SPILLED.equals(type))
            return R.drawable.spilled;
        else if (UPRIGHT.equals(type))
            return R.drawable.upright;
        else
            return R.drawable.notranked;
    }

    public static String getMovieReleaseDateString(Date releaseDate) {

        return new SimpleDateFormat("MM/dd/yy", Locale.US).format(releaseDate);
    }

    public static String getMovieDurationString(Integer runtime) {
        if (runtime != null) {
            int hours = runtime / 60;
            int minutes = runtime % 60;
            return hours + " hr " + minutes + " min";
        } else
            return "";
    }

}
