package com.example.nofoodsharingproject.models;

import com.yandex.mapkit.geometry.Point;

import java.util.Objects;

public class Market {
    Point point;
    String title;
    public boolean isReal = true;

    public Market() {}
    public Market(String title) {
        this.title = title;
    }
    public Market(double latitude, double longitude) {
        this.point = new Point(latitude, longitude);
    }
    public Market(String title, double latitude, double longitude) {
        this.point = new Point(latitude, longitude);
        this.title = title;
    }

    public Market(String title, double latitude, double longitude, boolean isReal) {
        this.point = new Point(latitude, longitude);
        this.title = title;
        this.isReal = isReal;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public void setReal(boolean real) {
        isReal = real;
    }

    public boolean isReal() {
        return isReal;
    }

    public double getLatitude() {
        return point.getLatitude();
    }

    public double getLongitude() {
        return point.getLongitude();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        Market market = (Market) o;
        return Objects.equals(point, market.point);
    }
}
