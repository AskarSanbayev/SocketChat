package com.askar.app.story;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;

public class History {

    private static Logger logger = LogManager.getLogger();
    private static LinkedList<String> storyList = new LinkedList<>();
    private static final int MAX_AMOUNT_OF_STORY = 10;


    public static void addStory(String message) {
        if (storyList.size() >= MAX_AMOUNT_OF_STORY) {
            storyList.removeFirst();
            storyList.add(message);
        } else {
            storyList.add(message);
        }
    }

    public static void printStory(BufferedWriter writer) {
        if (!storyList.isEmpty()) {
            try {
                writer.write("History of messages :" + "\n");
                for (String el : storyList) {
                    writer.write(el + "\n");
                }
                writer.flush();
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }
}
