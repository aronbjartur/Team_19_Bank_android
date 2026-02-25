package com.example.team19bank;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText editUser = findViewById(R.id.loginUser);
        EditText editPass = findViewById(R.id.loginPass);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            // Get text from inputs
            String user = editUser.getText().toString().trim();
            String pass = editPass.getText().toString().trim();

            // MOCK LOGIN VALIDATION
            if (!user.isEmpty() && !pass.isEmpty()) {

                // US2
                // BankAppPrefs
                SharedPreferences sharedPref = getSharedPreferences("BankAppPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                // Save the flags that MainActivity looks for
                editor.putBoolean("is_logged_in", true);
                editor.putString("active_username", user);

                // apply() is asynchronous and safe for the UI thread
                editor.apply();

                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Basic error handling if fields are empty
                if (user.isEmpty()) editUser.setError("Username required");
                if (pass.isEmpty()) editPass.setError("Password required");
                Toast.makeText(this, "Please enter your credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}