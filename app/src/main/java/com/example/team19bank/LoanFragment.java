package com.example.team19bank;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.team19bank.model.LoanRequest;

public class LoanFragment extends Fragment {

    private EditText sourceAccountEt, amountEt, memoEt;
    private Button btnBankMode, btnUserMode, submitBtn, cancelBtn;
    private LoanService loanService;
    private SharedPreferences sharedPref;

    private boolean isBankMode = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sourceAccountEt = view.findViewById(R.id.etLoanSource);
        amountEt = view.findViewById(R.id.etLoanAmount);
        memoEt = view.findViewById(R.id.etLoanMemo);
        btnBankMode = view.findViewById(R.id.btnLoanModeBank);
        btnUserMode = view.findViewById(R.id.btnLoanModeUser);
        submitBtn = view.findViewById(R.id.btnSubmitLoan);
        cancelBtn = view.findViewById(R.id.btnBackFromLoan);

        loanService = new LoanService(requireContext());
        sharedPref = requireActivity().getSharedPreferences("BankAppPrefs", Context.MODE_PRIVATE);

        setMode(true);

        btnBankMode.setOnClickListener(v -> setMode(true));
        btnUserMode.setOnClickListener(v -> setMode(false));

        if (cancelBtn != null) {
            cancelBtn.setOnClickListener(v ->
                    ((MainActivity) requireActivity()).replaceFragment(new HomeFragment(), false)
            );
        }

        submitBtn.setOnClickListener(v -> {
            String lenderAccount = sourceAccountEt.getText().toString().trim();
            String amountStr = amountEt.getText().toString().trim();
            String memoStr = memoEt.getText().toString().trim();

            String myAccountNumber = sharedPref.getString("account_number", "");

            Log.d("LOAN_DEBUG", "Sending loan request. Receiver Account: [" + myAccountNumber + "]");

            if (amountStr.isEmpty()) {
                amountEt.setError("Upphæð vantar");
                return;
            }

            if (!isBankMode && lenderAccount.isEmpty()) {
                sourceAccountEt.setError("Reikningsnúmer lánveitanda vantar");
                return;
            }

            if (myAccountNumber.isEmpty()) {
                Toast.makeText(getContext(), "Reikningsnúmer fannst ekki! Farðu til baka á Home.", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                double amount = Double.parseDouble(amountStr);

                LoanRequest req = new LoanRequest();
                req.setLoanGiverAccount(isBankMode ? "bank" : lenderAccount);
                req.setLoanReceiverAccount(myAccountNumber);
                req.setAmount(amount);
                req.setMemo(memoStr);

                loanService.applyForLoan(req, new LoanService.LoanActionCallback() {
                    @Override
                    public void onSuccess(String message, com.example.team19bank.model.Loan loan) {
                        String lowered = message.toLowerCase();
                        if (lowered.contains("waiting") || lowered.contains("approval")) {
                            Toast.makeText(getContext(), "Request sent, waiting for lender approval", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Loan created successfully", Toast.LENGTH_SHORT).show();
                        }
                        ((MainActivity) requireActivity()).replaceFragment(new SuccessFragment(), false);
                    }

                    @Override
                    public void onError(String errorMsg) {
                        Toast.makeText(getContext(), "Loan error: " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                });

            } catch (NumberFormatException e) {
                amountEt.setError("Ógild upphæð");
            }
        });
    }

    private void setMode(boolean bankMode) {
        isBankMode = bankMode;

        if (isBankMode) {
            sourceAccountEt.setText("bank");
            sourceAccountEt.setEnabled(false);
            sourceAccountEt.setFocusable(false);
            sourceAccountEt.setHint("bank");
            submitBtn.setText("Get Loan");
        } else {
            sourceAccountEt.setText("");
            sourceAccountEt.setEnabled(true);
            sourceAccountEt.setFocusableInTouchMode(true);
            sourceAccountEt.setHint("Enter lender account number");
            submitBtn.setText("Send Request");
        }
    }
}