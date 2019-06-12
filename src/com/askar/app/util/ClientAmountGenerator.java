package com.askar.app.util;

import java.util.concurrent.atomic.AtomicInteger;

public class ClientAmountGenerator {
    private static AtomicInteger counter = new AtomicInteger(0);

    public static void increment() {
        counter.incrementAndGet();
    }

    public static void decrement() {
        counter.decrementAndGet();
    }

    public static int getCounter(){
        return counter.get();
    }
}
