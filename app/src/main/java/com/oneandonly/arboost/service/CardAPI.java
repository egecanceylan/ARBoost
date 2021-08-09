package com.oneandonly.arboost.service;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CardAPI {

    String BASE_URL = "http://springbootawsrdsarboost-env.eba-zxetfam2.us-east-2.elasticbeanstalk.com/";

    @GET("creditcard/{cardNumber}")
    Call<JsonObject> getCard(@Path("cardNumber") String cardNumber);

}
