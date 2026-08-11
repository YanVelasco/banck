package com.bank.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;


@Configuration
public class ResponseTraceFilter {

    private final FilterUtility filterUtility;

    private static final Logger logger = LoggerFactory.getLogger(ResponseTraceFilter.class);

    public ResponseTraceFilter(FilterUtility filterUtility) {
        this.filterUtility = filterUtility;
    }

    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
            HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
            String correlationId = filterUtility.getCorrelationId(requestHeaders);
            logger.debug("Completing outgoing request for correlation id: {}", correlationId);
            if (correlationId != null) {
                exchange.getResponse().getHeaders().add(FilterUtility.CORRELATION_ID, correlationId);
            } else {
                logger.warn("No correlation id found in request headers; response header won't be set.");
            }
        }));
    }

}
