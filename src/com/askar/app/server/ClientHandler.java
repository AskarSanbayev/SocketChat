package com.askar.app.server;

import com.askar.app.validator.ClientAmountValidator;
import com.askar.app.story.History;
import com.askar.app.util.ClientAmountGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {

    private static final Logger LOGGER = LogManager.getLogger();
    private Socket socket;
    private BufferedWriter output;
    private BufferedReader input;
    private BufferedWriter writeToFile;
    private boolean running = true;
    private static final String ARCHIVE_PATH = "data/archive.txt";

    public ClientHandler(Socket socket) {
        this.socket = socket;
        ClientAmountGenerator.increment();
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writeToFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ARCHIVE_PATH, false)));
            checkClientAmount();
        } catch (IOException e) {
            LOGGER.error(e);
        }
        start();
    }

    @Override
    public void run() {
        String word;
        try {
            word = input.readLine();
            String finalWord = word;
            Server.serverlist.stream().forEach(e -> {
                if (!e.equals(this)) {
                    e.send(finalWord + " connected to chat");
                }
            });
            LOGGER.info(word + " connected to chat" + "\n");
            send("Welcome to chat " + word + "!" + "\n");
            History.printStory(output);
        } catch (IOException e) {
            LOGGER.error(e);
        }
        try {
            while (running) {
                word = input.readLine();
                if ("quit".equals(word)) {
                    Server.serverlist.remove(this);
                    ClientAmountGenerator.decrement();
                    running = false;
                } else {
                    System.out.println(word);
                    History.addStory(word);
                    writeToFile.write(word + "\n");
                    writeToFile.flush();
                    for (ClientHandler elem : Server.serverlist) {
                        elem.send(word);
                    }
                }
            }
            offService();
        } catch (IOException e) {
            LOGGER.error(e);
        }

    }

    private void send(String message) {
        try {
            output.write(message + "\n");
            output.flush();
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    private void checkClientAmount() {
        if (ClientAmountValidator.amountIsValid(ClientAmountGenerator.getCounter())) {
            send("full" + "\n");
            running = false;
        }
    }

    private void offService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                input.close();
                output.close();
            }
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }
}
