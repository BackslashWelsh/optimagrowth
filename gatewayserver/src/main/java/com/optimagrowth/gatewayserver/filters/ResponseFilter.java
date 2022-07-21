package com.optimagrowth.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

@Configuration
public class ResponseFilter {
    final Logger logger = LoggerFactory.getLogger(ResponseFilter.class);
    private final FilterUtils filterUtils;

    public ResponseFilter(final FilterUtils filterUtils) {
        this.filterUtils = filterUtils;
    }

    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
            var requestHeaders = exchange.getRequest().getHeaders();
            var correlationId = filterUtils.getCorrelationId(requestHeaders).orElse(null);
            logger.debug("Adding the correlation id to the outbound headers. {}", correlationId);
            exchange.getResponse().getHeaders().add(FilterUtils.CORRELATION_ID, correlationId);
            logger.debug("Completing outgoing request for {}.", exchange.getRequest().getURI());
        }));
    }
}
