package com.bank.accounts.dtos;

import lombok.Builder;

@Builder
public record CardDto(

        String mobileNumber,

        String cardNumber,

        String cardType,

        int totalLimit,

        int amountUsed,

        int availableAmount

) {
}
