package com.simpletransfer.rest;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.simpletransfer.SimpleTransferModule;
import com.simpletransfer.SparkApp;
import com.simpletransfer.dto.AccountDto;
import com.simpletransfer.dto.TransactionDto;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;

import java.io.IOException;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionControllerTest {
    private final static String TRANSACTIONS_PATH = "transactions/";
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
        Spark.awaitStop();
    }

    @Test
    public void shouldTransferSuccessfully() throws IOException, InterruptedException {

        // Create account 'from'
        AccountDto accountFrom = new AccountDto();
        accountFrom.setOwnerName("Test User 1");
        HttpResponse<String> createAccountFromResponse = RestTestUtils.post(ACCOUNT_PATH, gson.toJson(accountFrom));
        AccountDto createdAccountFrom = gson.fromJson(createAccountFromResponse.body(), AccountDto.class);

        // Deposit to account 'from'
        long accountFromInitialBalance = 100L;
        accountFrom.setBalance(accountFromInitialBalance);
        RestTestUtils.put(ACCOUNT_PATH + createdAccountFrom.getId(), gson.toJson(accountFrom));

        // Create account 'to'
        AccountDto accountTo = new AccountDto();
        accountTo.setOwnerName("Test User 2");
        HttpResponse<String> createAccountToResponse = RestTestUtils.post(ACCOUNT_PATH, gson.toJson(accountTo));
        AccountDto createdAccountTo = gson.fromJson(createAccountToResponse.body(), AccountDto.class);

        // Check account 'to' balance
        HttpResponse<String> getAccountToResponse = RestTestUtils.get(ACCOUNT_PATH + createdAccountTo.getId());
        accountTo = gson.fromJson(getAccountToResponse.body(), AccountDto.class);
        assertThat(accountTo.getBalance()).isEqualTo(0);

        // Make transfer
        long amount = 20L;
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(amount);
        transactionDto.setFromAccountId(createdAccountFrom.getId());
        transactionDto.setToAccountId(createdAccountTo.getId());
        RestTestUtils.post(TRANSACTIONS_PATH, gson.toJson(transactionDto));

        // Check account 'to' balance after transfer
        HttpResponse<String> getAccountToAfterResponse = RestTestUtils.get(ACCOUNT_PATH + createdAccountTo.getId());
        accountTo = gson.fromJson(getAccountToAfterResponse.body(), AccountDto.class);
        assertThat(accountTo.getBalance()).isEqualTo(amount);

        // Check account 'from' balance after transfer
        HttpResponse<String> getAccountFromAfterResponse = RestTestUtils.get(ACCOUNT_PATH + createdAccountFrom.getId());
        accountFrom = gson.fromJson(getAccountFromAfterResponse.body(), AccountDto.class);
        assertThat(accountFrom.getBalance()).isEqualTo(accountFromInitialBalance - amount);
    }
}
