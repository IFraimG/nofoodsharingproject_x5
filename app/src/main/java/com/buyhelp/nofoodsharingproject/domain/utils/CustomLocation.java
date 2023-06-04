package com.buyhelp.nofoodsharingproject.domain.utils;

import android.location.Location;

public class CustomLocation {
    private Location location;

    public CustomLocation(double lat, double longt) {
        location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(longt);
    }

    public Location getLocation() {
        return location;
    }
}
