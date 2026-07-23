package com.bank.accounts.dtos;

import lombok.Builder;

@Builder
public record AccountDto(
        Long accountNumber,
        String accountType,
        String branchAddress
) {
}
