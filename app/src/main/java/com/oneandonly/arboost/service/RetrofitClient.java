package com.oneandonly.arboost.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient instance = null;
    private CardAPI cardAPI;
    private RetrofitClient(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(cardAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        cardAPI = retrofit.create(CardAPI.class);
    }
    public static synchronized RetrofitClient getInstances(){
        if(instance == null){
            instance = new RetrofitClient();
        }
        return instance;
    }
    public CardAPI getCardAPI(){
        return cardAPI;
    }
}
