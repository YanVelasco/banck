package com.bank.gatewayserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @RequestMapping("/contactSupport")
    public Mono<String> contactSupportFallback() {
        return Mono.just("An error occurred. Please try again later.");
    }

}
