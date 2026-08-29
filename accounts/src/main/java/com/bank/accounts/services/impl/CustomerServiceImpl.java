package com.bank.accounts.services.impl;

import com.bank.accounts.client.CardsFeignClient;
import com.bank.accounts.client.LoansFeignClient;
import com.bank.accounts.dtos.CardDto;
import com.bank.accounts.dtos.CustomerDetailsDto;
import com.bank.accounts.dtos.LoanDto;
import com.bank.accounts.exceptions.NotFoundException;
import com.bank.accounts.mapper.CustomerMapper;
import com.bank.accounts.repositories.AccountRepository;
import com.bank.accounts.repositories.CustomerRepository;
import com.bank.accounts.services.ICustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Stream;

@Service
public class CustomerServiceImpl implements ICustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final CardsFeignClient cardsFeignClient;
    private final LoansFeignClient loansFeignClient;

    public CustomerServiceImpl(AccountRepository accountRepository, CustomerRepository customerRepository, CardsFeignClient cardsFeignClient, LoansFeignClient loansFeignClient) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.cardsFeignClient = cardsFeignClient;
        this.loansFeignClient = loansFeignClient;
    }

    @Override
    public CustomerDetailsDto getCustomerDetailsByMobileNumber(String mobileNumber, String correlationId) {
        LOGGER.debug("getCustomerDetailsByMobileNumber method start");
        var customerEntity = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new NotFoundException("Customer with mobile number " + mobileNumber + " not found."));
        var account = accountRepository.findByCustomerId(customerEntity.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Account for customer with mobile number " + mobileNumber + " not found."));

        LoanDto loanDto = Stream
                .ofNullable(
                        loansFeignClient.fetchLoanDetails(correlationId, mobileNumber)
                )
                .map(ResponseEntity::getBody)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        CardDto cardDto = Stream
                .ofNullable(
                        cardsFeignClient.fetchCardDetails(correlationId, mobileNumber)
                )
                .map(ResponseEntity::getBody)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        LOGGER.debug("getCustomerDetailsByMobileNumber method end");
        return CustomerMapper.toCustomerDetailsDto(customerEntity, account, loanDto, cardDto);
    }

}
