package com.bank.accounts.services;

import com.bank.accounts.dtos.CustomerDto;

public interface IAccountService {

    void createAccount(CustomerDto customerDto);

    CustomerDto fetchAccountDetails(String mobileNumber);

}
