package com.aneeshgarg.inclass4.utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import org.xmlpull.v1.XmlPullParserException;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.aneeshgarg.inclass4.activity.MainActivity;

public class AsyncApplicationsGet extends AsyncTask<String, Void, ArrayList<Application>> {
    private MainActivity   activity;
    private ProgressDialog dialog;

    public AsyncApplicationsGet(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<Application> doInBackground(String... params) {
        String urlString = params[0];

        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            int statusCode = con.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                InputStream in = con.getInputStream();
                ArrayList<Application> applications = ApplicationUtil.ApplicationsXMLPullParser.parsePersons(in);
                return applications;
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Application> result) {

        Collections.sort(result, Collections.reverseOrder());
        Log.d("AneeshGarg", result.toString());
        dialog.dismiss();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(activity);
        dialog.setCancelable(false);
        dialog.setMessage("Retrieving App Details...");
        dialog.show();
    }
}