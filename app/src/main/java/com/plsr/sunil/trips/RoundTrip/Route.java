package com.plsr.sunil.trips.RoundTrip;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunil on 4/28/17.
 */

public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public ArrayList<LatLng> endLocation;
    public String startAddress;
    public ArrayList<LatLng> startLocation;

    public long swla,swlo,nela,nelo;

    public List<LatLng> points;



}
