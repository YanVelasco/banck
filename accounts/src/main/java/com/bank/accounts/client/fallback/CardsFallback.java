package com.bank.accounts.client.fallback;

import com.bank.accounts.client.CardsFeignClient;
import com.bank.accounts.dtos.CardDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CardsFallback implements CardsFeignClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CardsFallback.class);

    @Override
    public ResponseEntity<CardDto> fetchCardDetails(String correlationId, String mobileNumber) {
        LOGGER.debug("fetchCardDetails fallback method start");
        LOGGER.debug("fetchCardDetails fallback method end");
        return null;
    }
}
