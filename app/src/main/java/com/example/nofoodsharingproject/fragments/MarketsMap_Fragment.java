package com.example.nofoodsharingproject.fragments;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.data.repository.MapRepository;
import com.example.nofoodsharingproject.models.Getter;
import com.example.nofoodsharingproject.models.Market;
import com.example.nofoodsharingproject.models.User;
import com.example.nofoodsharingproject.utils.CustomLocationListener;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.GeoObjectCollection;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.GeoObjectTapEvent;
import com.yandex.mapkit.layers.GeoObjectTapListener;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CameraUpdateReason;
import com.yandex.mapkit.map.GeoObjectSelectionMetadata;
import com.yandex.mapkit.map.InputListener;
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
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

// MapObjectTapListener потом добавить
public class MarketsMap_Fragment extends Fragment implements Session.SearchListener, CameraListener,
        GeoObjectTapListener, InputListener, UserLocationObjectListener {

    MapView mapView;
    int firstPermission;
    int secondPermission;
    private SearchManager searchManager;
    private Session searchSession;
    private UserLocationLayer userLocationLayer;

    private Button setMarketBtn;
    private Spinner listMarketsSpinner;

    private String choosenMarket;
    private String[] listMarkets;
    private final Market[] fullListMarkets = new Market[]{
            new Market("Выберите магазин", 0, 0, false),
            new Market("Большая Андроньевская улица, 22", 55.740813, 37.670078),
            new Market("Большая Андроньевская улицаd, 22", 55.740813, 37.670078),
            new Market("Большая Андроfeньевская улицeefа, 22", 55.740813, 37.670078),
            new Market("Большая Андроньевсwdwdкая уwwлица, 22", 55.740813, 37.670078),
            new Market("Большая Андроньfefefeевская улица, 22", 55.740813, 37.670078),
            new Market("Большая Андроньевсefedкая улица, 22", 55.740813, 37.670078)
    };

    // координаты экземпляра Пятерочки 55.740813, 37.670078
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

    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_markets_map, container, false);

        this.mapView = (MapView) view.findViewById(R.id.mapview);
        this.setMarketBtn = view.findViewById(R.id.map_set_market_btn);
        this.listMarketsSpinner = view.findViewById(R.id.map_list_markets);

        initMap();
        getPinnedMarketInfo();
        initListMarkets();

        setMarketBtn.setOnClickListener(View -> {
            Pair<String, Boolean> userDate = defineTypeUser();
            if (userDate.second) {
                MapRepository.setGetterMarket(userDate.first, choosenMarket).enqueue(new Callback<Getter>() {
                    @Override
                    public void onResponse(@NotNull Call<Getter> call, @NotNull retrofit2.Response<Getter> response) {
                        if (response.code() == 200) {
                            Toast.makeText(getContext(), "Успешно!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Getter> call, Throwable t) {
                        Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            // изменение прикрепленного магазина
        });

        loadMap();

        return view;
    }

    private void getPinnedMarketInfo() {
        Pair<String, Boolean> userDate = defineTypeUser();
        String userType = userDate.second ? "getter" : "setter";

        this.listMarkets = new String[fullListMarkets.length];
        this.listMarkets[0] = "Выберите магазин";
        MapRepository.getPinMarket(userType, userDate.first).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull retrofit2.Response<String> response) {
                if (response.code() == 404) choosenMarket = "";
                else if (response.code() == 400) Toast.makeText(getContext(), "Что-то пошло не так!", Toast.LENGTH_SHORT).show();
                else {
                    choosenMarket = response.body();
                    listMarkets[0] = response.body();
                    for (int i = 1; i < fullListMarkets.length; i++) listMarkets[i] = fullListMarkets[i].getTitle();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                for (int i = 1; i < fullListMarkets.length; i++) listMarkets[i] = fullListMarkets[i].getTitle();
            }
        });
    }

    private void initMap() {
        MapKitFactory.initialize(getContext());
        SearchFactory.initialize(getContext());

        this.searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED);
        CustomLocationListener.SetUpLocationListener(getActivity());

        this.mapView.getMap().addCameraListener(this);
        this.mapView.getMap().addTapListener(this);
        this.mapView.getMap().addInputListener(this);
        this.mapView.getMap().setRotateGesturesEnabled(false);

        MapKitFactory.getInstance().resetLocationManagerToDefault();

        this.userLocationLayer = MapKitFactory.getInstance().createUserLocationLayer(mapView.getMapWindow());
        this.userLocationLayer.setVisible(true);
        this.userLocationLayer.setHeadingEnabled(true);
        this.userLocationLayer.setObjectListener(this);
    }

    private void initListMarkets() {
        ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), R.layout.market_item, this.listMarkets);
        adapter.setDropDownViewResource(R.layout.map_dropdown_text);
        listMarketsSpinner.setAdapter(adapter);

        listMarketsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choosenMarket = listMarkets[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadMap() {
        if (checkLocationPermissions()) {
            try {
                Log.d("mapInfo", CustomLocationListener.location.getLatitude() + " " + CustomLocationListener.location.getLongitude());
                double lat = CustomLocationListener.location.getLatitude();
                double longt = CustomLocationListener.location.getLongitude();

//                mapView.getMap().move(new CameraPosition(new Point(lat, longt), 14, 0, 0),  new Animation(Animation.Type.SMOOTH, 0), null);
                this.mapView.getMap().move(new CameraPosition(moscowPoint, 14, 0, 0), pingAnimation, null);
            } catch (NullPointerException err) {
                this.mapView.getMap().move(new CameraPosition(moscowPoint, 14, 0, 0), pingAnimation, null);
            }
        } else this.mapView.getMap().move(new CameraPosition(moscowPoint, 14, 0, 0), pingAnimation, null);

        submitQuery("Пятёрочка");
        submitQuery("Перекрёсток");

    }

    private Pair<String, Boolean> defineTypeUser() {
        try {
            MasterKey masterKey = new MasterKey.Builder(getActivity().getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(getActivity().getApplicationContext(), "user", masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

            String userID = sharedPreferences.getString("X5_id", "");
            boolean isUser = sharedPreferences.getBoolean("isGetter", false);
            return new Pair<>(userID, isUser);
        } catch (GeneralSecurityException | IOException err) {
            Toast.makeText(getContext(), "Непредвиденная ошибка!", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private void submitQuery(String query) {
        searchSession = searchManager.submit(
                query,
                VisibleRegionUtils.toPolygon(mapView.getMap().getVisibleRegion()),
                new SearchOptions(),
                this
        );
    }

//    private void submitQueryWithCoords(Point point) {
//        searchSession = searchManager.submit(point, 11, new SearchOptions(), this);
//    }

    public boolean checkLocationPermissions() {
        firstPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        secondPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        return firstPermission == PackageManager.PERMISSION_GRANTED && secondPermission == PackageManager.PERMISSION_GRANTED;
    }


    // ________________ Session.SearchListener, CameraListener ________________
    @Override
    public void onSearchResponse(@NonNull Response response) {
        MapObjectCollection mapObjects = mapView.getMap().getMapObjects();
        mapObjects.clear();
//
//        Log.d("msg", response.getMetadata().getToponymResultMetadata());
        for (GeoObjectCollection.Item searchResult : response.getCollection().getChildren()) {
            Point resultLocation = searchResult.getObj().getGeometry().get(0).getPoint();
            if (resultLocation != null) {
                if (getContext() != null) mapObjects.addPlacemark(resultLocation, ImageProvider.fromResource(getContext(), R.drawable.location_on1));
            }
        }
    }

    @Override
    public void onSearchError(@NonNull Error error) {
        String errorMessage = "Error";
        if (error instanceof RemoteError) {
            errorMessage = "RemoteError";
        } else if (error instanceof NetworkError) {
            errorMessage = "NetworkError";
        }

        Log.i("MapErr", errorMessage);
    }

    @Override
    public void onCameraPositionChanged(@NonNull Map map, @NonNull CameraPosition cameraPosition, @NonNull CameraUpdateReason cameraUpdateReason, boolean finished) {
        if (finished) {
            submitQuery("Пятёрочка");
            submitQuery("Перекрёсток");
        }
    }

    // ________________ GeoObjectTapListener ________________

    @Override
    public boolean onObjectTap(@NonNull GeoObjectTapEvent geoObjectTapEvent) {
        final GeoObjectSelectionMetadata selectionMetadata = geoObjectTapEvent
                .getGeoObject()
                .getMetadataContainer()
                .getItem(GeoObjectSelectionMetadata.class);

        if (selectionMetadata != null) {
            mapView.getMap().selectGeoObject(selectionMetadata.getId(), selectionMetadata.getLayerId());
        }

        return selectionMetadata != null;
    }


    // при нажатии на любой объект
    @Override
    public void onMapTap(@NonNull Map map, @NonNull Point point) {
        mapView.getMap().deselectGeoObject();
//        Log.i("coords", Double.toString(point.getLatitude()));
    }
    @Override
    public void onMapLongTap(@NonNull Map map, @NonNull Point point) {}



    //________________  UserLocationObjectListener отображение метки пользователя ________________

    @Override
    public void onObjectAdded(UserLocationView userLocationView) {
        //нужно
//        userLocationLayer.setAnchor(
//                new PointF((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.5)),
//                new PointF((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.83)));
//
//        userLocationView.getArrow().setIcon(ImageProvider.fromResource(getContext(), R.drawable.location_on));
//        CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();
//
//        pinIcon.setIcon(
//                "icon",
//                ImageProvider.fromResource(getContext(), R.drawable.location_on),
//                new IconStyle().setAnchor(new PointF(0f, 0f))
//                        .setRotationType(RotationType.ROTATE)
//                        .setZIndex(0f)
//                        .setScale(1f)
//        );
//        userLocationView.getAccuracyCircle().setFillColor(Color.BLUE & 0x99ffffff);

        // ненужно
//        pinIcon.setIcon(
//                "pin",
//                ImageProvider.fromResource(getContext(), R.drawable.map_simbol),
//                new IconStyle().setAnchor(new PointF(0.5f, 0.5f))
//                        .setRotationType(RotationType.ROTATE)
//                        .setZIndex(1f)
//                        .setScale(0.5f)
//        );
    }

    @Override
    public void onObjectRemoved(UserLocationView view) {
    }

    @Override
    public void onObjectUpdated(UserLocationView view, ObjectEvent event) {
    }

    // ________________ MapObjectTapListener ________________
//
//    @Override
//    public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
//        GeoObject geoObject = (GeoObject) mapObject;
//        ToponymObjectMetadata metadata = geoObject.getMetadataContainer().getItem(ToponymObjectMetadata.class);
//        if (metadata != null) {
//            String toponym = metadata.getFormerName();
//            Log.i("msg",  toponym);
//            submitQuery(toponym);
//        }
//
//        return false;
//    }
}