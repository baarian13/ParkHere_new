package com.lazeebear.parkhere.DAOs.SentObjects;

import android.util.Base64;

/**
 * Created by rjaso on 10/27/2016.
 */

public class SentUserDAO {

    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber; // No delimters
    //private Base64 profilePic;
    private boolean seaker;
    private boolean owner;

    public SentUserDAO(String email, String password, String firstName, String lastName, String phoneNumber, boolean seaker, boolean owner) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        //this.profilePic = profilePic;
        this.seaker = seaker;
        this.owner = owner;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirst(String first) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

//    public Base64 getProfilePic() {
//        return profilePic;
//    }

//    public void setProfilePic(Base64 profilePic) {
//        this.profilePic = profilePic;
//    }

    public boolean isSeaker() {
        return seaker;
    }

    public void setSeaker(boolean seaker) {
        this.seaker = seaker;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }
}
