package com.simpletransfer;


import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.simpletransfer.rest.RestController;

import java.util.Set;

public class SparkApp {
    private Set<RestController> controllers;

    @Inject
    public SparkApp(Set<RestController> controllers) {
        this.controllers = controllers;
    }

    public void start() {
        controllers.forEach(controller -> controller.init());
    }

    public static void main(String[] args) {
        System.out.println(">>>> simple transfer");

        Injector injector = Guice.createInjector(new SimpleTransferModule());

        injector.getInstance(SparkApp.class).start();
    }
}
