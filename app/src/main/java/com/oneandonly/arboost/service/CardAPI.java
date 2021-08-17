package com.oneandonly.arboost.service;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CardAPI {

    String BASE_URL = "http://springbootawsrdsarboost-env.eba-zxetfam2.us-east-2.elasticbeanstalk.com/";

    @GET("card/info/{card_number}")
    Call<JsonObject> getCard(@Path("card_number") String cardNumber, @Query("id") Integer id);

}
