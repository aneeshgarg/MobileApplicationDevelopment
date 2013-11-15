/**
 * Assignment #: 1<br>
 * Filename: MainActivity.java<br>
 * Students in Group: Aneesh Garg
 */

package com.aneeshgarg.inclass1;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    LinearLayout root;
    TextView     countTextView;
    int          clickCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root = (LinearLayout) findViewById(R.id.rootLayout);
        countTextView = (TextView) findViewById(R.id.countTextView);

        ((Button) findViewById(R.id.blackButton)).setOnClickListener(new Clicker(Color.BLACK));
        ((Button) findViewById(R.id.whiteButton)).setOnClickListener(new Clicker(Color.WHITE));
        ((Button) findViewById(R.id.redButton)).setOnClickListener(new Clicker(Color.RED));
        ((Button) findViewById(R.id.greenButton)).setOnClickListener(new Clicker(Color.GREEN));
        ((Button) findViewById(R.id.blueButton)).setOnClickListener(new Clicker(Color.BLUE));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public class Clicker implements View.OnClickListener {

        private int color;

        /*
         * (non-Javadoc)
         * 
         * @see android.view.View.OnClickListener#onClick(android.view.View)
         */
        public Clicker(int color) {
            this.color = color;
        }

        @Override
        public void onClick(View v) {
            root.setBackgroundColor(this.color);
            countTextView.setText((++clickCount) + "");
        }

    }

}
