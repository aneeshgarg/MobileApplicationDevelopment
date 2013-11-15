package com.thetechdork.rottentomatoes.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thetechdork.rottentomatoes.model.Favorite;
import com.thetechdork.rottentomatoes.model.FavoritesAPIResponse;
import com.thetechdork.rottentomatoes.model.Movie;
import com.thetechdork.rottentomatoes.tasks.CallFavoriteAPITask;
import com.thetechdork.rottentomatoes.tasks.GetMovieInfoTask;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class StatisticsActivity extends BaseActivity {

    private WebView view;
    private List<Favorite> favoriteList;
    private Map<String,String> favCountMap;
    private Map<String,String> movieIdTitleMap;
    private ProgressDialog dialog;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        
        dialog= new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Getting Favorite Statistics.");
        dialog.show();
        
        new CallFavoriteAPITask(this).execute(GET_FAVORITE_STATS_MODE, getSharedPreferences(SHARED_PREF_NAME, 0).getString(USERNAME_KEY, ""));

        view = (WebView) findViewById(R.id.statistics_webview);
        view.getSettings().setJavaScriptEnabled(true);
        view.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            public void onReceivedError (WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(StatisticsActivity.this, "Error in loading graph." + description, Toast.LENGTH_LONG).show();
            }
            public void onPageFinished (WebView view, String url) {
                //Toast.makeText(StatisticsActivity.this, "Finished loading graph.", Toast.LENGTH_LONG).show();                
            }
        });

    }

    private String getChartPage() {
        String page = "";

        page += "<html>";
            page += "<head>";
                page += "<script type='text/javascript' src='https://www.google.com/jsapi'></script>";
                page += "<script type='text/javascript'>";
                    page += "google.load('visualization', '1', {packages:['corechart']});";
                    page += "google.setOnLoadCallback(drawChart);";
                    page += "function drawChart() {";
                        page += "var data = google.visualization.arrayToDataTable([";
                        int count = 0;
                        for (String id : favCountMap.keySet()) {
                            String y = movieIdTitleMap.get(id) != null ? movieIdTitleMap.get(id) : id;
                            String x = favCountMap.get(id);
                            page += "['"+y+"',"+x+"]";
                            if (count != favCountMap.keySet().size()-1)
                                page+= ",";
                        }
                        page += "]);";
                
                        page += "var options = {";
                            page += "chartArea:{left:50,top:0,width:'100%',height:'80%'}";                        
                        page += "};";
            
                        page += "var chart = new google.visualization.BarChart(document.getElementById('chart_div'));";
                        page += "chart.draw(data, options);";
                    page += "}";
                page += "</script>";
            page += "</head>";
            page += "<body>";
            page += "<div id='chart_div' style='width: 100%; height: 450px;'></div>";
            page += "</body>";
        page += "</html>";
        Log.d(LOG_KEY, page);
        return page;
    }
    
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void setFavoriteStats(FavoritesAPIResponse response) {
        if (response != null) {
            if (response.getError().getId() == 0) {
                favoriteList = response.getFavorites();
                favCountMap = new HashMap<String, String>();
                for (Favorite fav : favoriteList) {
                    favCountMap.put(fav.getId(),fav.getCount().toString());
                    new GetMovieInfoTask(this, null).execute(MovieActivity.MOVIE_TITLE_MODE, fav.getId());
                }
            }
            else 
                Toast.makeText(this, response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setMovieTile(Movie movie) {
        if (movieIdTitleMap == null) {
            movieIdTitleMap = new HashMap<String, String>();
        }
        movieIdTitleMap.put(movie.getId(), movie.getTitle());
        if (movieIdTitleMap.size() == favCountMap.size()) {
            view.loadData(getChartPage(), "text/html", "UTF-8");
            dialog.dismiss();
        }
        else
            dialog.setMessage("Received movie title for "+movieIdTitleMap.size()+" movies.");
        
    }

}
