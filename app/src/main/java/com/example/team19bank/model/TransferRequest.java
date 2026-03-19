package com.example.team19bank.model;

import com.google.gson.annotations.SerializedName;

public class TransferRequest {
    @SerializedName("sourceAccount")
    private String sourceAccount;

    @SerializedName("destinationAccount")
    private String destinationAccount;

    @SerializedName("amount")
    private double amount;

    @SerializedName("memo")
    private String memo;

    public TransferRequest() {}

    // Setters
    public void setSourceAccount(String sourceAccount) { this.sourceAccount = sourceAccount; }
    public void setDestinationAccount(String destinationAccount) { this.destinationAccount = destinationAccount; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setMemo(String memo) { this.memo = memo; }

    // Getters
    public String getSourceAccount() { return sourceAccount; }
    public String getDestinationAccount() { return destinationAccount; }
    public double getAmount() { return amount; }
    public String getMemo() { return memo; }
}