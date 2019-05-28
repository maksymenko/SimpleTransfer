package com.simpletransfer.repository;

import com.simpletransfer.exceptions.AccountNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Implements account storage API in memory.
 */
public class InMemoryAccountRepository implements AccountRepository {
    private Map<String, Account> storage = new HashMap<>();

    @Override
    public Account getAccountById(String id) throws AccountNotFoundException {
        if (!storage.containsKey(id)) {
            throw new AccountNotFoundException("Account " + id + " not found");
        }
        return storage.get(id);
    }

    @Override
    public String createAccount(Account account) {
        account.setId(UUID.randomUUID().toString());
        storage.put(account.getId(), account);

        return account.getId();
    }

    @Override
    public void updateAccount(Account account) throws AccountNotFoundException {
        if (!storage.containsKey(account.getId())) {
            throw new AccountNotFoundException("Account not found");
        }
        storage.put(account.getId(), account);
    }
}
