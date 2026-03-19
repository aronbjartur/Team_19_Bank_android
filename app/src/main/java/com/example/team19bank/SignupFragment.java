package com.example.team19bank;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SignupFragment extends Fragment {

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText creditScoreEditText; // NÝTT: Box fyrir Credit Score
    private Button signupButton;
    private Button backToLoginButton;

    private AuthService authService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameEditText = view.findViewById(R.id.editUsername);
        emailEditText = view.findViewById(R.id.editEmail);
        passwordEditText = view.findViewById(R.id.editPassword);
        confirmPasswordEditText = view.findViewById(R.id.editConfirmPassword);
        creditScoreEditText = view.findViewById(R.id.editCreditScore); // NÝTT: Tengja við XML
        signupButton = view.findViewById(R.id.btnSignup);
        backToLoginButton = view.findViewById(R.id.btnBackToLogin);

        authService = new AuthService(requireContext());

        signupButton.setOnClickListener(v -> onSignupClicked());

        backToLoginButton.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).replaceFragment(new LoginFragment(), false);
        });
    }

    private void onSignupClicked() {
        if (validateInputs()) {
            String username = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // NÝTT: Sækja töluna. Ef boxið er tómt, fær hann bara 500 sem default.
            String scoreStr = creditScoreEditText.getText().toString().trim();
            int creditScore = scoreStr.isEmpty() ? 500 : Integer.parseInt(scoreStr);

            // Bætt við creditScore í kallið!
            authService.signup(username, email, password, creditScore, new AuthService.AuthCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getContext(), "Account Created! Please log in.", Toast.LENGTH_SHORT).show();
                    ((MainActivity) requireActivity()).replaceFragment(new LoginFragment(), false);
                }

                @Override
                public void onError(String errorMsg) {
                    Toast.makeText(getContext(), "Account likely created! Please log in.", Toast.LENGTH_SHORT).show();
                    ((MainActivity) requireActivity()).replaceFragment(new LoginFragment(), false);
                }
            });
        }
    }

    private boolean validateInputs() {
        String username = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirm = confirmPasswordEditText.getText().toString().trim();

        if (username.isEmpty()) {
            nameEditText.setError("Username required");
            return false;
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Invalid email");
            return false;
        }
        if (password.length() < 4) {
            passwordEditText.setError("Password too short");
            return false;
        }
        if (!password.equals(confirm)) {
            confirmPasswordEditText.setError("Passwords do not match");
            return false;
        }
        return true;
    }
}