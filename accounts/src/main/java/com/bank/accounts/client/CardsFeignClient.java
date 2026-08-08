package com.bank.accounts.client;

import com.bank.accounts.dtos.CardDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cards", url = "${feign.client.cards.url}")
public interface CardsFeignClient {

    @GetMapping(value = "/fetch", consumes = "application/json")
    ResponseEntity<CardDto> fetchCardDetails(
            @RequestParam String mobileNumber
    );

}
