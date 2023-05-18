package com.example.nofoodsharingproject.fragments;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

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
import com.example.nofoodsharingproject.data.api.map.MapRepository;
import com.example.nofoodsharingproject.databinding.FragmentMarketsMapBinding;
import com.example.nofoodsharingproject.models.Getter;
import com.example.nofoodsharingproject.models.Market;
import com.example.nofoodsharingproject.models.Setter;
import com.example.nofoodsharingproject.data.api.map.dto.MarketTitleResponse;
import com.example.nofoodsharingproject.utils.DefineUser;
import com.example.nofoodsharingproject.utils.PermissionHandler;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.directions.DirectionsFactory;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.directions.driving.VehicleOptions;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.location.FilteringMode;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationManager;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.search.SearchFactory;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MarketsMapFragment extends Fragment implements UserLocationObjectListener,
        MapObjectTapListener, DrivingSession.DrivingRouteListener {

    private FragmentMarketsMapBinding binding;
    private MapView mapView;
    private LocationListener locationListener;
    private UserLocationLayer userLocationLayer;
    private Spinner listMarketsSpinner;
    private String choosenMarket;
    private int oldPosition = -1;
    private MapObjectCollection mapObjects;
    private String[] listMarkets;
    private DrivingRouter drivingRouter;
    private DrivingSession drivingSession;
    private boolean isAvailableLocation = false;
    private LocationManager locationManager;

    private final Market[] fullListMarkets = new Market[]{
            new Market("Выберите магазин", 0, 0, false),
            new Market("Большая Андроньевская улица, 22", 55.740813, 37.670078),
            new Market("1, микрорайон Парковый, Котельники", 55.660216, 37.875793),
            new Market("Ковров пер., 8, стр. 1", 55.740582, 37.681854),
            new Market("Нижегородская улица, 34", 55.736351, 37.695708)
    };

    private final List<Point> marketPoints = Arrays.asList(
            fullListMarkets[1].getPoint(),
            fullListMarkets[2].getPoint(),
            fullListMarkets[3].getPoint(),
            fullListMarkets[4].getPoint()
    );

    private final Point moscowPoint = new Point(55.71989101308894, 37.5689757769603);
    private final Animation pingAnimation = new Animation(Animation.Type.SMOOTH, 0);
    private Point myPoint;
    private DefineUser defineUser;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        PermissionHandler.requestMapPermissions(requireActivity(), requireContext());

        SearchFactory.initialize(requireContext());
        DirectionsFactory.initialize(requireContext());

        defineUser = new DefineUser(requireActivity());
        super.onCreate(savedInstanceState);
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
        if (locationListener != null) locationManager.unsubscribe(locationListener);
        super.onStop();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMarketsMapBinding.inflate(inflater);

        this.mapView = binding.mapview;
        Button setMarketBtn = binding.mapSetMarketBtn;
        Button makeRouteBtn = binding.mapMakeRoute;
        this.listMarketsSpinner = binding.mapListMarkets;

        initLocation();
        initMap();
        getPinnedMarketInfo();

        setMarketBtn.setOnClickListener(View -> updateMarket());
        makeRouteBtn.setOnClickListener(View -> createRoute());

        return binding.getRoot();
    }

    private void initLocation() {
        if (PermissionHandler.checkPermissions(requireContext())) {
            locationListener = new LocationListener(){
                @Override
                public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {
                    if (locationStatus == LocationStatus.AVAILABLE) isAvailableLocation = true;
                }
                @Override
                public void onLocationUpdated(@NonNull com.yandex.mapkit.location.Location location) {
                    myPoint = location.getPosition();
                }
            };

            locationManager = MapKitFactory.getInstance().createLocationManager();
            if (locationListener != null) locationManager.subscribeForLocationUpdates(0, 0, 0,false, FilteringMode.OFF, locationListener);
        }
    }

    private void initMap() {
        PermissionHandler.requestMapPermissions(requireActivity(), requireContext());

        mapView.getMap().setRotateGesturesEnabled(false);

        userLocationLayer = MapKitFactory.getInstance().createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener(this);

        mapView.getMap().move(new CameraPosition(moscowPoint, 14, 0, 0),  pingAnimation, null);

        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter();
        mapObjects = mapView.getMap().getMapObjects().addCollection();

        ImageProvider imageProvider = ImageProvider.fromResource(requireContext(), R.drawable.location_on2);
        for (Point point : marketPoints) {
            PlacemarkMapObject placemark = mapObjects.addPlacemark(point, imageProvider);
            placemark.addTapListener(this);
        }
    }

    private void updateMarket() {
        Pair<String, Boolean> userData = defineUser.getTypeUser();
        if (!choosenMarket.equals(getString(R.string.cchoose_shop_text))) {
            if (userData.second) {
                MapRepository.setGetterMarket(userData.first, choosenMarket).enqueue(new Callback<Getter>() {
                    @Override
                    public void onResponse(@NotNull Call<Getter> call, @NotNull retrofit2.Response<Getter> response) {
                        if (response.code() == 200) {
                            Toast.makeText(getContext(), R.string.sucses, Toast.LENGTH_SHORT).show();
                            if (oldPosition != -1) listMarketsSpinner.setSelection(oldPosition);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Getter> call, @NotNull Throwable t) {
                        Toast.makeText(getContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                MapRepository.setSetterMarket(userData.first, choosenMarket).enqueue(new Callback<Setter>() {
                    @Override
                    public void onResponse(@NotNull Call<Setter> call, @NotNull retrofit2.Response<Setter> response) {
                        if (response.code() == 200) {
                            Toast.makeText(getContext(), R.string.sucses, Toast.LENGTH_SHORT).show();
                            if (oldPosition != -1) listMarketsSpinner.setSelection(oldPosition);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Setter> call, @NotNull Throwable t) {
                        Toast.makeText(getContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void getPinnedMarketInfo() {
        Pair<String, Boolean> userData = defineUser.getTypeUser();
        String userType = userData.second ? "getter" : "setter";

        MapRepository.getPinMarket(userType, userData.first).enqueue(new Callback<MarketTitleResponse>() {
            @Override
            public void onResponse(@NotNull Call<MarketTitleResponse> call, @NotNull retrofit2.Response<MarketTitleResponse> response) {
                if (response.code() == 404) {
                    choosenMarket = "";
                    listMarkets = new String[fullListMarkets.length];
                    for (int i = 0; i < fullListMarkets.length; i++)
                        listMarkets[i] = fullListMarkets[i].getTitle();
                } else if (response.code() == 400) Toast.makeText(getContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                else {
                    if (response.body() != null) {
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
                }
                if (oldPosition != -1) {
                    for (int i = oldPosition; i < fullListMarkets.length - 1; i++) {
                        listMarkets[i] = fullListMarkets[i + 1].getTitle();
                    }
                }

                initListMarkets();
            }

            @Override
            public void onFailure(@NotNull Call<MarketTitleResponse> call, @NotNull Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initListMarkets() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.market_item, listMarkets);
        adapter.setDropDownViewResource(R.layout.map_dropdown_text);
        listMarketsSpinner.setAdapter(adapter);

        listMarketsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choosenMarket = listMarkets[position];
                oldPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void handleMarkerTap(Point point) {
        for (Market fullListMarket : fullListMarkets) {
            Point pointMarket = fullListMarket.getPoint();
            if (Math.abs(pointMarket.getLatitude() - point.getLatitude()) < 0.001 &&
                    Math.abs(pointMarket.getLongitude() - point.getLongitude()) < 0.001) {
                Toast.makeText(getContext(), getString(R.string.magazine_toast) + fullListMarket.getTitle(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createRoute() {
        if (isAvailableLocation) {
            Location myLocation = new Location("");
            myLocation.setLatitude(myPoint.getLatitude());
            myLocation.setLongitude(myPoint.getLongitude());

            Point resultPoint = marketPoints.get(0);

            Location resultLocation = new Location("");
            resultLocation.setLatitude(resultPoint.getLatitude());
            resultLocation.setLongitude(resultPoint.getLongitude());

            double minDistance = myLocation.distanceTo(resultLocation);

            for (int i = 1; i < marketPoints.size(); i++) {
                Point intermediatePoint = marketPoints.get(i);

                Location intermediateLocation = new Location("");
                intermediateLocation.setLatitude(intermediatePoint.getLatitude());
                intermediateLocation.setLongitude(intermediatePoint.getLongitude());

                if (myLocation.distanceTo(intermediateLocation) < minDistance) {
                    minDistance = myLocation.distanceTo(intermediateLocation);
                    resultPoint = intermediatePoint;
                }
            }

            DrivingOptions drivingOptions = new DrivingOptions();
            VehicleOptions vehicleOptions = new VehicleOptions();
            ArrayList<RequestPoint> requestPoints = new ArrayList<>();

            requestPoints.add(new RequestPoint(myPoint, RequestPointType.WAYPOINT, null));
            requestPoints.add(new RequestPoint(resultPoint, RequestPointType.WAYPOINT, null));

            this.drivingSession = drivingRouter.requestRoutes(requestPoints, drivingOptions, vehicleOptions, this);
        } else Toast.makeText(requireContext(), getString(R.string.open_location), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDrivingRoutes(@NonNull List<DrivingRoute> list) {
        for (DrivingRoute route: list) {
            mapObjects.addPolyline(route.getGeometry());
        }
    }

    @Override
    public void onDrivingRoutesError(@NonNull Error error) {
        Toast.makeText(getContext(), R.string.error_on_route, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
        if (mapObject instanceof PlacemarkMapObject) {
            handleMarkerTap(point);
            return true;
        }

        return false;
    }

    @Override
    public void onObjectAdded(UserLocationView userLocationView) {
        userLocationLayer.setAnchor(
                new PointF((float)(mapView.getWidth() * 0.5), (float)
                        (mapView.getHeight() * 0.5)),
                new PointF((float)(mapView.getWidth() * 0.5), (float)
                        (mapView.getHeight() * 0.83)));

        userLocationView.getArrow().setIcon(ImageProvider.fromResource(
                requireContext(), R.drawable.person_outline));

        CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();

        pinIcon.setIcon(
                "icon",
                ImageProvider.fromResource(requireContext(), R.drawable.person_outline),
                new IconStyle().setAnchor(new PointF(0f, 0f))
                        .setRotationType(RotationType.ROTATE)
                        .setZIndex(0f)
                        .setScale(1f)
        );

        pinIcon.setIcon(
                "pin",
                ImageProvider.fromResource(requireContext(), R.drawable.person_outline),
                new IconStyle().setAnchor(new PointF(0.5f, 0.5f))
                        .setRotationType(RotationType.ROTATE)
                        .setZIndex(1f)
                        .setScale(0.5f)
        );

    }

    @Override
    public void onObjectRemoved(@NotNull UserLocationView view) {}

    @Override
    public void onObjectUpdated(@NotNull UserLocationView view, @NotNull ObjectEvent event) {}

}