package com.simpletransfer.rest;

import static org.assertj.core.api.Assertions.assertThat;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.simpletransfer.SimpleTransferModule;
import com.simpletransfer.SparkApp;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;


public class AccountControllerTest {

    @BeforeClass
    public static void setUpClass() {
        Injector injector = Guice.createInjector(new SimpleTransferModule());
        injector.getInstance(SparkApp.class).start();
        Spark.awaitInitialization();
    }

    @AfterClass
    public static void tearDownClass() {
        Spark.stop();
    }


    @Test
    public void shouldCreateAccount() throws Exception {
        String id = RestTestUtils.post("accounts/", "{ownerName: \"test_user_1\"}");

        String resp = RestTestUtils.get("accounts/" + id);

        assertThat(resp).contains("test_user_1");
        assertThat(resp).contains(id);
    }

}
