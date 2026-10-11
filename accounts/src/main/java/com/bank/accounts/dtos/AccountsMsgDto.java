package com.bank.accounts.dtos;

import lombok.Builder;

@Builder
public record AccountsMsgDto(
        Long accountNumber,
        String name,
        String email,
        String mobileNumber
) {
}
