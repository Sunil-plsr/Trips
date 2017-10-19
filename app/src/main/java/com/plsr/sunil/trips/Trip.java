package com.plsr.sunil.trips;

import java.io.Serializable;
import java.util.ArrayList;


public class Trip implements Serializable {
    String tripID,title,location,imageurl;
    ArrayList<String> wayPoints;
    public Trip(){}

    public Trip(String tripID, String title, String location, String imageurl, ArrayList<String> wayPoints) {
        this.tripID = tripID;
        this.title = title;
        this.location = location;
        this.imageurl = imageurl;
        this.wayPoints = wayPoints;
    }






    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTripID() {
        return tripID;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "tripID='" + tripID + '\'' +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", imageurl='" + imageurl + '\'' +
                '}';
    }
}
