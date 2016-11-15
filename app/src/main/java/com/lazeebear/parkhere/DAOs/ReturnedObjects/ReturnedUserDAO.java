package com.lazeebear.parkhere.DAOs.ReturnedObjects;

import java.util.List;

/**
 * Created by rjaso on 10/27/2016.
 */

public class ReturnedUserDAO {

    private String first;
    private String last;
    private int isSeeker;
    private int isOwner;
    private String phoneNumber;
    private String email;
    private int rating;
    List<Integer> spots;
    private String picture;
    private int isVerified;


    public ReturnedUserDAO(String first, String last, int isSeeker, int isOwner, String phoneNumber, String email, int rating, int isVerified, String picture) {
        this.rating = rating;
        this.first = first;
        this.last = last;
        this.isSeeker = isSeeker;
        this.isOwner = isOwner;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.isVerified = isVerified;

        this.picture = picture;
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

    public int isSeeker() {
        return isSeeker;
    }

    public void setSeeker(int seeker) {
        isSeeker = seeker;
    }

    public int isOwner() {
        return isOwner;
    }

    public void setOwner(int owner) {
        isOwner = owner;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(int isVerified) {
        this.isVerified = isVerified;
    }
}

