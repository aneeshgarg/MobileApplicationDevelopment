package com.aneeshgarg.activity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    public static final String MODE_TAG = "Mode";
    public static final int PHOTO_MODE = 0x00;
    public static final int SLIDE_SHOW_MODE = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ((Button)findViewById(R.id.photosButton)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PhotosActivity.class);
                intent.putExtra(MODE_TAG, PHOTO_MODE);
                startActivity(intent);                
            }
        });
        
        ((Button)findViewById(R.id.slideButton)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PhotosActivity.class);
                intent.putExtra(MODE_TAG, SLIDE_SHOW_MODE);
                startActivity(intent);                
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
