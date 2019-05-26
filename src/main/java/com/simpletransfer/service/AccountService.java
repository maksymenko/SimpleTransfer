package com.simpletransfer.service;

import com.simpletransfer.dto.AccountDto;
import com.simpletransfer.dto.TransactionDto;
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

    public Account getAccountById(String id) {
        return accountRepository.getAccountById(id);
    }

    public String createAccount(AccountDto accountDto) {
        Account account = new Account();
        account.setOwnerName(accountDto.getOwnerName());
        account.setBalance(accountDto.getBalance());
        String accountId = accountRepository.createAccount(account);
        LOGGER.debug("created account: " + account);

        return accountId;
    }

    public void updateAccount(AccountDto accountDto) {
        Account account = new Account(accountDto.getId(), accountDto.getOwnerName(), accountDto.getBalance());
        synchronized (this) {
            accountRepository.updateAccount(account);
        }
        LOGGER.debug("account: " + account.getId() + " updated: " + accountDto);
    }

    public String transfer(TransactionDto transactionDto) {
        LOGGER.debug("transfer: " + transactionDto);
        return UUID.randomUUID().toString();
    }
}
