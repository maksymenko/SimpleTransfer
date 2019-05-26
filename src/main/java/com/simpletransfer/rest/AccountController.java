package com.simpletransfer.rest;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import com.google.gson.Gson;
import com.simpletransfer.dto.AccountDto;
import com.simpletransfer.service.AccountService;

import javax.inject.Inject;


public class AccountController implements RestController {
    private final AccountService accountService;
    private final Gson gson = new Gson();

    @Inject
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    public void init() {
        // Returns Account by id
        get("/accounts/:id", (req, res) -> accountService.getAccountById(req.params("id")), gson::toJson);

        // Creates new Account
        post("/accounts/", (req, res) -> accountService.createAccount(gson.fromJson(req.body(), AccountDto.class)));

        // update  Account
        put("/accounts/:id", (req, res) -> {
            accountService.updateAccount(gson.fromJson(req.body(), AccountDto.class));
            return "OK";
        });
    }
}
