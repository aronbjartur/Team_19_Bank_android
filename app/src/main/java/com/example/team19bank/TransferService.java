package com.example.team19bank;

import android.content.Context;
import com.example.team19bank.model.TransferRequest;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransferService {
    private final BankApi api;

    public interface TransferCallback {
        void onSuccess();
        void onError(String errorMsg);
    }

    public TransferService(Context context) {
        this.api = NetworkService.getApi(context);
    }

    public void createTransfer(TransferRequest req, TransferCallback callback) {
        api.createTransfer(req).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Insufficient funds or invalid accounts."); //
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}