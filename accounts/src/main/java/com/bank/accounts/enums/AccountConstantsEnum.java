package com.bank.accounts.enums;

import lombok.Getter;

@Getter
public enum AccountConstantsEnum {

    SAVINGS("savings"),
    ADDRESS("123 Main Street, New York"),
    STATUS_201("201"),
    MESSAGE_201("Account created successfully"),
    STATUS_200("200"),
    MESSAGE_200("Account retrieved successfully"),
    STATUS_500("500"),
    MESSAGE_500("Internal server error"),
    STATUS_204("204");

    private final String value;

    AccountConstantsEnum(String value) {
        this.value = value;
    }

}
