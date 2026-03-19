package com.example.team19bank;

import com.example.team19bank.model.BankUser;
import com.example.team19bank.model.LoanRequest;
import com.example.team19bank.model.LoginRequest;
import com.example.team19bank.model.TransferRequest;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BankApi {
    @POST("login")
    Call<Map<String, String>> login(@Body LoginRequest request);

    @POST("signup")
    Call<String> signup(@Body BankUser user);

    @POST("logout")
    Call<Map<String, String>> logout();

    @GET("users")
    Call<ResponseBody> getAllUsersRaw();

    @GET("users/{id}/account")
    Call<ResponseBody> getUserAccountRaw(@Path("id") Long id);

    @POST("transfer")
    Call<Map<String, Object>> createTransfer(@Body TransferRequest req);

    @POST("loans")
    Call<Map<String, Object>> applyForLoan(@Body LoanRequest req);

    @GET("users/{id}/transactions")
    Call<ResponseBody> getUserTransactions(@Path("id") Long id);
}