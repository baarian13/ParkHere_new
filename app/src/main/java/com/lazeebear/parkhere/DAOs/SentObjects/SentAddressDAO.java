package com.lazeebear.parkhere.DAOs.SentObjects;

/**
 * Created by robert on 11/28/16.
 */

public class SentAddressDAO {
    private int addressID;
    private String address;
    private String picture;
    private String description;
    private String email;
    private int spotType;
    private int isCovered;

    public SentAddressDAO(int addressID, String address, String picture, String description, String email, int spotType, int isCovered) {
        this.addressID = addressID;
        this.address = address;
        this.picture = picture;
        this.description = description;
        this.email = email;
        this.spotType = spotType;
        this.isCovered = isCovered;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSpotType() {
        return spotType;
    }

    public void setSpotType(int spotType) {
        this.spotType = spotType;
    }

    public int getIsCovered() {
        return isCovered;
    }

    public void setIsCovered(int isCovered) {
        this.isCovered = isCovered;
    }
}
