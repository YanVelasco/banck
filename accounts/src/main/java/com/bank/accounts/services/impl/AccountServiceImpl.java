package com.bank.accounts.services.impl;

import com.bank.accounts.dtos.AccountsMsgDto;
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
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class AccountServiceImpl implements IAccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final StreamBridge streamBridge;

    public AccountServiceImpl(CustomerRepository customerRepository, AccountRepository accountRepository, StreamBridge streamBridge) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.streamBridge = streamBridge;
    }

    @Transactional
    @Override
    public void createAccount(CustomerDto customerDto) {
        log.debug("createAccount method start");
        customerRepository.findByMobileNumberOrEmail(customerDto.mobileNumber(), customerDto.email()).ifPresent(existingCustomer -> {
            throw new AlreadyExistsException("Customer with mobile number " + customerDto.mobileNumber() + " or email " + customerDto.email() + " already exists.");
        });

        CustomerEntity customerEntity = CustomerMapper.toCustomerEntity(customerDto);
        customerRepository.save(customerEntity);

        AccountEntity accountEntity = createAccountEntity(customerEntity);
        var savedAccount = accountRepository.save(accountEntity);
        sendCommunication(savedAccount, customerEntity);
        log.debug("createAccount method end");
    }

    private void sendCommunication(AccountEntity accountEntity, CustomerEntity customerEntity) {
        var accountsMsgDto = AccountsMsgDto.builder()
                .accountNumber(accountEntity.getAccountNumber())
                .name(customerEntity.getName())
                .email(customerEntity.getEmail())
                .mobileNumber(customerEntity.getMobileNumber())
                .build();
        log.debug("Sending account creation message to stream: {}", accountsMsgDto);
        var result = streamBridge.send("sendCommunication-out-0", accountsMsgDto);
        log.debug("Is message sent successfully? {}", result);
    }

    @Override
    public CustomerDto fetchAccountDetails(String mobileNumber) {
        log.debug("fetchAccountDetails method start");
        var customerEntity = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new NotFoundException("Customer with mobile number " + mobileNumber + " not found."));
        var account = accountRepository.findByCustomerId(customerEntity.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Account for customer with mobile number " + mobileNumber + " not found."));
        log.debug("fetchAccountDetails method end");
        return CustomerMapper.toCustomerDto(customerEntity, account);
    }

    @Override
    public boolean updateAccountDetails(CustomerDto customerDto) {
        log.debug("updateAccountDetails method start");
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

        log.debug("updateAccountDetails method end");
        return isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        log.debug("deleteAccount method start");
        var customerEntity = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new NotFoundException("Customer with mobile number " + mobileNumber + " not found."));

        accountRepository.deleteByCustomerId(customerEntity.getCustomerId());
        customerRepository.deleteById(customerEntity.getCustomerId());

        log.debug("deleteAccount method end");
        return true;
    }

    @Override
    public boolean updateCommunicationStatus(Long accountNumber) {
        boolean isUpdated = false;
        if(accountNumber!= null) {
            var accountEntity = accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new NotFoundException("Account with account number " + accountNumber + " not found."));
            accountEntity.setCommunicationStatus(true);
            accountRepository.save(accountEntity);
            isUpdated = true;
        }
        return isUpdated;
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
