package com.example.team19bank;

import android.content.Context;

import com.example.team19bank.model.LoanRequest;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoanService {
    private final BankApi api;

    public interface LoanCallback {
        void onSuccess();
        void onError(String errorMsg);
    }

    public LoanService(Context context) {
        this.api = NetworkService.getApi(context);
    }

    public void applyForLoan(LoanRequest req, LoanCallback callback) {
        api.applyForLoan(req).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    // Náum í alvöru villuna frá bakendanum
                    try {
                        String errorJson = response.errorBody().string();

                        // Bakendinn sendir oft {"error":"..."}
                        if (errorJson != null && errorJson.contains("error")) {
                            callback.onError(errorJson);
                        } else {
                            callback.onError("Server error (400/500)");
                        }
                    } catch (Exception e) {
                        callback.onError("Rejected by bank rules.");
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}