package com.example.gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    //直接访问：http://localhost:88/product/1 -> 401 Unauthorized (拦截成功)。
    //带参数访问：http://localhost:88/product/1?authorization=admin -> 成功返回数据。

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 获取请求参数
        MultiValueMap<String, String> params = exchange.getRequest().getQueryParams();

        // 2. 获取 authorization 参数 (模拟 token)
        String auth = params.getFirst("authorization");

        // 3. 校验
        if ("admin".equals(auth)) {
            // 合法，放行
            return chain.filter(exchange);
        }

        // 4. 不合法，拦截并返回 401 状态码
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1; // 优先级，越小越先执行
    }
}