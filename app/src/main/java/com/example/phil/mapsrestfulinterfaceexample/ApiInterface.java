package com.example.phil.mapsrestfulinterfaceexample;

/**
 * Created by Phil on 19-Nov-16.
 */

import com.example.phil.mapsrestfulinterfaceexample.POJO.Company;
import com.example.phil.mapsrestfulinterfaceexample.POJO.Login;
import com.example.phil.mapsrestfulinterfaceexample.POJO.Review;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Field;
import retrofit2.http.Query;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("/reviewsapp/login.php") //if this doesn't work, check that php isn't echoing anything that might upset the <> bit of Call
    Call<Login> tryLogin(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("/reviewsapp/insert_user.php")
    Call<Login> register(
            @Field("email") String email,
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("username") String username,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("/reviewsapp/add_company.php")
    Call<Company> addCompany(
            @Field("company_name") String company_name,
            @Field("address") String address,
            @Field("lat") double lat,
            @Field("lng") double lng,
            @Field("googleId") String googleId);

    @POST("/reviewsapp/get_all_companies.php")
    Call<List<Company>> getAllCompanies();

    @FormUrlEncoded
    @POST("/reviewsapp/add_review.php")
    Call<Review> addReview(
            @Field("company_ID") String company_ID,
            @Field("user_ID") String user_ID,
            @Field("title") String title,
            @Field("text_body") String text_body,
            @Field("star_rating") double star_rating);

    @FormUrlEncoded
    @POST("/reviewsapp/get_single_company.php")
    Call<Company> getCompany(
            @Field("company_ID") String company_ID
    );

    @FormUrlEncoded
    @POST("/reviewsapp/get_reviews_for_company.php")
    Call<List<Review>> getAllReviewsForCompany(
            @Field("company_ID") String company_ID
    );


}