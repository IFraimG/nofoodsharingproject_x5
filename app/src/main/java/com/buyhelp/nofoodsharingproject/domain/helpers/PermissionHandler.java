/**
 * Класс {@code PermissionHandler} необходим для работы с проверкой Permissions и запросами на их включение
 * @author Кулагин Александр
 */


package com.buyhelp.nofoodsharingproject.domain.helpers;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import javax.inject.Inject;

public class PermissionHandler {
    private AppCompatActivity activity;

    public PermissionHandler() {}

    @Inject
    public PermissionHandler(AppCompatActivity activity) {
        this.activity = activity;
    }

    /**
     * Метод для проверки включенной геолокации
     */
    public boolean checkPermissions(AppCompatActivity activity) {
        int firstPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        int secondPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);

        return firstPermission == PackageManager.PERMISSION_GRANTED && secondPermission == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Метод для проверки включенной геолокации, в том числе и в фоновом режиме
     */
    public void requestPermissions(AppCompatActivity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                }, 200);

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                != PackageManager.PERMISSION_GRANTED && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{ android.Manifest.permission.ACCESS_BACKGROUND_LOCATION }, 201);
        }
    }

    /**
     * Метод для проверки и запросов разрешений на включение календаря
     */
    public void requestCalendarPermissions(AppCompatActivity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CALENDAR)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{ android.Manifest.permission.READ_CALENDAR, android.Manifest.permission.WRITE_CALENDAR,  }, 202);
        }
    }

    /**
     * Метод для проверки включенной геолокации
     */
    public void requestMapPermissions(AppCompatActivity activity) {
        int firstPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        int secondPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);

        if (firstPermission != PackageManager.PERMISSION_GRANTED && secondPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        } else if (firstPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
        } else if (secondPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
    }
}
