package com.thetechdork.rottentomatoes.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MovieWebActivity extends BaseActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_web);
        
        if (getIntent().getExtras() != null) {
            String url = getIntent().getExtras().getString(MovieActivity.MOVIE_ALTERNATE_LINK);
            if (url.length() > 0) {
            WebView view= (WebView) findViewById(R.id.movie_web_view);
            view.getSettings().setJavaScriptEnabled(true);
            view.setWebViewClient(new WebViewClient(){
                public boolean shouldOverrideUrlLoading (WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            view.loadUrl(url);
            }
            else {
                Toast.makeText(this, "Error in loading movie web page.", Toast.LENGTH_LONG).show();
                Log.e(BaseActivity.LOG_KEY, "Error in Alternate URL::: "+url);
            }
        }
        else {
            Toast.makeText(this, "Error in loading movie web page.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
