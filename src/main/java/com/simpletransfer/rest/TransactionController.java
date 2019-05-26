package com.simpletransfer.rest;

import static spark.Spark.post;

import com.google.gson.Gson;
import com.simpletransfer.dto.TransactionDto;
import com.simpletransfer.service.AccountService;
import javax.inject.Inject;


public class TransactionController implements RestController {
    private final AccountService accountService;
    private final Gson gson = new Gson();

    @Inject
    public TransactionController(AccountService accountService) {
        this.accountService = accountService;
    }

    public void init() {
        post("/transaction", (req, res) -> accountService.transfer(gson.fromJson(req.body(), TransactionDto.class)));
    }
}
