package com.example.team19bank;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;

    private TextView textWelcome;
    private TextView textAccountNumber;
    private TextView textBalance;

    private Button btnSendMoney;
    private Button btnHistory;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Views
        textWelcome = findViewById(R.id.textWelcome);
        textAccountNumber = findViewById(R.id.textAccountNumber);
        textBalance = findViewById(R.id.textBalance);

        btnSendMoney = findViewById(R.id.btnSendMoney);
        btnHistory = findViewById(R.id.btnHistory);
        btnLogout = findViewById(R.id.btnLogout);

        sharedPref = getSharedPreferences("BankAppPrefs", Context.MODE_PRIVATE);

        // Send Money -> TransferActivity
        btnSendMoney.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, TransferActivity.class);
            startActivity(intent);
        });

        // US5: Transaction History
        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, TransactionHistoryActivity.class);
            startActivity(intent);
        });

        // LOGOUT
        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Set initial UI
        refreshAccountSummary();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // When coming back from TransferActivity, refresh balance
        refreshAccountSummary();
    }

    private void refreshAccountSummary() {
        // saved username
        String user = sharedPref.getString("active_username", "User");
        textWelcome.setText("Welcome back, " + user + "!");

        // US3: Load account summary
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
    }
}