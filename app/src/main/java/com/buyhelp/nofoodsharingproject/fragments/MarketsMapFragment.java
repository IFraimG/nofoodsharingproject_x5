package com.buyhelp.nofoodsharingproject.fragments;

import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.databinding.FragmentMarketsMapBinding;
import com.buyhelp.nofoodsharingproject.models.Market;
import com.buyhelp.nofoodsharingproject.utils.DefineUser;
import com.buyhelp.nofoodsharingproject.utils.PermissionHandler;
import com.buyhelp.nofoodsharingproject.view_models.MapViewModel;
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
import java.util.List;

public class MarketsMapFragment extends Fragment implements UserLocationObjectListener,
        MapObjectTapListener, DrivingSession.DrivingRouteListener {

    private FragmentMarketsMapBinding binding;
    private MapView mapView;
    private LocationListener locationListener;
    private UserLocationLayer userLocationLayer;
    private MapObjectCollection mapObjects;
    private DrivingRouter drivingRouter;
    private DrivingSession drivingSession;
    private boolean isAvailableLocation = false;
    private LocationManager locationManager;
    private final Animation pingAnimation = new Animation(Animation.Type.SMOOTH, 0);
    private Point myPoint;
    private DefineUser defineUser;
    private MapViewModel viewModel;
    private ArrayAdapter<String> adapter = null;

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

        viewModel = new ViewModelProvider(requireActivity(),
                (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(MapViewModel.class);

        initLocation();
        initMap();
        getPinnedMarketInfo();

        setMarketBtn.setOnClickListener(View -> {
            Pair<String, Boolean> userData = defineUser.getTypeUser();
            viewModel.updateMarket(userData.second, userData.first);
        });
        makeRouteBtn.setOnClickListener(View -> createRoute());

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPinnedMarketInfo();
    }

    private void initLocation() {
        if (PermissionHandler.checkPermissions(requireContext())) {
            locationListener = new LocationListener() {
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
            if (locationListener != null) locationManager.subscribeForLocationUpdates(0, 0, 0, false, FilteringMode.OFF, locationListener);
        }
    }

    private void initMap() {
        PermissionHandler.requestMapPermissions(requireActivity(), requireContext());

        mapView.getMap().setRotateGesturesEnabled(false);

        userLocationLayer = MapKitFactory.getInstance().createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener(this);

        mapView.getMap().move(new CameraPosition(new Point(55.71989101308894, 37.5689757769603), 14, 0, 0), pingAnimation, null);

        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter();
        mapObjects = mapView.getMap().getMapObjects().addCollection();

        ImageProvider imageProvider = ImageProvider.fromResource(requireContext(), R.drawable.location_on2);
        for (Point point : viewModel.getMarketPoints()) {
            PlacemarkMapObject placemark = mapObjects.addPlacemark(point, imageProvider);
            placemark.addTapListener(this);
        }
    }

    private void getPinnedMarketInfo() {
        Pair<String, Boolean> userData = defineUser.getTypeUser();
        String userType = userData.second ? "getter" : "setter";

        viewModel.getPinnedMarketInfo(userType, userData.first).observe(requireActivity(), listMarkets -> {
            if (adapter == null) {
                adapter = new ArrayAdapter<>(getContext(), R.layout.market_item, listMarkets);
                adapter.setDropDownViewResource(R.layout.map_dropdown_text);
                binding.mapListMarkets.setAdapter(adapter);

                if (viewModel.getOldPosition() != -1) binding.mapListMarkets.setSelection(viewModel.getOldPosition() - 1);
            } else {
                if (viewModel.getOldPosition() != -1) binding.mapListMarkets.setSelection(viewModel.getOldPosition() - 1);
                adapter.notifyDataSetChanged();
            }

            binding.mapListMarkets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    viewModel.changePosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        });
    }

    private void handleMarkerTap(Point point) {
        for (Market fullListMarket : viewModel.getFullListMarkets()) {
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

            Point resultPoint = viewModel.getMarketPoints().get(0);

            Location resultLocation = new Location("");
            resultLocation.setLatitude(resultPoint.getLatitude());
            resultLocation.setLongitude(resultPoint.getLongitude());

            double minDistance = myLocation.distanceTo(resultLocation);

            for (int i = 1; i < viewModel.getMarketPoints().size(); i++) {
                Point intermediatePoint = viewModel.getMarketPoints().get(i);

                Location intermediateLocation = new Location("");
                intermediateLocation.setLatitude(intermediatePoint.getLatitude());
                intermediateLocation.setLongitude(intermediatePoint.getLongitude());

                if (myLocation.distanceTo(intermediateLocation) < minDistance) {
                    minDistance = myLocation.distanceTo(intermediateLocation);
                    resultPoint = intermediatePoint;
                }
            }

            ArrayList<RequestPoint> requestPoints = new ArrayList<>();

            requestPoints.add(new RequestPoint(myPoint, RequestPointType.WAYPOINT, null));
            requestPoints.add(new RequestPoint(resultPoint, RequestPointType.WAYPOINT, null));

            this.drivingSession = drivingRouter.requestRoutes(requestPoints, new DrivingOptions(), new VehicleOptions(), this);
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
                requireContext(), R.drawable.accessibility));

        CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();

        pinIcon.setIcon("icon", ImageProvider.fromResource(requireContext(), R.drawable.accessibility),
                new IconStyle().setAnchor(new PointF(0f, 0f))
                        .setRotationType(RotationType.ROTATE)
                        .setZIndex(0f)
                        .setScale(1f)
        );
    }

    @Override
    public void onObjectRemoved(@NotNull UserLocationView view) {}

    @Override
    public void onObjectUpdated(@NotNull UserLocationView view, @NotNull ObjectEvent event) {}

}