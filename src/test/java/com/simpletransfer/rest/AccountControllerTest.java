package com.simpletransfer.rest;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.simpletransfer.SimpleTransferModule;
import com.simpletransfer.SparkApp;
import com.simpletransfer.dto.AccountDto;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;

import java.io.IOException;
import java.net.http.HttpResponse;


public class AccountControllerTest {
    private final static String ACCOUNT_PATH = "accounts/";
    private final Gson gson = new Gson();

    @BeforeClass
    public static void setUpClass() {
        Injector injector = Guice.createInjector(new SimpleTransferModule());
        injector.getInstance(SparkApp.class).start();
        Spark.awaitInitialization();
    }

    @AfterClass
    public static void tearDownClass() {
        Spark.stop();
    }

    @Test
    public void shouldReturn404IfAccountNotFound() throws IOException, InterruptedException {
        HttpResponse<String> getAccountResponse = RestTestUtils.get(ACCOUNT_PATH + "123");
        assertThat(getAccountResponse.statusCode()).isEqualTo(404);
        assertThat(getAccountResponse.body()).contains("Account not found");
    }

    @Test
    public void shouldCreateAccount() throws Exception {
        AccountDto account = new AccountDto();
        account.setOwnerName("Test User");

        HttpResponse<String> createAccountResponse = RestTestUtils.post(ACCOUNT_PATH, gson.toJson(account));

        assertThat(createAccountResponse.statusCode()).isEqualTo(201);
        assertThat(createAccountResponse.body()).isNotBlank();
        AccountDto createdAccount = gson.fromJson(createAccountResponse.body(), AccountDto.class);
        assertThat(createdAccount.getOwnerName()).isEqualTo(account.getOwnerName());
        assertThat(createdAccount.getBalance()).isEqualTo(0);

        HttpResponse<String> getAccountResponse = RestTestUtils.get(ACCOUNT_PATH + createdAccount.getId());

        assertThat(getAccountResponse.statusCode()).isEqualTo(200);
        AccountDto fetchedAccount = gson.fromJson(createAccountResponse.body(), AccountDto.class);
        assertThat(fetchedAccount.getOwnerName()).isEqualTo(account.getOwnerName());
        assertThat(fetchedAccount.getId()).isEqualTo(createdAccount.getId());
    }

    @Test
    public void shouldUpdateAccount() throws Exception {
        AccountDto account = new AccountDto();
        account.setOwnerName("Test User");

        HttpResponse<String> createAccountResponse = RestTestUtils.post(ACCOUNT_PATH, gson.toJson(account));
        AccountDto createdAccount = gson.fromJson(createAccountResponse.body(), AccountDto.class);

        createdAccount.setOwnerName("Another User");
        createdAccount.setBalance(1000);
        HttpResponse<String> updateAccountResponse = RestTestUtils.put(ACCOUNT_PATH + createdAccount.getId(),
                gson.toJson(createdAccount));

        assertThat(updateAccountResponse.statusCode()).isEqualTo(202);
        AccountDto updatedAccount = gson.fromJson(updateAccountResponse.body(), AccountDto.class);
        assertThat(updatedAccount.getOwnerName()).isEqualTo(createdAccount.getOwnerName());
        assertThat(updatedAccount.getBalance()).isEqualTo(createdAccount.getBalance());
    }
}
