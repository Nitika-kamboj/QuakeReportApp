/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Intent;
import android.content.Context;
import android.net.ConnectivityManager;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.widget.TextView;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<Earthquake>> {
    private TextView mEmptyStateTextView;
    private EarthquakeAdapter mAdapter;
     public static final String LOG_TAG = EarthquakeActivity.class.getName();
     public static final int EARTHQUAKE_LOADER_ID=1;
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Create a fake list of earthquake locations.
       // ArrayList<Earthquake> earthquakes = QueryUtils.extractFeatureFromJson();

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);
        // Create a new {@link ArrayAdapter} of earthquakes
       mAdapter=new EarthquakeAdapter(this,new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Earthquake currentEarthquake = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getmUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
        ConnectivityManager connMgr=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo!=null&&networkInfo.isConnected()){
        LoaderManager loaderManager=getLoaderManager();
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null,this);}
        else
        {
        View loadingIndicator =findViewById(R.id.loading_indicator);
         loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    public Loader<List<Earthquake>> onCreateLoader(int i,Bundle bundle){
      SharedPreferences sharedPrefs=PreferenceManager.getDefaultSharedPreferences(this);
       String minMagnitude=sharedPrefs.getString(getString(R.string.settings_min_magnitude_key),getString(R.string.settings_min_magnitude_default));
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        Uri baseUri=Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder=baseUri.buildUpon();
      uriBuilder.appendQueryParameter("format","geojson");
      uriBuilder.appendQueryParameter("limit","10");
       uriBuilder.appendQueryParameter("minmag",minMagnitude);
        uriBuilder.appendQueryParameter("orderby",orderBy);
     return new EarthquakeLoader(this,uriBuilder.toString());
    }

    public void onLoadFinished(Loader<List<Earthquake>> loader,List<Earthquake> earthquakes)
    {    View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_earthquakes);

        mAdapter.clear();
        if (earthquakes != null && !earthquakes.isEmpty()) {
          mAdapter.addAll(earthquakes);
        }
    }

    public void onLoaderReset(Loader<List<Earthquake>> loader){
     mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_settings)
        {
         Intent settingsIntent=new Intent(this,SettingsActivity.class);
         startActivity(settingsIntent);
         return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

