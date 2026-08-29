package com.bank.gatewayserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FallbackController.class);

    @RequestMapping("/contactSupport")
    public Mono<String> contactSupportFallback() {
        LOGGER.debug("contactSupportFallback method start");
        Mono<String> response = Mono.just("An error occurred. Please try again later.");
        LOGGER.debug("contactSupportFallback method end");
        return response;
    }

}
