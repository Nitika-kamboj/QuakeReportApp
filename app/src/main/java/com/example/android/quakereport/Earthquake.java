package com.example.android.quakereport;

/**
 * Created by Nitika Kamboj on 02-07-2017.
 */

public class Earthquake {

    private double mMagnitude;
    private String mLocation;
    private long mTimeInMilliseconds;
    private String mUrl;

    public Earthquake(double magnitude,String location,long timeinmilliseconds,String url)
    {
        mMagnitude=magnitude;
        mLocation=location;
        mTimeInMilliseconds=timeinmilliseconds;
        mUrl=url;
    }

    public double getmMagnitude()
    {
     return mMagnitude;
    }
    public String getmLocation()
    {
     return mLocation;
    }
    public long getmTimeInMilliseconds()
    {
    return mTimeInMilliseconds;
    }
    public String getmUrl()
    {
    return mUrl;
    }
}
