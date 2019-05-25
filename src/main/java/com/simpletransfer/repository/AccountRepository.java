package com.simpletransfer.repository;

public interface AccountRepository {
    Account getAccountById(String id);
    Account createAccount(String name);
    Account updateAccount(Account account);
}
