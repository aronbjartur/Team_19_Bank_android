package com.example.team19bank;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private final List<Transaction> transactions;
    private final String myAccountNumber;

    public TransactionAdapter(List<Transaction> transactions, String myAccountNumber) {
        this.transactions = transactions;
        this.myAccountNumber = myAccountNumber;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction tx = transactions.get(position);

        boolean isSender =
                tx.getSourceAccount() != null &&
                        tx.getSourceAccount().equals(myAccountNumber);

        if (isSender) {
            holder.textReceiver.setText("To: " + tx.getDestinationAccount());
            holder.textAmount.setText("-" + tx.getAmount() + " ISK");
            holder.textAmount.setTextColor(0xFFD32F2F);
        } else {
            holder.textReceiver.setText("From: " + tx.getSourceAccount());
            holder.textAmount.setText("+" + tx.getAmount() + " ISK");
            holder.textAmount.setTextColor(0xFF388E3C);
        }

        holder.textDate.setText(
                tx.getCreatedAt() != null ? tx.getCreatedAt() : "Date missing"
        );

        if (tx.getMemo() != null && !tx.getMemo().isEmpty()) {
            holder.textReference.setText("Ref: " + tx.getMemo());
            holder.textReference.setVisibility(View.VISIBLE);
        } else {
            holder.textReference.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textReceiver, textAmount, textDate, textReference;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textReceiver = itemView.findViewById(R.id.textReceiver);
            textAmount = itemView.findViewById(R.id.textAmount);
            textDate = itemView.findViewById(R.id.textDate);
            textReference = itemView.findViewById(R.id.textReference);
        }
    }
}