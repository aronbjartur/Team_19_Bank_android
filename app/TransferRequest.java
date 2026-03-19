package com.example.team19bank.model;

public class TransferRequest {
    private String sourceAccount;
    private String destinationAccount;
    private double amount;
    private String memo;

    // Setters sem TransferLoanFragment notar
    public void setSourceAccount(String sourceAccount) { this.sourceAccount = sourceAccount; }
    public void setDestinationAccount(String destinationAccount) { this.destinationAccount = destinationAccount; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setMemo(String memo) { this.memo = memo; }
}