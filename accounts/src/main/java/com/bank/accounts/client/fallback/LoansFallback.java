package com.bank.accounts.client.fallback;

import com.bank.accounts.client.LoansFeignClient;
import com.bank.accounts.dtos.LoanDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LoansFallback implements LoansFeignClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoansFallback.class);

    @Override
    public ResponseEntity<LoanDto> fetchLoanDetails(String correlationId, String mobileNumber) {
        LOGGER.debug("fetchLoanDetails fallback method start");
        LOGGER.debug("fetchLoanDetails fallback method end");
        return null;
    }
}
