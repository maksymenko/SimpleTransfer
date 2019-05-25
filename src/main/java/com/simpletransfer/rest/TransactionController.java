package com.simpletransfer.rest;

public class TransactionController implements RestController {

    @Override
    public void init() {
        System.out.println(">>>> transaction controller");
    }
}
