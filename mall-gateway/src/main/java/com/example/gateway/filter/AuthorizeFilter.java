package com.example.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 获取请求路径 (为了放行 favicon.ico)
        String path = exchange.getRequest().getURI().getPath();
        if ("/favicon.ico".equals(path)) {
            return chain.filter(exchange);
        }

        // 2. 【核心修改】从请求头 (Header) 中获取 Authorization
        HttpHeaders headers = exchange.getRequest().getHeaders();
        // getFirst 会忽略大小写，Authorization 和 authorization 都能取到
        String auth = headers.getFirst("Authorization");

        // 打印日志方便调试
        System.out.println("请求路径: " + path + ", 获取到的 Header Authorization: " + auth);

        // 3. 校验 (模拟：必须是 admin)
        if ("admin".equals(auth)) {
            return chain.filter(exchange);
        }

        // 4. 拦截
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}