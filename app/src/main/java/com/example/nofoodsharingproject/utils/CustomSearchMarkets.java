package com.example.nofoodsharingproject.utils;

import com.example.nofoodsharingproject.R;
import com.yandex.mapkit.GeoObjectCollection;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CameraUpdateReason;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.VisibleRegionUtils;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.search.Response;
import com.yandex.mapkit.search.SearchFactory;
import com.yandex.mapkit.search.SearchManager;
import com.yandex.mapkit.search.SearchManagerType;
import com.yandex.mapkit.search.SearchOptions;
import com.yandex.mapkit.search.Session;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

public class CustomSearchMarkets extends AppCompatActivity implements Session.SearchListener, CameraListener {
    private MapView mapView;
    private SearchManager searchManager;
    private Session searchSession;

    private void submitQuery() {
        searchSession = searchManager.submit("Пятерочка",
                VisibleRegionUtils.toPolygon(mapView.getMap().getVisibleRegion()),
                new SearchOptions(),
                this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SearchFactory.initialize(this);

//        setContentView(R.layout.search);

        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED);

        mapView = findViewById(R.id.mapview);
        mapView.getMap().addCameraListener(this);

        mapView.getMap().move(new CameraPosition(new Point(59, 30), 14.0f, 0.0f, 0.0f));
        submitQuery();
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    public void onSearchResponse(Response response) {
        MapObjectCollection mapObjects = mapView.getMap().getMapObjects();
        mapObjects.clear();

        for (GeoObjectCollection.Item searchResult : response.getCollection().getChildren()) {
            Point resultLocation = searchResult.getObj().getGeometry().get(0).getPoint();
            if (resultLocation != null) {
                mapObjects.addPlacemark(
                        resultLocation,
                        ImageProvider.fromResource(this, R.drawable.location_on));
            }
        }
    }

    @Override
    public void onSearchError(@NonNull Error error) {
        if (error instanceof RemoteError) Log.i("msg", "RemoteError");
        else if (error instanceof NetworkError) Log.i("msg", "NetworkError");
    }

    @Override
    public void onCameraPositionChanged(Map map, CameraPosition cameraPosition, CameraUpdateReason cameraUpdateReason, boolean finished) {
        if (finished) submitQuery();
    }
}
