package com.lazeebear.parkhere.DAOs.ReturnedObjects;

/**
 * Created by robert on 11/28/16.
 */

public class AddressDetailsDAO {
    private String address;
    private int spotType;
    private String ownerEmail;
    private int isCovered;
    private String description;
    private String picture;

    public AddressDetailsDAO(String address, int spotType, String ownerEmail, int isCovered, String description, String picture) {
        this.address = address;
        this.spotType = spotType;
        this.ownerEmail = ownerEmail;
        this.isCovered = isCovered;
        this.description = description;
        this.picture = picture;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSpotType() {
        return spotType;
    }

    public void setSpotType(int spotType) {
        this.spotType = spotType;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public int getIsCovered() {
        return isCovered;
    }

    public void setIsCovered(int isCovered) {
        this.isCovered = isCovered;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
