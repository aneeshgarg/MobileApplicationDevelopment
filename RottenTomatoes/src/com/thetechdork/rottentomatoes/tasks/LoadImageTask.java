/**
 * Midterm <br>
 * Filename: LoadImageTask.java<br>
 * Students in Group: Aneesh Garg
 */
package com.thetechdork.rottentomatoes.tasks;

import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.thetechdork.rottentomatoes.activity.BaseActivity;
import com.thetechdork.rottentomatoes.activity.R;
import com.thetechdork.rottentomatoes.interfaces.ImageInterface;
import com.thetechdork.rottentomatoes.model.Movie;

/**
 * 
 * @author Aneesh Garg
 * @since Nov 3, 2013
 */
public class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
    public static final String SYNC_IMAGE = "SyncImage";
    private String             id;
    private ImageInterface     imageInterface;
    private View               convertView;
    private String             mode;

    public LoadImageTask(View convertView, String mode) {
        this.convertView = convertView;
        this.mode = mode;
    }

    public LoadImageTask(ImageInterface imgInterface) {
        this.imageInterface = imgInterface;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap image = null;
        this.id = params[0];

        String imageUrl = params[1];
        try {
            Log.d(BaseActivity.LOG_KEY, "Downloading Image");
            URL url = new URL(imageUrl);
            image = BitmapFactory.decodeStream(url.openStream());
        } catch (Exception e) {
            Log.d(BaseActivity.LOG_KEY, "Error in downloading image::: " + e.getMessage());
            return null;
        }

        return image;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        Log.d(BaseActivity.LOG_KEY, "Setting Image");
        if (mode == null)
            imageInterface.setImage(id, result);
        else if (mode.equals(SYNC_IMAGE)) {
            Movie movie = (Movie) convertView.getTag();
            if (movie.getId().equals(id)) {
                ImageView img = (ImageView) convertView.findViewById(R.id.row_movie_thumbnail);
                img.setImageBitmap(result);
            }
        }
    }
}
