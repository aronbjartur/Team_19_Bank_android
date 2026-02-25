package com.example.team19bank;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        TextView textAccountNumber = findViewById(R.id.textAccountNumber);
        TextView textBalance = findViewById(R.id.textBalance);

        TextView textWelcome = findViewById(R.id.textWelcome);
        Button btnLogout = findViewById(R.id.btnLogout);

        // saved username
        SharedPreferences sharedPref = getSharedPreferences("BankAppPrefs", Context.MODE_PRIVATE);
        String user = sharedPref.getString("active_username", "User");

        textWelcome.setText("Welcome back, " + user + "!");

        // US3: Loada account summary immediately
        String accountNumber = sharedPref.getString("account_number", null);
        long balanceISK = sharedPref.getLong("balance_isk", Long.MIN_VALUE);

        // Demo defaults (þangað til backend er græjað)
        if (accountNumber == null) {
            accountNumber = "0130-26-000001";
            sharedPref.edit().putString("account_number", accountNumber).apply();
        }
        if (balanceISK == Long.MIN_VALUE) {
            balanceISK = 125000;
            sharedPref.edit().putLong("balance_isk", balanceISK).apply();
        }

        textAccountNumber.setText("Account: " + accountNumber);
        textBalance.setText("Balance: " + balanceISK + " ISK");

        // LOGOUT
        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear(); // Delete the "Remember Me" data
            editor.apply();

            // heima aftur
            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}