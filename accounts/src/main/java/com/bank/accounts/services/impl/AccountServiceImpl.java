package com.bank.accounts.services.impl;

import com.bank.accounts.dtos.CustomerDto;
import com.bank.accounts.entities.AccountEntity;
import com.bank.accounts.entities.CustomerEntity;
import com.bank.accounts.enums.AccountConstantsEnum;
import com.bank.accounts.exceptions.AlreadyExistsException;
import com.bank.accounts.exceptions.NotFoundException;
import com.bank.accounts.mapper.AccountMapper;
import com.bank.accounts.mapper.CustomerMapper;
import com.bank.accounts.repositories.AccountRepository;
import com.bank.accounts.repositories.CustomerRepository;
import com.bank.accounts.services.IAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AccountServiceImpl implements IAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    public AccountServiceImpl(CustomerRepository customerRepository, AccountRepository accountRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    @Override
    public void createAccount(CustomerDto customerDto) {
        LOGGER.debug("createAccount method start");
        customerRepository.findByMobileNumberOrEmail(customerDto.mobileNumber(), customerDto.email()).ifPresent(existingCustomer -> {
            throw new AlreadyExistsException("Customer with mobile number " + customerDto.mobileNumber() + " or email " + customerDto.email() + " already exists.");
        });

        CustomerEntity customerEntity = CustomerMapper.toCustomerEntity(customerDto);
        customerRepository.save(customerEntity);

        AccountEntity accountEntity = createAccountEntity(customerEntity);
        accountRepository.save(accountEntity);
        LOGGER.debug("createAccount method end");
    }

    @Override
    public CustomerDto fetchAccountDetails(String mobileNumber) {
        LOGGER.debug("fetchAccountDetails method start");
        var customerEntity = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new NotFoundException("Customer with mobile number " + mobileNumber + " not found."));
        var account = accountRepository.findByCustomerId(customerEntity.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Account for customer with mobile number " + mobileNumber + " not found."));
        LOGGER.debug("fetchAccountDetails method end");
        return CustomerMapper.toCustomerDto(customerEntity, account);
    }

    @Override
    public boolean updateAccountDetails(CustomerDto customerDto) {
        LOGGER.debug("updateAccountDetails method start");
        var isUpdated = false;

        var accountDto = customerDto.accountDto();
        if (accountDto != null) {
            AccountEntity accountEntity = accountRepository.findByAccountNumber((accountDto.accountNumber()))
                    .orElseThrow(() -> new NotFoundException("Account with account number " + accountDto.accountNumber() + " not found."));
            AccountMapper.updateAccountEntity(accountEntity, accountDto);
            accountRepository.save(accountEntity);

            Long customerId = accountEntity.getCustomerId();
            CustomerEntity customerEntity = customerRepository.findByCustomerId(customerId)
                    .orElseThrow(() -> new NotFoundException("Customer with ID " + customerId + " not found."));
            CustomerMapper.updateCustomerEntity(customerEntity, customerDto);
            customerRepository.save(customerEntity);

            isUpdated = true;
        }

        LOGGER.debug("updateAccountDetails method end");
        return isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        LOGGER.debug("deleteAccount method start");
        var customerEntity = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new NotFoundException("Customer with mobile number " + mobileNumber + " not found."));

        accountRepository.deleteByCustomerId(customerEntity.getCustomerId());
        customerRepository.deleteById(customerEntity.getCustomerId());

        LOGGER.debug("deleteAccount method end");
        return true;
    }

    private AccountEntity createAccountEntity(CustomerEntity customerEntity) {

        long randomAccNumber = generateUniqueAccountNumber();

        return AccountEntity.builder()
                .customerId(customerEntity.getCustomerId())
                .accountNumber(randomAccNumber)
                .accountType(AccountConstantsEnum.SAVINGS.getValue())
                .branchAddress(AccountConstantsEnum.ADDRESS.getValue())
                .build();
    }

    private long generateUniqueAccountNumber() {
        long number;
        do {
            number = ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L);
        } while (accountRepository.existsByAccountNumber(number));
        return number;
    }
}
