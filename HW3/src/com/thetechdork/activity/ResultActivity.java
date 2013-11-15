/**
 * Assignment #: 3<br>
 * Filename: ResultActivity.java<br>
 * Students in Group: Aneesh Garg
 */
package com.thetechdork.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        long time = getIntent().getLongExtra(MainActivity.TIME_KEY, -1L);
        float timeInSec = (float) Math.ceil(((time / 1000.0F) * 10)) / 10;
        
        String timeString = String.format(getResources().getString(R.string.timeStringFormat), timeInSec);
        ((TextView) findViewById(R.id.timeTextView)).setText(timeString);

        if (timeInSec > 20) {
            ((TextView) findViewById(R.id.greetingTextView)).setText(getResources().getString(R.string.looseGreeting));            
            ((ImageView)findViewById(R.id.resultImageView)).setImageResource(R.drawable.loose);                       
        }
        else {
            ((TextView) findViewById(R.id.greetingTextView)).setText(getResources().getString(R.string.winGreeting));            
            ((ImageView)findViewById(R.id.resultImageView)).setImageResource(R.drawable.win);            
        }
        
        ((Button)findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.result, menu);
        return true;
    }

}
