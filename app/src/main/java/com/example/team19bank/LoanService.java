package com.example.team19bank;

import android.content.Context;

import com.example.team19bank.model.Loan;
import com.example.team19bank.model.LoanPaymentRequest;
import com.example.team19bank.model.LoanRequest;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoanService {
    private final BankApi api;

    public interface LoanActionCallback {
        void onSuccess(String message, Loan loan);
        void onError(String errorMsg);
    }

    public interface LoanListCallback {
        void onSuccess(List<Loan> loans);
        void onError(String errorMsg);
    }

    public LoanService(Context context) {
        this.api = NetworkService.getApi(context);
    }

    public void applyForLoan(LoanRequest req, LoanActionCallback callback) {
        api.applyForLoan(req).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> body = response.body();
                    String message = body.get("message") != null ? body.get("message").toString() : "Success";
                    callback.onSuccess(message, null);
                } else {
                    callback.onError(readError(response, "Loan request failed."));
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void getMyLoans(LoanListCallback callback) {
        api.getMyLoans().enqueue(new Callback<List<Loan>>() {
            @Override
            public void onResponse(Call<List<Loan>> call, Response<List<Loan>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(readError(response, "Failed to load loans."));
                }
            }

            @Override
            public void onFailure(Call<List<Loan>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void getPendingApprovals(LoanListCallback callback) {
        api.getPendingApprovalLoans().enqueue(new Callback<List<Loan>>() {
            @Override
            public void onResponse(Call<List<Loan>> call, Response<List<Loan>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(readError(response, "Failed to load pending approvals."));
                }
            }

            @Override
            public void onFailure(Call<List<Loan>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void approveLoan(Long loanId, String dueAt, LoanActionCallback callback) {
        api.approveLoan(loanId, dueAt).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body().get("message") != null
                            ? response.body().get("message").toString()
                            : "Loan approved";
                    callback.onSuccess(message, null);
                } else {
                    callback.onError(readError(response, "Approve failed."));
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void rejectLoan(Long loanId, LoanActionCallback callback) {
        api.rejectLoan(loanId).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body().get("message") != null
                            ? response.body().get("message").toString()
                            : "Loan rejected";
                    callback.onSuccess(message, null);
                } else {
                    callback.onError(readError(response, "Reject failed."));
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void payLoan(LoanPaymentRequest req, LoanActionCallback callback) {
        api.payLoan(req).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body().get("message") != null
                            ? response.body().get("message").toString()
                            : "Payment successful";
                    callback.onSuccess(message, null);
                } else {
                    callback.onError(readError(response, "Payment failed."));
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    private String readError(Response<?> response, String fallback) {
        try {
            if (response.errorBody() != null) {
                return response.errorBody().string();
            }
        } catch (Exception ignored) {
        }
        return fallback;
    }
}