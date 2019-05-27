package com.simpletransfer.rest;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

public class RestModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<RestController> restBinder = Multibinder.newSetBinder(binder(), RestController.class);
        restBinder.addBinding().to(AccountController.class);
        restBinder.addBinding().to(TransactionController.class);
    }

}
