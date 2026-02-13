package com.example.team19bank;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText mEditUsername, mEditEmail, mEditPassword, mEditConfirmPassword;
    private Button mBtnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // variables og xml tengt
        mEditUsername = findViewById(R.id.editUsername);
        mEditEmail = findViewById(R.id.editEmail);
        mEditPassword = findViewById(R.id.editPassword);
        mEditConfirmPassword = findViewById(R.id.editConfirmPassword);
        mBtnSignup = findViewById(R.id.btnSignup);

        // click listner
        mBtnSignup.setOnClickListener(v -> {
            // Check validation strax
            if (validateInputs()) {
                // ef það virka þá fer í US2
                saveUserAndProceed();
            }
        });
    }

    // US1 aðal
    private boolean validateInputs() {
        String username = mEditUsername.getText().toString().trim();
        String email = mEditEmail.getText().toString().trim();
        String password = mEditPassword.getText().toString().trim();
        String confirm = mEditConfirmPassword.getText().toString().trim();

        if (username.isEmpty()) {
            mEditUsername.setError("Username required");
            return false;
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEditEmail.setError("Invalid email");
            return false;
        }
        if (password.length() < 4) {
            mEditPassword.setError("Password too short");
            return false;
        }
        if (!password.equals(confirm)) {
            mEditConfirmPassword.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    // US2 aðal (eis og er eftir Aron)
    private void saveUserAndProceed() {
        String username = mEditUsername.getText().toString().trim();

        // Save that we are logged in using "BankAppPrefs"
        SharedPreferences sharedPref = getSharedPreferences("BankAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("is_logged_in", true);
        editor.putString("active_username", username);
        editor.apply();

        Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show();

        // heim
        Intent intent = new Intent(SignupActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish(); // Close signup activity so back button doesn't return here
    }
}