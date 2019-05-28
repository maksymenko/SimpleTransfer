package com.simpletransfer.service;

import com.simpletransfer.dto.AccountDto;
import com.simpletransfer.dto.TransactionDto;
import com.simpletransfer.exceptions.AccountNotFoundException;
import com.simpletransfer.exceptions.InsufficiantBalanceException;
import com.simpletransfer.repository.Account;
import com.simpletransfer.repository.AccountRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private AccountRepository accountRepository;

    private AccountService accountService;

    @Captor
    private ArgumentCaptor<Account> accountCaptor;

    @Captor
    private ArgumentCaptor<Account> accountTransferCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        accountService = new AccountService(accountRepository);
    }

    @Test
    public void shouldFetchAccountFromRepository() throws AccountNotFoundException {
        Account expectedAccount = new Account("123", "test_user", 100L);

        when(accountRepository.getAccountById(expectedAccount.getId())).thenReturn(expectedAccount);

        Account actualAccount = accountService.getAccountById(expectedAccount.getId());

        assertThat(actualAccount).isEqualTo(expectedAccount);
    }

    @Test
    public void shouldCreateAccount() throws AccountNotFoundException {
        String accountId = "123";
        String ownerName = "test user 2";
        Account expectedAccount = new Account();
        expectedAccount.setId(accountId);
        expectedAccount.setOwnerName(ownerName);

        when(accountRepository.createAccount(accountCaptor.capture())).thenReturn(accountId);
        when(accountRepository.getAccountById(accountId)).thenReturn(expectedAccount);

        AccountDto accountDto = new AccountDto();
        accountDto.setOwnerName(ownerName);

        Account createsAccount = accountService.createAccount(accountDto);

        Account passedAccount = accountCaptor.getValue();

        assertThat(passedAccount.getOwnerName()).isEqualTo(ownerName);

        assertThat(createsAccount).isEqualTo(expectedAccount);
    }

    @Test
    public void shouldUpdateAccount() throws AccountNotFoundException {
        String accountId = "asd";
        String updatedOwnerName = "new name";
        long updatedBalance = 234L;

        Account storedAccount = new Account();
        storedAccount.setId(accountId);
        storedAccount.setOwnerName("initial name");
        storedAccount.setBalance(100L);

        Account updatedAccount = new Account();
        updatedAccount.setId(accountId);
        updatedAccount.setOwnerName(updatedOwnerName);
        updatedAccount.setBalance(updatedBalance);

        when(accountRepository.getAccountById(accountId)).thenReturn(storedAccount, updatedAccount);

        Account account = accountService.updateAccount(accountId, new AccountDto(accountId, updatedOwnerName,
                updatedBalance));

        verify(accountRepository).updateAccount(accountCaptor.capture());
        Account passedAccount = accountCaptor.getValue();
        assertThat(passedAccount).isEqualTo(updatedAccount);
        assertThat(account).isEqualTo(updatedAccount);
    }


    @Test
    public void shouldThrowExceptionIfAmountInvalid() throws InsufficiantBalanceException, AccountNotFoundException {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(-10);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Transfer amount must me positive");

        accountService.transfer(transactionDto);
    }

    @Test
    public void shouldTransferSuccessfully() throws InsufficiantBalanceException, AccountNotFoundException {
        String fromAccountId = "fromAccountId";
        String toAccountId = "toAccountId";

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(20);
        transactionDto.setFromAccountId(fromAccountId);
        transactionDto.setToAccountId(toAccountId);

        Account fromAccount = new Account();
        fromAccount.setId(fromAccountId);
        fromAccount.setOwnerName("user_1");
        fromAccount.setBalance(100L);

        Account toAccount = new Account();
        toAccount.setId(fromAccountId);
        toAccount.setOwnerName("user_2");
        toAccount.setBalance(10L);

        when(accountRepository.getAccountById(fromAccountId)).thenReturn(fromAccount);
        when(accountRepository.getAccountById(toAccountId)).thenReturn(toAccount);

        accountService.transfer(transactionDto);

        verify(accountRepository, times(2)).updateAccount(accountTransferCaptor.capture());
        List<Account> updatedAccount = accountTransferCaptor.getAllValues();

        assertThat(updatedAccount.get(0).getBalance()).isEqualTo(80L);
        assertThat(updatedAccount.get(1).getBalance()).isEqualTo(30L);
    }

    @Test
    public void shouldThrowExceptionIfInsufficientBalance() throws InsufficiantBalanceException,
            AccountNotFoundException {
        String fromAccountId = "fromAccountId";
        String toAccountId = "toAccountId";

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(200);
        transactionDto.setFromAccountId(fromAccountId);
        transactionDto.setToAccountId(toAccountId);

        Account fromAccount = new Account();
        fromAccount.setId(fromAccountId);
        fromAccount.setOwnerName("user_1");
        fromAccount.setBalance(100L);

        when(accountRepository.getAccountById(fromAccountId)).thenReturn(fromAccount);

        thrown.expect(InsufficiantBalanceException.class);
        thrown.expectMessage("Account balance less then transfer amount");

        accountService.transfer(transactionDto);
    }
}
