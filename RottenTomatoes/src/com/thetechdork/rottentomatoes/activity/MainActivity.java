package com.thetechdork.rottentomatoes.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView mainMenuListView = (ListView) findViewById(R.id.main_menu_listview);
        String[] values = getResources().getStringArray(R.array.main_menu_items);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, values);

        mainMenuListView.setAdapter(adapter);
        mainMenuListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String itemLabel = (String) ((TextView) view).getText();

                if (FAVORITE_STATISTICS.equals(itemLabel)) {
                    startActivity(new Intent(MainActivity.this,StatisticsActivity.class));
                    return;
                }

                Intent moviesIntent = new Intent(MainActivity.this, MoviesActivity.class);

                if (MY_FAVORITE_MOVIES.equals(itemLabel))
                    moviesIntent.putExtra(MODE_KEY, MY_FAVORITE_MOVIES);
                else if (BOX_OFFICE_MOVIES.equals(itemLabel)) 
                    moviesIntent.putExtra(MODE_KEY, BOX_OFFICE_MOVIES);
                 else if (IN_THEATERS_MOVIES.equals(itemLabel)) 
                     moviesIntent.putExtra(MODE_KEY, IN_THEATERS_MOVIES);
                 else if (OPENING_MOVIES.equals(itemLabel)) 
                     moviesIntent.putExtra(MODE_KEY, OPENING_MOVIES);
                 else if (UPCOMING_MOVIES.equals(itemLabel))
                     moviesIntent.putExtra(MODE_KEY, UPCOMING_MOVIES);                
                startActivity(moviesIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences(SHARED_PREF_NAME, 0);
        String username = pref.getString(USERNAME_KEY, null);
        if (username == null)
            startActivity(new Intent(this, UsernameActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return super.onCreateOptionsMenu(menu);
    }

}
