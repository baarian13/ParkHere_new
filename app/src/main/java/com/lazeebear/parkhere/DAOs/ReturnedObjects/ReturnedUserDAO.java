package com.lazeebear.parkhere.DAOs.ReturnedObjects;

import java.util.List;

/**
 * Created by rjaso on 10/27/2016.
 */

public class ReturnedUserDAO {

    private String first;
    private String last;
    private boolean isSeeker;
    private boolean isOwner;
    private int phoneNumber;
    private String email;
    private int rating;
    List<Integer> spots;

    public ReturnedUserDAO(String first, String last, boolean isSeeker, boolean isOwner, int phoneNumber, String email, int rating) {
        this.rating = rating;
        this.first = first;
        this.last = last;
        this.isSeeker = isSeeker;
        this.isOwner = isOwner;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }


    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public boolean isSeeker() {
        return isSeeker;
    }

    public void setSeeker(boolean seeker) {
        isSeeker = seeker;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public List<Integer> getSpots() {
        return spots;
    }

    public void setSpots(List<Integer> spots) {
        this.spots = spots;
    }
}
