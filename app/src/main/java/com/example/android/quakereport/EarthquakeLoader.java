package com.example.android.quakereport;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by Nitika Kamboj on 04-07-2017.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    private static final String LOG_TAG = EarthquakeLoader.class.getName();
    private String mUrl;

    public EarthquakeLoader(Context context, String url) {
    super(context);
    mUrl=url;
    }
    protected void onStartLoading(){
        forceLoad();
    }
    public List<Earthquake> loadInBackground()
    {
     if(mUrl==null)
     {
     return null;
     }
     List<Earthquake> earthquakes=QueryUtils.fetchEarthquakeData(mUrl);
        return earthquakes;
    }
}
