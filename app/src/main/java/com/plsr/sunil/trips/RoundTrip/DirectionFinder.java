package com.plsr.sunil.trips.RoundTrip;


import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by sunil on 4/28/17.
 */

//directions api
//AIzaSyB6JFM9sbbBgDm7ilzAwD9kWMiO9G0uFHg

public class DirectionFinder {
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyB6JFM9sbbBgDm7ilzAwD9kWMiO9G0uFHg";
    private DirectionFinderListener listener;
    private String origin;
    private String destination;

    public DirectionFinder(DirectionFinderListener listener, String origin, String destination) {
        this.listener = listener;
        this.origin = origin;
        this.destination = destination;
    }

    public void execute() throws UnsupportedEncodingException {
        listener.onDirectionFinderStart();
        new DownloadRawData().execute(createUrl());
    }

    private String createUrl() throws UnsupportedEncodingException {
        String urlOrigin = URLEncoder.encode(origin, "utf-8");
        String urlWaypoints = URLEncoder.encode(destination, "utf-8");
//&waypoints=Charlestown,MA|Lexington,MA
//        With a waypoint
        return DIRECTION_URL_API + "origin=" + urlOrigin + "&destination=" + urlOrigin+ "&waypoints="+ urlWaypoints + "&key=" + GOOGLE_API_KEY;
//        return DIRECTION_URL_API + "origin=" + urlOrigin + "&destination=" + urlDestination+ "&key=" + GOOGLE_API_KEY;
    }

    private class DownloadRawData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                parseJSon(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseJSon(String data) throws JSONException {
        if (data == null)
            return;

        List<Route> routes = new ArrayList<Route>();
        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonRoutes = jsonData.getJSONArray("routes");
        JSONArray jsonWayPoints = jsonData.getJSONArray("geocoded_waypoints");

        for (int i = 0; i < jsonRoutes.length(); i++) {
            JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
            Route route = new Route();



            JSONObject overview_polylineJson = jsonRoute.getJSONObject("overview_polyline");

            //for bounds
            JSONObject jsonBounds = jsonRoute.getJSONObject("bounds");
            JSONObject jsonNE = jsonBounds.getJSONObject("northeast");
            JSONObject jsonSW = jsonBounds.getJSONObject("southwest");

            route.swla = jsonSW.getLong("lat");
            route.swlo = jsonSW.getLong("lng");
            route.nela = jsonNE.getLong("lat");
            route.nelo = jsonNE.getLong("lng");







            JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
            JSONObject jsonLeg = jsonLegs.getJSONObject(0);
            JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
            JSONObject jsonDuration = jsonLeg.getJSONObject("duration");

            route.endLocation = new ArrayList<>();
            route.startLocation = new ArrayList<>();


            for (int h=0; h<jsonLegs.length(); h++){
                JSONObject leg = jsonLegs.getJSONObject(h);
                JSONObject jsonEndLocation = leg.getJSONObject("end_location");
                JSONObject jsonStartLocation = leg.getJSONObject("start_location");
                route.startLocation.add(new LatLng(jsonStartLocation.getDouble("lat"), jsonStartLocation.getDouble("lng")));
                route.endLocation.add(new LatLng(jsonEndLocation.getDouble("lat"), jsonEndLocation.getDouble("lng")));
            }



            route.distance = new Distance(jsonDistance.getString("text"), jsonDistance.getInt("value"));
            route.duration = new Duration(jsonDuration.getString("text"), jsonDuration.getInt("value"));
            route.endAddress = jsonLeg.getString("end_address");
            route.startAddress = jsonLeg.getString("start_address");

            route.points = decodePolyLine(overview_polylineJson.getString("points"));

            routes.add(route);
        }

        listener.onDirectionFinderSuccess(routes);
    }

    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }
}