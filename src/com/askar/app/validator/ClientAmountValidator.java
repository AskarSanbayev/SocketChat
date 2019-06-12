package com.askar.app.validator;

public class ClientAmountValidator {

    public static final int MAX_CLIENT_AMOUNT = 3;

    public static boolean amountIsValid(int size) {
        return size > MAX_CLIENT_AMOUNT;
    }
}
