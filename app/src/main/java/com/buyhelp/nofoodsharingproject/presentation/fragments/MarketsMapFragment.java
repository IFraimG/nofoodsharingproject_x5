/**
 * Класс {@code MarketsMapFragment} - фрагмент яндекс карты
 * @author Кулагин Александр
 */


package com.buyhelp.nofoodsharingproject.presentation.fragments;

import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.data.api.map.MapRepository;
import com.buyhelp.nofoodsharingproject.databinding.FragmentMarketsMapBinding;
import com.buyhelp.nofoodsharingproject.data.models.Market;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.domain.helpers.PermissionHandler;
import com.buyhelp.nofoodsharingproject.domain.utils.CustomLocation;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.buyhelp.nofoodsharingproject.presentation.di.components.DaggerPermissionComponent;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.ActivityModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.PermissionHandlerModule;
import com.buyhelp.nofoodsharingproject.presentation.factories.MapFactory;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.MapViewModel;
import com.google.android.material.snackbar.Snackbar;
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MarketsMapFragment extends Fragment implements UserLocationObjectListener,
        MapObjectTapListener, DrivingSession.DrivingRouteListener {

    private FragmentMarketsMapBinding binding;
    private WeakReference<FragmentMarketsMapBinding> mBinding;
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
    private MapRepository mapRepository;
    private PermissionHandler permissionHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissionHandler = DaggerPermissionComponent.builder().defineActivity(new ActivityModule(requireActivity())).definePermissions(new PermissionHandlerModule()).build().getPermissionHandler();
        if (permissionHandler != null) permissionHandler.requestMapPermissions((AppCompatActivity) requireActivity());

        SearchFactory.initialize(requireContext());
        DirectionsFactory.initialize(requireContext());

        ApplicationCore app = (ApplicationCore) requireActivity().getApplication();
        mapRepository = app.getAppComponent().getMapRepository();
        defineUser = app.getHelpersComponent().getDefineUser();
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
        mBinding = new WeakReference<>(binding);

        viewModel = new ViewModelProvider(requireActivity(),
                new MapFactory(requireActivity().getApplication(), mapRepository))
                .get(MapViewModel.class);

        this.mapView = binding.mapview;

        if (permissionHandler != null && permissionHandler.checkPermissions((AppCompatActivity) requireActivity())) {
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

        initMap();
        getPinnedMarketInfo();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPinnedMarketInfo();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding.clear();

        if (mapView != null) mapView.getMap().getMapObjects().clear();
    }

    /**
     * Инициализация параметров отображения яндекс карты
     * Отображение меток магазинов на карте
     * Отображение метки пользователя по умолчанию
     * Запрос на включение геолокации
     */
    private void initMap() {
        if (permissionHandler != null) permissionHandler.requestMapPermissions((AppCompatActivity) requireActivity());

        mapView.getMap().setRotateGesturesEnabled(false);

        userLocationLayer = MapKitFactory.getInstance().createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener(this);
        mapView.getMap().setRotateGesturesEnabled(false);

        mapView.getMap().move(new CameraPosition(new Point(55.71989101308894, 37.5689757769603), 14, 0, 0), pingAnimation, null);

        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter();
        mapObjects = mapView.getMap().getMapObjects().addCollection();

        ImageProvider imageProvider = ImageProvider.fromResource(requireContext(), R.drawable.location_on2);
        for (Point point : viewModel.getMarketPoints()) {
            PlacemarkMapObject placemark = mapObjects.addPlacemark(point, imageProvider);
            placemark.addTapListener(this);
        }
    }

    /**
     * Получение информации о прикрепленном магазине
     */
    private void getPinnedMarketInfo() {
        binding.mapSetMarketBtn.setOnClickListener(View -> viewModel.updateMarket(defineUser.getTypeUser().second, defineUser.getTypeUser().first));
        binding.mapMakeRoute.setOnClickListener(View -> createRoute());


        Pair<String, Boolean> userData = defineUser.getTypeUser();
        String userType = userData.second ? "needy" : "giver";

        viewModel.getPinnedMarketInfo(userType, userData.first).observe(requireActivity(), listMarkets -> {

            if (adapter == null) {
                adapter = new ArrayAdapter<>(requireContext(), R.layout.market_item, listMarkets);
                adapter.setDropDownViewResource(R.layout.map_dropdown_text);
                binding.mapListMarkets.setAdapter(adapter);

                if (viewModel.getOldPosition() != -1) binding.mapListMarkets.setSelection(viewModel.getOldPosition());
            } else {
                if (viewModel.getOldPosition() != -1) binding.mapListMarkets.setSelection(viewModel.getOldPosition());
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

    /**
     * Этот метод отображает название магазина
     * @param point координаты магазина
     */
    private void handleMarkerTap(Point point) {
        for (Market fullListMarket : viewModel.getFullListMarkets()) {
            Point pointMarket = fullListMarket.getPoint();
            if (Math.abs(pointMarket.getLatitude() - point.getLatitude()) < 0.001 &&
                    Math.abs(pointMarket.getLongitude() - point.getLongitude()) < 0.001) {
                Snackbar.make(requireContext(), requireView(), getString(R.string.magazine_toast) + fullListMarket.getTitle(), Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Этот метод строит маршрут до ближайшего магазина
     */
    private void createRoute() {
        if (isAvailableLocation) {
            Location myLocation = new CustomLocation(myPoint.getLatitude(), myPoint.getLongitude()).getLocation();
            Point resultPoint = viewModel.getMarketPoints().get(0);

            double minDistance = myLocation.distanceTo(new CustomLocation(resultPoint.getLatitude(), resultPoint.getLongitude()).getLocation());

            for (int i = 1; i < viewModel.getMarketPoints().size(); i++) {
                Point intermediatePoint = viewModel.getMarketPoints().get(i);

                Location intermediateLocation = new CustomLocation(intermediatePoint.getLatitude(), intermediatePoint.getLongitude()).getLocation();

                if (myLocation.distanceTo(intermediateLocation) < minDistance) {
                    minDistance = myLocation.distanceTo(intermediateLocation);
                    resultPoint = intermediatePoint;
                }
            }

            ArrayList<RequestPoint> requestPoints = new ArrayList<>();

            requestPoints.add(new RequestPoint(myPoint, RequestPointType.WAYPOINT, null));
            requestPoints.add(new RequestPoint(resultPoint, RequestPointType.WAYPOINT, null));

            this.drivingSession = drivingRouter.requestRoutes(requestPoints, new DrivingOptions(), new VehicleOptions(), this);
        } else Snackbar.make(requireContext(), requireView(), getString(R.string.open_location), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDrivingRoutes(@NonNull List<DrivingRoute> list) {
        for (DrivingRoute route: list) {
            mapObjects.addPolyline(route.getGeometry());
        }
    }

    @Override
    public void onDrivingRoutesError(@NonNull Error error) {
        Snackbar.make(requireContext(), requireView(), getString(R.string.error_on_route), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
        if (mapObject instanceof PlacemarkMapObject) {
            handleMarkerTap(point);
            return true;
        }

        return false;
    }

    /**
     * Этот метод отображает метку пользователя
     */
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