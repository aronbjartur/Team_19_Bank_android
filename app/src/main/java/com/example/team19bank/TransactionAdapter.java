package com.example.team19bank;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// US5: RecyclerView adapter - sýnir millifærslur í skrollanlegum lista
public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private final List<Transaction> transactions;
    private final String currentUser; // Núverandi notandi
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());

    public TransactionAdapter(List<Transaction> transactions, String currentUser) {
        this.transactions = transactions;
        this.currentUser = currentUser;
    }

    // Búa til nýja línu (inflate layout)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    // Fylla í gögn fyrir hverja línu í listanum
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction tx = transactions.get(position);

        // Ákvarða stefnu millifærslu (sent eða móttekið)
        boolean isSender = tx.sender.equals(currentUser);
        if (isSender) {
            holder.textReceiver.setText("To: " + tx.receiver);
            holder.textAmount.setText("-" + tx.amount + " ISK");
            holder.textAmount.setTextColor(0xFFD32F2F); // Rautt fyrir sent
        } else {
            holder.textReceiver.setText("From: " + tx.sender);
            holder.textAmount.setText("+" + tx.amount + " ISK");
            holder.textAmount.setTextColor(0xFF388E3C); // Grænt fyrir móttekið
        }
        holder.textDate.setText(dateFormat.format(new Date(tx.timestamp)));

        // Sýna tilvísun ef hún er til
        if (tx.reference != null && !tx.reference.isEmpty()) {
            holder.textReference.setText("Ref: " + tx.reference);
            holder.textReference.setVisibility(View.VISIBLE);
        } else {
            holder.textReference.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    // ViewHolder - geymir tilvísanir í views fyrir hverja línu
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
