package com.plsr.sunil.trips;

import java.util.ArrayList;
import java.util.Date;


public class Chat {
    String message,userid,imageurl,username;
    Date date;
    ArrayList<String> deletedFor;
    public Chat(){}

    public Chat(String message, String userid, String imageurl, String username, Date date, ArrayList<String> deletedFor) {
        this.message = message;
        this.userid = userid;
        this.imageurl = imageurl;
        this.username = username;
        this.date = date;
        this.deletedFor = deletedFor;
    }


    public ArrayList<String> getDeletedFor() {
        return deletedFor;
    }

    public void setDeletedFor(ArrayList<String> deletedFor) {
        this.deletedFor = deletedFor;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "message='" + message + '\'' +
                ", userid='" + userid + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", username='" + username + '\'' +
                ", date=" + date +
                '}';
    }
}
