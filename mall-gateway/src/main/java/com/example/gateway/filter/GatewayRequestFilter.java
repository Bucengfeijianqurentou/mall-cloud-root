package com.example.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GatewayRequestFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 构建新的请求，添加自定义 Header
        // "Gateway-Key" 是我们约定的暗号 Key
        // "token-secret" 是暗号的值 (实际开发中应该配置在 Nacos 里)
        var newRequest = exchange.getRequest().mutate()
                .header("Gateway-Key", "token-secret")
                .build();

        // 2. 放行，使用新的请求对象
        return chain.filter(exchange.mutate().request(newRequest).build());
    }

    @Override
    public int getOrder() {
        return 0; // 优先级
    }
}