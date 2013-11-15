/**
 * Assignment #: 5<br>
 * Filename: PhotoActivity.java<br>
 * Students in Group: Aneesh Garg
 */
package com.aneeshgarg.unccphotos;

import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aneeshgarg.unccphotos.model.Photo;
import com.aneeshgarg.unccphotos.utility.DiskLruImageCache;

public class PhotoActivity extends Activity {

    private static final String      LOG_TAG            = "AneeshGarg";

    private static final int         START              = 0x00;
    private static final int         NEXT               = 0x01;
    private static final int         PREVIOUS           = 0x02;

    private ArrayList<Photo>         photos;
    private int                      currentIndex;
    private ImageView                imageView;
    private Handler                  handler;
    private int                      mode;
    private TextView                 titleTextView, viewsTextView;

    private static boolean           activityDestroyed  = false;
    // Memory Cache
    private LruCache<String, Bitmap> mMemoryCache;

    private static DiskLruImageCache mDiskLruImageCache;
    private static boolean           mDiskCacheStarting = true;
    private final Object             mDiskCacheLock     = new Object();
    private static final int         DISK_CACHE_SIZE    = 1024 * 1024 * 10; // 10MB
    private static final String      DISK_CACHE_SUBDIR  = "unccphotos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        activityDestroyed = false;
        // Initializing Memory cache
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;
        Log.d(LOG_TAG, "Memory Cache Size: " + cacheSize);

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

        // Initialize disk cache on background thread
        new InitDiskCacheTask().execute();

        photos = ((ArrayList<Photo>) getIntent().getExtras().getSerializable(MainActivity.PHOTOS_LIST_TAG));
        imageView = (ImageView) findViewById(R.id.photo);
        titleTextView = (TextView) findViewById(R.id.photoTitle);
        viewsTextView = (TextView) findViewById(R.id.viewsTextView);

        mode = getIntent().getIntExtra(MainActivity.MODE_TAG, -1);

        switch (mode) {

            case MainActivity.PHOTO_MODE:
                new LoadImage(mode).execute(getImageObject(START));
                imageView.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // TODO Auto-generated method stub
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            float touchX = event.getX();
                            float width = v.getWidth();
                            if (touchX <= (width * 0.20F)) {
                                Log.d(LOG_TAG, "Loading Previous Pic");
                                new LoadImage(mode).execute(getImageObject(PREVIOUS));

                            } else if (touchX >= (width * 0.80F)) {
                                Log.d(LOG_TAG, "Loading Next Pic");
                                new LoadImage(mode).execute(getImageObject(NEXT));

                            }

                        }
                        return true;
                    }
                });
                break;

            case MainActivity.SLIDE_SHOW_MODE:
                new LoadImage(mode).execute(getImageObject(START));

                handler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        if (msg.what == LoadImage.LOAD_NEXT)
                            new LoadImage(mode).execute(getImageObject(NEXT));
                        return true;
                    }
                });
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photo, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityDestroyed = true;
    }

    public Photo getImageObject(int mode) {

        switch (mode) {
            case START:
                currentIndex = 0;
                break;

            case NEXT:
                currentIndex++;
                if (currentIndex >= photos.size())
                    currentIndex = 0;
                break;

            case PREVIOUS:
                currentIndex--;
                if (currentIndex < 0)
                    currentIndex = photos.size() - 1;

        }

        return photos.get(currentIndex);
    }

    public class LoadImage extends AsyncTask<Photo, Void, Bitmap> {
        public static final int LOAD_NEXT = 0x00;
        private ProgressDialog  dialog;
        private int             mode;
        private Photo           photo;

        public LoadImage(int mode) {
            this.mode = mode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mode == MainActivity.PHOTO_MODE) {
                dialog = new ProgressDialog(PhotoActivity.this);
                dialog.setMessage("Loading Image");
                dialog.setCancelable(false);
                dialog.show();
            }
        }

        @Override
        protected Bitmap doInBackground(Photo... params) {
            Bitmap image = null;
            this.photo = params[0];

            // Trying to get image from memory cache
            image = getBitmapFromMemCache(photo.getId());

            if (image != null) {
                Log.d(LOG_TAG, "Image found in memory cache.");
                return image;
            } else {
                // checking image on disk cache
                image = getBitmapFromDiskCache(photo.getId());
                if (image != null) {
                    Log.d(LOG_TAG, "Image found in disk cache.");
                    return image;
                }
                try {
                    Log.d(LOG_TAG, "Downloading Image");
                    URL url = new URL(photo.getUrl_m());
                    image = BitmapFactory.decodeStream(url.openStream());
                    addBitmapToMemoryCache(photo.getId(), image);
                    addBitmapToDiskCache(photo.getId(), image);
                    Log.d(LOG_TAG, "Size of downloaded image: " + (image.getByteCount() / 1024));

                } catch (Exception e) {
                    Log.d(LOG_TAG, "Error in downloading image." + e.getMessage());
                    return null;
                }
            }

            return image;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            if (result != null) {
                imageView.setImageBitmap(result);
                titleTextView.setText(photo.getTitle());
                viewsTextView.setText(String.valueOf(photo.getViews()));
            } else {
                Toast.makeText(PhotoActivity.this, "Error in downloading image.", Toast.LENGTH_LONG).show();
            }

            if (mode == MainActivity.SLIDE_SHOW_MODE && !activityDestroyed)
                handler.sendEmptyMessageDelayed(LOAD_NEXT, 2000);
            else if (mode == MainActivity.PHOTO_MODE)
                dialog.dismiss();
        }
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    class InitDiskCacheTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            synchronized (mDiskCacheLock) {
                mDiskLruImageCache = new DiskLruImageCache(PhotoActivity.this, DISK_CACHE_SUBDIR, DISK_CACHE_SIZE,
                        CompressFormat.JPEG, 100);
                mDiskCacheStarting = false;
                mDiskCacheLock.notifyAll();
            }
            return null;
        }

    }

    public void addBitmapToDiskCache(String key, Bitmap bitmap) {
        // Add to memory cache as before
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }

        // Also add to disk cache
        synchronized (mDiskCacheLock) {
            if (mDiskLruImageCache != null && mDiskLruImageCache.getBitmap(key) == null) {
                mDiskLruImageCache.put(key, bitmap);
            }
        }
    }

    public Bitmap getBitmapFromDiskCache(String key) {
        synchronized (mDiskCacheLock) {
            // Wait while disk cache is started from background thread
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLock.wait();
                } catch (InterruptedException e) {
                }
            }
            if (mDiskLruImageCache != null) {
                return mDiskLruImageCache.getBitmap(key);
            }
        }
        return null;
    }

}
