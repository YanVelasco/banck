package com.bank.accounts.dtos;

public record AccountDto(
        Long accountNumber,
        String accountType,
        String branchAddress
) {
}
