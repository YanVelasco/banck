package com.bank.gatewayserver.config;

import org.springframework.cloud.gateway.filter.factory.RetryGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator myRoutes(
            RouteLocatorBuilder routeLocatorBuilder,
            RedisRateLimiter redisRateLimiter,
            KeyResolver userKeyResolver
    ) {
        return routeLocatorBuilder
                .routes()
                .route(p -> p
                        .path("/bank/accounts/**")
                        .filters(f -> f.rewritePath("/bank/accounts/(?<segment>.*)", "/${segment}")
                                .circuitBreaker(c -> c.setName("accountsCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/contactSupport")
                                )
                        )
                        .uri("lb://ACCOUNTS")
                )
                .route(p -> p
                        .path("/bank/cards/**")
                        .filters(f -> f.rewritePath("/bank/cards/(?<segment>.*)", "/${segment}")
                                .retry(RouteConfig::configureTransientRetry)
                                .requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter).setKeyResolver(userKeyResolver))
                        )
                        .uri("lb://CARDS")
                )
                .route(p -> p
                        .path("/bank/loans/**")
                        .filters(f -> f.rewritePath("/bank/loans/(?<segment>.*)", "/${segment}")
                                .retry(RouteConfig::configureTransientRetry)
                        )
                        .uri("lb://LOANS")
                ).build();
    }

    @SuppressWarnings("unchecked")
    private static void configureTransientRetry(RetryGatewayFilterFactory.RetryConfig retryConfig) {
        retryConfig.setRetries(3)
                .setMethods(HttpMethod.GET)
                .setStatuses(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        HttpStatus.BAD_GATEWAY,
                        HttpStatus.SERVICE_UNAVAILABLE,
                        HttpStatus.GATEWAY_TIMEOUT
                )
                .setExceptions(IOException.class, TimeoutException.class)
                .setBackoff(
                        Duration.ofMillis(100), Duration.ofMillis(1000), 2, true
                );
    }

}
