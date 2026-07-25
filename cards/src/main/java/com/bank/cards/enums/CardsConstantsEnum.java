package com.bank.cards.enums;

import lombok.Getter;

@Getter
public enum CardsConstantsEnum {

    SAVINGS("savings"),
    ADDRESS("123 Main Street, New York"),
    CREDIT_CARD("Credit Card"),
    STATUS_201("201"),
    MESSAGE_201("Card created successfully"),
    STATUS_200("200"),
    MESSAGE_200("Card retrieved successfully"),
    STATUS_500("500"),
    MESSAGE_500("Internal server error"),
    STATUS_204("204"),
    MESSAGE_204("No content available"),
    MESSAGE_417_UPDATE("Failed to update card details"),
    STATUS_417("417"),
    MESSAGE_417_DELETE("Failed to delete card details");

    private final String value;

    CardsConstantsEnum(String value) {
        this.value = value;
    }

}
