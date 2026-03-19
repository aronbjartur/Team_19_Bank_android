package com.example.team19bank;

import android.content.Context;
import android.util.Log;
import com.example.team19bank.model.Account;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountService {
    private final BankApi api;
    private static final String TAG = "ACCOUNT_SERVICE_DEBUG";

    public interface AccountCallback {
        void onSuccess(Account account);
        void onError(String errorMsg);
    }

    public AccountService(Context context) {
        this.api = NetworkService.getApi(context);
    }

    public void getAccountForUser(String username, AccountCallback callback) {
        api.getAllUsersRaw().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String rawJson = response.body().string();
                        Log.d(TAG, "Leita að [" + username + "] í " + rawJson.length() + " stöfum.");

                        String patternString = "\"username\"\\s*:\\s*\"" + Pattern.quote(username) + "\"";
                        Matcher m = Pattern.compile(patternString).matcher(rawJson);

                        if (m.find()) {
                            String sub = rawJson.substring(Math.max(0, m.start() - 100), m.start());
                            Matcher idMatcher = Pattern.compile("\"id\"\\s*:\\s*(\\d+)").matcher(sub);

                            Long myId = null;
                            while (idMatcher.find()) {
                                myId = Long.parseLong(idMatcher.group(1));
                            }

                            if (myId != null) {
                                Log.d(TAG, "Fann ID [" + myId + "] fyrir " + username);
                                fetchAccountById(myId, callback);
                                return;
                            }
                        }
                        callback.onError("Notandi fannst ekki í kerfinu.");
                    } catch (Exception e) {
                        callback.onError("Gagnavilla: " + e.getMessage());
                    }
                } else {
                    callback.onError("Mistókst að sækja notendalista.");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError("Netvilla: " + t.getMessage());
            }
        });
    }


    private void fetchAccountById(Long id, AccountCallback callback) {
        api.getUserAccountRaw(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String rawJson = response.body().string();
                        org.json.JSONObject json = new org.json.JSONObject(rawJson);

                        Account account = new Account();
                        account.setAccountNumber(json.getString("accountNumber"));
                        account.setBalance(json.getDouble("balance"));

                        callback.onSuccess(account);
                    } catch (Exception e) {
                        Log.e(TAG, "Gat ekki lesið reikning: " + e.getMessage());
                        callback.onError("Gagnavilla í reikningi.");
                    }
                } else {
                    callback.onError("Fann ekki reikning fyrir ID: " + id);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError("Netvilla við að sækja reikning.");
            }
        });
    }
}