package com.plsr.sunil.trips;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.plsr.sunil.trips.RoundTrip.DirectionFinder;
import com.plsr.sunil.trips.RoundTrip.DirectionFinderListener;
import com.plsr.sunil.trips.RoundTrip.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {

    private GoogleMap mMap;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
//    String origin = "Boston";
//    String destination = "Charlestown";
//
    Trip t;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng charlotte = new LatLng(35.2033533,-80.9799146);
//        mMap.addMarker(new MarkerOptions().position(charlotte).title("Charlotte"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(charlotte,6));

        t = (Trip) getIntent().getSerializableExtra("trip");

        String waypoints = "";

        if (t.wayPoints != null){
            for (String s : t.wayPoints){
                waypoints = waypoints + s;
                if (s != t.wayPoints.get(t.wayPoints.size() - 1))
                    waypoints = waypoints + "|";
            }

        }

//        waypoints = waypoints + "|" + t.location;

        Log.d("demo", waypoints);


        try {
            new DirectionFinder(this, t.getLocation(), waypoints).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }






    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
//            Log.d("demo", "StartAddress:"+route.startAddress+"End Address"+route.endAddress);
//            Log.d("demo",route.points.toString());
//            Log.d("demo",route.startLocation+" "+route.endLocation);
            Log.d("demo","\n"+route.startLocation.toString()+"\n"+route.endLocation.toString());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation.get(0), 16));
//            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
//            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

//            originMarkers.add(mMap.addMarker(new MarkerOptions()
////                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))
//                    .title(route.startAddress)
//                    .position(route.startLocation.get(2))));
//            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
////                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))
//                    .title(route.endAddress)
//                    .position(route.endLocation.get(2))));


            int ii =0;
            for (LatLng loc : route.startLocation){
                originMarkers.add(mMap.addMarker(new MarkerOptions()
                .position(route.endLocation.get(ii))));

                ii++;
            }

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);



            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));



            //code to set map bounds
            LatLng NE = new LatLng(route.nela, route.nelo);
            LatLng SW = new LatLng(route.swla, route.swlo);

            LatLngBounds llb = new LatLngBounds(SW, NE);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.endLocation.get(0),6));
            mMap.setLatLngBoundsForCameraTarget(llb);
        }






    }
}
