package com.example.team19bank.model;

import com.google.gson.annotations.SerializedName;

/**
 * Módel fyrir lánabeiðni sem passar 1:1 við Spring Boot bakendann.
 * SerializedName tryggir að JSON lyklarnir séu nákvæmlega þeir sömu og bakendinn notar.
 */
public class LoanRequest {

    @SerializedName("loanGiverAccount")
    private String loanGiverAccount;

    @SerializedName("loanReceiverAccount")
    private String loanReceiverAccount;

    @SerializedName("amount")
    private double amount;

    @SerializedName("memo")
    private String memo;


    public LoanRequest() {
    }

    // Smiður til þæginda
    public LoanRequest(String loanGiverAccount, String loanReceiverAccount, double amount, String memo) {
        this.loanGiverAccount = loanGiverAccount;
        this.loanReceiverAccount = loanReceiverAccount;
        this.amount = amount;
        this.memo = memo;
    }

    // Getterar og Setterar
    public String getLoanGiverAccount() {
        return loanGiverAccount;
    }

    public void setLoanGiverAccount(String loanGiverAccount) {
        this.loanGiverAccount = loanGiverAccount;
    }

    public String getLoanReceiverAccount() {
        return loanReceiverAccount;
    }

    public void setLoanReceiverAccount(String loanReceiverAccount) {
        this.loanReceiverAccount = loanReceiverAccount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return "LoanRequest{" +
                "giver='" + loanGiverAccount + '\'' +
                ", receiver='" + loanReceiverAccount + '\'' +
                ", amount=" + amount +
                ", memo='" + memo + '\'' +
                '}';
    }
}