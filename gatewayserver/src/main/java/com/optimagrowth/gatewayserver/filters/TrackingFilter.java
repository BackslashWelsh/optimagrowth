package com.optimagrowth.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Order(1)
@Component
public class TrackingFilter implements GlobalFilter {
    private static final Logger logger = LoggerFactory.getLogger(TrackingFilter.class);
    private final FilterUtils filterUtils;

    public TrackingFilter(final FilterUtils filterUtils) {
        this.filterUtils = filterUtils;
    }

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final GatewayFilterChain chain) {
        final var headers = exchange.getRequest().getHeaders();
        final var correlationId = filterUtils.getCorrelationId(headers);
        if (correlationId.isPresent()) {
            logger.debug("tmx-correlation-id found in tracking filter: {}. ", correlationId.get());
            return chain.filter(exchange);
        }
        String correlationID = UUID.randomUUID().toString();
        logger.debug("tmx-correlation-id generated in tracking filter: {}.", correlationID);
        return chain.filter(filterUtils.setCorrelationId(exchange, correlationID));
    }
}
