package com.optimagrowth.gatewayserver.filters;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Optional;

@Component
public class FilterUtils {
    public static final String CORRELATION_ID = "tmx-correlation-id";
    public static final String AUTH_TOKEN = "tmx-auth-token";
    public static final String USER_ID = "tmx-user-id";
    public static final String ORG_ID = "tmx-org-id";
    public static final String PRE_FILTER_TYPE = "pre";
    public static final String POST_FILTER_TYPE = "post";
    public static final String ROUTE_FILTER_TYPE = "route";

    public Optional<String> getCorrelationId(final HttpHeaders headers) {
        return Optional.ofNullable(headers.get(CORRELATION_ID))
                .flatMap(l -> l.stream().findFirst());
    }

    public ServerWebExchange setCorrelationId(final ServerWebExchange exchange, final String correlationID) {
        return setRequestHeader(exchange, CORRELATION_ID, correlationID);
    }

    private ServerWebExchange setRequestHeader(
            final ServerWebExchange exchange,
            final String name,
            final String value
    ) {
        return exchange.mutate().request(
                        exchange.getRequest().mutate()
                                .header(name, value)
                                .build())
                .build();
    }
}
