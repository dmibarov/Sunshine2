package com.example.android.sunshine2.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final String FORECASTFRAGMENT_TAG = "FFTAG";
    private  String mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLocation = Utility.getPreferredLocation(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment(),FORECASTFRAGMENT_TAG)
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_map) {
                   openPreferredLocationInMap();
                        return true;
                    }

        return super.onOptionsItemSelected(item);
    }
    private void openPreferredLocationInMap() {

        String location = Utility.getPreferredLocation(this);

                        // Using the URI scheme for showing a location found on a map.  This super-handy
                               // intent can is detailed in the "Common Intents" page of Android's developer site:
                                // http://developer.android.com/guide/components/intents-common.html#Maps
                            Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                                .appendQueryParameter("q", location)
                                .build();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(geoLocation);

              if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Log.d(LOG_TAG, "Couldn't call " + location + ", no receiving apps installed!");
                }
            }
    @Override
       protected void onResume() {
           super.onResume();
           String location = Utility.getPreferredLocation( this );
           // update the location in our second pane using the fragment manager
           if (location != null && !location.equals(mLocation)) {
             ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentByTag(FORECASTFRAGMENT_TAG);
              if ( null != ff ) {
                   ff.onLocationChanged();
               }
               mLocation = location;
          }
       }

}
