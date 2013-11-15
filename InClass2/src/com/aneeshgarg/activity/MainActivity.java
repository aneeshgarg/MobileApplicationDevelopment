/**
 * Assignment #: 2<br>
 * Filename: MainActivity.java<br>
 * Students in Group: Aneesh Garg
 */
package com.aneeshgarg.activity;

import java.util.Iterator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.aneeshgarg.activity.Data.News;

public class MainActivity extends Activity implements View.OnClickListener {

    public final static String URL = "URL";
    private RadioGroup         newsLinkRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsLinkRadioGroup = new RadioGroup(this);
        boolean isFirst = true;
        Iterator<News> newsIter = Data.NEWS.iterator();
        while (newsIter.hasNext()) {
            News news = newsIter.next();
            RadioButton radio = new RadioButton(this);
            radio.setText(news.title);
            radio.setTag(news.url);
            newsLinkRadioGroup.addView(radio);
            if (isFirst) {
                newsLinkRadioGroup.check(radio.getId());
                isFirst = false;
            }
        }
        ((LinearLayout) findViewById(R.id.newsLinkLayout)).addView(newsLinkRadioGroup);
        ((Button) findViewById(R.id.goButton)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        RadioButton button = (RadioButton) findViewById(newsLinkRadioGroup.getCheckedRadioButtonId());
        String url = (String) button.getTag();
        if (url != null && !url.equals("")) {
            Intent intent = new Intent(this,WebActivity.class);
            intent.putExtra(URL, url);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
