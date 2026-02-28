package com.example.team19bank;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class TransferActivity extends AppCompatActivity {

    private TextInputLayout tilReceiverLayout;
    private TextInputLayout tilAmountLayout;
    private TextInputLayout tilReferenceLayout;

    private TextInputEditText etReceiver;
    private TextInputEditText etAmount;
    private TextInputEditText etReference;

    private Button btnSend;
    private Button btnBack;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        sharedPref = getSharedPreferences("BankAppPrefs", Context.MODE_PRIVATE);

        // Views
        tilReceiverLayout = findViewById(R.id.tilReceiver);
        tilAmountLayout = findViewById(R.id.tilAmount);
        tilReferenceLayout = findViewById(R.id.tilReference);

        etReceiver = findViewById(R.id.etReceiver);
        etAmount = findViewById(R.id.etAmount);
        etReference = findViewById(R.id.etReference);

        btnSend = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack);

        // Default: disable send until valid
        btnSend.setEnabled(false);

        // Instant validation (live)
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                validateForm();
            }
        };

        etReceiver.addTextChangedListener(watcher);
        etAmount.addTextChangedListener(watcher);
        etReference.addTextChangedListener(watcher);

        btnBack.setOnClickListener(v -> finish());

        btnSend.setOnClickListener(v -> {
            if (!validateForm()) return;

            String receiver = safeText(etReceiver);
            long amount = Long.parseLong(safeText(etAmount)); // safe because validateForm() passed

            long currentBalance = sharedPref.getLong("balance_isk", 125000);
            long newBalance = currentBalance - amount;

            sharedPref.edit()
                    .putLong("balance_isk", newBalance)
                    .apply();

            // US5: Vista millifærslu í Room gagnagrunn (á bakgrunnsþræði)
            String sender = sharedPref.getString("active_username", "Unknown");
            String ref = safeText(etReference);
            Transaction transaction = new Transaction(sender, receiver, amount, ref, System.currentTimeMillis());
            java.util.concurrent.Executors.newSingleThreadExecutor().execute(() ->
                    AppDatabase.getInstance(this).transactionDao().insert(transaction)
            );

            Toast.makeText(this,
                    "Transfer submitted!\nTo: " + receiver + "\nAmount: " + amount + " ISK",
                    Toast.LENGTH_LONG).show();

            finish();
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        // Clear previous errors
        tilReceiverLayout.setError(null);
        tilAmountLayout.setError(null);
        tilReferenceLayout.setError(null);

        String receiver = safeText(etReceiver);
        String amountStr = safeText(etAmount);
        // reference is optional in this version:
        // String reference = safeText(etReference);

        // Receiver validation
        if (receiver.isEmpty()) {
            tilReceiverLayout.setError("Receiver is required");
            valid = false;
        } else if (receiver.length() < 4) {
            tilReceiverLayout.setError("Receiver must be at least 4 characters");
            valid = false;
        }

        // Amount validation
        long amount = -1;
        if (amountStr.isEmpty()) {
            tilAmountLayout.setError("Amount is required");
            valid = false;
        } else {
            try {
                amount = Long.parseLong(amountStr);
                if (amount <= 0) {
                    tilAmountLayout.setError("Amount must be greater than 0");
                    valid = false;
                }
            } catch (NumberFormatException e) {
                tilAmountLayout.setError("Amount must be a whole number (ISK)");
                valid = false;
            }
        }

        // Optional bonus: cannot transfer more than balance
        if (valid) {
            long currentBalance = sharedPref.getLong("balance_isk", 125000);
            if (amount > currentBalance) {
                tilAmountLayout.setError("Insufficient funds. Balance: " + currentBalance + " ISK");
                valid = false;
            }
        }

        btnSend.setEnabled(valid);
        return valid;
    }

    private String safeText(TextInputEditText et) {
        if (et.getText() == null) return "";
        return et.getText().toString().trim();
    }
}