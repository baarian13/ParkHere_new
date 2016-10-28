package com.lazeebear.parkhere.DAOs.SentObjects;

/**
 * Created by rjaso on 10/27/2016.
 */

public class BookDAO {

    private int spotID;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSpotID() {
        return spotID;
    }

    public void setSpotID(int spotID) {
        this.spotID = spotID;
    }
}
