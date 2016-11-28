package com.lazeebear.parkhere.DAOs.ReturnedObjects;

/**
 * Created by robert on 11/27/16.
 */

public class SpotDateDAO {
    private String start;
    private String end;
    private int id;
    private String address;

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
