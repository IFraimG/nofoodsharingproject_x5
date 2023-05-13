package com.example.nofoodsharingproject.utils;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class CustomLocationListener implements LocationListener {
    public Location location;

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public void onLocationChanged(Location loc) {
        location = loc;
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }



}
