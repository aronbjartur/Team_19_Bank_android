package com.example.team19bank;

//allt ai
//ekki byrjað að nota því þetta er networking dæmi

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BankApi {
    @POST("signup")
    Call<ResponseBody> signup(@Body SignupRequest request);
}