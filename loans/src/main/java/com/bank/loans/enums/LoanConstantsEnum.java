package com.bank.loans.enums;

import lombok.Getter;

@Getter
public enum LoanConstantsEnum {

    SAVINGS("savings"),
    ADDRESS("123 Main Street, New York"),
    STATUS_201("201"),
    MESSAGE_201("Loan created successfully"),
    STATUS_200("200"),
    MESSAGE_200("Loan retrieved successfully"),
    STATUS_500("500"),
    MESSAGE_500("Internal server error"),
    STATUS_204("204"),
    HOME_LOAN("home_loan"),
    NEW_LOAN_LIMIT("10000"),
    MESSAGE_417_UPDATE("Failed to update loan details"),
    STATUS_417("417"),
    MESSAGE_417_DELETE("Failed to delete loan details");

    private final String value;

    LoanConstantsEnum(String value) {
        this.value = value;
    }

}
