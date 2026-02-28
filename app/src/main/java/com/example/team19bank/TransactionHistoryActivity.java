package com.example.team19bank;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.Executors;

// US5: Sýnir færslusögu í skrollanlegum lista
// Sækir gögn úr Room gagnagrunni á bakgrunnsþræði
public class TransactionHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView textEmpty; // Sýnist ef engar færslur eru til

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        recyclerView = findViewById(R.id.recyclerTransactions);
        textEmpty = findViewById(R.id.textEmpty);
        Button btnBack = findViewById(R.id.btnBackHistory);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> finish()); // Til baka á Dashboard

        loadTransactions();
    }

    // Sækja millifærslur úr gagnagrunni og sýna í lista
    private void loadTransactions() {
        SharedPreferences sharedPref = getSharedPreferences("BankAppPrefs", Context.MODE_PRIVATE);
        String username = sharedPref.getString("active_username", "");

        // Room krefst bakgrunnsþráðar fyrir gagnagrunnsfyrirspurnir
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Transaction> transactions = AppDatabase.getInstance(this)
                    .transactionDao()
                    .getAllByUser(username);

            // Uppfæra UI á aðalþræði
            runOnUiThread(() -> {
                if (transactions.isEmpty()) {
                    textEmpty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    textEmpty.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(new TransactionAdapter(transactions));
                }
            });
        });
    }
}
