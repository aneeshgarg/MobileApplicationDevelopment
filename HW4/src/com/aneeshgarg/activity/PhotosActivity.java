package com.aneeshgarg.activity;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.aneeshgarg.util.DiskLruCache;
import com.aneeshgarg.util.DiskLruImageCache;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class PhotosActivity extends Activity {

    private static final String      LOG_TAG            = "AneeshGarg";

    private static final int         START              = 0x00;
    private static final int         NEXT               = 0x01;
    private static final int         PREVIOUS           = 0x02;

    private String[]                 photoURLs;
    private int                      currentIndex;
    private ImageView                imageView;
    private Handler                  handler;
    private int                      mode;

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
        setContentView(R.layout.activity_photos);

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

        photoURLs = getResources().getStringArray(R.array.photo_urls);
        imageView = (ImageView) findViewById(R.id.photo);

        mode = getIntent().getIntExtra(MainActivity.MODE_TAG, -1);

        switch (mode) {

            case MainActivity.PHOTO_MODE:
                new LoadImage(mode).execute(getImageUrl(START));
                imageView.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // TODO Auto-generated method stub
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            float touchX = event.getX();
                            float width = v.getWidth();
                            if (touchX <= (width * 0.20F)) {
                                Log.d(LOG_TAG, "Loading Previous Pic");
                                new LoadImage(mode).execute(getImageUrl(PREVIOUS));

                            } else if (touchX >= (width * 0.80F)) {
                                Log.d(LOG_TAG, "Loading Next Pic");
                                new LoadImage(mode).execute(getImageUrl(NEXT));

                            }

                        }
                        return true;
                    }
                });
                break;

            case MainActivity.SLIDE_SHOW_MODE:
                new LoadImage(mode).execute(getImageUrl(START));

                handler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        if (msg.what == LoadImage.LOAD_NEXT)
                            new LoadImage(mode).execute(getImageUrl(NEXT));
                        return true;
                    }
                });
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photos, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityDestroyed = true;
    }

    public String getImageUrl(int mode) {

        switch (mode) {
            case START:
                currentIndex = 0;
                break;

            case NEXT:
                currentIndex++;
                if (currentIndex >= photoURLs.length)
                    currentIndex = 0;
                break;

            case PREVIOUS:
                currentIndex--;
                if (currentIndex < 0)
                    currentIndex = photoURLs.length - 1;

        }

        return photoURLs[currentIndex];
    }

    public class LoadImage extends AsyncTask<String, Void, Bitmap> {
        public static final int LOAD_NEXT = 0x00;
        private ProgressDialog  dialog;
        private int             mode;

        public LoadImage(int mode) {
            this.mode = mode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mode == MainActivity.PHOTO_MODE) {
                dialog = new ProgressDialog(PhotosActivity.this);
                dialog.setMessage("Loading Image");
                dialog.setCancelable(false);
                dialog.show();
            }
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap image = null;
            String urlString = params[0];

            // Trying to get image from memory cache
            image = getBitmapFromMemCache(urlString);

            if (image != null) {
                Log.d(LOG_TAG, "Image found in memory cache.");
                return image;
            } else {
                // checking image on disk cache
                image = getBitmapFromDiskCache(urlString);
                if (image != null) {
                    Log.d(LOG_TAG, "Image found in disk cache.");
                    return image;
                }
                try {
                    Log.d(LOG_TAG, "Downloading Image");
                    URL url = new URL(urlString);
                    image = BitmapFactory.decodeStream(url.openStream());
                    addBitmapToMemoryCache(urlString, image);
                    addBitmapToDiskCache(urlString, image);
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
            } else {
                Toast.makeText(PhotosActivity.this, "Error in downloading image.", Toast.LENGTH_LONG).show();
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
                mDiskLruImageCache = new DiskLruImageCache(PhotosActivity.this, DISK_CACHE_SUBDIR, DISK_CACHE_SIZE,
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
