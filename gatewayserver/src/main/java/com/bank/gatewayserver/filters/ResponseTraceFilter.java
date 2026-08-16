package com.bank.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Configuration
public class ResponseTraceFilter {

    private final FilterUtility filterUtility;

    private static final String RESPONSE_TIME_HEADER = "X-Response-Time-Ms";

    private static final Logger logger = LoggerFactory.getLogger(ResponseTraceFilter.class);

    public ResponseTraceFilter(FilterUtility filterUtility) {
        this.filterUtility = filterUtility;
    }

    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> {
            long startTimeNanos = System.nanoTime();
            exchange.getResponse().beforeCommit(() -> {
                HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
                HttpHeaders responseHeaders = exchange.getResponse().getHeaders();
                String correlationId = filterUtility.getCorrelationId(requestHeaders);

                long responseTimeMs = Duration.ofNanos(System.nanoTime() - startTimeNanos).toMillis();
                responseHeaders.set(RESPONSE_TIME_HEADER, String.valueOf(responseTimeMs));

                logger.debug("Completing outgoing request for correlation id: {}", correlationId);
                if (correlationId != null) {
                    if (!responseHeaders.containsHeader(FilterUtility.CORRELATION_ID)) {
                        responseHeaders.add(FilterUtility.CORRELATION_ID, correlationId);
                    }
                } else {
                    logger.debug("No correlation id found in request headers; response header won't be set.");
                }
                return Mono.empty();
            });
            return chain.filter(exchange);
        };
    }

}
