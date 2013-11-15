/**
 * Assignment #: 5<br>
 * Filename: WebServiceTask.java<br>
 * Students in Group: Aneesh Garg
 */
package com.aneeshgarg.unccphotos.utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.aneeshgarg.unccphotos.MainActivity;
import com.aneeshgarg.unccphotos.R;
import com.aneeshgarg.unccphotos.model.Photo;

public class WebServiceTask extends AsyncTask<String, Void, ArrayList<Photo>> {

    private static final String XML_URL            = "http://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=9649c7f6d6aa8c022588a5eba39cb3b5&tags=uncc&extras=views%2Curl_m&per_page=100&format=rest";
    private static final String JSON_URL           = "http://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=9649c7f6d6aa8c022588a5eba39cb3b5&tags=uncc&extras=views%2Curl_m&per_page=100&format=json&nojsoncallback=1";
    public static int           WEBSERVICE_MESSAGE = 0x00;
    private ProgressDialog      dialog;
    private MainActivity        mainActivity;
    private Handler handler;

    public WebServiceTask(MainActivity mainActivity, Handler handler) {
        this.mainActivity = mainActivity;
        this.handler = handler;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(mainActivity);
        dialog.setMessage(mainActivity.getResources().getString(R.string.retrieve_images_messages));
        dialog.setCancelable(false);
        dialog.show();

    }

    @Override
    protected ArrayList<Photo> doInBackground(String... params) {
        String urlString = null;
        ArrayList<Photo> photosList = null;
        if (MainActivity.JSON_MODE.equals(params[0]))
            urlString = JSON_URL;
        else if (MainActivity.XML_MODE.equals(params[0]))
            urlString = XML_URL;

        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = con.getInputStream();
                if (MainActivity.JSON_MODE.equals(params[0]))
                    photosList= Parser.JSONParsor.parseJSON(in);
                else if (MainActivity.XML_MODE.equals(params[0]))
                    photosList = Parser.PhotoXmlPullParser.parseXML(in);
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("AneeshGarg", e.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("AneeshGarg", e.getMessage());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("AneeshGarg", e.getMessage());
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("AneeshGarg", e.getMessage());
        }
        return photosList;
    }

    @Override
    protected void onPostExecute(ArrayList<Photo> result) {
        super.onPostExecute(result);
        Message msg = new Message();
        msg.what = WEBSERVICE_MESSAGE;
        msg.obj = result;
        handler.sendMessage(msg);
        dialog.dismiss();
    }

}
