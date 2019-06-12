package com.askar.app.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int PORT = 9022;
    public static List<ClientHandler> serverlist = new ArrayList<>();


    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        LOGGER.info("server started");
        try {
            while (true) {
                try {
                    Socket socket = server.accept();
                    serverlist.add(new ClientHandler(socket));
                } catch (IOException e) {
                    LOGGER.error(e);
                }
            }
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }
    }
}

