package com.simpletransfer;


import static spark.Spark.get;

public class RestServer {
    public static void main(String[] args) {
        System.out.println(">>>> simple transfer");
        get("/hello", (req, res) -> "Hello Simple Transfer");
    }
}
