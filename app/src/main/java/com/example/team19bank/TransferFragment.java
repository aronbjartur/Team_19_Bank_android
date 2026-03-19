package com.example.team19bank;

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
import androidx.fragment.app.Fragment;
import com.example.team19bank.model.TransferRequest;

public class TransferFragment extends Fragment {

    private EditText toAccountEditText, amountEditText, memoEditText;
    private Button sendButton, backButton;
    private TransferService transferService;
    private SharedPreferences sharedPref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Gakktu úr skugga um að nafnið hér passi við XML skrána þína
        return inflater.inflate(R.layout.activity_transfer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toAccountEditText = view.findViewById(R.id.etReceiver);
        amountEditText = view.findViewById(R.id.etAmount);
        memoEditText = view.findViewById(R.id.etReference);
        sendButton = view.findViewById(R.id.btnSend);
        backButton = view.findViewById(R.id.btnBackTransfer); // Nýi takkinn

        transferService = new TransferService(requireContext());
        sharedPref = requireActivity().getSharedPreferences("BankAppPrefs", Context.MODE_PRIVATE);

        // Millifæra takki
        sendButton.setOnClickListener(v -> onTransferClicked());

        // Til baka takki
        backButton.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).replaceFragment(new HomeFragment(), false);
        });
    }

    private void onTransferClicked() {
        String receiver = toAccountEditText.getText().toString().trim();
        String amountStr = amountEditText.getText().toString().trim();

        if (receiver.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(getContext(), "Vinsamlegast fylltu út alla reiti", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);
        String myAccount = sharedPref.getString("account_number", "");

        if (myAccount.isEmpty()) {
            Toast.makeText(getContext(), "Villa: Reikningsnúmer þitt fannst ekki", Toast.LENGTH_LONG).show();
            return;
        }

        TransferRequest req = new TransferRequest();
        req.setSourceAccount(myAccount);
        req.setDestinationAccount(receiver);
        req.setAmount(amount);
        req.setMemo(memoEditText.getText().toString().trim());

        transferService.createTransfer(req, new TransferService.TransferCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Millifærsla tókst!", Toast.LENGTH_LONG).show();
                ((MainActivity) requireActivity()).replaceFragment(new SuccessFragment(), false);
            }

            @Override
            public void onError(String errorMsg) {
                Toast.makeText(getContext(), "Villa: " + errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }
}