package com.simpletransfer.rest;

import com.google.gson.Gson;
import com.simpletransfer.dto.AccountDto;
import com.simpletransfer.exceptions.AccountNotFoundException;
import com.simpletransfer.exceptions.ErrorResponseDto;
import com.simpletransfer.service.AccountService;

import javax.inject.Inject;

import static spark.Spark.*;


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

        // Update  Account
        put("/accounts/:id", (req, res) -> {
            accountService.updateAccount(gson.fromJson(req.body(), AccountDto.class));
            return "OK";
        });

        // Handle error when requested account does not exist.
        exception(AccountNotFoundException.class, (exception, request, response) -> {
            ErrorResponseDto errorResponse = new ErrorResponseDto(404, "Account not found");
            response.body(gson.toJson(errorResponse));
            response.status(404);
        });
    }
}
