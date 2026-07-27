package com.nexus.gateway.infrastructure.web.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class TrustedHeaderStrippingFilter implements GlobalFilter, Ordered {

    // Any header a caller could plausibly spoof to impersonate identity/authorization.
    // Extend this list if Nexus ever introduces custom identity headers.
    private static final List<String> UNTRUSTED_HEADERS = List.of(
            "X-User-Id", "X-User-Role", "X-User-Roles", "X-Auth-User"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest.Builder mutated = exchange.getRequest().mutate();
        UNTRUSTED_HEADERS.forEach(h -> mutated.headers(headers -> headers.remove(h)));
        return chain.filter(exchange.mutate().request(mutated.build()).build());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1; // after correlation ID assignment, before routing/TokenRelay
    }

}
