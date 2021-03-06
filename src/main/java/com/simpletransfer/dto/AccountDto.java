package com.simpletransfer.dto;

public class AccountDto {
    private String id;
    private String ownerName;
    private Long balance;

    public AccountDto() {
    }

    public AccountDto(String id, String ownerName, Long balance) {
        this.id = id;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "AccountDto{" + "id='" + id + '\'' + ", ownerName='" + ownerName + '\'' + ", balance=" + balance + '}';
    }
}
