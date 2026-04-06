package com.example.team19bank.model;

import com.google.gson.annotations.SerializedName;

public class Loan {

    @SerializedName("loanId")
    private Long loanId;

    @SerializedName("loanAmount")
    private Double loanAmount;

    @SerializedName("loanGiverAccount")
    private String loanGiverAccount;

    @SerializedName("loanReceiverAccount")
    private String loanReceiverAccount;

    @SerializedName("authenticatedUser")
    private String authenticatedUser;

    @SerializedName("memo")
    private String memo;

    @SerializedName("status")
    private String status;

    @SerializedName("remainingAmount")
    private Double remainingAmount;

    @SerializedName("dueAt")
    private String dueAt;

    @SerializedName("approvedAt")
    private String approvedAt;

    @SerializedName("paidOffAt")
    private String paidOffAt;

    @SerializedName("failureReason")
    private String failureReason;

    @SerializedName("createdAt")
    private String createdAt;

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
    }

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

    public String getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void setAuthenticatedUser(String authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(Double remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public String getDueAt() {
        return dueAt;
    }

    public void setDueAt(String dueAt) {
        this.dueAt = dueAt;
    }

    public String getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(String approvedAt) {
        this.approvedAt = approvedAt;
    }

    public String getPaidOffAt() {
        return paidOffAt;
    }

    public void setPaidOffAt(String paidOffAt) {
        this.paidOffAt = paidOffAt;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}