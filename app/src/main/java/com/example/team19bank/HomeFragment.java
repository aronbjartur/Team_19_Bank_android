package com.example.team19bank;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.team19bank.model.Account;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private SharedPreferences sharedPref;

    private TextView textWelcome;
    private TextView textAccountNumber;
    private TextView textBalance;

    private Button transferButton;
    private Button loanButton;
    private Button btnHistory;
    private Button logoutButton;
    private Button btnTogglePrivacy;

    private AccountService accountService;

    private boolean isPrivacyModeOn = false;
    private String realAccountNumber = "—";
    private String realBalance = "—";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textWelcome = view.findViewById(R.id.textWelcome);
        textAccountNumber = view.findViewById(R.id.textAccountNumber);
        textBalance = view.findViewById(R.id.textBalance);

        transferButton = view.findViewById(R.id.btnSendMoney);
        loanButton = view.findViewById(R.id.btnGoToLoan);
        btnHistory = view.findViewById(R.id.btnHistory);
        logoutButton = view.findViewById(R.id.btnLogout);
        btnTogglePrivacy = view.findViewById(R.id.btnTogglePrivacy);

        sharedPref = requireActivity().getSharedPreferences("BankAppPrefs", Context.MODE_PRIVATE);
        accountService = new AccountService(requireContext());

        String user = sharedPref.getString("active_username", "User");
        textWelcome.setText("Welcome back, " + user + "!");

        isPrivacyModeOn = sharedPref.getBoolean("privacy_mode", false);

        transferButton.setEnabled(false);
        loanButton.setEnabled(false);

        transferButton.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).replaceFragment(new TransferFragment(), true);
        });

        loanButton.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).replaceFragment(new LoanFragment(), true);
        });

        btnHistory.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).replaceFragment(new HistoryFragment(), true);
        });

        logoutButton.setOnClickListener(v -> onLogoutClicked());

        btnTogglePrivacy.setOnClickListener(v -> {
            isPrivacyModeOn = !isPrivacyModeOn;
            sharedPref.edit().putBoolean("privacy_mode", isPrivacyModeOn).apply();
            updatePrivacyUI();
        });

        updatePrivacyUI();
        loadAccounts();
    }

    private void loadAccounts() {
        String username = sharedPref.getString("active_username", "");

        android.util.Log.d("DEBUG_USER", "HOME loading for user = [" + username + "]");

        accountService.getAccountForUser(username, new AccountService.AccountCallback() {
            @Override
            public void onSuccess(Account account) {
                if (!isAdded() || getActivity() == null) return;

                realAccountNumber = account.getAccountNumber();
                realBalance = account.getBalance() + " ISK";

                sharedPref.edit()
                        .putString("account_number", account.getAccountNumber())
                        .apply();

                updatePrivacyUI();

                transferButton.setEnabled(true);
                loanButton.setEnabled(true);
            }

            @Override
            public void onError(String errorMsg) {
                if (!isAdded() || getActivity() == null) return;
                Toast.makeText(getContext(), "Error: " + errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updatePrivacyUI() {
        if (isPrivacyModeOn) {
            textAccountNumber.setText("Account: **** **** ****");
            textBalance.setText("Balance: ******");
            btnTogglePrivacy.setText("Show Info");
        } else {
            textAccountNumber.setText("Account: " + realAccountNumber);
            textBalance.setText("Balance: " + realBalance);
            btnTogglePrivacy.setText("Hide Info");
        }
    }

    private void onLogoutClicked() {
        BankApi api = NetworkService.getApi(requireContext());
        api.logout().enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                clearLocalDataAndGoToLogin();
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                clearLocalDataAndGoToLogin();
            }
        });
    }

    private void clearLocalDataAndGoToLogin() {
        if (!isAdded() || getActivity() == null) return;

        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("BankAppPrefs", Context.MODE_PRIVATE);
        sharedPrefs.edit().clear().apply();

        Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
        ((MainActivity) getActivity()).replaceFragment(new LoginFragment(), false);
    }
}