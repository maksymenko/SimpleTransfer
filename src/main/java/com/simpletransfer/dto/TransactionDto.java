package com.simpletransfer.dto;

public class TransactionDto {
    private String fromAccountId;
    private String toAccountId;
    private long amount;

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TransactionDto{" +
                "fromAccountId='" + fromAccountId + '\'' +
                ", toAccountId='" + toAccountId + '\'' +
                ", amount=" + amount +
                '}';
    }
}
