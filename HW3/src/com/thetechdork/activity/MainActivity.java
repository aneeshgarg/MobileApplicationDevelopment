/**
 * Assignment #: 3<br>
 * Filename: MainActivity.java<br>
 * Students in Group: Aneesh Garg
 */
package com.thetechdork.activity;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class MainActivity extends Activity {
    public static final String    TECH_DORK_TAG         = "TechDork";
    public static final String    TIME_KEY              = "time";

    /**
     * Array which stores all 4 image resource id that are needed to find.
     */
    private static final int[]    imageResources        = { R.drawable.icon1, R.drawable.icon2, R.drawable.icon3,
            R.drawable.icon4                           };
    /**
     * Map which stores no of active instances as value and image resource id as
     * key. Used to get no if active icons which can be clicked.
     */
    private static SparseIntArray imageCountMap;
    private static long           startTime;
    private static Set<Integer>   availableImagesToFind = new HashSet<Integer>(4);
    private ImageView             focusImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        focusImageView = (ImageView) findViewById(R.id.focusImageView);

        ((Button) findViewById(R.id.resetButton)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resetGame();
            }
        });

        ((Button) findViewById(R.id.exitButton)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        resetGame();
    }

    /**
     * This method will initialize/reset the complete game.
     */
    private void resetGame() {

        imageCountMap = new SparseIntArray(4);
        // Resetting game table
        resetGameTable();

        setRandomlyAvailableImageToFocus();
        startTime = System.currentTimeMillis();
    }

    /**
     * 
     */
    private void setRandomlyAvailableImageToFocus() {
        boolean imageFound = false;
        int size = availableImagesToFind.size();
        do {
            if (size > 0) {
                int resource = (Integer) availableImagesToFind.toArray()[getRandomIndex(size - 1)];
                int count = imageCountMap.get(resource);
                if (count > 0) {
                    focusImageView.setImageResource(resource);
                    focusImageView.setTag(resource);
                    imageFound = true;
                } else {
                    if (availableImagesToFind.contains(resource))
                        availableImagesToFind.remove(resource);
                    size = availableImagesToFind.size();
                }
            } else {
                focusImageView.setImageResource(R.drawable.empty);
                focusImageView.setTag(null);
                imageFound = true;
                goToResultActivity();
            }
        } while (!imageFound);
    }

    /**
     * 
     */
    private void goToResultActivity() {
        long timeTaken = System.currentTimeMillis() - startTime;
        Log.d(TECH_DORK_TAG, "Game Complete");
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(TIME_KEY, timeTaken);
        startActivity(intent);
    }

    /**
     * This method will reset game table
     */
    private void resetGameTable() {
        TableLayout table = (TableLayout) findViewById(R.id.gameTable);
        for (int i = 0; i < table.getChildCount(); i++) {
            View rowView = table.getChildAt(i);
            if (rowView instanceof TableRow) {
                TableRow row = (TableRow) rowView;
                for (int j = 0; j < row.getChildCount(); j++) {
                    View view = row.getChildAt(j);
                    if (view instanceof View) {
                        ImageView imageView = (ImageView) view;
                        int resource = getAvailableRandomImageResource();
                        imageView.setImageResource(resource);
                        imageView.setTag(resource);
                        imageView.setAlpha(1.0F);
                        imageView.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                int clickedTag = (Integer) v.getTag();
                                int focusTag = (Integer) focusImageView.getTag();
                                if (clickedTag == focusTag) {
                                    v.setAlpha(0.3F);
                                    int count = imageCountMap.get(clickedTag);
                                    imageCountMap.put(clickedTag, --count);
                                    if (count <= 0 && availableImagesToFind.contains(clickedTag)) {
                                        availableImagesToFind.remove(clickedTag);
                                        setRandomlyAvailableImageToFocus();
                                    }
                                    v.setClickable(false);
                                    Log.d(TECH_DORK_TAG, "Image Found");
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    /**
     * @return Random Image Resource from imageResources array which is
     *         available to add
     */
    private int getAvailableRandomImageResource() {
        boolean found = false;
        int resource = imageResources[getRandomIndex(3)];
        do {
            if (imageCountMap.indexOfKey(resource) < 0) {
                imageCountMap.put(resource, 1);
                availableImagesToFind.add(resource);
                found = true;
            } else {
                int count = imageCountMap.get(resource);
                if (count < 4) {
                    imageCountMap.put(resource, ++count);
                    found = true;
                } else {
                    resource = imageResources[getRandomIndex(3)];
                    found = false;
                }
            }
        } while (!found);
        return resource;
    }

    /**
     * @return random index from 0-3 to pick and image from imageResources array
     */
    private int getRandomIndex(int maxValue) {
        int index = (int) ((Math.random() * 10) % (maxValue + 1));
        Log.d(TECH_DORK_TAG, index + "");
        return index;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
