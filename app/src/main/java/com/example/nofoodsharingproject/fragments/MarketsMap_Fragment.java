package com.example.nofoodsharingproject.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
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
import com.example.nofoodsharingproject.models.Setter;
import com.example.nofoodsharingproject.utils.CustomLocationListener;
import com.example.nofoodsharingproject.data.api.map.MarketTitleResponse;
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

import retrofit2.Call;
import retrofit2.Callback;

// MapObjectTapListener потом добавить
public class MarketsMap_Fragment extends Fragment implements Session.SearchListener, CameraListener,
        GeoObjectTapListener, InputListener, UserLocationObjectListener {

    MapView mapView;
    int firstPermission;
    int secondPermission;
    private SearchManager searchManager;
    private LocationManager locationManager;
    private CustomLocationListener locationListener;
    private Session searchSession;
    private UserLocationLayer userLocationLayer;

    private Button setMarketBtn;
    private Spinner listMarketsSpinner;

    private String choosenMarket;
    ArrayAdapter<String> adapter;
    private int oldPosition = -1;
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

        requestPermissions();

        MapKitFactory.initialize(getContext());
        SearchFactory.initialize(getContext());
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

        setMarketBtn.setOnClickListener(View -> updateMarket());

        return view;
    }

    public void updateMarket() {
        Pair<String, Boolean> userData = defineTypeUser();
        if (!choosenMarket.equals("Выберите магазин")) {
            if (userData != null && userData.second) {
                MapRepository.setGetterMarket(userData.first, choosenMarket).enqueue(new Callback<Getter>() {
                    @Override
                    public void onResponse(@NotNull Call<Getter> call, @NotNull retrofit2.Response<Getter> response) {
                        if (response.code() == 200) {
                            Toast.makeText(getContext(), "Успешно!", Toast.LENGTH_SHORT).show();
                            if (oldPosition != -1) listMarketsSpinner.setSelection(oldPosition);
                        }
                    }

                    @Override
                    public void onFailure(Call<Getter> call, Throwable t) {
                        Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (!userData.second) {
                MapRepository.setSetterMarket(userData.first, choosenMarket).enqueue(new Callback<Setter>() {
                    @Override
                    public void onResponse(@NotNull Call<Setter> call, @NotNull retrofit2.Response<Setter> response) {
                        if (response.code() == 200) {
                            Toast.makeText(getContext(), "Успешно!", Toast.LENGTH_SHORT).show();
                            if (oldPosition != -1) listMarketsSpinner.setSelection(oldPosition);
                        }
                    }

                    @Override
                    public void onFailure(Call<Setter> call, Throwable t) {
                        Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    public void requestPermissions() {
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
    private void getPinnedMarketInfo() {
        Pair<String, Boolean> userData = defineTypeUser();
        String userType = userData.second ? "getter" : "setter";

        MapRepository.getPinMarket(userType, userData.first).enqueue(new Callback<MarketTitleResponse>() {
            @Override
            public void onResponse(@NotNull Call<MarketTitleResponse> call, @NotNull retrofit2.Response<MarketTitleResponse> response) {
                if (response.code() == 404) {
                    choosenMarket = "";
                    listMarkets = new String[fullListMarkets.length];
                    for (int i = 0; i < fullListMarkets.length; i++) listMarkets[i] = fullListMarkets[i].getTitle();
                }
                else if (response.code() == 400) Toast.makeText(getContext(), "Что-то пошло не так!", Toast.LENGTH_SHORT).show();
                else {
                    listMarkets = new String[fullListMarkets.length - 1];
                    listMarkets[0] = response.body().market;
                    choosenMarket = response.body().market;
                    for (int i = 1; i < fullListMarkets.length; i++) {
                        if (fullListMarkets[i].getTitle().equals(choosenMarket)) {
                            oldPosition = i;
                            listMarketsSpinner.setSelection(i);
                            break;
                        }
                        listMarkets[i] = fullListMarkets[i].getTitle();
                    }
                }
                if (oldPosition != -1) {
                    for (int i = oldPosition; i < fullListMarkets.length - 1; i++) {
                        listMarkets[i] = fullListMarkets[i + 1].getTitle();
                    }
                }

                initListMarkets();
            }
            @Override
            public void onFailure(Call<MarketTitleResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Что-то пошло не так!!!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @SuppressLint("MissingPermission")
    private void setUpLocationListener() {
//        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        locationListener = new CustomLocationListener();
//
//        if (checkLocationPermissions()) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 25,  locationListener);
//            locationListener.setLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
//        }
    }

    private void initMap() {
        requestPermissions();

        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED);

        setUpLocationListener();
        mapView.getMap().addCameraListener(this);
        mapView.getMap().addTapListener(this);
        mapView.getMap().addInputListener(this);

        MapKitFactory.getInstance().resetLocationManagerToDefault();

        mapView.getMap().setRotateGesturesEnabled(false);

        userLocationLayer = MapKitFactory.getInstance().createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener(this);

        if (checkLocationPermissions()) {
            try {
//                Log.d("mapInfo", locationListener.getLocation().getLongitude() + " " + locationListener.getLocation().getLongitude());
//                double lat = CustomLocationListener.location.getLatitude();
//                double longt = CustomLocationListener.location.getLongitude();

//                mapView.getMap().move(new CameraPosition(new Point(lat, longt), 14, 0, 0),  new Animation(Animation.Type.SMOOTH, 0), null);
                this.mapView.getMap().move(new CameraPosition(moscowPoint, 14, 0, 0), pingAnimation, null);
            } catch (NullPointerException err) {
                this.mapView.getMap().move(new CameraPosition(moscowPoint, 14, 0, 0), pingAnimation, null);
            }
        } else this.mapView.getMap().move(new CameraPosition(moscowPoint, 14, 0, 0), pingAnimation, null);

        submitQuery("Пятёрочка");
        submitQuery("Перекрёсток");
    }

    private void initListMarkets() {
        adapter = new ArrayAdapter<>(getContext(), R.layout.market_item, listMarkets);
        adapter.setDropDownViewResource(R.layout.map_dropdown_text);
        listMarketsSpinner.setAdapter(adapter);

        listMarketsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choosenMarket = listMarkets[position];
                oldPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
            Log.e("esp_error", err.getMessage());
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