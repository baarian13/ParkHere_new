package com.lazeebear.parkhere.DAOs.ReturnedObjects;

/**
 * Created by rjaso on 10/27/2016.
 */

public class ReturnedUserDAO {

    private String username;
    private String email;
    private int rating;
    private int phoneNumber;

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
