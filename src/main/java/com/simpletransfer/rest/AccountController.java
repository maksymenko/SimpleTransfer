package com.simpletransfer.rest;

import com.simpletransfer.service.AccountService;

import javax.inject.Inject;

import static spark.Spark.get;

public class AccountController implements RestController {
    private final AccountService accountService;

    @Inject
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    public void init() {
        System.out.println(">>>>> accountController");
        get("/account", (req, res) -> accountService.getAccount());
    }
}
