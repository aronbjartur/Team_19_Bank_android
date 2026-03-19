package com.example.team19bank;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {

    // Lagað P2: Breyttum nafninu úr emailEditText í usernameEditText til að forðast rugling
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
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
    }

    // Aðferð beint úr class diagram
    private void onLoginClicked() {
        String user = usernameEditText.getText().toString().trim(); // Uppfært nafn!
        String pass = passwordEditText.getText().toString().trim();

        if (!user.isEmpty() && !pass.isEmpty()) {
            authService.login(user, pass, new AuthService.AuthCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                    // Förum á HomeFragment
                    ((MainActivity) requireActivity()).replaceFragment(new HomeFragment(), false);
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