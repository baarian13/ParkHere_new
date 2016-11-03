package com.lazeebear.parkhere.DAOs.ReturnedObjects;

import android.util.Base64;

/**
 * Created by rjaso on 10/27/2016.
 */

public class SpotDAO {

    private float distance;
    private String start;
    private String end;
    private int id;
    private String address;

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
