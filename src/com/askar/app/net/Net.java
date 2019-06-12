package com.askar.app.net;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class Net {

    private static final Logger LOGGER = LogManager.getLogger();
    private Socket socket;
    private BufferedReader input;
    private BufferedWriter output;
    private String word;

    public Net(String address, int port) {
        try {
            socket = new Socket(address, port);
        } catch (IOException e) {
            offService();
        }
        try {
            if (socket != null) {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            }
        } catch (IOException e) {
            offService();
        }
    }

    public boolean isConnected() {
        return !socket.isClosed();
    }

    public String receive() {
        try {
            word = input.readLine();
        } catch (IOException e) {
            offService();
        }
        return word;
    }

    public void send(String message) {
        try {
            output.write(message);
            output.flush();
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    public void offService() {
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
