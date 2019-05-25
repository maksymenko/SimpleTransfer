package com.simpletransfer.service;

import com.simpletransfer.repository.AccountRepository;

import javax.inject.Inject;

public class AccountService {
    private final AccountRepository accountRepository;

    @Inject
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public String getAccount() {
        return "account";
    }
}
