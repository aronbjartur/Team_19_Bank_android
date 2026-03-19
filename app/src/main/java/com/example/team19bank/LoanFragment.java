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
    private Button submitBtn, cancelBtn; // Bætti við cancelBtn
    private LoanService loanService;
    private SharedPreferences sharedPref;

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
        submitBtn = view.findViewById(R.id.btnSubmitLoan);
        cancelBtn = view.findViewById(R.id.btnBackFromLoan);

        loanService = new LoanService(requireContext());
        sharedPref = requireActivity().getSharedPreferences("BankAppPrefs", Context.MODE_PRIVATE);

        sourceAccountEt.setText("bank");

        if (cancelBtn != null) {
            cancelBtn.setOnClickListener(v -> {
                // Fer aftur á HomeFragment
                ((MainActivity) requireActivity()).replaceFragment(new HomeFragment(), false);
            });
        }

        submitBtn.setOnClickListener(v -> {
            String amountStr = amountEt.getText().toString().trim();
            String memoStr = memoEt.getText().toString().trim();

            String myAccountNumber = sharedPref.getString("account_number", "");

            Log.d("LOAN_DEBUG", "Sending loan request. Receiver Account: [" + myAccountNumber + "]");

            if (amountStr.isEmpty()) {
                amountEt.setError("Upphæð vantar");
                return;
            }

            if (myAccountNumber.isEmpty()) {
                Toast.makeText(getContext(), "Reikningsnúmer fannst ekki! Farðu til baka á Home.", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                double amount = Double.parseDouble(amountStr);

                LoanRequest req = new LoanRequest();
                req.setLoanGiverAccount("bank");
                req.setLoanReceiverAccount(myAccountNumber);
                req.setAmount(amount);
                req.setMemo(memoStr);

                loanService.applyForLoan(req, new LoanService.LoanCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getContext(), "Lán samþykkt!", Toast.LENGTH_SHORT).show();
                        ((MainActivity) requireActivity()).replaceFragment(new SuccessFragment(), false);
                    }

                    @Override
                    public void onError(String errorMsg) {
                        Toast.makeText(getContext(), "Bankinn segir: " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                });

            } catch (NumberFormatException e) {
                amountEt.setError("Ógild upphæð");
            }
        });
    }
}