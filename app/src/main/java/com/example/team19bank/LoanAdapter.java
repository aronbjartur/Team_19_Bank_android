package com.example.team19bank;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team19bank.model.Loan;

import java.util.List;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.ViewHolder> {

    public interface LoanActionListener {
        void onApprove(Loan loan);
        void onReject(Loan loan);
        void onPay(Loan loan, String amount);
    }

    private final List<Loan> loans;
    private final String myAccountNumber;
    private final boolean showApprovalButtons;
    private final boolean showPayControls;
    private final LoanActionListener listener;

    public LoanAdapter(List<Loan> loans,
                       String myAccountNumber,
                       boolean showApprovalButtons,
                       boolean showPayControls,
                       LoanActionListener listener) {
        this.loans = loans;
        this.myAccountNumber = myAccountNumber;
        this.showApprovalButtons = showApprovalButtons;
        this.showPayControls = showPayControls;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_loan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Loan loan = loans.get(position);

        holder.textAmount.setText("Amount: " + safeAmount(loan.getLoanAmount()) + " ISK");
        holder.textRemaining.setText("Remaining: " + safeAmount(loan.getRemainingAmount()) + " ISK");
        holder.textLender.setText("Lender: " + safe(loan.getLoanGiverAccount()));
        holder.textBorrower.setText("Borrower: " + safe(loan.getLoanReceiverAccount()));
        holder.textStatus.setText("Status: " + safe(loan.getStatus()));
        holder.textMemo.setText("Memo: " + safe(loan.getMemo()));
        holder.textDueDate.setText("Due: " + safe(loan.getDueAt()));

        boolean canPay = showPayControls
                && "APPROVED".equalsIgnoreCase(safe(loan.getStatus()))
                && myAccountNumber.equals(loan.getLoanReceiverAccount());

        holder.etPayAmount.setVisibility(canPay ? View.VISIBLE : View.GONE);
        holder.btnPay.setVisibility(canPay ? View.VISIBLE : View.GONE);

        holder.btnApprove.setVisibility(showApprovalButtons ? View.VISIBLE : View.GONE);
        holder.btnReject.setVisibility(showApprovalButtons ? View.VISIBLE : View.GONE);

        holder.btnApprove.setOnClickListener(v -> listener.onApprove(loan));
        holder.btnReject.setOnClickListener(v -> listener.onReject(loan));
        holder.btnPay.setOnClickListener(v -> {
            String amount = holder.etPayAmount.getText().toString().trim();
            if (!TextUtils.isEmpty(amount)) {
                listener.onPay(loan, amount);
            } else {
                holder.etPayAmount.setError("Enter amount");
            }
        });
    }

    @Override
    public int getItemCount() {
        return loans.size();
    }

    private String safe(String s) {
        return s == null || s.isEmpty() ? "-" : s;
    }

    private String safeAmount(Double d) {
        return d == null ? "0" : String.valueOf(d);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textAmount, textRemaining, textLender, textBorrower, textStatus, textMemo, textDueDate;
        EditText etPayAmount;
        Button btnApprove, btnReject, btnPay;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textAmount = itemView.findViewById(R.id.textLoanAmount);
            textRemaining = itemView.findViewById(R.id.textLoanRemaining);
            textLender = itemView.findViewById(R.id.textLoanLender);
            textBorrower = itemView.findViewById(R.id.textLoanBorrower);
            textStatus = itemView.findViewById(R.id.textLoanStatus);
            textMemo = itemView.findViewById(R.id.textLoanMemo);
            textDueDate = itemView.findViewById(R.id.textLoanDueDate);
            etPayAmount = itemView.findViewById(R.id.etLoanPayAmount);
            btnApprove = itemView.findViewById(R.id.btnApproveLoan);
            btnReject = itemView.findViewById(R.id.btnRejectLoan);
            btnPay = itemView.findViewById(R.id.btnPayLoan);
        }
    }
}