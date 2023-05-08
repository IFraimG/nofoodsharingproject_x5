package com.example.nofoodsharingproject.models;

public class Market {
    double latitude;
    double longitude;
    String title;
    public boolean isReal = true;

    public Market() {}
    public Market(String title) {
        this.title = title;
    }
    public Market(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Market(String title, double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
    }

    public Market(String title, double latitude, double longitude, boolean isReal) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.isReal = isReal;
    }

    public void setReal(boolean real) {
        isReal = real;
    }

    public boolean isReal() {
        return isReal;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
