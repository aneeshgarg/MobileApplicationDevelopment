/**
 * Assignment #: 4<br>
 * Filename: MainActivity.java<br>
 * Students in Group: Aneesh Garg
 */
package com.aneeshgarg.inclass4.activity;

import com.aneeshgarg.inclass4.utility.AsyncApplicationsGet;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {
    
    private static final String rssURL = "https://itunes.apple.com/us/rss/toppaidapplications/limit=50/xml";
   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        new AsyncApplicationsGet(this).execute(rssURL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
