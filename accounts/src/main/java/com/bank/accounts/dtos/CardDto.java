package com.bank.accounts.dtos;

public record CardDto(

        String mobileNumber,

        String cardNumber,

        String cardType,

        int totalLimit,

        int amountUsed,

        int availableAmount

) {
}
