package com.simpletransfer.service;

import com.simpletransfer.dto.AccountDto;
import com.simpletransfer.exceptions.AccountNotFoundException;
import com.simpletransfer.repository.Account;
import com.simpletransfer.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    private AccountService accountService;

    @Captor
    private ArgumentCaptor<Account> accountCaptor;

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
        updatedAccount.setOwnerName("new name");
        updatedAccount.setBalance(updatedBalance);

        when(accountRepository.getAccountById(accountId)).thenReturn(storedAccount, updatedAccount);

        Account account = accountService.updateAccount(accountId, new AccountDto(accountId, updatedOwnerName,
                updatedBalance));

        verify(accountRepository).updateAccount(accountCaptor.capture());
        Account passedAccount = accountCaptor.getValue();
        assertThat(passedAccount).isEqualTo(updatedAccount);
        assertThat(account).isEqualTo(updatedAccount);
    }
}
