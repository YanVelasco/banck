package com.bank.cards.exceptions;

public class SubscriptionAlreadyExistsException extends RuntimeException{
    public SubscriptionAlreadyExistsException(String message) {
        super(message);
    }
}
