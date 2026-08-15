package com.bank.accounts.client.fallback;

import com.bank.accounts.client.CardsFeignClient;
import com.bank.accounts.dtos.CardDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CardsFallback implements CardsFeignClient {
    @Override
    public ResponseEntity<CardDto> fetchCardDetails(String correlationId, String mobileNumber) {
        return null;
    }
}
