/**
 * Класс {@code Market} необходим хранения информации о магазине для их отображения на карте
 * @author Кулагин Александр
 */

package com.buyhelp.nofoodsharingproject.data.models;

import com.yandex.mapkit.geometry.Point;

import java.util.Objects;

public class Market {
    private Point point;
    private String title;
    private boolean isReal = true;

    public Market() {}
    public Market(String title) {
        this.title = title;
    }

    /**
     * Конструктор для получения координат
     */
    public Market(double latitude, double longitude) {
        this.point = new Point(latitude, longitude);
    }

    /**
     * Конструктор для получения координат
     */
    public Market(String title, double latitude, double longitude) {
        this.point = new Point(latitude, longitude);
        this.title = title;
    }

    /**
     * Конструктор для получения координат
     * @param isReal нужен, чтобы отделять магазин от элемента "Выберите магазин", который есть вместе с другими Market в Spinner
     */
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
