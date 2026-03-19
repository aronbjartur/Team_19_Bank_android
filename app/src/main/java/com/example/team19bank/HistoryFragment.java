package com.example.team19bank;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textEmpty;
    private Button btnBack;
    private BankApi api;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_transaction_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerTransactions);
        textEmpty = view.findViewById(R.id.textEmpty);
        btnBack = view.findViewById(R.id.btnBackHistory);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        api = NetworkService.getApi(requireContext());

        btnBack.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).replaceFragment(new HomeFragment(), false);
        });

        SharedPreferences sp = requireActivity().getSharedPreferences("BankAppPrefs", Context.MODE_PRIVATE);
        String currentUser = sp.getString("active_username", "");

        loadTransactionsForUsername(currentUser);
    }

    private void loadTransactionsForUsername(String username) {
        api.getAllUsersRaw().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!isAdded() || getActivity() == null) return;

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String rawJson = response.body().string();
                        Log.d("HISTORY_DEBUG", "users raw = " + rawJson);

                        Long userId = extractUserId(rawJson, username);
                        Log.d("HISTORY_DEBUG", "resolved userId = " + userId);

                        if (userId == null) {
                            showError("User ID not found.");
                            return;
                        }

                        fetchTransactionsForId(userId);

                    } catch (Exception e) {
                        Log.e("HISTORY_DEBUG", "users parse failed", e);
                        showError("Data error: " + e.getMessage());
                    }
                } else {
                    showError("Failed to fetch users.");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (!isAdded() || getActivity() == null) return;
                Log.e("HISTORY_DEBUG", "users onFailure", t);
                showError("Network error: " + t.getMessage());
            }
        });
    }

    private Long extractUserId(String rawJson, String username) {
        try {
            String marker = "\"username\":\"" + username + "\"";
            int userIndex = rawJson.indexOf(marker);

            if (userIndex == -1) {
                Log.e("HISTORY_DEBUG", "Username marker not found: " + marker);
                return null;
            }

            String before = rawJson.substring(0, userIndex);
            Matcher idMatcher = Pattern.compile("\"id\":(\\d+)").matcher(before);

            Long foundId = null;
            while (idMatcher.find()) {
                foundId = Long.parseLong(idMatcher.group(1));
            }

            return foundId;
        } catch (Exception e) {
            Log.e("HISTORY_DEBUG", "extractUserId failed", e);
            return null;
        }
    }

    private void fetchTransactionsForId(Long id) {
        api.getUserTransactions(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!isAdded() || getActivity() == null) return;

                Log.d("HISTORY_DEBUG", "response.code = " + response.code());
                Log.d("HISTORY_DEBUG", "response.success = " + response.isSuccessful());

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String rawJson = response.body().string();
                        Log.d("HISTORY_DEBUG", "transactions rawJson = " + rawJson);

                        List<Transaction> transactions = parseTransactions(rawJson);
                        Log.d("HISTORY_DEBUG", "transactions.size = " + transactions.size());

                        if (transactions.isEmpty()) {
                            textEmpty.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            textEmpty.setText("No transactions found.");
                        } else {
                            textEmpty.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            SharedPreferences sp = requireActivity().getSharedPreferences("BankAppPrefs", Context.MODE_PRIVATE);
                            String myAccountNumber = sp.getString("account_number", "");

                            recyclerView.setAdapter(new TransactionAdapter(transactions, myAccountNumber));
                        }
                    } catch (Exception e) {
                        Log.e("HISTORY_DEBUG", "transactions parse failed", e);
                        showError("Data error: " + e.getMessage());
                    }
                } else {
                    try {
                        String err = response.errorBody() != null ? response.errorBody().string() : "null";
                        Log.e("HISTORY_DEBUG", "errorBody = " + err);
                    } catch (Exception e) {
                        Log.e("HISTORY_DEBUG", "errorBody read failed", e);
                    }

                    showError("Gat ekki sótt færslur.");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (!isAdded() || getActivity() == null) return;
                Log.e("HISTORY_DEBUG", "transactions onFailure", t);
                showError("Netvilla: " + t.getMessage());
            }
        });
    }

    private List<Transaction> parseTransactions(String rawJson) throws Exception {
        List<Transaction> transactions = new ArrayList<>();

        JSONArray array = new JSONArray(rawJson);
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);

            Transaction tx = new Transaction();

            if (obj.has("id") && !obj.isNull("id")) {
                tx.setId(obj.getLong("id"));
            }

            if (obj.has("amount") && !obj.isNull("amount")) {
                tx.setAmount(obj.getDouble("amount"));
            }

            if (obj.has("sourceAccount") && !obj.isNull("sourceAccount")) {
                tx.setSourceAccount(obj.getString("sourceAccount"));
            }

            if (obj.has("destinationAccount") && !obj.isNull("destinationAccount")) {
                tx.setDestinationAccount(obj.getString("destinationAccount"));
            }

            if (obj.has("status") && !obj.isNull("status")) {
                tx.setStatus(obj.getString("status"));
            }

            if (obj.has("createdAt") && !obj.isNull("createdAt")) {
                tx.setCreatedAt(obj.getString("createdAt"));
            }

            if (obj.has("memo") && !obj.isNull("memo")) {
                tx.setMemo(obj.getString("memo"));
            }

            transactions.add(tx);
        }

        return transactions;
    }

    private void showError(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        textEmpty.setText(msg);
        textEmpty.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }
}