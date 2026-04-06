package com.example.team19bank;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team19bank.model.Loan;

import java.util.ArrayList;
import java.util.List;

public class PendingApprovalsFragment extends Fragment {

    private LoanService loanService;
    private RecyclerView recyclerView;
    private LoanAdapter adapter;
    private final List<Loan> loans = new ArrayList<>();
    private String myAccountNumber = "";

    public PendingApprovalsFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull android.view.LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Context context = requireContext();

        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(24, 24, 24, 24);

        TextView title = new TextView(context);
        title.setText("Pending Approvals");
        title.setTextSize(24f);
        title.setGravity(Gravity.START);
        root.addView(title);

        recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        root.addView(recyclerView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        loanService = new LoanService(requireContext());

        SharedPreferences sharedPref = requireActivity().getSharedPreferences("BankAppPrefs", Context.MODE_PRIVATE);
        myAccountNumber = sharedPref.getString("account_number", "");

        adapter = new LoanAdapter(loans, myAccountNumber, true, false, new LoanAdapter.LoanActionListener() {
            @Override
            public void onApprove(Loan loan) {
                loanService.approveLoan(loan.getLoanId(), null, new LoanService.LoanActionCallback() {
                    @Override
                    public void onSuccess(String message, Loan updatedLoan) {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        loadLoans();
                    }

                    @Override
                    public void onError(String errorMsg) {
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onReject(Loan loan) {
                loanService.rejectLoan(loan.getLoanId(), new LoanService.LoanActionCallback() {
                    @Override
                    public void onSuccess(String message, Loan updatedLoan) {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        loadLoans();
                    }

                    @Override
                    public void onError(String errorMsg) {
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onPay(Loan loan, String amount) {
            }
        });

        recyclerView.setAdapter(adapter);
        loadLoans();
    }

    private void loadLoans() {
        loanService.getPendingApprovals(new LoanService.LoanListCallback() {
            @Override
            public void onSuccess(List<Loan> result) {
                loans.clear();
                loans.addAll(result);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMsg) {
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }
}