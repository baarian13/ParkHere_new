package com.lazeebear.parkhere.DAOs.SentObjects;

/**
 * Created by rjaso on 10/27/2016.
 */

public class RateDAO {

    private int spotID;
    private int rating;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getSpotID() {
        return spotID;
    }

    public void setSpotID(int spotID) {
        this.spotID = spotID;
    }
}
