package com.bank.accounts.mapper;

import com.bank.accounts.dtos.AccountDto;
import com.bank.accounts.entities.AccountEntity;

public class AccountMapper {

    public static AccountDto toAccountDto(AccountEntity accountEntity) {
        return AccountDto.builder()
                .accountNumber(accountEntity.getAccountNumber())
                .accountType(accountEntity.getAccountType())
                .branchAddress(accountEntity.getBranchAddress())
                .build();
    }

    public static AccountEntity toAccountEntity(AccountDto accountDto) {
        return AccountEntity.builder()
                .accountNumber(accountDto.accountNumber())
                .accountType(accountDto.accountType())
                .branchAddress(accountDto.branchAddress())
                .build();
    }

}
