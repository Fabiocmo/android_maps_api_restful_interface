package com.example.phil.mapsrestfulinterfaceexample.POJO;

/**
 * Created by Phil on 29-Nov-16.
 */

public class Review {

    private String company_ID;
    private String user_ID;
    private String review_ID;
    private String title;
    private String submission_date;
    private String text_body;
    private double star_rating;

    public String getCompany_ID() {
        return company_ID;
    }

    public void setCompany_ID(String company_ID) {
        this.company_ID = company_ID;
    }

    public String getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubmission_date() {
        return submission_date;
    }

    public void setSubmission_date(String submission_date) {
        this.submission_date = submission_date;
    }

    public String getText_body() {
        return text_body;
    }

    public void setText_body(String text_body) {
        this.text_body = text_body;
    }

    public double getStar_rating() {
        return star_rating;
    }

    public void setStar_rating(double star_rating) {
        this.star_rating = star_rating;
    }

    public String getReview_ID() {
        return review_ID;
    }

    public void setReview_ID(String review_ID) {
        this.review_ID = review_ID;
    }
}
