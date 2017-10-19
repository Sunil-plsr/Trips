package com.plsr.sunil.trips;

import java.io.Serializable;


public class User implements Serializable{
    String firstName,lastName,imageUrl,userID,gender,email,password;

    public User(){}

    public User(String firstName, String lastName, String imageUrl, String userID, String gender, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUrl = imageUrl;
        this.userID = userID;
        this.gender = gender;
        this.email = email;
        this.password = password;
    }

    //    public void addFriendreq(String s){
//        friendsrequests.add(s);
//    }
//    public void addSentrequets(String s){
//        sentrequests.add(s);
//    }
//
//    public ArrayList<String> getSentrequests() {
//        return sentrequests;
//    }
//
//    public void setSentrequests(ArrayList<String> sentrequests) {
//        this.sentrequests = sentrequests;
//    }
//
//    public ArrayList<String> getFriendsrequests() {
//        return friendsrequests;
//    }
//
//    public void setFriendsrequests(ArrayList<String> friendsrequests) {
//        this.friendsrequests = friendsrequests;
//    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", userID='" + userID + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
