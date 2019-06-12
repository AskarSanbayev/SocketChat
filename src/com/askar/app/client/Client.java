package com.askar.app.client;

import com.askar.app.net.Net;
import com.askar.app.validator.NameValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Client {

    private static final Logger LOGGER = LogManager.getLogger();
    private BufferedReader consoleReader;
    private String name;
    private String address;
    private int port;
    private Thread writeMessage, readMessage;
    private ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
    private LocalTime dTime = zonedDateTime.toLocalTime();
    private Net net;
    private boolean running = true;

    public Client(String address, int port) {
        this.address = address;
        this.port = port;
        net = new Net(address, port);
        consoleReader = new BufferedReader(new InputStreamReader(System.in));
        printName();
        readMessage();
        writeMessage();
    }

    private void printName() {
        System.out.println("Print your name:");
        try {
            name = consoleReader.readLine();
            while (NameValidator.nameIsValid(name)) {
                System.out.println("Name should contain more than 1 letter");
                name = consoleReader.readLine();
            }
            net.send(name + "\n");
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    private void closeReader() {
        try {
            consoleReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMessage() {
        readMessage = new Thread(() -> {
            String str = net.receive();
            while (net.isConnected()) {
                if ("full".equals(str)) {
                    System.out.println("Currently server is full \n See you next time" + name);
                    closeReader();
                    break;
                } else {
                    System.out.println(str);
                    str = net.receive();
                }
            }
        }, "readMessage");
        readMessage.start();
    }

    private void writeMessage() {
        writeMessage = new Thread(() -> {
            while (running) {
                String userWord;
                try {
                    String time = dTime.getHour() + ":" + dTime.getMinute() + ":" + dTime.getSecond();
                    userWord = consoleReader.readLine();
                    if (userWord.equals("quit")) {
                        net.send(userWord + "\n");
                        running = false;
                    } else {
                        net.send("(" + time + ") " + name + " says : " + userWord + "\n");
                    }
                } catch (IOException e) {
                    break;
                }
            }
            net.offService();
        }, "writeMessage");
        writeMessage.start();
    }
}

