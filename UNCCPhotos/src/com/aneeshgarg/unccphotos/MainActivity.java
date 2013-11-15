/**
 * Assignment #: 5<br>
 * Filename: MainActivity.java<br>
 * Students in Group: Aneesh Garg
 */
package com.aneeshgarg.unccphotos;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.aneeshgarg.unccphotos.model.Photo;
import com.aneeshgarg.unccphotos.utility.WebServiceTask;

public class MainActivity extends Activity {

    public static final String JSON_MODE       = "JSON";
    public static final String XML_MODE        = "XML";

    public static final String PHOTOS_LIST_TAG = "Photos List";
    public static final String MODE_TAG        = "Mode";
    public static final int    PHOTO_MODE      = 0x00;
    public static final int    SLIDE_SHOW_MODE = 0x01;

    private int                mode;
    private Handler            handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == WebServiceTask.WEBSERVICE_MESSAGE) {
                    ArrayList<Photo> photosList = (ArrayList<Photo>) msg.obj;
                    Collections.sort(photosList, Collections.reverseOrder());
                    Intent intent = new Intent(MainActivity.this, PhotoActivity.class);
                    intent.putExtra(PHOTOS_LIST_TAG, photosList);
                    intent.putExtra(MODE_TAG, mode);
                    startActivity(intent);
                    //Log.d("AneeshGarg", photosList.toString());
                }
                return true;
            }
        });

        ((Button) findViewById(R.id.photosButton)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mode = PHOTO_MODE;
                new WebServiceTask(MainActivity.this, handler).execute(getWebServiceType());

            }
        });

        ((Button) findViewById(R.id.slideButton)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mode = SLIDE_SHOW_MODE;
                new WebServiceTask(MainActivity.this, handler).execute(getWebServiceType());

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private String getWebServiceType() {
        RadioGroup group = (RadioGroup) findViewById(R.id.responseTypeRadioGroup);

        switch (group.getCheckedRadioButtonId()) {
            case R.id.jsonRadioButton:
                return JSON_MODE;
            case R.id.xmlRadioButton:
                return XML_MODE;
        }
        return null;
    }

}
