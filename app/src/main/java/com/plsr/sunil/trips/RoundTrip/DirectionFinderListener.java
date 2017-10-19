package com.plsr.sunil.trips.RoundTrip;


import java.util.List;


/**
 * Created by sunil on 4/28/17.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
