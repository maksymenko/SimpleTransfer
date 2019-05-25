package com.simpletransfer.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InMemoryAccountRepository implements AccountRepository {
    private Map<String, Account> storage = new HashMap<>();

    @Override
    public Account getAccountById(String id) {
        if (storage.containsKey(id)) {
            return storage.get(id);
        } else {
            throw new IllegalArgumentException("Account not found");
        }
    }

    @Override
    public Account createAccount(String name) {
        Account account = new Account();
        account.setId(UUID.randomUUID().toString());
        account.setName(name);
        // By default each account is initialized with 100 unit balance.
        account.setAmount(100);
        storage.put(account.getId(), account);

        return account;
    }

    @Override
    public Account updateAccount(Account account) {
        storage.put(account.getId(), account);
        return account;
    }
}
