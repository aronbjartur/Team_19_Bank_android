package com.example.team19bank;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    // Slóðin á bakendann ykkar
    private static final String BASE_URL = "https://team-19-banki.onrender.com/";
    private static BankApi api;

    public static BankApi getApi(Context context) {
        if (api == null) {
            SharedPreferences prefs = context.getSharedPreferences("BankAppPrefs", Context.MODE_PRIVATE);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request.Builder builder = chain.request().newBuilder();
                        String token = prefs.getString("auth_token", null);

                        // Ef við erum skráð inn, bætum við tokeninu við beiðnina
                        if (token != null) {
                            builder.addHeader("Authorization", "Bearer " + token);
                        }

                        // Sendum beiðnina og fáum svar
                        Response response = chain.proceed(builder.build());

                        if (response.code() == 401) {
                            prefs.edit().clear().apply();

                            Intent intent = new Intent(context, MainActivity.class);
                            // Þessi "flags" tryggja að notandinn geti ekki ýtt á 'Back' og farið aftur inn
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        }

                        return response;
                    }).build();

            api = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(BankApi.class);
        }
        return api;
    }
}