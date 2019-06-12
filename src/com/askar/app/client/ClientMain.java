package com.askar.app.client;

public class ClientMain {

    private static String Ip = "localhost";
    private static int port = 9022;

    public static void main(String[] args) {
        new Client(Ip, port);
    }

}
