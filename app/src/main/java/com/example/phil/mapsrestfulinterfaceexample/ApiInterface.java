package com.example.phil.mapsrestfulinterfaceexample;

/**
 * Created by Phil on 19-Nov-16.
 */

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Field;

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

}