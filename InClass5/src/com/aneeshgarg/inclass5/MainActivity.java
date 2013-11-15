/**
 * Assignment #: 5<br>
 * Filename: MainActivity.java<br>
 * Students in Group: Aneesh Garg
 */
package com.aneeshgarg.inclass5;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final float MIN_DISTANCE      = 10;
    private static final long  MIN_TIME          = 0;
    private static final float ZOOM = 15.0f;

    GoogleMap                  gmap;
    LocationManager            locationManager;
    Location                   location;
    boolean                    isTrackingEnabled = false;
    LatLng                     lastLocation      = null;

    LocationListener           locationListener  = new LocationListener() {

                                                     @Override
                                                     public void onStatusChanged(String provider, int status, Bundle extras) {
                                                         // TODO Auto-generated
                                                         // method stub

                                                     }

                                                     @Override
                                                     public void onProviderEnabled(String provider) {
                                                         // TODO Auto-generated
                                                         // method stub

                                                     }

                                                     @Override
                                                     public void onProviderDisabled(String provider) {

                                                     }

                                                     @Override
                                                     public void onLocationChanged(Location location) {
                                                         LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());                                                         
                                                         if (lastLocation == null) {
                                                             lastLocation = newLatLng;
                                                             gmap.addMarker(new MarkerOptions().position(lastLocation).title("Start Point"));
                                                         }
                                                         gmap.addPolyline(new PolylineOptions().add(lastLocation,newLatLng).width(5).color(Color.BLUE));
                                                         gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(newLatLng,ZOOM));
                                                         lastLocation = newLatLng;                                                         
                                                         String loc = location.getLatitude() + ", " + location.getLongitude();
                                                         Log.d("aneeshgarg", loc);
                                                     }
                                                 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        gmap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        gmap.setMyLocationEnabled(true);
        gmap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM));

        gmap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng point) {
                if (!isTrackingEnabled) {
                    isTrackingEnabled = true;
                    Toast.makeText(MainActivity.this, "Start Location Tracking", Toast.LENGTH_LONG).show();
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);                    
                } else {
                    isTrackingEnabled = false;
                    Toast.makeText(MainActivity.this, "Stop Location Tracking", Toast.LENGTH_LONG).show();
                    locationManager.removeUpdates(locationListener);
                    gmap.addMarker(new MarkerOptions().position(lastLocation).title("Stop Point"));
                    lastLocation = null;
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            Toast.makeText(this, "Please Enable GPS Settings", Toast.LENGTH_LONG).show();
        } else {
            if (location != null) {
                gmap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
