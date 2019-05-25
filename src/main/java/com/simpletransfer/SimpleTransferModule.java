package com.simpletransfer;

import com.google.inject.AbstractModule;
import com.simpletransfer.repository.RepositoryModule;
import com.simpletransfer.rest.RestModule;

public class SimpleTransferModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new RepositoryModule());
        install(new RestModule());
    }
}
