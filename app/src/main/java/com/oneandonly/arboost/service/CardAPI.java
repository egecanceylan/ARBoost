package com.oneandonly.arboost.service;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CardAPI {

    String BASE_URL = "https://api.agify.io/";

    @GET("?name=bella")
    Call<JsonObject> getCard();

}
