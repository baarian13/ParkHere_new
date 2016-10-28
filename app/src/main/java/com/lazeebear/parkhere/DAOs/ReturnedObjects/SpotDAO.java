package com.lazeebear.parkhere.DAOs.ReturnedObjects;

import android.util.Base64;

/**
 * Created by rjaso on 10/27/2016.
 */

public class SpotDAO {

    private int spotID;
    private String address;
    private int latitude;
    private int longitude;
    private int ownerRating;
    private Base64 picture;
    private String phoneNumber;
    private String startTime;
    private String endTime;
    private String description;
    private float price;
    private int rating;

    public int getSpotID() {
        return spotID;
    }

    public void setSpotID(int spotID) {
        this.spotID = spotID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getOwnerRating() {
        return ownerRating;
    }

    public void setOwnerRating(int ownerRating) {
        this.ownerRating = ownerRating;
    }

    public Base64 getPicture() {
        return picture;
    }

    public void setPicture(Base64 picture) {
        this.picture = picture;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
