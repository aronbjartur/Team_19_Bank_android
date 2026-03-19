package com.example.team19bank;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "transactions")
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    private Long id;

    @SerializedName("amount")
    private double amount;

    @SerializedName("sourceAccount")
    private String sourceAccount;

    @SerializedName("destinationAccount")
    private String destinationAccount;

    @SerializedName("status")
    private String status;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("memo")
    private String memo;

    public Transaction() {}

    // GETTERS
    public Long getId() { return id; }
    public double getAmount() { return amount; }
    public String getSourceAccount() { return sourceAccount; }
    public String getDestinationAccount() { return destinationAccount; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
    public String getMemo() { return memo; }

    // SETTERS
    public void setId(Long id) { this.id = id; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setSourceAccount(String sourceAccount) { this.sourceAccount = sourceAccount; }
    public void setDestinationAccount(String destinationAccount) { this.destinationAccount = destinationAccount; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setMemo(String memo) { this.memo = memo; }
}