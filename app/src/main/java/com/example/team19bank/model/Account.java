package com.example.team19bank.model;
import com.google.gson.annotations.SerializedName;

public class Account {
    @SerializedName("accountNumber")
    private String accountNumber;
    @SerializedName("balance")
    private double balance;

    public Account() {}

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}