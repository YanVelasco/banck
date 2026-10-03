package com.bank.message.dto;

public record AccountsMessageDto(
        Long accountNumber,
        String name,
        String email,
        String phoneNumber
) {
}
