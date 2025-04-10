package com.edufun.music;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient mInstance;
    private Retrofit retrofit;
    private String BASE_URL ="https://spotify23.p.rapidapi.com/";


    public RetrofitClient(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
    public static synchronized RetrofitClient getInstance(){
        if (mInstance==null){
            mInstance=new RetrofitClient();
        }
        return mInstance;
    }
    public WebService searchApiData(){
        return retrofit.create(WebService.class);
    }
}
