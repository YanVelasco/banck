package com.bank.accounts.dtos;

import lombok.Builder;

@Builder
public record CustomerDto(
        String name,
        String email,
        String mobileNumber,
        AccountDto accountDto
) {
}
