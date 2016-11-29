package com.lazeebear.parkhere.DAOs.ReturnedObjects;

/**
 * Created by Zhicheng on 11/28/16.
 */

public class SpotAddressDateDAO {
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
