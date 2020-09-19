package com.heremap.heremapfragment.presenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.routing.CoreRouter;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;
import com.here.android.mpa.routing.RouteWaypoint;
import com.here.android.mpa.routing.Router;
import com.here.android.mpa.routing.RoutingError;
import com.heremap.heremapfragment.R;
import com.heremap.heremapfragment.mylocation.GPSLocation;
import com.heremap.heremapfragment.product.Product;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FragmentHereMapView {
    AppCompatActivity activity;

    AndroidXMapFragment mapFragment;
    Map map;
    MapRoute mapRoute;
    MapMarker markerLocation;
    List<MapMarker> mapMarkers;
    int codeProduct = 0;

    GPSLocation gpsLocation;

    public FragmentHereMapView(AppCompatActivity activity) {
        this.activity = activity;
        initMapFragment();
    }

    private AndroidXMapFragment getMapFragment() {
        return (AndroidXMapFragment) activity.getSupportFragmentManager().findFragmentById(R.id.mapFragment);
    }

    public void initMapFragment() {
        /* Locate the mapFragment UI element */
        mapFragment = getMapFragment();
        gpsLocation = new GPSLocation(activity);

        // This will use external storage to save map cache data, it is also possible to set
        // private app's path
        String path = new File(activity.getExternalFilesDir(null), ".here-map-data")
                .getAbsolutePath();
        // This method will throw IllegalArgumentException if provided path is not writable
        com.here.android.mpa.common.MapSettings.setDiskCacheRootPath(path);

        if (mapFragment != null) {
            /* Initialize the AndroidXMapFragment, results will be given via the called back. */
            mapFragment.init(new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                    if (error == Error.NONE) {
                        map = mapFragment.getMap();
                        map.setCenter(new GeoCoordinate(gpsLocation.getLatitude(), gpsLocation.getLongitude()), Map.Animation.LINEAR);
//                        map.setCenter(new GeoCoordinate(21.052862, 105.739858), Map.Animation.LINEAR);
                        map.setZoomLevel(13.5);
//                        addMarkerOnMyMap(gpsLocation.getLatitude(), gpsLocation.getLongitude(), R.drawable.poi);
                        if (markerLocation != null) {
                            map.removeMapObject(markerLocation);
                        }
                        markerLocation = createMarker(gpsLocation.getLatitude(), gpsLocation.getLongitude(), R.drawable.marker_mylocation);
                        map.addMapObject(markerLocation);
//                        map.getPositionIndicator().setVisible(true);
//                        map.getPositionIndicator().setAccuracyIndicatorVisible(true);
//                        map.getPositionIndicator().setZIndex(0);
                    }
                }
            });
        }
    }
    public void setMyLocation() {
        gpsLocation = new GPSLocation(activity);
        if(markerLocation != null) {
            removeMyMapObject(markerLocation);
        }
        markerLocation = createMarker(gpsLocation.getLatitude(),gpsLocation.getLongitude(),R.drawable.marker_mylocation);
        addMyMapObject(markerLocation, null, new GeoCoordinate(gpsLocation.getLatitude(), gpsLocation.getLongitude()));
        if (mapRoute != null) {
            GeoCoordinate destination = mapRoute.getRoute().getDestination();
            removeMyRoute();
            createRoute(destination);
        }
    }
    public void addMyRoute(Product product) {
        removeMyRoute();
        createRoute(new GeoCoordinate(product.getLatitude(),product.getLongitude()));
    }
    public void removeMyRoute() {
        if (mapRoute != null) {
            removeMyMapObject(mapRoute);
            mapRoute = null;
        }
    }
    private void createRoute(GeoCoordinate coordinate) {
        gpsLocation = new GPSLocation(activity);
        /* Initialize a CoreRouter */
        CoreRouter coreRouter = new CoreRouter();

        /* Initialize a RoutePlan */
        RoutePlan routePlan = new RoutePlan();

        /*
         * Initialize a RouteOption. HERE Mobile SDK allow users to define their own parameters for the
         * route calculation,including transport modes,route types and route restrictions etc.Please
         * refer to API doc for full list of APIs
         */
        RouteOptions routeOptions = new RouteOptions();
        /* Other transport modes are also available e.g Pedestrian */
        routeOptions.setTransportMode(RouteOptions.TransportMode.SCOOTER);
        /* Disable highway in this route. */
        routeOptions.setHighwaysAllowed(false);
        /* Calculate the shortest route available. */
        routeOptions.setRouteType(RouteOptions.Type.SHORTEST);
        /* Calculate 1 route. */
        routeOptions.setRouteCount(1);
        /* Finally set the route option */
        routePlan.setRouteOptions(routeOptions);

//        RouteWaypoint startPoint = new RouteWaypoint(new GeoCoordinate(21.052862, 105.739858));
        RouteWaypoint startPoint = new RouteWaypoint(new GeoCoordinate(gpsLocation.getLatitude(), gpsLocation.getLongitude()));

        RouteWaypoint destination = new RouteWaypoint(coordinate);
        routePlan.addWaypoint(startPoint);
        routePlan.addWaypoint(destination);
        coreRouter.calculateRoute(routePlan, new Router.Listener<List<RouteResult>, RoutingError>() {
            @Override
            public void onProgress(int i) {
            }

            @Override
            public void onCalculateRouteFinished(@Nullable List<RouteResult> routeResults, @NonNull RoutingError routingError) {
                if (routingError == RoutingError.NONE) {
                    if (routeResults.get(0).getRoute() != null) {
                        mapRoute = new MapRoute(routeResults.get(0).getRoute());
                        mapRoute.setManeuverNumberVisible(true);
//                        product.setLength(routeResults.get(0).getRoute().getLength());
//                        product.setTime(routeResults.get(0).getRoute().getTtaExcludingTraffic(Route.WHOLE_ROUTE).getDuration());
                        GeoBoundingBox box = routeResults.get(0).getRoute().getBoundingBox();
                        addMyMapObject(mapRoute, box, null);
                    }
                }
            }
        });
    }

    public void addListMarkerProductOnMyMap(List<Product> products) {
        mapMarkers = new ArrayList<>();
        codeProduct = products.get(0).getCode();
        int markerIcon = 0;
        if(codeProduct == 0) {
            markerIcon = R.drawable.marker_rice;
        }
        if(codeProduct == 1) {
            markerIcon = R.drawable.marker_noodle;
        }
        if(codeProduct == 2) {
            markerIcon = R.drawable.marker_snack;
        }
        if(codeProduct == 3) {
            markerIcon = R.drawable.marker_milk_tea;
        }
        for (int i = 0; i < products.size(); i++) {
            MapMarker marker = createMarker(products.get(i).getLatitude(), products.get(i).getLongitude(), markerIcon);
            mapMarkers.add(marker);
            addMyMapObject(mapMarkers.get(i), null, null);
        }
    }
    public void removeListMarkerProductOnMyMap() {
        for(MapMarker marker : mapMarkers) {
            if(marker != null) {
                removeMyMapObject(marker);
            }
        }
    }
    public void addMarkerProductSelected(int position, boolean typeMarker, int sizeList) {
        addMarkerProductSelectedHelper(position, true);
        if(typeMarker == true) {
            if (position == 0) {
                if(sizeList > 1) {
                    addMarkerProductSelectedHelper(position + 1, false);
                }
            } else if (position == (mapMarkers.size() - 1)) {
                addMarkerProductSelectedHelper(position - 1, false);
            } else {
                addMarkerProductSelectedHelper(position + 1, false);
                addMarkerProductSelectedHelper(position - 1, false);
            }
        }
    }

    private void addMarkerProductSelectedHelper(int position, boolean typePosition) {
        removeMyMapObject(mapMarkers.get(position));
        Double latitude = mapMarkers.get(position).getCoordinate().getLatitude();
        Double longitude = mapMarkers.get(position).getCoordinate().getLongitude();
        MapMarker marker = null;
        int markerIcon = 0;
        int markerIconSelected = 0;
        if(codeProduct == 0) {
            markerIcon = R.drawable.marker_rice;
            markerIconSelected = R.drawable.marker_rice_selected;
        }
        if(codeProduct == 1) {
            markerIcon = R.drawable.marker_noodle;
            markerIconSelected = R.drawable.marker_noodle_selected;
        }
        if(codeProduct == 2) {
            markerIcon = R.drawable.marker_snack;
            markerIconSelected = R.drawable.marker_snack_selected;
        }
        if(codeProduct == 3) {
            markerIcon = R.drawable.marker_milk_tea;
            markerIconSelected = R.drawable.marker_milk_tea_selected;
        }
        if (typePosition == true) {
            marker = createMarker(latitude, longitude, markerIconSelected);
        } else {
            marker = createMarker(latitude, longitude, markerIcon);
        }
        mapMarkers.set(position, marker);
        if (typePosition == true) {
            addMyMapObject(mapMarkers.get(position), null, new GeoCoordinate(latitude, longitude));
        } else {
            addMyMapObject(mapMarkers.get(position), null, null);
        }
    }

    private MapMarker createMarker(Double latitude, Double longitude, int poi_marker) {
        Image marker_img = new Image();
        try {
            marker_img.setImageResource(poi_marker);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MapMarker marker = new MapMarker(new GeoCoordinate(latitude, longitude), marker_img);
        return marker;
    }

    private void addMyMapObject(MapObject mapObject, GeoBoundingBox box, GeoCoordinate coordinate) {
        if (mapFragment != null) {
            /* Initialize the AndroidXMapFragment, results will be given via the called back. */
            mapFragment.init(new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(Error error) {
                    if (error == Error.NONE) {
                        if (mapObject != null) {
                            map.addMapObject(mapObject);
                        }
                        if (box != null) {
                            map.zoomTo(box, Map.Animation.BOW, Map.MOVE_PRESERVE_ORIENTATION);
                        }
                        if (coordinate != null) {
                            map.setCenter(coordinate, Map.Animation.BOW);
                            map.setZoomLevel(13.5);
                        }
                    }
                }
            });
        }
    }

    private void removeMyMapObject(MapObject mapObject) {
        if (mapFragment != null) {
            /* Initialize the AndroidXMapFragment, results will be given via the called back. */
            mapFragment.init(new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(Error error) {
                    if (error == Error.NONE) {
                        if(mapObject != null) {
                            map.removeMapObject(mapObject);
                        }
                    }
                }
            });
        }
    }

    public void setMapType(String type) {
        if (mapFragment != null) {
            /* Initialize the AndroidXMapFragment, results will be given via the called back. */
            mapFragment.init(new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(Error error) {
                    if (error == Error.NONE) {
                        map.setMapScheme(type);
                    }
                }
            });
        }
    }
}
