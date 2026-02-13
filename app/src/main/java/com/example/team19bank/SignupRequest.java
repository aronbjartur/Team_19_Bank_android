package com.example.team19bank;

// allt ai, bara mjög basic grunnur.
// Nota held ég ekki lengur neitt af þessu, en man ekki ):
// þurfum sam tlíklegast að nota þegar network tenging byrjar

public class SignupRequest {
    private String username;
    private String password;
    private String email;
    private int creditScore;

    public SignupRequest(String username, String password, String email, int creditScore) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.creditScore = creditScore;
    }
}