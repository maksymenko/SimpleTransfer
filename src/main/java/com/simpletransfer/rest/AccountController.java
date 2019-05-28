package com.simpletransfer.rest;

import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import com.google.gson.Gson;
import com.simpletransfer.dto.AccountDto;
import com.simpletransfer.exceptions.AccountNotFoundException;
import com.simpletransfer.exceptions.ErrorResponseDto;
import com.simpletransfer.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Provides REST Api for account entity.
 */
public class AccountController implements RestController {
    private final static Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

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
        post("/accounts/", (req, res) -> {
            res.status(201);
            return accountService.createAccount(gson.fromJson(req.body(), AccountDto.class));
        }, gson::toJson);

        // Update  Account
        put("/accounts/:id", (req, res) -> {
            res.status(202);
            return accountService.updateAccount(req.params("id"), gson.fromJson(req.body(), AccountDto.class));
        }, gson::toJson);

        // Handle error errors in application.

        exception(AccountNotFoundException.class, (exception, request, response) -> {
            LOGGER.error("Account not found", exception);
            ErrorResponseDto errorResponse = new ErrorResponseDto(404, exception.getMessage());
            response.body(gson.toJson(errorResponse));
            response.status(404);
        });

        exception(IllegalArgumentException.class, (exception, request, response) -> {
            LOGGER.error("Invalid parameter value in create account request", exception);
            ErrorResponseDto errorResponse = new ErrorResponseDto(412, exception.getMessage());
            response.body(gson.toJson(errorResponse));
            response.status(412);
        });

        exception(Exception.class, (exception, request, response) -> {
            LOGGER.error("Unexpected error", exception);
            ErrorResponseDto errorResponse = new ErrorResponseDto(400, exception.getMessage());
            response.body(gson.toJson(errorResponse));
            response.status(400);
        });
    }
}
