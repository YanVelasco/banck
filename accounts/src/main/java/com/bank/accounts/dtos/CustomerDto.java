package com.bank.accounts.dtos;

public record CustomerDto(
        String name,
        String email,
        String mobileNumber
) {
}
