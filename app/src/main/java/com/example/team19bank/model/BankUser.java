package com.example.team19bank.model;

import com.google.gson.annotations.SerializedName;

public class BankUser {
    @SerializedName("id")
    private Long id;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("email")
    private String email;

    @SerializedName("creditScore")
    private int creditScore;


    public BankUser(String username, String password, String email, int creditScore) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.creditScore = creditScore;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public int getCreditScore() { return creditScore; }

    public String getPassword() { return password; }
    public String getEmail() { return email; }
}