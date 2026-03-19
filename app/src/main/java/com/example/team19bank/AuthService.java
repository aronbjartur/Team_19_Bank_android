package com.example.team19bank;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.team19bank.model.BankUser;
import com.example.team19bank.model.LoginRequest;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthService {

    private final BankApi api;
    private final Context context;

    public interface AuthCallback {
        void onSuccess();
        void onError(String errorMsg);
    }

    public AuthService(Context context) {
        this.context = context;
        this.api = NetworkService.getApi(context);
    }

    public void login(String username, String password, AuthCallback callback) {
        LoginRequest loginReq = new LoginRequest(username, password);

        api.login(loginReq).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    String token = response.body().get("token");

                    SharedPreferences sharedPref = context.getSharedPreferences("BankAppPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    editor.putBoolean("is_logged_in", true);
                    editor.putString("active_username", username);
                    editor.putString("auth_token", token);
                    editor.apply();

                    callback.onSuccess();
                } else {
                    callback.onError("Invalid credentials or user not found.");
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    // LAGAÐ: Tekur núna við creditScore úr viðmótinu í stað þess að nota alltaf 500
    public void signup(String username, String email, String password, int creditScore, AuthCallback callback) {

        BankUser newUser = new BankUser(username, password, email, creditScore);

        api.signup(newUser).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Signup failed. Username might already exist.");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}