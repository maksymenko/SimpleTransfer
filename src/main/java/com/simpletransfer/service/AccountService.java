package com.simpletransfer.service;

import com.google.common.base.Preconditions;
import com.simpletransfer.dto.AccountDto;
import com.simpletransfer.dto.TransactionDto;
import com.simpletransfer.exceptions.AccountNotFoundException;
import com.simpletransfer.exceptions.InsufficiantBalanceException;
import com.simpletransfer.repository.Account;
import com.simpletransfer.repository.AccountRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class AccountService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository accountRepository;

    @Inject
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccountById(String id) throws AccountNotFoundException {
        return accountRepository.getAccountById(id);
    }

    public Account createAccount(AccountDto accountDto) throws AccountNotFoundException {
        Preconditions.checkArgument(accountDto.getOwnerName() != null, "Name is required to create account");
        Account account = new Account();
        account.setOwnerName(accountDto.getOwnerName());
        if (accountDto.getBalance() != null) {
            account.setBalance(accountDto.getBalance());
        }
        String accountId = accountRepository.createAccount(account);
        LOGGER.debug("created account: " + account);

        return accountRepository.getAccountById(accountId);
    }

    public Account updateAccount(String accountId, AccountDto accountDto) throws AccountNotFoundException {
        Account account = accountRepository.getAccountById(accountId);
        if (accountDto.getOwnerName() != null) {
            account.setOwnerName(accountDto.getOwnerName());
        }
        if (accountDto.getBalance() != null) {
            account.setBalance(accountDto.getBalance());
        }
        synchronized (this) {
            accountRepository.updateAccount(account);
        }
        LOGGER.debug("account: " + account.getId() + " updated: " + accountDto);
        return accountRepository.getAccountById(accountId);
    }

    public void transfer(TransactionDto transactionDto) throws AccountNotFoundException, InsufficiantBalanceException {
        if (transactionDto.getAmount() <= 0) {
            throw new IllegalArgumentException("Transfer amount must me positive");
        }
        synchronized (this) {
            Account accountFrom = accountRepository.getAccountById(transactionDto.getFromAccountId());
            Account accountTo = accountRepository.getAccountById(transactionDto.getToAccountId());

            if (accountFrom.getBalance() >= transactionDto.getAmount()) {
                accountFrom.setBalance(accountFrom.getBalance() - transactionDto.getAmount());
                accountTo.setBalance(accountTo.getBalance() + transactionDto.getAmount());

                accountRepository.updateAccount(accountFrom);
                accountRepository.updateAccount(accountTo);
            } else {
                throw new InsufficiantBalanceException("Account balance less then transfer amount");
            }
        }
    }
}
