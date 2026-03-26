package com.example.team19bank;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.concurrent.Executor;

public class LoginFragment extends Fragment {

    // Lagað P2: Breyttum nafninu úr emailEditText í usernameEditText til að forðast rugling
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button biometricButton;
    private Button signupLinkButton;
    private Button homeButton; // NÝTT: Breyta fyrir nýja Heim/Loka takkann

    private AuthService authService; // Þjónustan sem sér um API kallið

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Notum þitt gamla XML!
        return inflater.inflate(R.layout.activity_login, container, false);
    }

    // Aðferð beint úr class diagram
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameEditText = view.findViewById(R.id.loginUser);
        passwordEditText = view.findViewById(R.id.loginPass);
        loginButton = view.findViewById(R.id.btnLogin);
        biometricButton = view.findViewById(R.id.btnBiometric);
        signupLinkButton = view.findViewById(R.id.btnGoToSignup);
        homeButton = view.findViewById(R.id.btnHomeFromLogin);

        authService = new AuthService(requireContext());

        loginButton.setOnClickListener(v -> onLoginClicked());

        if (signupLinkButton != null) {
            signupLinkButton.setOnClickListener(v -> onSignupLinkClicked());
        }

        if (homeButton != null) {
            homeButton.setOnClickListener(v -> {
                // Lokum Activity-inu (og þar með appinu þar sem þetta er upphafsskjárinn)
                requireActivity().finish();
            });
        }

        setupBiometricButton();
    }

    private void setupBiometricButton() {
        // Show the fingerprint button only if the user has previously opted in.
        // Uses a separate prefs file so it isn't wiped on logout/401.
        SharedPreferences prefs = requireContext().getSharedPreferences("BiometricPrefs", Context.MODE_PRIVATE);
        boolean biometricEnabled = prefs.getBoolean("biometric_enabled", false);

        if (biometricEnabled) {
            biometricButton.setVisibility(View.VISIBLE);
            biometricButton.setOnClickListener(v -> showBiometricPrompt());
        }
    }

    private boolean isBiometricAvailable() {
        BiometricManager biometricManager = BiometricManager.from(requireContext());
        return biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS;
    }

    private void promptEnableBiometric() {
        if (!isBiometricAvailable()) {
            // Device has no enrolled fingerprints — skip the prompt
            navigateToHome();
            return;
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("Enable Fingerprint Login")
                .setMessage("Would you like to use your fingerprint to sign in next time?")
                .setPositiveButton("Enable", (dialog, which) -> {
                    // Save biometric flag alongside the current token and username
                    // so they survive logout (which clears BankAppPrefs)
                    SharedPreferences bankPrefs = requireContext().getSharedPreferences("BankAppPrefs", Context.MODE_PRIVATE);
                    SharedPreferences bioPrefs = requireContext().getSharedPreferences("BiometricPrefs", Context.MODE_PRIVATE);
                    bioPrefs.edit()
                            .putBoolean("biometric_enabled", true)
                            .putString("auth_token", bankPrefs.getString("auth_token", null))
                            .putString("active_username", bankPrefs.getString("active_username", null))
                            .apply();
                    navigateToHome();
                })
                .setNegativeButton("Not now", (dialog, which) -> navigateToHome())
                .show();
    }

    private void navigateToHome() {
        Toast.makeText(getContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
        // Förum á HomeFragment
        ((MainActivity) requireActivity()).replaceFragment(new HomeFragment(), false);
    }

    private void showBiometricPrompt() {
        Executor executor = ContextCompat.getMainExecutor(requireContext());

        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        // Restore token and username from BiometricPrefs back into BankAppPrefs
                        SharedPreferences bioPrefs = requireContext().getSharedPreferences("BiometricPrefs", Context.MODE_PRIVATE);
                        SharedPreferences bankPrefs = requireContext().getSharedPreferences("BankAppPrefs", Context.MODE_PRIVATE);
                        bankPrefs.edit()
                                .putBoolean("is_logged_in", true)
                                .putString("auth_token", bioPrefs.getString("auth_token", null))
                                .putString("active_username", bioPrefs.getString("active_username", null))
                                .apply();
                        navigateToHome();
                    }

                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        // Cancelled or hardware error — do nothing, user can still use password
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Toast.makeText(getContext(), "Fingerprint not recognized", Toast.LENGTH_SHORT).show();
                    }
                });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Sign in to Team 19 Bank")
                .setSubtitle("Use your fingerprint to sign in")
                .setNegativeButtonText("Use password")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    // Aðferð beint úr class diagram
    private void onLoginClicked() {
        String user = usernameEditText.getText().toString().trim(); // Uppfært nafn!
        String pass = passwordEditText.getText().toString().trim();

        if (!user.isEmpty() && !pass.isEmpty()) {
            authService.login(user, pass, new AuthService.AuthCallback() {
                @Override
                public void onSuccess() {
                    SharedPreferences prefs = requireContext().getSharedPreferences("BiometricPrefs", Context.MODE_PRIVATE);
                    boolean alreadyEnabled = prefs.getBoolean("biometric_enabled", false);
                    if (alreadyEnabled) {
                        // Keep the stored biometric token in sync with the latest login
                        SharedPreferences bankPrefs = requireContext().getSharedPreferences("BankAppPrefs", Context.MODE_PRIVATE);
                        prefs.edit()
                                .putString("auth_token", bankPrefs.getString("auth_token", null))
                                .putString("active_username", bankPrefs.getString("active_username", null))
                                .apply();
                        navigateToHome();
                    } else {
                        // First login — ask if they want to enable fingerprint
                        promptEnableBiometric();
                    }
                }

                @Override
                public void onError(String errorMsg) {
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            if (user.isEmpty()) usernameEditText.setError("Username required");
            if (pass.isEmpty()) passwordEditText.setError("Password required");
            Toast.makeText(getContext(), "Please enter your credentials", Toast.LENGTH_SHORT).show();
        }
    }

    // Aðferð beint úr class diagram
    private void onSignupLinkClicked() {
        ((MainActivity) requireActivity()).replaceFragment(new SignupFragment(), true);
    }
}