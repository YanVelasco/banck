package com.bank.accounts.mapper;

import com.bank.accounts.dtos.CustomerDto;
import com.bank.accounts.entities.CustomerEntity;

public class CustomerMapper {

    public static CustomerDto toCustomerDto(CustomerEntity customerEntity) {
        return CustomerDto.builder()
                .name(customerEntity.getName())
                .email(customerEntity.getEmail())
                .mobileNumber(customerEntity.getMobileNumber())
                .build();
    }

    public static CustomerEntity toCustomerEntity(CustomerDto customerDto) {
        return CustomerEntity.builder()
                .name(customerDto.name())
                .email(customerDto.email())
                .mobileNumber(customerDto.mobileNumber())
                .build();
    }

}
