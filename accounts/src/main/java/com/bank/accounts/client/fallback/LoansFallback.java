package com.bank.accounts.client.fallback;

import com.bank.accounts.client.LoansFeignClient;
import com.bank.accounts.dtos.LoanDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LoansFallback implements LoansFeignClient {
    @Override
    public ResponseEntity<LoanDto> fetchLoanDetails(String correlationId, String mobileNumber) {
        return null;
    }
}
