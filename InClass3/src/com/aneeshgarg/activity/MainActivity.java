/**
 * Assignment #: 3<br>
 * Filename: MainActivity.java<br>
 * Students in Group: Aneesh Garg
 */
package com.aneeshgarg.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

public class MainActivity extends Activity {

    private static final String PROGRESS_TITLE   = "Doing Work";
    private static final String PROGRESS_MESSAGE = "Retrieving the number...";

    private Handler             handler;
    private ProgressDialog      dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button) findViewById(R.id.asyncButton)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new AsyncWork().execute();
            }
        });

        ((Button) findViewById(R.id.threadButton)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                (new Thread(new ThreadWork())).start();

            }
        });

        handler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {

                if (msg != null) {
                    switch (msg.what) {

                        case ThreadWork.START:
                            dialog = new ProgressDialog(MainActivity.this);
                            dialog.setTitle(PROGRESS_TITLE);
                            dialog.setMessage(PROGRESS_MESSAGE);
                            dialog.setCancelable(false);
                            dialog.show();
                            break;

                        case ThreadWork.END:
                            ((TextView) findViewById(R.id.numberTextView)).setText(((Double) msg.obj).toString());
                            dialog.dismiss();
                    }
                }

                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public class ThreadWork implements Runnable {

        public final static int START = 0x00;
        public final static int END   = 0x01;

        @Override
        public void run() {

            handler.sendEmptyMessage(START);

            double number = HeavyWork.getNumber();

            Message msg = new Message();
            msg.what = END;
            msg.obj = number;
            handler.sendMessage(msg);

        }
    }

    public class AsyncWork extends AsyncTask<Void, Void, Double> {

        @Override
        protected void onPostExecute(Double result) {
            super.onPostExecute(result);
            ((TextView) findViewById(R.id.numberTextView)).setText(result.toString());
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle(PROGRESS_TITLE);
            dialog.setMessage(PROGRESS_MESSAGE);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Double doInBackground(Void... params) {
            return HeavyWork.getNumber();
        }

    }

}
