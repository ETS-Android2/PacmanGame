package com.omer.mypackman.gps;
import android.content.Context;
public class Location_Manager {
    private GPS_Track gps;

    public Location_Manager(Context context) {
        gps = new GPS_Track(context);
    }

    public double getLat () {
        double lat=0.0;
        if(gps.canGetLocation()){
            lat = gps.getLatitude();
            return lat;

        }else{
            gps.showSettingsAlert();
            return 0.0;
        }
    }

    public double getLon () {
        double lon = 0.0;
        if(gps.canGetLocation()){
            lon = gps.getLongitude();
            return lon;

        }else{
            return 0.0;
        }
    }

}
