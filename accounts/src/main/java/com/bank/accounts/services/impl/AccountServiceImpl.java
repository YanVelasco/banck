package com.bank.accounts.services.impl;

import com.bank.accounts.dtos.CustomerDto;
import com.bank.accounts.repositories.AccountRepository;
import com.bank.accounts.repositories.CustomerRepository;
import com.bank.accounts.services.IAccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements IAccountService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    public AccountServiceImpl(CustomerRepository customerRepository, AccountRepository accountRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void createAccount(CustomerDto customerDto) {

    }
}
