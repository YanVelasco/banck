package com.bank.accounts.mapper;

import com.bank.accounts.dtos.AccountDto;
import com.bank.accounts.dtos.CardDto;
import com.bank.accounts.dtos.CustomerDetailsDto;
import com.bank.accounts.dtos.CustomerDto;
import com.bank.accounts.dtos.LoanDto;
import com.bank.accounts.entities.AccountEntity;
import com.bank.accounts.entities.CustomerEntity;
import org.springframework.http.ResponseEntity;

public class CustomerMapper {

    public static CustomerDto toCustomerDto(CustomerEntity customerEntity, AccountEntity account) {
        return CustomerDto.builder()
                .name(customerEntity.getName())
                .email(customerEntity.getEmail())
                .mobileNumber(customerEntity.getMobileNumber())
                .accountDto(AccountDto.builder()
                        .accountNumber(account.getAccountNumber())
                        .accountType(account.getAccountType())
                        .branchAddress(account.getBranchAddress())
                        .build())
                .build();
    }

    public static CustomerEntity toCustomerEntity(CustomerDto customerDto) {
        return CustomerEntity.builder()
                .name(customerDto.name())
                .email(customerDto.email())
                .mobileNumber(customerDto.mobileNumber())
                .build();
    }

    public static void updateCustomerEntity(CustomerEntity customerEntity, CustomerDto customerDto) {
        customerEntity.setName(customerDto.name());
        customerEntity.setEmail(customerDto.email());
        customerEntity.setMobileNumber(customerDto.mobileNumber());
    }

    public static CustomerDetailsDto toCustomerDetailsDto(CustomerEntity customerEntity, AccountEntity account, LoanDto loanDto, CardDto cardDto) {
        return CustomerDetailsDto.builder()
                .name(customerEntity.getName())
                .email(customerEntity.getEmail())
                .mobileNumber(customerEntity.getMobileNumber())
                .accountDto(AccountDto.builder()
                        .accountNumber(account.getAccountNumber())
                        .accountType(account.getAccountType())
                        .branchAddress(account.getBranchAddress())
                        .build())
                .loanDto(loanDto)
                .cardDto(cardDto)
                .build();
    }

}
