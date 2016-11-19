package com.lazeebear.parkhere.DAOs.SentObjects;

import android.util.Base64;

/**
 * Created by rjaso on 10/27/2016.
 */

public class SentSpotDAO {

    private String address;
    private String picture;
    private String startTime;
    private String endTime;
    private String description;
    private String price;
    private int spotType;
    private int isCovered;
    private int cancellationPolicy;
    private int isRecurring;

    public SentSpotDAO(String address, String startTime, String endTime, String picture, String description, String price, int spotType, int isCovered, int cancellationPolicy, int isRecurring) {
        this.address = address;
        this.startTime = startTime;
        this.endTime = endTime;
        this.picture = picture;
        this.description = description;
        this.price = price;
        this.spotType = spotType;
        this.isCovered = isCovered;
        this.cancellationPolicy = cancellationPolicy;
        this.isRecurring = isRecurring;
    }

    public int getSpotType() {
        return spotType;
    }

    public void setSpotType(int spotType) {
        this.spotType = spotType;
    }

    public int isCovered() {
        return isCovered;
    }

    public void setCovered(int covered) {
        isCovered = covered;
    }

    public int getCancellationPolicy() {
        return cancellationPolicy;
    }

    public void setCancellationPolicy(int cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
    }

    public int isRecurring() {
        return isRecurring;
    }

    public void setRecurring(int recurring) {
        isRecurring = recurring;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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
}
