package com.lazeebear.parkhere.DAOs.SentObjects;

// acts as a template for an owner when they're creating spots
// there doesn't need to be separate Sent and Received DAOs, since extra details won't be added later.
public class AddressDetailsDAO {
    private int addressID;
    private String address;
    private int spotType;
    private String ownerEmail;
    private int isCovered;
    private String description;
    private String picture;

    // from SpotDetailsDAO, not needed for Address
    //private String start;
    //private String end;
    //private String renterEmail;
    //private int isRecurring;
    //private int cancelationPolicy;
    //private int rating;

    public AddressDetailsDAO(String address, String ownerEmail, String description,
                                 int spotType, int isCovered, String picture) {
        this.address = address;
        this.ownerEmail = ownerEmail;
        this.description = description;
        this.spotType = spotType;
        this.isCovered = isCovered;
        this.picture = picture;
    }

    public AddressDetailsDAO(int addressID, String address, String ownerEmail, String description,
                             int spotType, int isCovered, String picture){
        this(address,ownerEmail,description,spotType,isCovered,picture);
        this.addressID = addressID;
    }

    public int getAddressID() { return addressID; }
    public void setAddressID(int addressID ) { this.addressID = addressID; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getSpotType() { return spotType; }
    public void setSpotType(int spotType) { this.spotType = spotType; }

    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }

    public int getIsCovered() { return isCovered; }
    public void setIsCovered(int isCovered) { this.isCovered = isCovered; }

    public String getDescription() { return description; }
    public void setDescription() { this.description = description; }

    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }
}
