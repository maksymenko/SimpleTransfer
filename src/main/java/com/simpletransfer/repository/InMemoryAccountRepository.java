package com.simpletransfer.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InMemoryAccountRepository implements AccountRepository {
    private Map<String, Account> storage = new HashMap<>();

    @Override
    public Account getAccountById(String id) {
        if (!storage.containsKey(id)) {
            throw new IllegalArgumentException("Account not found");
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
    public void updateAccount(Account account) {
        if (!storage.containsKey(account.getId())) {
            throw new IllegalArgumentException("Account not found");
        }
        storage.put(account.getId(), account);
    }
}
