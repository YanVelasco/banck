package com.bank.accounts.exceptions;

public class SubscriptionAlreadyExistsException extends RuntimeException{
    public SubscriptionAlreadyExistsException(String message) {
        super(message);
    }
}
