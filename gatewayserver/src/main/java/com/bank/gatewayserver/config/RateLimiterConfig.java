package com.bank.gatewayserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Principal;

import static reactor.core.publisher.Mono.justOrEmpty;

@Configuration
public class RateLimiterConfig {

    @Bean
    public RedisRateLimiter redisRateLimiter(
            @Value("${bank.gateway.rate-limit.replenish-rate:10}") int replenishRate,
            @Value("${bank.gateway.rate-limit.burst-capacity:20}") int burstCapacity,
            @Value("${bank.gateway.rate-limit.requested-tokens:1}") int requestedTokens
    ) {
        return new RedisRateLimiter(replenishRate, burstCapacity, requestedTokens);
    }

    @Bean
    KeyResolver userKeyResolver() {
        return exchange -> exchange.getPrincipal()
                .map(Principal::getName)
                .switchIfEmpty(justOrEmpty(exchange.getRequest().getHeaders().getFirst("user")))
                .switchIfEmpty(justOrEmpty(exchange.getRequest().getRemoteAddress())
                        .map(address -> address.getAddress() != null
                                ? address.getAddress().getHostAddress()
                                : address.getHostString()))
                .defaultIfEmpty("anonymous");
    }

}
