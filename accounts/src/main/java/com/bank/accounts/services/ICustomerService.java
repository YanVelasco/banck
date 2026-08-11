package com.bank.accounts.services;

import com.bank.accounts.dtos.CustomerDetailsDto;

public interface ICustomerService {

    CustomerDetailsDto getCustomerDetailsByMobileNumber(String correlationId, String mobileNumber);

}
