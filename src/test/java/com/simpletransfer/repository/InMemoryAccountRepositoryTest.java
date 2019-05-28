package com.simpletransfer.repository;


import com.simpletransfer.exceptions.AccountNotFoundException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class InMemoryAccountRepositoryTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private InMemoryAccountRepository repository = new InMemoryAccountRepository();

    @Test
    public void getAccountById_shouldThrowExceptionIfNotFound() throws Exception {
        thrown.expect(AccountNotFoundException.class);
        thrown.expectMessage("not found");
        repository.getAccountById("does_not_exist_id");
    }

    @Test
    public void createAccount_shouldCreateAccount() throws Exception {
        Account account = new Account();
        account.setOwnerName("test user");
        String id = repository.createAccount(account);
        assertThat(id).isNotEmpty();

        Account createdAccount = repository.getAccountById(id);
        assertThat(createdAccount.getId()).isEqualTo(id);
        assertThat(createdAccount.getOwnerName()).isEqualTo(account.getOwnerName());
        assertThat(createdAccount.getBalance()).isEqualTo(0);
    }

    @Test
    public void updateAccount_shouldThrowExceptionIfNotFound() throws Exception {
        Account account = new Account();
        account.setId("does_not_exist_id");

        thrown.expect(AccountNotFoundException.class);
        thrown.expectMessage("not found");

        repository.updateAccount(account);
    }

    @Test
    public void updateAccount_shouldUpdateAccount() throws Exception {
        Account account = new Account();
        account.setOwnerName("test user");
        String id = repository.createAccount(account);

        account.setId(id);
        account.setOwnerName("updated name");
        account.setBalance(1000L);
        repository.updateAccount(account);

        Account updatedAccount = repository.getAccountById(id);

        assertThat(updatedAccount).isEqualTo(account);
    }
}
