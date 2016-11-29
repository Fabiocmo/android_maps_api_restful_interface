package com.example.phil.mapsrestfulinterfaceexample;

/**
 * Created by Phil on 19-Nov-16.
 */

import com.example.phil.mapsrestfulinterfaceexample.POJO.Company;
import com.example.phil.mapsrestfulinterfaceexample.POJO.Login;
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
    @POST("/reviewsapp/insert_user.php") //if this doesn't work, check that php isn't echoing anything that might upset the <> bit of Call
    Call<Login> register(
            @Field("email") String email,
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("username") String username,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("/reviewsapp/add_company.php") //if this doesn't work, check that php isn't echoing anything that might upset the <> bit of Call
    Call<Company> addCompany(
            @Field("company_name") String company_name,
            @Field("address") String address,
            @Field("lat") double lat,
            @Field("lng") double lng,
            @Field("googleId") String googleId);

    @POST("/reviewsapp/get_all_companies.php")
    Call<List<Company>> getAllCompanies();

}