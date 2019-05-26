package com.simpletransfer.repository;

public interface AccountRepository {
    Account getAccountById(String id);
    String createAccount(Account account);
    void updateAccount(Account account);
}
