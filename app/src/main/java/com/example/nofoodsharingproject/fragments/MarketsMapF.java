package com.example.nofoodsharingproject.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.utils.CustomLocationListener;
import com.example.nofoodsharingproject.utils.CustomSearchMarkets;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

// Карта на данный момент будет единой для всех пользователей

public class MarketsMapF extends Fragment {

    MapView mapView;
    int firstPermission;
    int secondPermission;

    final Point moscowPoint = new Point(55.71989101308894, 37.5689757769603);
    final Animation pingAnimation = new Animation(Animation.Type.SMOOTH, 0);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firstPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        secondPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        if (firstPermission != PackageManager.PERMISSION_GRANTED && secondPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        } else if (firstPermission != PackageManager.PERMISSION_GRANTED && secondPermission == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION }, 200);
        } else if (firstPermission == PackageManager.PERMISSION_GRANTED && secondPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION }, 200);
        }
//        final Activity mainContext = getActivity();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                CustomLocationListener.SetUpLocationListener(mainContext);
//            }
    }

    public boolean checkLocationPermissions() {
        firstPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        secondPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        return firstPermission == PackageManager.PERMISSION_GRANTED && secondPermission == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_markets_map, container, false);

        MapKitFactory.initialize(getContext());
        CustomLocationListener.SetUpLocationListener(getActivity());

        mapView = (MapView) view.findViewById(R.id.mapview);
        if (checkLocationPermissions()) {
            Location location = CustomLocationListener.location;
            try {
                mapView.getMap().move(new CameraPosition(new Point(location.getLatitude(), location.getLongitude()), 14, 0, 0),  new Animation(Animation.Type.SMOOTH, 0), null);
            } catch (NullPointerException err) {
                mapView.getMap().move(new CameraPosition(moscowPoint, 14, 0, 0), pingAnimation, null);
            }
        } else mapView.getMap().move(new CameraPosition(moscowPoint, 14, 0, 0), pingAnimation, null);

        return view;
    }

    @Override
    public void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }
}