package com.example.phil.mapsrestfulinterfaceexample.POJO;

/**
 * Created by Phil on 19-Nov-16.
 */

public class Login {
    private String first_name;
    private String last_name;
    private String email;
    private String id;
    private String password;
    private String reviews_written;

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public String getReviews_written() {
        return reviews_written;
    }

    public void setReviews_written(String reviews_written) {
        this.reviews_written = reviews_written;
    }
}
