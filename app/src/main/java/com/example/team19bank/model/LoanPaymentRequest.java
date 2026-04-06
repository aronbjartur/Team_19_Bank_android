package com.example.team19bank.model;

import com.google.gson.annotations.SerializedName;

public class LoanPaymentRequest {

    @SerializedName("payerAccount")
    private String payerAccount;

    @SerializedName("amount")
    private double amount;

    @SerializedName("lenderAccount")
    private String lenderAccount;

    @SerializedName("loanId")
    private Long loanId;

    public LoanPaymentRequest() {
    }

    public LoanPaymentRequest(String payerAccount, double amount, String lenderAccount, Long loanId) {
        this.payerAccount = payerAccount;
        this.amount = amount;
        this.lenderAccount = lenderAccount;
        this.loanId = loanId;
    }

    public String getPayerAccount() {
        return payerAccount;
    }

    public void setPayerAccount(String payerAccount) {
        this.payerAccount = payerAccount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getLenderAccount() {
        return lenderAccount;
    }

    public void setLenderAccount(String lenderAccount) {
        this.lenderAccount = lenderAccount;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }
}