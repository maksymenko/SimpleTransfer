package com.simpletransfer.repository;

import com.simpletransfer.exceptions.AccountNotFoundException;

/**
 * Defines repository API for account storage.
 */
public interface AccountRepository {
    Account getAccountById(String id) throws AccountNotFoundException;

    String createAccount(Account account);

    void updateAccount(Account account) throws AccountNotFoundException;
}
