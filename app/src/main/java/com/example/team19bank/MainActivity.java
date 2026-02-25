package com.example.team19bank;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Takið eftir "BankAppPrefs"
        SharedPreferences sharedPref = getSharedPreferences("BankAppPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean("is_logged_in", false);

        if (isLoggedIn) {
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }

        // ef ekki logged in, fara annað
        setContentView(R.layout.activity_main);

        Button btnLogin = findViewById(R.id.btnGoToLogin);
        Button btnSignup = findViewById(R.id.btnGoToSignup);

        // Navigation
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnSignup.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
}