package com.simpletransfer.rest;

import static spark.Spark.exception;
import static spark.Spark.post;

import com.google.gson.Gson;
import com.simpletransfer.dto.TransactionDto;
import com.simpletransfer.exceptions.ErrorResponseDto;
import com.simpletransfer.exceptions.InsufficiantBalanceException;
import com.simpletransfer.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;


/**
 * Provides REST Api for transactions entity.
 */
public class TransactionController implements RestController {
    private final static Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);

    private final AccountService accountService;
    private final Gson gson = new Gson();

    @Inject
    public TransactionController(AccountService accountService) {
        this.accountService = accountService;
    }

    public void init() {
        post("/transactions/", (req, res) -> {
            accountService.transfer(gson.fromJson(req.body(), TransactionDto.class));
            res.status(201);
            return "";
        });

        exception(InsufficiantBalanceException.class, (exception, request, response) -> {
            LOGGER.error("Transfer amount has to be less that account valance", exception);
            ErrorResponseDto errorResponse = new ErrorResponseDto(400, exception.getMessage());
            response.body(gson.toJson(errorResponse));
            response.status(400);
        });
    }
}
