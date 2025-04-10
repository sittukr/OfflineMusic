package com.edufun.music;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface WebService {


    @Headers({"x-rapidapi-key: 952419c26amsh0bfa047b76fd3d7p142efajsn5afe4a59446c",
            "x-rapidapi-host: spotify23.p.rapidapi.com"})
    @GET("search")
    Call<JsonObject> searchApiData(
            @Query("q") String q,
            @Query("type")String type
    );
}
