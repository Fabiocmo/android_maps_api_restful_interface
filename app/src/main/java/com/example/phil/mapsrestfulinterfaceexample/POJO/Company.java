package com.example.phil.mapsrestfulinterfaceexample.POJO;

/**
 * Created by Phil on 27-Nov-16.
 */

public class Company {

    private String company_ID;
    private String company_name;
    private double lat;
    private double lng;
    private String address;
    private String googleId;
    private double avg_rating;

    public String getCompany_ID() {
        return company_ID;
    }

    public void setCompanyID(String company_ID) {
        this.company_ID = company_ID;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public double getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(double avg_rating) {
        this.avg_rating = avg_rating;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }


}
