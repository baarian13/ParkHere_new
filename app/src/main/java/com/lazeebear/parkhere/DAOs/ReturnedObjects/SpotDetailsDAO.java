package com.lazeebear.parkhere.DAOs.ReturnedObjects;

/**
 * Created by rjaso on 10/31/2016.
 */

public class SpotDetailsDAO {

    private String address;
    private String start;
    private String end;
    private int spotType;
    private String ownerEmail;
    private String renterEmail;
    private int isRecurring;
    private int isCovered;
    private int cancelationPolicy;
    private String description;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    public String getRenterEmail() {
        return renterEmail;
    }

    public void setRenterEmail(String renterEmail) {
        this.renterEmail = renterEmail;
    }

    public int getIsRecurring() {
        return isRecurring;
    }

    public void setIsRecurring(int isRecurring) {
        this.isRecurring = isRecurring;
    }

    public int getIsCovered() {
        return isCovered;
    }

    public void setIsCovered(int isCovered) {
        this.isCovered = isCovered;
    }

    public int getCancelationPolicy() {
        return cancelationPolicy;
    }

    public void setCancelationPolicy(int cancelationPolicy) {
        this.cancelationPolicy = cancelationPolicy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
